package com.example.doan3.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doan3.roomdb.ProductModel
import com.example.doan3.R
import com.example.doan3.activity.ProductDetailsActivity
import com.example.doan3.roomdb.AppDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class cartAdapter (private val items: List<ProductModel>):RecyclerView.Adapter<cartAdapter.ViewHolder>(){
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        val nameTV = view.findViewById<TextView>(R.id.textView11)
        val price = view.findViewById<TextView>(R.id.textView12)
        val imgView = view.findViewById<ImageView>(R.id.imageView4)
        val deleteBtn = view.findViewById<Button>(R.id.button4)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_cart_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]
        holder.itemView.setOnClickListener(){
            val intent = Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra("id",item.productID)

            context.startActivity(intent)
        }

        holder.nameTV.text = item.productName
        Glide.with(context).load(item.productImg).into(holder.imgView)
        holder.price.text = item.productPrice

        val dao = AppDataBase.getInstance(context).productDao()

        holder.deleteBtn.setOnClickListener(){
            GlobalScope.launch(Dispatchers.IO) {

                dao.deleteProduct(
                    ProductModel(
                        item.productID,
                        item.productName,
                        item.productImg,
                        item.productPrice
                    )
                )
            }

        }
    }

    override fun getItemCount() = items.size


}