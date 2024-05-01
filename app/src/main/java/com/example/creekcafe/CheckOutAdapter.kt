package com.example.creekcafe

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class CheckOutAdapter(val context: Context, var checkOutList: ArrayList<Cart>):
    RecyclerView.Adapter<CheckOutAdapter.CheckOutViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckOutViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.check_out_layout, parent, false)
        return CheckOutViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckOutAdapter.CheckOutViewHolder, position: Int) {
        val currentDrink = checkOutList[position]
        holder.drinkName_.text = currentDrink.drinkName
        holder.drink_custom.text = "${currentDrink.coffeeType} | ${currentDrink.temperature}"
        holder.drinkPrice_.text = "RM ${currentDrink.txtPrice}"
        holder.txtQuantity_.text = currentDrink.count.toString()
        Glide.with(context).load(checkOutList.get(position).image).into(holder.drink_image_)
        holder.btnIncrease_.setOnClickListener{
            increase(checkOutList[position], holder)
        }
        holder.btnDecrease_.setOnClickListener{
            decrease(checkOutList[position], holder)
        }
        val shape = GradientDrawable().apply {
            cornerRadii = floatArrayOf(
                50f, 50f,  // Top left corner radius
                50f, 50f,  // Top right corner radius
                50f, 50f,    // Bottom right corner radius
                50f, 50f     // Bottom left corner radius
            )
        }

        // Set the custom shape as the background of the CardView
        holder.checkoutCard.background = shape
//
            holder.deleteBtn.setOnClickListener{
                val currentItem = checkOutList[position]
                val databaseRef = FirebaseDatabase.getInstance().reference
                val currentUser = FirebaseAuth.getInstance().currentUser
                val userId = currentUser?.uid

                // Assuming your items are stored under a "cart" node with user-specific nodes
                val cartRef = databaseRef.child("cart").child(userId!!).child(currentItem.drinkName!!)

                // Remove the item from Firebase
                cartRef.removeValue()
            }
        }


    private fun increase(currentDrink: Cart, holder: CheckOutAdapter.CheckOutViewHolder) {
        val oriCount = currentDrink.count
        currentDrink.count = currentDrink.count?.plus(1)
        holder.txtQuantity_.text = currentDrink.count.toString()
        updatePrice(currentDrink,holder,oriCount)
    }
    private fun decrease(currentDrink: Cart, holder: CheckOutAdapter.CheckOutViewHolder) {
        if (currentDrink.count!! <= 1) {
            currentDrink.count = 1
        } else {
            val oriCount = currentDrink.count
            currentDrink.count = currentDrink.count?.minus(1)
            holder.txtQuantity_.text = currentDrink.count.toString()
            updatePrice(currentDrink,holder,oriCount)
        }
    }


    private fun updatePrice(currentDrink: Cart, holder: CheckOutAdapter.CheckOutViewHolder, oriCount: Int?) {
        val pricePerDrink = currentDrink.txtPrice!! / oriCount!!  // Calculate total price
        currentDrink.txtPrice = pricePerDrink * currentDrink.count!!  // Calculate total price
        holder.drinkPrice_.text = "RM ${currentDrink.txtPrice}"
        val databaseRef = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        // Assuming your items are stored under a "cart" node with user-specific nodes
        val cartRef = databaseRef.child("cart").child(userId!!).child(currentDrink.drinkName!!)
        cartRef.child("txtPrice").setValue(currentDrink.txtPrice)
        cartRef.child("count").setValue(currentDrink.count)

    }


    override fun getItemCount(): Int {
        return checkOutList.size
    }

    fun calculateTotalPrice(): Int {
        var totalPrice = 0
        for (item in checkOutList) {
            totalPrice += item.txtPrice!!
        }
        return totalPrice
    }

    class CheckOutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val drinkName_ = itemView.findViewById<TextView>(R.id.drinkName_)
        val drink_custom = itemView.findViewById<TextView>(R.id.drink_custom)
        val drinkPrice_ = itemView.findViewById<TextView>(R.id.drinkPrice_)
        val drink_image_ = itemView.findViewById<ImageView>(R.id.drink_image_)
        val btnIncrease_ = itemView.findViewById<Button>(R.id.btnIncrease_)
        val txtQuantity_ = itemView.findViewById<TextView>(R.id.txtQuantity_)
        val btnDecrease_ = itemView.findViewById<Button>(R.id.btnDecrease_)
        val deleteBtn = itemView.findViewById<ImageButton>(R.id.deleteBtn)
        val checkoutCard = itemView.findViewById<CardView>(R.id.checkoutCard)
    }
}