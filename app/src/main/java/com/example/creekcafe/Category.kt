package com.example.creekcafe

class Category {
    var category: String? = null
    var picture: String? = null

    constructor()
    constructor(category: String?, picture:String?){
        this.category = category
        this.picture = picture
    }
}