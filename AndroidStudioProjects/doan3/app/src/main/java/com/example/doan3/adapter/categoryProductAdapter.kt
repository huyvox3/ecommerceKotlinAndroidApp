package com.example.doan3.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doan3.R
import com.example.doan3.activity.ProductDetailsActivity
import com.example.doan3.model.productModel

class categoryProductAdapter (val items: List<productModel>):RecyclerView.Adapter<categoryProductAdapter.ViewHolder>() {
    class ViewHolder (view: View): RecyclerView.ViewHolder(view){
        val coverImg: ImageView = view.findViewById(R.id.imageView3)
        val title:TextView = view.findViewById(R.id.textView5)
        val price:TextView = view.findViewById(R.id.textView6)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context)
           .inflate(R.layout.item_category_product_layout,parent,false)

       return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]
        Glide.with(context).load(item.productCoverImage).into(holder.coverImg)
        holder.title.text = item.productName
        holder.price.text = item.productP

        holder.itemView.setOnClickListener(){
            val intent = Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra("id",item.productId)
            context.startActivities(arrayOf(intent))
        }
    }

    override fun getItemCount() = items.size

}




