package com.example.doan3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doan3.databinding.ItemOrderLineBinding
import com.example.doan3.model.OrderLineModel
import com.example.doan3.model.OrderLinesModel

class OrderlineAdapter(val list: ArrayList<OrderLinesModel>, val context: Context): RecyclerView.Adapter<OrderlineAdapter.OrderLineAdapterViewHolder>(){
    inner class OrderLineAdapterViewHolder (val binding: ItemOrderLineBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderLineAdapterViewHolder {
        return  OrderLineAdapterViewHolder(
            ItemOrderLineBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: OrderLineAdapterViewHolder, position: Int) {


        holder.binding.productPriceTv.text = "Price: $"+ list[position].price

        holder.binding.productNameTv.text =  list[position].name

        holder.binding.quantityTv.text = "1"

        Glide.with(context).load(list[position].img).into(holder.binding.lineImgIv)


    }



    override fun getItemCount() = list.size


}