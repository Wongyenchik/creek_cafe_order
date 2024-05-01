package com.example.creekcafe

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sectionedrecyclerview.SectionAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Menu : Fragment(), CategoryAdapter.OnCategoryClickListener {
    private lateinit var categoryList: ArrayList<Category>
    private lateinit var adapter: CategoryAdapter
    private lateinit var recycler_view: RecyclerView
    private lateinit var recyclerView: RecyclerView

    private lateinit var itemArrayList: MutableList<Drink>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchView = requireView().findViewById<ConstraintLayout>(R.id.searchView)
        searchView.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }
        val mDbRef = FirebaseDatabase.getInstance().getReference()
        categoryList = ArrayList()
        adapter = CategoryAdapter(requireContext(), categoryList, this)

        recycler_view = requireActivity().findViewById(R.id.recTemperature)
        recycler_view.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)

        recycler_view.adapter = adapter
        val imageButton = requireActivity().findViewById<ImageButton>(R.id.imageButton)
        imageButton.setOnClickListener{
            val cartRef = FirebaseDatabase.getInstance().getReference().child("cart")
            cartRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        // Cart doesn't exist, show a toast
                        Toast.makeText(requireContext(), "No drink in your cart", Toast.LENGTH_SHORT).show()
                    } else {
                        // Cart exists, proceed with the intent
                        startActivity(Intent(requireContext(), CheckoutActivity::class.java))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled
//                    Log.e("MenuFragment", "Database error: ${error.message}")
                }
            })
        }
        mDbRef.child("category").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryList.clear()
                for(postSnapshot in snapshot.children){
                    val category = postSnapshot.getValue(Category::class.java)
                    categoryList.add(category!!)
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })

        recyclerView = requireActivity().findViewById(R.id.menu_homepage)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager

        val sections: MutableList<Section> = ArrayList()
        itemArrayList = ArrayList()
        val adapter = SectionAdapter(requireContext(), sections)
        recyclerView.adapter = adapter

        mDbRef.child("drink").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemArrayList.clear()
                for(categorySnapshot in snapshot.children){
                    val categoryKey = categorySnapshot.key
                    for(drinkSnapshot in categorySnapshot.children){
                        val currentDrink = drinkSnapshot.getValue(Drink::class.java)
                        itemArrayList.add(currentDrink!!)
                        sections.add(Section(categoryKey, itemArrayList))

                    }

                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }
    override fun onCategoryClicked(category: String) {
        // Find the position of the section with the given category
        val position = itemArrayList.indexOfFirst { it.category.equals(category, ignoreCase = true) }
        if (position != -1) {
            // Scroll to the section
            recyclerView.smoothScrollToPosition(position)
        }
    }

}