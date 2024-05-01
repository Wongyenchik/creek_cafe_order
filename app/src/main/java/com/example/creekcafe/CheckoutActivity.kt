package com.example.creekcafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CheckoutActivity : AppCompatActivity() {
    private lateinit var mDbRef: DatabaseReference
    private var auth = FirebaseAuth.getInstance()
    private lateinit var cartList: ArrayList<Cart>
    private lateinit var adapter: CheckOutAdapter
    private lateinit var recycler_view: RecyclerView
    private var grandTotalFormatted: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        cartList = ArrayList()
        adapter = CheckOutAdapter(this, cartList)

        recycler_view = findViewById(R.id.checkoutItem)
        val linearLayoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = linearLayoutManager
        recycler_view.adapter = adapter
        mDbRef = FirebaseDatabase.getInstance().getReference()
        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener{
            finish()
        }

        val totalPrice = findViewById<TextView>(R.id.totalPrice)
        val sst = findViewById<TextView>(R.id.sst)
        val serviceTax = findViewById<TextView>(R.id.serviceTax)
        val grandTotalPrice = findViewById<TextView>(R.id.grandTotalPrice)
        val grandTotal = findViewById<TextView>(R.id.grandTotal)
        mDbRef.child("cart").child(auth.currentUser?.uid!!).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartList.clear()
                for(drinkSnapshot in snapshot.children){
                    val name = drinkSnapshot.child("drinkName").getValue(String::class.java)
                    val coffeeType = drinkSnapshot.child("coffeeType").getValue(String::class.java)
                    val temperature = drinkSnapshot.child("temperature").getValue(String::class.java)
                    val uid = drinkSnapshot.child("uid").getValue(String::class.java)
                    val image = drinkSnapshot.child("image").getValue(String::class.java)
                    val txtPrice = drinkSnapshot.child("txtPrice").getValue(Int::class.java)
                    val count = drinkSnapshot.child("count").getValue(Int::class.java)
                    if (name != null && coffeeType != null && temperature != null && uid != null && image != null && txtPrice != null && count != null) {
                        val cartItem = Cart(name, count, coffeeType, temperature, txtPrice, uid, image)
                        cartList.add(cartItem)
                    }
                }
                adapter.notifyDataSetChanged()

                val totalPrice1 = adapter.calculateTotalPrice()
                totalPrice.text = "RM $totalPrice1" // Adjust as needed
                val sstPercent = totalPrice1.toDouble() * 6 / 100
                val sstPercentFormatted = String.format("%.2f", sstPercent)
                sst.text = "RM $sstPercentFormatted"
                val servicePercent = totalPrice1.toDouble() * 10 / 100
                val servicePercentFormatted = String.format("%.2f", servicePercent)
                serviceTax.text = "RM $servicePercentFormatted"
                val grandtotal = totalPrice1.toDouble() + sstPercent + servicePercent
                grandTotalFormatted = String.format("%.2f", grandtotal)
                grandTotalPrice.text = "RM $grandTotalFormatted"
                grandTotal.text = grandTotalPrice.text
                if (grandtotal == 0.00){
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")

            }

        })

        val btnPlaceOrder = findViewById<Button>(R.id.btnPlaceOrder)
        btnPlaceOrder.setOnClickListener {
            val currentUserUid = auth.currentUser?.uid

            // Reference to user's cart items
            val cartRef = mDbRef.child("cart").child(currentUserUid!!)
            cartRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val orderRef = mDbRef.child("order").child(currentUserUid)

                    // Generate a unique order ID
                    val orderId = orderRef.push().key

                    // Iterate through cart items
                    snapshot.children.forEach { drinkSnapshot ->
                        val name = drinkSnapshot.child("drinkName").getValue(String::class.java)
                        val coffeeType = drinkSnapshot.child("coffeeType").getValue(String::class.java)
                        val temperature = drinkSnapshot.child("temperature").getValue(String::class.java)
                        val uid = drinkSnapshot.child("uid").getValue(String::class.java)
                        val txtPrice = drinkSnapshot.child("txtPrice").getValue(Int::class.java)
                        val count = drinkSnapshot.child("count").getValue(Int::class.java)
                        val timestamp = System.currentTimeMillis().toString()

                        val status = "In Progress"

                        // Add cart item to order history
                        orderRef.child(orderId!!).child("drink").child(name!!).setValue(Order(name, count, coffeeType, temperature, txtPrice, uid,orderId, timestamp,grandTotalFormatted,status))
//                        orderRef.child(orderId!!).setValue(grandTotalFormatted)
                        orderRef.child(orderId!!).child("totalPrice").setValue(grandTotalFormatted)
                        orderRef.child(orderId!!).child("oid").setValue(orderId)
                        orderRef.child(orderId!!).child("status").setValue(status)

                        // Remove cart item
                        drinkSnapshot.ref.removeValue()
                    }

                    // Start OrderHistoryActivity
                    startActivity(Intent(this@CheckoutActivity, OrderHistoryActivity::class.java))
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled
                }
            })
        }

    }
}