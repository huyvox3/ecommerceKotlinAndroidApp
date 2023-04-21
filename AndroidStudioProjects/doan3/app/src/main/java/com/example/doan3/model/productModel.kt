package com.example.doan3.model

data class productModel(var productName: String? = "",
                   var productDescription: String? = "",
                   var productCoverImage: String? = "",
                   var productCategory: String? = "",
                   var productId: String? = "",
                   var productQt: String? = "",
                   var productP: String? ="",
                   var productImgs: ArrayList<String>? =null,

) {

    constructor() : this(null, null, null, null, null, null, null, null)

}