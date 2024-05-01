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
import com.example.creekcafe.Order
import com.example.creekcafe.R

class OrderItemAdapter(private val items: List<Order>) : RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = items[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val drinkName: TextView = itemView.findViewById(R.id.drinkname)
        private val drinkcustomise: TextView = itemView.findViewById(R.id.drinkcustomise)
        private val orderquantity: TextView = itemView.findViewById(R.id.orderquantity)

        fun bind(order: Order ) {
            drinkName.text = order.drinkName
            drinkcustomise.text = "${order.coffeeType} | ${order.temperature}"
            orderquantity.text = "${order.count}"
        }
    }
}
