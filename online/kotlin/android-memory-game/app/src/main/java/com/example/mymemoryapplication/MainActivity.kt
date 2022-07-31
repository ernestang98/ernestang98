package com.example.mymemoryapplication

import android.animation.ArgbEvaluator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemoryapplication.adapters.MemoryBoardAdapter
import com.example.mymemoryapplication.clicklisteners.CardClickListener
import com.example.mymemoryapplication.constants.BoardSize
import com.example.mymemoryapplication.constants.EXTRA_BOARD_SIZE
import com.example.mymemoryapplication.constants.EXTRA_GAME_NAME
import com.example.mymemoryapplication.models.UserImageList
import com.example.mymemoryapplication.services.MemoryGame
import com.github.jinatonic.confetti.CommonConfetti
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

// coordinator layout allows dismissable snackbar

class MainActivity : AppCompatActivity() {

    /* lateinit because you know that you won't want the variables to be created
        at construction of MainActivity but onCreate(), hence lateinit
    */
    private lateinit var clRoot: CoordinatorLayout
    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumberOfMoves: TextView
    private lateinit var tvNumberOfPairs: TextView

    private var gameName:String? = null
    private var customGameImages: List<String>? = null
    private val db = Firebase.firestore

    private var boardSize: BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvBoard = findViewById(R.id.rvBoard)
        tvNumberOfMoves = findViewById(R.id.tvNumberOfMoves)
        tvNumberOfPairs = findViewById(R.id.tvNumberOfPairs)
        clRoot = findViewById(R.id.clRoot)

//        val intent = Intent(this, CreateActivity::class.java)
//        intent.putExtra(EXTRA_BOARD_SIZE, BoardSize.MEDIUM)
//        startActivity(intent)

        setUpBoard()

    }

    private fun setUpBoard() {
        supportActionBar?.title = gameName ?: getString(R.string.app_name)
        tvNumberOfPairs.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))
        tvNumberOfMoves.text = "Moves: 0"
        tvNumberOfPairs.text = "Pairs: 0 / ${boardSize.getNumPairs()}"

        memoryGame = MemoryGame(boardSize, customGameImages)
        adapter = MemoryBoardAdapter(this, boardSize, memoryGame.memoryCards, object : CardClickListener {
            override fun onCardClicked(position: Int) {
                Log.i("I", "CARD_CLICK_LISTENER: Clicked button at position $position")
                updateGameWithFlip(position)
            }
        })
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.my_refresh -> {
                if (memoryGame.getNumberOfMoves() > 0 && !memoryGame.haveWonGame()) {
                    showAlertDialog("Quite current game?", null, View.OnClickListener {
                        setUpBoard()
                    })
                }
                else {
                    setUpBoard()
                }
                return true
            }
            R.id.new_size -> {
               showNewSizeDialog()
                return true
            }
            R.id.custom -> {
                showCreationDialog()
                return true
            }
            R.id.download -> {
                showDownloadDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDownloadDialog() {
        val boardDownloadView = LayoutInflater.from(this).inflate(R.layout.dialog_download_board, null)
        showAlertDialog("Fetch memory game", boardDownloadView, View.OnClickListener { it ->
            val etDownloadGame = boardDownloadView.findViewById<EditText>(R.id.etDownloadGame)
            val gameToDownload = etDownloadGame.text.toString().trim()
            downloadGame(gameToDownload)
        })
    }


    private fun showCreationDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize : RadioGroup = boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
        radioGroupSize.check(R.id.rbEasy)
        showAlertDialog("Choose your icon ", boardSizeView, View.OnClickListener {
            val desiredBoardSize = when (radioGroupSize.checkedRadioButtonId) {
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            val intent = Intent(this, CreateActivity::class.java)
            intent.putExtra(EXTRA_BOARD_SIZE, desiredBoardSize)
            // startActivityForResult if you want to get back the data for the activity
            startActivityForResult(intent, CREATE_REQUEST_CODE)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val customGameName = data?.getStringExtra(EXTRA_GAME_NAME)
            if (customGameName == null) {
                Log.e(TAG, "Got null custom game name from CreateActivity")
                return
            }
            downloadGame(customGameName)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun downloadGame(customGameName: String) {
        db.collection("games").document(customGameName).get().addOnSuccessListener {
            doc ->
            val userImageList = doc.toObject(UserImageList::class.java)
            if (userImageList?.images == null) {
                Log.e(TAG, "invalid custom game data from firestore")
                Snackbar.make(clRoot, "Sorry, we could not find any such game, '$customGameName'", Snackbar.LENGTH_LONG).show()
                return@addOnSuccessListener
            }
            val numCards = userImageList.images.size * 2
            boardSize = BoardSize.getByValue(numCards)
            customGameImages = userImageList.images
            for (imageUrl in userImageList.images) {
                Picasso.get().load(imageUrl).fetch()
            }
            Snackbar.make(clRoot, "You are not playing $customGameName", Snackbar.LENGTH_LONG).show()
            gameName = customGameName
            setUpBoard()
        }.addOnFailureListener {
            ex ->
            Log.e(TAG, "Exception when retrieving game", ex)
        }
    }

    private fun showNewSizeDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize : RadioGroup = boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
        when (boardSize) {
            BoardSize.EASY -> radioGroupSize.check(R.id.rbEasy)
            BoardSize.MEDIUM -> radioGroupSize.check(R.id.rbMedium)
            else -> radioGroupSize.check(R.id.rbHard)
        }
        showAlertDialog("Choose new size: ", boardSizeView, View.OnClickListener {
            boardSize = when (radioGroupSize.checkedRadioButtonId) {
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            gameName = null
            customGameImages = null
            setUpBoard()
        })
    }

    private fun showAlertDialog(title: String, view: View?, positionClickListener: View.OnClickListener) {
        AlertDialog.Builder(this).setTitle(title).setView(view).setNegativeButton("Cancel", null).setPositiveButton("OK") {
            _,_ -> positionClickListener.onClick(null)
        }.show()
    }

    private fun updateGameWithFlip(position: Int) {
        if (memoryGame.haveWonGame()) {
            Snackbar.make(clRoot, "You won!", Snackbar.LENGTH_LONG).show()
            return
        }
        if (memoryGame.isCardFaceUp(position)) {
            Snackbar.make(clRoot, "Invalid!", Snackbar.LENGTH_LONG).show()
            return
        }
        if (memoryGame.flipCard(position)) {
            val color: Int = ArgbEvaluator().evaluate(
                    memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                    ContextCompat.getColor(this, R.color.color_progress_none),
                    ContextCompat.getColor(this, R.color.color_progress_full)
            ) as Int
            tvNumberOfPairs.setTextColor(color)
            tvNumberOfPairs.text = "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
            if (memoryGame.haveWonGame()) {
                Snackbar.make(clRoot, "Congratulations!", Snackbar.LENGTH_LONG).show()
                CommonConfetti.rainingConfetti(clRoot, intArrayOf(Color.RED, Color.YELLOW, Color.GREEN)).oneShot()
            }
        }
        tvNumberOfMoves.text = "Moves: ${memoryGame.getNumberOfMoves()}"

        /* need to update the contents of the adapter */
        adapter.notifyDataSetChanged()
    }

    companion object {
        private const val CREATE_REQUEST_CODE = 248
        private const val TAG = "MainActivity"
    }

}