package com.example.chaquitaclla_appmovil_android

import DB.AppDataBase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import Entities.Product
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.example.chaquitaclla_appmovil_android.crops_details.adapters.ProductAdapter
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

        setupSpinner()
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
        val productQuantityEditText = dialogView.findViewById<EditText>(R.id.edittext_product_quantity)
        val addProductButton = dialogView.findViewById<Button>(R.id.addProductButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.button_cancel)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Add Product")
            .create()

        addProductButton.setOnClickListener {
            val sowingId = intent.getIntExtra("SOWING_ID", -1)
            val productName = productNameEditText.text.toString()
            val productType = productTypeEditText.text.toString()
            val productQuantity = productQuantityEditText.text.toString().toFloat()
            addProduct(sowingId, productName, productType, productQuantity)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun addProduct(sowingId: Int, productName: String, productType: String, quantity: Float) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newProduct = Product(
                    id = 0, // 0 because it will be auto-generated
                    sowingId = sowingId,
                    name = productName,
                    type = productType,
                    quantity = quantity
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
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_product, null)
        val productNameEditText = dialogView.findViewById<EditText>(R.id.edittext_product_name)
        val productTypeEditText = dialogView.findViewById<EditText>(R.id.edittext_product_type)
        val addProductButton = dialogView.findViewById<Button>(R.id.addProductButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.button_cancel)

        productNameEditText.setText(product.name)
        productTypeEditText.setText(product.type)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Edit Product")
            .create()

        addProductButton.setOnClickListener {
            product.name = productNameEditText.text.toString()
            product.type = productTypeEditText.text.toString()
            updateProduct(product)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateProduct(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appDB.productDAO().updateProduct(product)
                fetchProductsBySowingId(product.sowingId) // Refresh the list
            } catch (e: Exception) {
                Log.e("ProductsActivity", "Error updating product: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductsActivity, "Error updating product: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onDeleteClick(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appDB.productDAO().deleteProduct(product.id)
                fetchProductsBySowingId(product.sowingId) // Refresh the list
            } catch (e: Exception) {
                Log.e("ProductsActivity", "Error deleting product: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductsActivity, "Error deleting product: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupSpinner() {
        val spinner: Spinner = findViewById(R.id.dropdown_menu)
        ArrayAdapter.createFromResource(
            this,
            R.array.crop_info_options,
            R.layout.spinner_item_white_text
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item_white_text)
            spinner.adapter = adapter
        }

        var isFirstSelection = true

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                if (isFirstSelection) {
                    isFirstSelection = false
                    return
                }
                view?.let {
                    val sowingId = intent.getIntExtra("SOWING_ID", -1)
                    when (position) {
                        0 -> startActivity(Intent(this@ProductsActivity, GeneralCropInfo::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                        1 -> startActivity(Intent(this@ProductsActivity, CropCareActivity::class.java))
                        2 -> startActivity(Intent(this@ProductsActivity, ControlsActivity::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                        3 -> startActivity(Intent(this@ProductsActivity, DiseasesActivity::class.java))
                        4 -> startActivity(Intent(this@ProductsActivity, ProductsActivity::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }

        val productsPosition = resources.getStringArray(R.array.crop_info_options).indexOf("Products")
        spinner.setSelection(productsPosition)
    }

}