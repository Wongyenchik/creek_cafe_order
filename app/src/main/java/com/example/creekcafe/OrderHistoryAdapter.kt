package com.example.sectionedrecyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.creekcafe.OrderTotal
import com.example.creekcafe.R
import com.example.creekcafe.Section


class OrderHistoryAdapter(private val context: Context, private val OrderHistoryList: MutableList<OrderTotal>) :
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDrink = OrderHistoryList[position]
        holder.bind(currentDrink)

    }

    override fun getItemCount(): Int {
        return OrderHistoryList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val totalPrice = itemView.findViewById<TextView>(R.id.totalPrice)
        val orderID = itemView.findViewById<TextView>(R.id.orderID)
        val orderDetail = itemView.findViewById<RecyclerView>(R.id.orderDetail)
        val status = itemView.findViewById<TextView>(R.id.status)
        val checkoutCard = itemView.findViewById<CardView>(R.id.checkoutCard)
        private lateinit var itemAdapter: OrderItemAdapter

        fun bind(order: OrderTotal) {
            if (order.status.equals("Order Done", ignoreCase = true)) {
                totalPrice.text = order.totalPrice
                orderID.text = order.orderID
                status.text = order.status
                // RecyclerView for items
                val linearLayoutManager = LinearLayoutManager(context)
                orderDetail.layoutManager = linearLayoutManager
                val filteredItems = order.allItemsInSection.filter {it.oid == order.orderID }


//            itemRecyclerView.layoutManager = linearLayoutManager
                itemAdapter = OrderItemAdapter(filteredItems)
                orderDetail.adapter = itemAdapter
            } else {
                checkoutCard.visibility = View.GONE // Hide the checkoutCard
            }


        }
    }
    }
