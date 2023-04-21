package com.example.doan3.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.reactivex.annotations.NonNull

@Entity(tableName = "products")
data class ProductModel(
    @PrimaryKey
    @NonNull
    val productID: String,
    @ColumnInfo(name = "productName")
    val productName: String? ="",
    @ColumnInfo(name = "productImg")
    val productImg: String? ="",
    @ColumnInfo(name = "productPrice")
    val productPrice: String? ="",
)