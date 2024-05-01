package com.example.creekcafe

class Drink {
    var category: String? = null
    var image: String? = null
    var name: String? = null
    var price: Int? = null
    var description: String? = null

    constructor()

    constructor(category: String?, image:String?, name:String?, price:Int?, description:String?){
        this.category = category
        this.image = image
        this.name = name
        this.price = price
        this.description = description
    }
}