package com.example.doan3.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.doan3.R
import com.example.doan3.adapter.categoryProductAdapter
import com.example.doan3.model.productModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        intent.getStringExtra("name")?.let { getCategory(it) }
    }

    private fun getCategory(category:String) {
       val list = ArrayList<productModel>()
        FirebaseFirestore.getInstance().collection("products").whereEqualTo("productCategory",category)
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents){
                    val data = doc.toObject(productModel::class.java)
                    list.add(data!!)

                }
                val recyclerView = findViewById<RecyclerView>(R.id.categoryRV)

                recyclerView.adapter = categoryProductAdapter(list)
            }
    }
}