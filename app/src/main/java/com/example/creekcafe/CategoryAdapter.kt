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


class CategoryAdapter(val context: Context, var categoryList: ArrayList<Category>, private val clickListener: OnCategoryClickListener):
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.section_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryViewHolder, position: Int) {
        val currentCategory = categoryList[position]
        holder.category_drink.text = currentCategory.category
        Glide.with(context).load(categoryList.get(position).picture).into(holder.category_image)

//
        holder.itemView.setOnClickListener{
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val currentCategory = categoryList[position]
                currentCategory.category?.let { it1 -> clickListener.onCategoryClicked(it1) }
            }

        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    interface OnCategoryClickListener {
        fun onCategoryClicked(category: String)
    }


    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val category_drink = itemView.findViewById<TextView>(R.id.category_drink)
        val category_image = itemView.findViewById<ImageView>(R.id.category_image)
    }
}