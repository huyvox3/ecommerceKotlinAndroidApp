package com.example.doan3.model

data class OrderLineModel (
    var orderId:String?="",
    var userId:String?="",
    var productId: String? = "",
    var productName: String? = "",
    var productCoverImage: String? = "",
    var productQt: String? = "",
    var productP: String? ="",
    var status:String? =""
        ){

}