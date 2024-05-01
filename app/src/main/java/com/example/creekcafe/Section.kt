package com.example.creekcafe

class Section {
    var sectionTitle: String?= null
    lateinit var allItemsInSection: List<Drink>

    constructor()

    constructor(sectionTitle: String?, allItemsInSection:List<Drink>){
        if (sectionTitle != null) {
            this.sectionTitle = sectionTitle
        }
        this.allItemsInSection = allItemsInSection
    }
}