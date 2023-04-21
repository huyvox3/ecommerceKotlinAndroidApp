package com.example.doan3.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.doan3.adapter.OrderlineAdapter
import com.example.doan3.databinding.ActivityOrderDetailsBinding
import com.example.doan3.model.OrderLineModel
import com.example.doan3.model.OrderLinesModel


import com.example.doan3.model.OrderModel
import com.google.firebase.firestore.FirebaseFirestore


class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var list:ArrayList<OrderLinesModel>
    private lateinit var binding: ActivityOrderDetailsBinding
    private lateinit var orderID:String
    private lateinit var order:OrderModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderID = intent.getStringExtra("orderID").toString()
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)

        FirebaseFirestore.getInstance().collection("orders").
        document(orderID)
            .get()
            .addOnSuccessListener {

                list = ArrayList()

                order = it.toObject(OrderModel::class.java)!!
                Log.e("ORDER", order.toString())


                FirebaseFirestore.getInstance().collection("orders")
                    .document(orderID).collection("orderLines")
                    .get().addOnSuccessListener {
                        for (doc in it){
                            list.add(doc.toObject(OrderLinesModel::class.java))
                        }

                        if (!list.isEmpty() && it != null){
                            binding.itemDetailsRv.visibility = View.VISIBLE
                            binding.emptyOrderDetailsTv.visibility = View.INVISIBLE
                            binding.linearLayout.visibility = View.VISIBLE
                            binding.itemDetailsRv.adapter = OrderlineAdapter(list,this)

                            binding.orderIDTv.text = "Order ID: \n ${order.orderID}"
                            binding.orderPriceTv.text = "Price: $${order.price}"
                            binding.orderStatusTv.text = "Status: ${order.status}"



                        }
                    }




            }
        setContentView(binding.root)

    }
}