package com.example.mymemoryapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemoryapplication.adapters.ImagePickerAdapter
import com.example.mymemoryapplication.clicklisteners.ImageClickListener
import com.example.mymemoryapplication.constants.BoardSize
import com.example.mymemoryapplication.constants.EXTRA_BOARD_SIZE
import com.example.mymemoryapplication.constants.EXTRA_GAME_NAME
import com.example.mymemoryapplication.services.isPermissionsGranted
import com.example.mymemoryapplication.services.requestPermission
import com.example.mymemoryapplication.services.BitmapScaler
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class CreateActivity : AppCompatActivity() {

    private lateinit var rvImagePicker: RecyclerView
    private lateinit var editText: EditText
    private lateinit var btnSave: Button

    private lateinit var adapter: ImagePickerAdapter
    private lateinit var boardSize : BoardSize
    private lateinit var upload: ProgressBar

    private var numImagesRequired = -1
    private val chosenImageUrls = mutableListOf<Uri>()

    private val storage = Firebase.storage
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        rvImagePicker = findViewById(R.id.rvImagePicker)
        editText = findViewById(R.id.editText)
        btnSave = findViewById(R.id.saveBtn)
        upload = findViewById(R.id.progressBar2)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        boardSize = intent.getSerializableExtra(EXTRA_BOARD_SIZE) as BoardSize
        // if i pick hard, then i need 12 unique photos from my phone
        numImagesRequired = boardSize.getNumPairs()
        supportActionBar?.title = "Choose pics (0 / $numImagesRequired)"

        editText.filters = arrayOf(InputFilter.LengthFilter(MAX_GAME_NAME_LENGTH))
        editText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnSave.isEnabled = shouldEnableSaveButton()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        btnSave.setOnClickListener{
            saveDataToFirebase()
        }

        // number of columns
        adapter = ImagePickerAdapter(this, chosenImageUrls, boardSize, object: ImageClickListener {
            override fun onPlaceHolderClick() {
                // 2 types of intents
                // 1. Implicit: request to perform an action based on desired action
                // 2. Explicit: launch other activities within your app
                if (isPermissionsGranted(this@CreateActivity, READ_PHOTOS_PERMISSION)) {
                    launchIntentForPhotos()
                } else {
                    requestPermission(this@CreateActivity, READ_PHOTOS_PERMISSION, READ_EXTERNAL_PHOTOS_CODE)
                }
            }
        })
        rvImagePicker.adapter = adapter
        rvImagePicker.setHasFixedSize(true)
        rvImagePicker.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    private fun saveDataToFirebase() {
        // we need to downscale the image, cause images are very large, and we want to use lesser storage on firebase
        Log.i(TAG, "saving data to firebase")
        btnSave.isEnabled = false
        val customGameName = editText.text.toString()
        // Check that we're not over writing someone else's data
        db.collection("games").document(customGameName).get().addOnSuccessListener {
            document ->
            if (document != null && document.data != null ) {
                AlertDialog.Builder(this).setTitle("Name Taken").setMessage("A game with the name '$customGameName' already exist")
                        .setPositiveButton("OK", null)
                        .show()
            }
            else {
                handleImageUploading(customGameName)
            }
        }.addOnFailureListener{
           exception ->
            Log.e(TAG, "error happen while saving new game", exception)
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            btnSave.isEnabled = true
        }
    }

    private fun handleImageUploading(customGameName: String) {
        upload.visibility = View.VISIBLE
        Log.w(TAG, "save data to firebase")
        var didEncounterError = false
        val uploadImageUrls = mutableListOf<String>()
        for ((index, photoUri) in chosenImageUrls.withIndex()) {
            val imageByteArray = getImageByteArray(photoUri)
            val filepath = "images/$customGameName/${System.currentTimeMillis()}=${index}.jpg"
            val photoReference : StorageReference = storage.reference.child(filepath)
            photoReference.putBytes(imageByteArray)
                    .continueWithTask {  iterator ->
                        Log.i(TAG, "Upload task...")
                        photoReference.downloadUrl
                    }.addOnCompleteListener { iter ->
                        if (!iter.isSuccessful) {
                            Log.e(TAG, "Exception with Firebase storage",  iter.exception)
                            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                            didEncounterError = true
                            return@addOnCompleteListener
                        }
                        if (didEncounterError) {
                            upload.visibility = View.GONE
                            return@addOnCompleteListener
                        }
                        val downloadUrl:String = iter.result.toString()
                        uploadImageUrls.add(downloadUrl)
                        upload.progress = uploadImageUrls.size / chosenImageUrls.size
                        Log.i(TAG, "Finished!!")
                        if (uploadImageUrls.size == chosenImageUrls.size) {
                            handleAllImagesUploaded(customGameName, uploadImageUrls)
                        }

                    }
        }
    }

    private fun handleAllImagesUploaded(customGameName: String, uploadImageUrls: MutableList<String>) {
        Log.w(TAG, "IT IOS WORKING!!!!!!")
        db.collection("games").document(customGameName)
                .set(mapOf("images" to uploadImageUrls))
                .addOnCompleteListener {
                    gameCreationTask ->

                    upload.visibility = View.GONE

                    if (!gameCreationTask.isSuccessful) {
                        Log.e(TAG, "Exception with game creation", gameCreationTask.exception)
                        Toast.makeText(this, "failed game creation", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                    Log.i(TAG, "succesfully created game $customGameName")
                    AlertDialog.Builder(this)
                            .setTitle("upload complete! let's play your game $customGameName")
                            .setPositiveButton("OK") {
                                _,_ ->
                                val resultData = Intent()
                                resultData.putExtra(EXTRA_GAME_NAME, customGameName)
                                setResult(Activity.RESULT_OK, resultData)
                                finish()
                            }.show()
                }
    }

    private fun getImageByteArray(photoUri: Uri): ByteArray {
        val originalBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, photoUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
        }
        val scaledBitmap = BitmapScaler.scaleToFitHeight(originalBitmap, 250)
        val byteOutputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteOutputStream)
        return byteOutputStream.toByteArray()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != PICK_PHOTOS_REQUEST_CODE || resultCode != Activity.RESULT_OK || data == null) {
            Log.w(TAG, "Did get data back from the launched activity, user likely cancelled flow")
            return
        }
        Log.i(TAG, "THIS IS WORKINGGGGGGGGGGGGGG")
        val selectedUri = data.data
        val clipData = data.clipData
        if (clipData != null) {
            Log.i(TAG, "clipData numImages ${clipData.itemCount}: $clipData")
            for (i in 0 until clipData.itemCount) {
                val clipItem = clipData.getItemAt(i)
                if (chosenImageUrls.size  < numImagesRequired) {
                    chosenImageUrls.add(clipItem.uri)
                }
            }
        } else if (selectedUri != null) {
            Log.i(TAG, "data: $selectedUri")
            chosenImageUrls.add(selectedUri)
        }
        adapter.notifyDataSetChanged()
        supportActionBar?.title = "Choose pics: (${chosenImageUrls.size} / $numImagesRequired)"
        btnSave.isEnabled = shouldEnableSaveButton()

    }

    private fun shouldEnableSaveButton(): Boolean {
        if (chosenImageUrls.size != numImagesRequired) {
            return false
        }
        if (editText.text.isBlank() || editText.text.length < MIN_GAME_NAME_LENGTH) {
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        if (requestCode == READ_EXTERNAL_PHOTOS_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchIntentForPhotos()
            }
            else {
                Toast.makeText(this, "In order to create a custom game, you need to provide access to your photos", Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun launchIntentForPhotos() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        // by default, android does not allow you to read external storage, so we need to define it in AndroidManifest.xml
        // 2 types of permissions:
        // 1. Normal: data access with little risk to user's privacy
        // 2. Dangerous: data access that involves user's private information, will warn user if really want to allow access to private information
        startActivityForResult(Intent.createChooser(intent, "Choose pics"), PICK_PHOTOS_REQUEST_CODE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val PICK_PHOTOS_REQUEST_CODE = 655
        private const val READ_EXTERNAL_PHOTOS_CODE = 248
        private const val READ_PHOTOS_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
        private const val MIN_GAME_NAME_LENGTH = 3
        private const val MAX_GAME_NAME_LENGTH = 14
        private const val TAG = "CreateActivity"
    }

}