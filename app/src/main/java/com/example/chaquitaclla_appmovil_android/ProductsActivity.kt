// app/src/main/java/com/example/chaquitaclla_appmovil_android/ProductsActivity.kt
package com.example.chaquitaclla_appmovil_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.crops_details.ProductService
import com.example.chaquitaclla_appmovil_android.crops_details.adapters.ProductAdapter
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsActivity : AppCompatActivity() {
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productService: ProductService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        productRecyclerView = findViewById(R.id.productRecyclerView)
        productRecyclerView.layoutManager = LinearLayoutManager(this)

        productService = ProductService(this)

        fetchProductsByCropId(1) // Example cropId
    }

    private fun fetchProductsByCropId(cropId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val productsList = productService.getProductsByCropId(cropId)
                withContext(Dispatchers.Main) {
                    productRecyclerView.adapter = ProductAdapter(productsList, ::onEditClick, ::onDeleteClick)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductsActivity, "Failed to load products", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onEditClick(product: Product) {
        // Handle edit action
        Toast.makeText(this, "Edit ${product.name}", Toast.LENGTH_SHORT).show()
    }

    private fun onDeleteClick(product: Product) {
        // Handle delete action
        Toast.makeText(this, "Delete ${product.name}", Toast.LENGTH_SHORT).show()
    }
}