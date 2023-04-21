package com.example.doan3.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doan3.R
import com.example.doan3.activity.ProductDetailsActivity

import com.example.doan3.model.productModel
import com.example.doan3.roomdb.AppDataBase
import com.example.doan3.roomdb.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class productAdapter( val items: List<productModel> ):RecyclerView.Adapter<productAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.textView4)
        val qt:TextView = view.findViewById(R.id.textView2)
        val price:TextView = view.findViewById(R.id.button)
        val category:TextView = view.findViewById(R.id.textView3)
        val imageView: ImageView = view.findViewById(R.id.imageView2)
        val btn: Button = view.findViewById(R.id.button2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_product_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]
        holder.nameTextView.text = item.productName

        Glide.with(context).load(item.productCoverImage).into(holder.imageView)
        holder.category.text = item.productCategory
        holder.qt.text = item.productQt
        holder.price.text = "$ ${item.productP}"
        holder.btn.setOnClickListener {
            val productDao = AppDataBase.getInstance(holder.imageView.context).productDao()

            val data = ProductModel(item.productId!!,item.productName,item.productCoverImage,item.productP)
           if (productDao.isExist(item.productId!!) != null){
               Log.e("DAO", "onBindViewHolder: ", )
                GlobalScope.launch(Dispatchers.IO) {
                    productDao.insertProduct(data)
                }
           }
        }
        holder.itemView.setOnClickListener(){
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id",item.productId)
            context.startActivity(intent)
        }
    }

    private fun addData(data:ProductModel) {




    }

    override fun getItemCount() = items.size

}