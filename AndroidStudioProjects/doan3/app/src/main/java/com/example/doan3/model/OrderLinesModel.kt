package com.example.doan3.model

data class OrderLinesModel(
    var img:String? ="",
    var name:String? ="",
    var orderID:String = "",
    var price:String = "",
    var prodID:String = "",
    var quantity:String = "",
) {
}