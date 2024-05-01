package com.example.creekcafe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DrinkDetailActivity : AppCompatActivity(), CoffeeItemClickListener, TemperatureItemClickListener {

    private lateinit var orderQuantity: TextView
    private var count: Int?= 1
    private var totalPrice: Int?= 0
    private var coffeePrice: Int = 0 // Store coffee price separately
    private var coffeeType: String? = null // Store coffee price separately
    private var temperaturePrice: Int = 0 // Store coffee price separately
    private var temperature: String? = null // Store coffee price separately
    private var auth = FirebaseAuth.getInstance()

    private lateinit var mDbRef: DatabaseReference
    private lateinit var temperatureList: ArrayList<Temperature>
    private lateinit var adapter: TemperatureAdapter
    private lateinit var recycler_view: RecyclerView
    private lateinit var recycler_viewCof: RecyclerView
    private lateinit var coffeeList: ArrayList<Coffee>
    private lateinit var Coffeeadapter: CoffeeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_detail)
        orderQuantity = findViewById<TextView>(R.id.txtQuantity)
        val btnIncrease = findViewById<Button>(R.id.btnIncrease)
        val btnDecrease = findViewById<Button>(R.id.btnDecrease)
        val backbtn = findViewById<Button>(R.id.btnBack)
        val btnAddToCart = findViewById<Button>(R.id.btnAddToCart)
        val drinkName = findViewById<TextView>(R.id.drinkName)
        val txtDescription = findViewById<TextView>(R.id.description)
        val txtPrice = findViewById<TextView>(R.id.grandTotal)

        val drinkImage = findViewById<ImageView>(R.id.drinkImage)
        val intent = intent
        val name = intent.getStringExtra("name")
        val image = intent.getStringExtra("image")
        val description = intent.getStringExtra("description")
        val price = intent.getIntExtra("price", 0)
        drinkName.text = name
        txtPrice.text = "RM $price"
        Glide.with(this)
            .load(image)
            .into(drinkImage)
        txtDescription.text = description
        backbtn.setOnClickListener{
            finish()
        }
        btnIncrease.setOnClickListener{
            increase() // Pass coffee price to increase function
        }

        btnDecrease.setOnClickListener{
            decrease() // Pass coffee price to decrease function
        }
        mDbRef = FirebaseDatabase.getInstance().getReference()
        temperatureList = ArrayList()
        coffeeList = ArrayList()
        adapter = TemperatureAdapter(this, temperatureList,this)
        Coffeeadapter = CoffeeAdapter(this, coffeeList, this) // Pass the activity instance as the listener

        recycler_view = findViewById(R.id.recTemperature)
        recycler_view.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        recycler_view.adapter = adapter

        recycler_viewCof = findViewById(R.id.recCoffee)
        recycler_viewCof.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        recycler_viewCof.adapter = Coffeeadapter


        mDbRef.child("drink").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                temperatureList.clear()
                for(categorySnapshot in snapshot.children){
                    for (drinkSnapshot in categorySnapshot.children) {
                        val currentDrink = drinkSnapshot.getValue(Drink::class.java)
                        if(currentDrink?.name == name){
                            for (temperatureSnapshot in drinkSnapshot.children) {
                                if (temperatureSnapshot.key == "temperature"){
                                    for (tempSnapshot in temperatureSnapshot.children){
                                        val temperature = tempSnapshot.child("temperature").getValue(String::class.java)
                                        val price = tempSnapshot.child("price").getValue(Int::class.java)
                                        val temperature1 = Temperature(price, temperature )
                                        temperatureList.add(temperature1)

                                    }
                                }
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")

            }

        })

        mDbRef.child("drink").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                coffeeList.clear()
                for(categorySnapshot in snapshot.children){
                    for (drinkSnapshot in categorySnapshot.children) {
                        val currentDrink = drinkSnapshot.getValue(Drink::class.java)
                        if(currentDrink?.name == name){
                            for (temperatureSnapshot in drinkSnapshot.children) {
                                if (temperatureSnapshot.key == "coffee"){
                                    for (tempSnapshot in temperatureSnapshot.children){
                                        val coffee = tempSnapshot.child("coffee").getValue(String::class.java)
                                        val price1 = tempSnapshot.child("price").getValue(Int::class.java)
                                        val coffee1 = Coffee(price1, coffee)
                                        coffeeList.add(coffee1)
                                    }
                                }
                            }
                        }
                    }
                }
                Coffeeadapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")

            }

        })

        btnAddToCart.setOnClickListener{
            if (coffeeType != null && temperature != null ) {

                count?.let { it1 ->
                        addCartToDatabase(
                            drinkName.text.toString(),
                            it1,
                            coffeeType.toString(),
                            temperature.toString(),
                            totalPrice,
                            auth.currentUser?.uid!!,
                            image
                        )
                }
                finish()
                Toast.makeText(this, "Your drink have added to cart", Toast.LENGTH_SHORT).show()
            } else if (temperature == null){
                Toast.makeText(this, "Please select your coffee temperature", Toast.LENGTH_SHORT).show()
            } else if (coffeeType == null){
                Toast.makeText(this, "Please select a coffee type", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun addCartToDatabase(
        drinkName: String?, count:Int?, coffeeType:String?, temperature:String?, txtPrice:Int?, uid:String?, image:String?){
        mDbRef = FirebaseDatabase.getInstance().getReference()
        if (uid != null) {
            if (drinkName != null) {
                mDbRef.child("cart").child(uid).child(drinkName).setValue(txtPrice?.let {
                    Cart(drinkName,count,coffeeType,temperature,
                        it, uid, image)
                })
            }
        }
    }


    fun increase() {
        count = count?.plus(1)
        orderQuantity.text = count.toString()
        updatePrice()
    }

    fun decrease() {
        if (count!! <= 1) {
            count = 1
        } else {
            count = count?.minus(1)
            orderQuantity.text = count.toString()
            updatePrice()
        }
    }


    private fun updatePrice() {
        val intent = intent
        val txtPrice = findViewById<TextView>(R.id.grandTotal)
        val price = intent.getIntExtra("price", 0)
        totalPrice = (price + coffeePrice + temperaturePrice) * count!!  // Calculate total price
        txtPrice.text = "RM $totalPrice"
    }


    override fun onCoffeeItemClicked(coffeePrice: Int, coffeeType: String) {
        this.coffeePrice = coffeePrice
        this.coffeeType = coffeeType
        updatePrice() // Update the total price in the activity

    }

    override fun onTemperatureItemClicked(temperaturePrice: Int, temperature: String) {
        this.temperaturePrice = temperaturePrice
        this.temperature = temperature
        updatePrice()
    }


}