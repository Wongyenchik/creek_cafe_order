package com.example.creekcafe

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.api.Distribution.BucketOptions.Linear
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchActivity : AppCompatActivity() {
    private lateinit var recycler_view: RecyclerView
    private lateinit var drinkList: ArrayList<Drink>
    private lateinit var adapter: DrinkAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backbtn = findViewById<Button>(R.id.btnBack)
        backbtn.setOnClickListener{
            finish()
        }
        mDbRef = FirebaseDatabase.getInstance().getReference()
        drinkList = ArrayList()
        adapter = DrinkAdapter(this, drinkList)

        recycler_view = findViewById(R.id.listSearchDrink)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter


        mDbRef.child("drink").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                drinkList.clear()
                for(categorySnapshot in snapshot.children){
                    for (drinkSnapshot in categorySnapshot.children) {
                        val currentDrink = drinkSnapshot.getValue(Drink::class.java)
                        drinkList.add(currentDrink!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")

            }

        })

        val searchView2 = findViewById<SearchView>(R.id.searchView2)
        searchView2.setIconified(false)
        searchView2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission (if needed)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the drinkList based on the new text
                newText?.let { text ->
                    val filteredList = drinkList.filter { drink ->
                        drink.name?.contains(text, ignoreCase = true) == true
                    }
                    adapter.setData(filteredList)

                }
                return true
            }
        })


    }


}
