package com.example.mymemoryapplication.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemoryapplication.R
import com.example.mymemoryapplication.clicklisteners.CardClickListener
import com.example.mymemoryapplication.constants.BoardSize
import com.example.mymemoryapplication.models.MemoryCardModel
import com.squareup.picasso.Picasso
import kotlin.math.min

class MemoryBoardAdapter(
        private val context: Context,
        private val boardSize: BoardSize,
        private val memoryCards: List<MemoryCardModel>,
        private val cardClickListener: CardClickListener
) : RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("I", "Logging... ${parent.width}")
        val cardWidth: Int = parent.width / boardSize.getWidth() - (2 * MARGIN_SIZE)
        val cardHeight: Int = parent.height / boardSize.getHeight() - (2 * MARGIN_SIZE)
        val cardSideLength: Int = min(cardWidth, cardHeight)

        // Getting the memory card xml
        val view = LayoutInflater.from(context).inflate(R.layout.memory_card, parent, false)

        // Getting the layoutParams of the memory card xml
        val layoutParams: ViewGroup.MarginLayoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams

        // editing the layoutParams of the memory card xml
        layoutParams.width = cardSideLength
        layoutParams.height = cardSideLength
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)

        // returning the memory card xml
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return boardSize.numCards
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)
        fun bind(position: Int) {
            val memoryCard: MemoryCardModel = memoryCards[position]
            if (memoryCard.isFaceUp) {
                if (memoryCard.imageUrl != null) {
                    // while picasso is downloading image, can use placeholder as a temp image till image is fully downloaded and ready
                    Picasso.get().load(memoryCard.imageUrl).placeholder(R.drawable.ic_image).into(imageButton)
                } else {
                    imageButton.setImageResource(memoryCards[position].identifier)
                }
            }
            else {
                imageButton.setImageResource(R.drawable.meme)
            }
//            imageButton.setImageResource(
//                    if (memoryCards[position].isFaceUp) memoryCards[position].identifier
//                    else R.drawable.ic_launcher_background
//            )
            imageButton.alpha = if (memoryCards[position].isMatched) .4f else 1.0f
            val colorStateList: ColorStateList? = if (memoryCards[position].isMatched) ContextCompat.getColorStateList(context, R.color.color_gray) else null
            ViewCompat.setBackgroundTintList(imageButton, colorStateList)
            imageButton.setOnClickListener {
                Log.i("I", "IMAGE_BUTTON: Clicked button at position $position, print details ... $it")
                cardClickListener.onCardClicked(position)
            }
        }
    }
    companion object {
        private const val MARGIN_SIZE = 10
        private const val TAG = "Tapped..."
    }
}
