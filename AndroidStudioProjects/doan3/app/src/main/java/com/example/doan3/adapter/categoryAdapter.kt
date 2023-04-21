package com.example.doan3a.adapter

import android.content.Context
import android.content.Intent
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doan3.R
import com.example.doan3.activity.CategoryActivity
import com.example.doan3.model.categoryModel



class categoryAdapter( private val items: List<categoryModel>) :
    RecyclerView.Adapter<categoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.textView)

        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_category_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.name
        Log.e("RecyclerView", "ok")
        Glide.with(holder.imageView.context).load(item.image).into(holder.imageView)
        holder.itemView.setOnClickListener(){
            val intent = Intent(holder.imageView.context,CategoryActivity::class.java)
            intent.putExtra("name",item.name)
            holder.imageView.context.startActivities(arrayOf(intent))
        }

    }

    override fun getItemCount() = items.size
}
