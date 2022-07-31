package com.example.mymemoryapplication.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemoryapplication.R
import com.example.mymemoryapplication.clicklisteners.ImageClickListener
import com.example.mymemoryapplication.constants.BoardSize
import kotlin.math.min

class ImagePickerAdapter(
        private val context: Context,
        private val chosenImageUrls: List<Uri>,
        private val boardSize: BoardSize,
        private val imageClickListener: ImageClickListener
) : RecyclerView.Adapter<ImagePickerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_image, parent, false)
        val cardWidth = parent.width / boardSize.getWidth()
        val cardHeight = parent.height / boardSize.getHeight()
        val cardSideLength = min(cardWidth, cardHeight)
        val layoutParams = view.findViewById<ImageView>(R.id.ivCustomImage).layoutParams
        layoutParams.width = cardSideLength
        layoutParams.height = cardSideLength
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return boardSize.getNumPairs()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < chosenImageUrls.size) {
            holder.bind(chosenImageUrls[position])
        } else {
            holder.bind()
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        private val ivCustomImage = itemView.findViewById<ImageView>(R.id.ivCustomImage)

        fun bind(uri: Uri) {
            ivCustomImage.setImageURI(uri)

            // once you set the image, you cannot change it
            ivCustomImage.setOnClickListener(null)
        }

        fun bind() {
            ivCustomImage.setOnClickListener{
                // launch user intent to select photos
                imageClickListener.onPlaceHolderClick()
            }
        }

    }

}
