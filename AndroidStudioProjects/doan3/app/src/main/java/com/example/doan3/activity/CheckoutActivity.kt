package com.example.doan3.activity




import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.example.doan3.MainActivity

import com.example.doan3.databinding.ActivityCheckoutBinding
import com.example.doan3.model.OrderLineModel

import com.example.doan3.roomdb.AppDataBase
import com.example.doan3.roomdb.ProductModel

import com.google.firebase.firestore.FirebaseFirestore

import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCheckoutBinding
    private val YOUR_CLIENT_ID = "AU63RI48vq3opK53t5Z7jBJgT2mvvksOx_jhoVyvC6fDygP8PnsRbqrITsIliPKXYBrIOyBjgIs-I0xU"
    private lateinit var price :String
    private lateinit var pref: SharedPreferences
    private var dao = AppDataBase.getInstance(this).productDao()

    private lateinit var productIDs :ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       price = intent.getStringExtra("totalCost").toString()
        pref = this.getSharedPreferences("user", MODE_PRIVATE)
        productIDs = intent.getStringArrayListExtra("productIDs") as ArrayList<String>


        Log.e("PRICE", price )
        Log.e("IDPROD", productIDs.toString() )

        val config = CheckoutConfig(
            application = application,
            clientId = YOUR_CLIENT_ID,
            environment = Environment.SANDBOX,
            returnUrl = String.format("%s://paypalpay","com.example.doan3"),
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )

        )


        PayPalCheckout.setConfig(config)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        binding.paymentButtonContainer.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    Order(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = price)
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
//                    Toast.makeText(this,"Payment Success",Toast.LENGTH_SHORT).show()
                    Log.i("CaptureOrder", "CaptureOrderResult: $captureOrderResult")
                   lifecycleScope.launch {
                       uploadData()
                   }
                }
            }
            ,onCancel =
            OnCancel{
                Log.e("OnCancel", "Buyer canceled the PayPal experience.")
                Toast.makeText(this,"Payment Canceled",Toast.LENGTH_SHORT).show()
            }
        )
        Log.e("CHECKOUT", "onCreate:OK ", )
        setContentView(binding.root)
    }

    private suspend fun uploadData() {
        Log.e("PROD Data idss", productIDs.toString() )
       fetchData(productIDs)
    }

    private suspend fun fetchData(ids: ArrayList<String>?) {
        val userId = pref.getString("number", "")!!

        val  orderLines = ArrayList<OrderLineModel>()

        for (id in ids!!) {


            val firestore = FirebaseFirestore.getInstance().collection("products")
            val docRef = firestore.whereEqualTo("productId", id).get()
            val querySnapshot = docRef.await() // wait for the query to complete
            val doc = querySnapshot.documents[0]
            val orderLine = OrderLineModel(
                "",
                userId,
                doc.getString("productId"),
                doc.getString("productName"),
                doc.getString("productCoverImage"),
                doc.getString("productQt"),
                doc.getString("productP"),
                "",
            )
            orderLines.add(orderLine)
            Log.e("ORDERLINE", orderLine.toString())
        }
        Log.e("ORDERLINEssss", orderLines.size.toString())
        saveData(orderLines)


    }



    private fun saveData(orderLineModel: ArrayList<OrderLineModel>) {
        val firestore = FirebaseFirestore.getInstance().collection("orders")
        val orderID = firestore.document().id
        val order = hashMapOf<String,Any>()
        order["orderID"] = orderID
        order["price"] = price
        order["status"] = "Ordered"
        order["userID"] = pref.getString("number", "")!!

        firestore.document(orderID).set(order)

      val orderLineRef =   firestore.document(orderID).collection("orderLines")


       for (ol in orderLineModel){
           val newOl = orderLineRef.document()
           val data = hashMapOf<String,Any>()
           data["orderID"] = orderID
           data["name"] = ol.productName!!
           data["prodID"] = ol.productId!!
           data["price"] = ol.productP!!
           data["quantity"] = ol.productQt!!
           data["img"] = ol.productCoverImage!!

           newOl.set(data)
               .addOnSuccessListener {

                   lifecycleScope.launch {
                       dao.deleteProduct(ProductModel(ol.productId!!))
                   }
                   validateOrder(true)

               }.addOnFailureListener {
                   it.printStackTrace()
                   validateOrder(false)

               }



       }



    }
    private fun validateOrder(flag: Boolean){
        if (flag){
            Toast.makeText(this,"Order Placed",Toast.LENGTH_SHORT).show()

            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        else{
            Toast.makeText(this,"Failed to place order",Toast.LENGTH_SHORT).show()
        }
    }
}





