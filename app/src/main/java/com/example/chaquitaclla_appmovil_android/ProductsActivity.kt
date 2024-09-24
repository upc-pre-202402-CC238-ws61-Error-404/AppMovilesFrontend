// ProductsActivity.kt
package com.example.chaquitaclla_appmovil_android

import DB.AppDataBase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.crops_details.adapters.ProductAdapter
import Entities.Product
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsActivity : AppCompatActivity() {
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var appDB: AppDataBase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        productRecyclerView = findViewById(R.id.productRecyclerView)
        productRecyclerView.layoutManager = LinearLayoutManager(this)

        appDB = AppDataBase.getDatabase(this)

        val sowingId = intent.getIntExtra("SOWING_ID", -1)
        Log.d("ProductsActivity", "Received sowingId: $sowingId")
        if (sowingId != -1) {
            fetchProductsBySowingId(sowingId)
        } else {
            Log.e("ProductsActivity", "Invalid sowing ID")
            Toast.makeText(this, "Invalid sowing ID", Toast.LENGTH_SHORT).show()
        }

        val addButton: Button = findViewById(R.id.addProductButton)
        addButton.setOnClickListener {
            showAddProductDialog()
        }
    }

    private fun fetchProductsBySowingId(sowingId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val products = appDB.productDAO().getProductsBySowingId(sowingId)
                withContext(Dispatchers.Main) {
                    productAdapter = ProductAdapter(products, ::onEditClick, ::onDeleteClick)
                    productRecyclerView.adapter = productAdapter
                }
            } catch (e: Exception) {
                Log.e("ProductsActivity", "Error fetching products: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductsActivity, "Error fetching products: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showAddProductDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_product, null)
        val productNameEditText = dialogView.findViewById<EditText>(R.id.edittext_product_name)
        val productTypeEditText = dialogView.findViewById<EditText>(R.id.edittext_product_type)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Add Product")
            .setPositiveButton("Add") { _, _ ->
                val sowingId = intent.getIntExtra("SOWING_ID", -1)
                val productName = productNameEditText.text.toString()
                val productType = productTypeEditText.text.toString()
                addProduct(sowingId, productName, productType)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun addProduct(sowingId: Int, productName: String, productType: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newProduct = Product(
                    id = 0, // 0 because it will be auto-generated
                    sowingId = sowingId,
                    name = productName,
                    type = productType
                )
                appDB.productDAO().insertProduct(newProduct)
                fetchProductsBySowingId(sowingId) // Refresh the list
            } catch (e: Exception) {
                Log.e("ProductsActivity", "Error adding product: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductsActivity, "Error adding product: ${e.message}", Toast.LENGTH_SHORT).show()
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