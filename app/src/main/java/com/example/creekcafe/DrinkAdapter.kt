package com.example.creekcafe

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class DrinkAdapter(val context: Context, var drinkList: ArrayList<Drink>):
    RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DrinkViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.search_layout, parent, false)
        return DrinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrinkAdapter.DrinkViewHolder, position: Int) {
        val currentDrink = drinkList[position]
        holder.drink_name.text = currentDrink.name
        holder.drink_category.text = currentDrink.category
        holder.drink_price.text = "RM ${currentDrink.price}"
//        Picasso.get().load(currentDrink.image).into(holder.drink_image)
        Glide.with(context).load(drinkList.get(position).image).into(holder.drink_image)

//
        holder.itemView.setOnClickListener{
            val intent = Intent(context,DrinkDetailActivity::class.java)

            intent.putExtra("name",currentDrink.name)
            intent.putExtra("description",currentDrink.description)
            intent.putExtra("price", currentDrink.price)
            intent.putExtra("image", currentDrink.image)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return drinkList.size
    }

    fun setData(filteredList: List<Drink>) {
        drinkList = filteredList as ArrayList<Drink>
        notifyDataSetChanged()
    }

    class DrinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val drink_category = itemView.findViewById<TextView>(R.id.drink_category)
        val drink_name = itemView.findViewById<TextView>(R.id.drink_Name)
        val drink_price = itemView.findViewById<TextView>(R.id.drinkPrice)
        val drink_image = itemView.findViewById<ImageView>(R.id.drink_image)
    }
}