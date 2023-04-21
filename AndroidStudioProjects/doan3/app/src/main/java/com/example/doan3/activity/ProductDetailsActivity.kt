package com.example.doan3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.doan3.MainActivity
import com.example.doan3.R
import com.example.doan3.adapter.categoryProductAdapter
import com.example.doan3.databinding.ActivityProductDetailsBinding
import com.example.doan3.model.productModel
import com.example.doan3.roomdb.AppDataBase
import com.example.doan3.roomdb.ProductDao
import com.example.doan3.roomdb.ProductModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)

        getProductDetails(intent.getStringExtra("id"))


        setContentView(binding.root)
    }

    private fun getProductDetails(prodID: String?) {
        FirebaseFirestore.getInstance().collection("products")
            .whereEqualTo("productId",prodID)
            .get().addOnSuccessListener {
              val doc = it.documents.get(0)

                val list = doc.get("productImgs") as ArrayList<String>
                binding.prodNameTv.text = doc.getString("productName")
                binding.priceProdTv.text = doc.getString("productP")
                binding.prodDesTv.text = doc.getString("productDescription")

                val slideList = ArrayList<SlideModel>()
                for (img in list){
                    slideList.add(SlideModel(img,ScaleTypes.CENTER_INSIDE))
                }

                cartAction(prodID,doc.getString(
                    "productName"),
                    doc.getString("productP"),
                    doc.getString("productCoverImage")
                )

                binding.imageSlider.setImageList(slideList)

            }.addOnFailureListener {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
    }

    private fun cartAction(prodID: String?, name: String?, price: String?, coverImg: String?) {
        val productDao = AppDataBase.getInstance(this).productDao()

        if (productDao.isExist(prodID!!) != null) {
            binding.button6.text = "Go to Cart"

        }else{
            binding.button6.text = "Add to Cart"
        }


        binding.button6.setOnClickListener{
            if (productDao.isExist(prodID) != null){
                openCart()
            }else{
                binding.button6.text = "Add to Cart"
                addToCart(productDao,prodID,name,price,coverImg)
            }

        }


    }

    private fun addToCart(
        productDao: ProductDao,
        prodID: String,
        name: String?,
        price: String?,
        coverImg: String?
    ) {
        val data = ProductModel(prodID,name,coverImg,price)
        lifecycleScope.launch(Dispatchers.IO ) {
         productDao.insertProduct(data)
            binding.button6.text = "Go to Cart"
        }
    }

    private fun openCart() {
        val pref = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isCart",true)
        editor.apply()
        startActivity(Intent(this,MainActivity::class.java))
        finish()

    }


}