package com.example.creekcafe

class OrderTotal {
    var orderID: String?= null
    var status: String?= null
    var totalPrice: String?= null
    lateinit var allItemsInSection: List<Order>

    constructor()

    constructor(orderID: String?, status: String?, totalPrice: String?,allItemsInSection: List<Order>){
        this.orderID = orderID
        this.status = status
        this.totalPrice = totalPrice
        this.allItemsInSection = allItemsInSection
    }
}