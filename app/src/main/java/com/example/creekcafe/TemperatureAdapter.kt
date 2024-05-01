package com.example.creekcafe

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

interface TemperatureItemClickListener {
    fun onTemperatureItemClicked(coffeePrice: Int, coffeeTemperature: String)
}


class TemperatureAdapter(val context: Context, var temperatureList: ArrayList<Temperature>, private val listener: TemperatureItemClickListener):
    RecyclerView.Adapter<TemperatureAdapter.TemperatureViewHolder>(){
    private var selectedPosition = RecyclerView.NO_POSITION // Initially set to no position

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TemperatureViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.customise_layout, parent, false)
        return TemperatureViewHolder(view)
    }

    override fun onBindViewHolder(holder: TemperatureViewHolder, position: Int) {
        val currentTemperature = temperatureList[position]
        val priceText = if (currentTemperature.price == 0) {
            "Default"
        } else {
            "+RM ${currentTemperature.price}"
        }
        holder.drink_price.text = priceText
        holder.drink_temperature.text = currentTemperature.temperature
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
            temperatureList[position].price?.let { it1 ->
                val coffeeTemperature = temperatureList[position].temperature
                if (coffeeTemperature != null) {
                    listener.onTemperatureItemClicked(it1,coffeeTemperature)
                }
            } // Notify activity about the coffee price

        }
    }

    override fun getItemCount(): Int {
        return temperatureList.size
    }

    class TemperatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val drink_temperature = itemView.findViewById<TextView>(R.id.drink_temperature)
        val  drink_price= itemView.findViewById<TextView>(R.id.drink_price)
    }


}
