package com.example.creekcafe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sectionedrecyclerview.CurrentOrderAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CurrentOrder : Fragment() {

    private lateinit var orderList: MutableList<Order>
    private lateinit var recycler_view: RecyclerView
    private var auth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_current_order, container, false)
        recycler_view = view.findViewById(R.id.order_history)
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderList = ArrayList()

        val orderTotal: MutableList<OrderTotal> = ArrayList()
        val adapter = CurrentOrderAdapter(requireContext(), orderTotal)

        recycler_view.adapter = adapter
        val mDbRef = FirebaseDatabase.getInstance().getReference().child("order").child(auth.currentUser?.uid!!)
        mDbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orderList.clear()
                    for(drinkSnapshot in snapshot.children){
                        val orderID = drinkSnapshot.child("oid").getValue(String::class.java)
                        val status = drinkSnapshot.child("status").getValue(String::class.java)

                        val totalPrice = drinkSnapshot.child("totalPrice").getValue(String::class.java)
                        for (drinkSnapshot1 in drinkSnapshot.child("drink").children) {
                            val currentDrink = drinkSnapshot1.getValue(Order::class.java) // Get the name of the drink
                            orderList.add(currentDrink!!)
                        }

                        orderTotal.add(OrderTotal(orderID, status, totalPrice,orderList))

                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }
}