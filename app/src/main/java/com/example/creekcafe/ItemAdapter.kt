package com.example.sectionedrecyclerview

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.creekcafe.Drink
import com.example.creekcafe.DrinkDetailActivity
import com.example.creekcafe.R

class ItemAdapter(private val items: List<Drink>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DrinkDetailActivity::class.java)

            intent.putExtra("name",item.name)
            intent.putExtra("description",item.description)
            intent.putExtra("price", item.price)
            intent.putExtra("image", item.image)
            holder.itemView.context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val drinkName: TextView = itemView.findViewById(R.id.Txtdrink_Name)
        private val drinkPrice: TextView = itemView.findViewById(R.id.Txtdrink_price)
        private val imageDrink: ImageView = itemView.findViewById(R.id.imageDrink)
        private val cardView = itemView.findViewById<CardView>(R.id.cardView2)

        // Create a custom shape with rounded corners only on top left and top right
        val shape = GradientDrawable().apply {
            cornerRadii = floatArrayOf(
                50f, 50f,  // Top left corner radius
                50f, 50f,  // Top right corner radius
                0f, 0f,    // Bottom right corner radius
                0f, 0f     // Bottom left corner radius
            )
        }

        init {
            // Set the custom shape as the background of the CardView
            cardView.background = shape
        }

        fun bind(item: Drink ) {
            drinkName.text = item.name
            drinkPrice.text = "RM ${item.price}"
            Glide.with(itemView)
                .load(item.image).into(imageDrink)
        }
    }
}
