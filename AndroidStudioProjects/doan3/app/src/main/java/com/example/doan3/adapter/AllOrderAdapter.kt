package com.example.doan3.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.doan3.activity.OrderDetailsActivity
import com.example.doan3.databinding.AllOrderItemLayoutBinding
import com.example.doan3.model.OrderModel
import com.google.firebase.firestore.FirebaseFirestore

class AllOrderAdapter(private val list: ArrayList<OrderModel>, private val context: Context) :
    RecyclerView.Adapter<AllOrderAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: AllOrderItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AllOrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = list[position]

        with(holder.binding) {
            orderIDTv.text = "Order ID: ${order.orderID}"
            productPriceTv.text = "Price: $${order.price}"
            orderStatusTv.text = "Status: ${order.status}"

            if (order.status.equals("Delivered")){
                cancelBtn.isEnabled = false
                cancelBtn.text= "Delivered"
                cancelBtn.backgroundTintBlendMode
            }

            cancelBtn.setOnClickListener {
                updateStatus("Canceled", order.orderID!!)
                cancelBtn.isEnabled = false
            }





            holder.itemView.setOnClickListener {
                val intent = Intent(context, OrderDetailsActivity::class.java).apply {
                    putExtra("orderID", order.orderID)
                }
                context.startActivity(intent)
            }
        }
    }

    private fun updateStatus(status: String, id: String):Boolean {
        var flag = false
        val data = hashMapOf("status" to status)
        FirebaseFirestore.getInstance().collection("orders")
            .whereEqualTo("orderID", id).get().addOnSuccessListener {
                for (doc in it) {
                    doc.reference.update(data as Map<String, Any>).addOnSuccessListener {
                        Toast.makeText(context, "Order status updated.", Toast.LENGTH_SHORT).show()

                        flag = true
                    }
                }
            }.addOnFailureListener {
                flag = false

            }
        return flag
    }

    override fun getItemCount() = list.size
}