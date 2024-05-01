package com.example.creekcafe

class Order {
    var drinkName: String? = null
    var count: Int? = 0
    var coffeeType: String? = null
    var temperature: String? = null
    var txtPrice: Int? = 0
    var uid: String? = null
    var oid: String? = null
    var time: String? = null
    var grandTotalFormatted: String? = null
    var status: String? = null

    constructor()

    constructor(
        drinkName: String?, count:Int?, coffeeType:String?, temperature:String?, txtPrice:Int?, uid:String?,
        oid: String?,
        time: String?,grandTotalFormatted: String?,status: String?
    ){
        this.drinkName = drinkName
        this.count = count
        this.coffeeType = coffeeType
        this.temperature = temperature
        this.txtPrice = txtPrice
        this.uid = uid
        this.oid = oid
        this.time = time
        this.grandTotalFormatted = grandTotalFormatted
        this.status = status
    }
}