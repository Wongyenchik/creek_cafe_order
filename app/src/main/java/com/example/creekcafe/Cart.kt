package com.example.creekcafe

class Cart {
    var drinkName: String? = null
    var count: Int? = 0
    var coffeeType: String? = null
    var temperature: String? = null
    var txtPrice: Int? = 0
    var uid: String? = null
    var image: String? = null


    constructor(drinkName: String?, count:Int?, coffeeType:String?, temperature:String?, txtPrice:Int?, uid:String?, image:String?){
        this.drinkName = drinkName
        this.count = count
        this.coffeeType = coffeeType
        this.temperature = temperature
        this.txtPrice = txtPrice
        this.uid = uid
        this.image = image
    }
}