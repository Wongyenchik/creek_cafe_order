package com.example.sectionedrecyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.creekcafe.R
import com.example.creekcafe.Section


class SectionAdapter(private val context: Context, private val sectionList: List<Section>) :
    RecyclerView.Adapter<SectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = sectionList[position]
        holder.bind(section)
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sectionName: TextView = itemView.findViewById(R.id.section_title)
        private val itemRecyclerView: RecyclerView = itemView.findViewById(R.id.item_recycler_view)
        private lateinit var itemAdapter: ItemAdapter

        fun bind(section: Section) {
            sectionName.text = section.sectionTitle

            // RecyclerView for items
            val gridLayoutManager = GridLayoutManager(context, 2)
            itemRecyclerView.setLayoutManager(gridLayoutManager) // set LayoutManager to RecyclerView
            val filteredItems = section.allItemsInSection.filter { it.category.equals(section.sectionTitle, ignoreCase = true) }


//            itemRecyclerView.layoutManager = linearLayoutManager
            itemAdapter = ItemAdapter(filteredItems)
            itemRecyclerView.adapter = itemAdapter
        }
    }

}
