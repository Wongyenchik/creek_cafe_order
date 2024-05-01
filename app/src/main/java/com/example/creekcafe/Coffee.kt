package com.example.creekcafe

class Coffee {
    var price: Int? = 0
    var coffee: String? = null

    constructor()

    constructor(price: Int?, coffee:String?){
        this.price = price
        this.coffee = coffee
    }
}