package com.example.doan3.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.doan3.activity.AddressActivity
import com.example.doan3.activity.CategoryActivity

import com.example.doan3.adapter.cartAdapter

import com.example.doan3.databinding.FragmentCartBinding

import com.example.doan3.roomdb.AppDataBase
import com.example.doan3.roomdb.ProductModel


class CartFragment : Fragment() {
   private lateinit var binding: FragmentCartBinding
   private lateinit var list: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentCartBinding.inflate(layoutInflater)

        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart",false)
        editor.apply()

        getProduct()
        return binding.root

    }

    private fun getProduct() {
       val dao = AppDataBase.getInstance(requireContext()).productDao()



        dao.getAllProduct().observe(viewLifecycleOwner, { products ->
            binding.cartItemRV.adapter = cartAdapter(products)

            totalCost(products)
        })



    }

    private fun totalCost(products: List<ProductModel>?) {
        list = ArrayList()
        list.clear()
        Log.e("List", list.toString() )
        var total = 0
        for (item in products!!){
            total += item.productPrice!!.toInt()
            list.add(item.productID)
        }
        if (list != null){
            Log.e("List", "List: OK " )
            Log.e("List", list.toString() )
        }
        binding.textView12.text = "Total item in cart is ${products.size}"
        binding.textView13.text ="Total cost :$total"

        binding.checkoutBtn.setOnClickListener(){
            val intent = Intent(requireContext(),AddressActivity::class.java)
            intent.putExtra("totalCost",total.toString())
            intent.putExtra("productIDs",list)
            requireContext().startActivity(intent)
        }
    }
}