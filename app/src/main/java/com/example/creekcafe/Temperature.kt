package com.example.creekcafe

class Temperature {
    var price: Int? = 0
    var temperature: String? = null

    constructor()

    constructor(price: Int?, temperature:String?){
        this.price = price
        this.temperature = temperature
    }
}