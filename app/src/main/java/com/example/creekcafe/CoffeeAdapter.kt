package com.example.creekcafe

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


interface CoffeeItemClickListener {
    fun onCoffeeItemClicked(coffeePrice: Int, coffeeType: String)
}

class CoffeeAdapter(val context: Context, var coffeeList: ArrayList<Coffee>, private val listener: CoffeeItemClickListener):
    RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder>(){

    private var selectedPosition = RecyclerView.NO_POSITION // Initially set to no position

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoffeeViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.customise_layout, parent, false)
        return CoffeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeAdapter.CoffeeViewHolder, position: Int) {
        val currentCoffee = coffeeList[position]
        val priceText = if (currentCoffee.price == 0) {
            "Default"
        } else {
            "+RM ${currentCoffee.price}"
        }
        holder.drink_price.text = priceText
        holder.drink_temperature.text = currentCoffee.coffee

        if (position == selectedPosition) {
            val drawable = ContextCompat.getDrawable(context, R.drawable.button_background) // Get the drawable from resources
            holder.itemView.background = drawable // Set the drawable as the background
            holder.drink_temperature.setTextColor(Color.WHITE)
            holder.drink_price.setTextColor(Color.WHITE)
        } else {
            val drawable1 = ContextCompat.getDrawable(context, R.drawable.choice_button_background) // Get the drawable from resources
            holder.itemView.background = drawable1 // Set the drawable as the background
            holder.drink_temperature.setTextColor(Color.BLACK)
            holder.drink_price.setTextColor(Color.BLACK)

        }
        holder.itemView.setOnClickListener{
            notifyItemChanged(selectedPosition) // Clear previous selection
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition) // Highlight the clicked item
            coffeeList[position].price?.let { it1 ->
                val type = coffeeList[position].coffee
                if (type != null) {
                    listener.onCoffeeItemClicked(it1,type)
                }
            } // Notify activity about the coffee price


        }
    }

    override fun getItemCount(): Int {
        return coffeeList.size
    }

    class CoffeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val drink_temperature = itemView.findViewById<TextView>(R.id.drink_temperature)
        val  drink_price= itemView.findViewById<TextView>(R.id.drink_price)
    }
}