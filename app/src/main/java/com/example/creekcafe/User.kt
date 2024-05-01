package com.example.creekcafe

class User {
    var name: String? = null
    var phoneNo: String? = null
    var birthDate: String? = null
    var email: String? = null
    var uid: String? = null

    constructor(name: String?, phoneNo:String?, birthDate:String?, email:String?, uid:String?){
        this.name = name
        this.phoneNo = phoneNo
        this.birthDate = birthDate
        this.email = email
        this.uid = uid
    }
}