package com.example.glowup_app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.glowup_app.data.models.Product
import com.example.glowup_app.data.models.RoutineStep
import com.example.glowup_app.data.network.RetrofitInstance
import com.example.glowup_app.data.repository.FirestoreRepository
import com.example.glowup_app.databinding.ActivityProductsBinding
import com.example.glowup_app.ui.adapters.ProductAdapter
import kotlinx.coroutines.launch
import java.util.UUID

class ProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductsBinding
    private lateinit var firestoreRepository: FirestoreRepository
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestoreRepository = FirestoreRepository(this)

        setupRecyclerView()

        binding.btnSearchProduct.setOnClickListener {
            val query = binding.etProductSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                searchProducts(query)
            } else {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
            }
        }

        // Load initial default products
        loadDefaultProducts()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(emptyList()) { product ->
            showAddToRoutineDialog(product)
        }
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = productAdapter
    }

    private fun searchProducts(query: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvEmptyState.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.searchProducts(query)
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val apiProducts = response.body()?.products ?: emptyList()
                    val domainProducts = apiProducts.map {
                        Product(
                            id = it.id ?: UUID.randomUUID().toString(),
                            name = it.productName ?: "Skincare Product",
                            brand = it.brands ?: "Various Brands",
                            category = it.categories ?: "Skincare",
                            imageUrl = it.imageUrlSmall ?: it.imageUrl ?: ""
                        )
                    }.filter { it.name.isNotBlank() }

                    if (domainProducts.isNotEmpty()) {
                        productAdapter.updateData(domainProducts)
                        binding.tvEmptyState.visibility = View.GONE
                    } else {
                        showEmptyState("No products found for '$query'. Try another query!")
                    }
                } else {
                    showEmptyState("Failed to fetch products. Loading popular products...")
                    loadDefaultProducts()
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                showEmptyState("Network issue. Displaying offline products list.")
                loadDefaultProducts()
            }
        }
    }

    private fun loadDefaultProducts() {
        val curatedProducts = listOf(
            Product("1", "CeraVe Hydrating Facial Cleanser", "CeraVe", "Cleanser", "https://images.openbeautyfacts.org/images/products/333/787/559/7179/front_en.11.200.jpg"),
            Product("2", "The Ordinary Niacinamide 10% + Zinc 1%", "The Ordinary", "Serum", "https://images.openbeautyfacts.org/images/products/076/991/519/0311/front_en.8.200.jpg"),
            Product("3", "La Roche-Posay Anthelios SPF 50+", "La Roche-Posay", "Sunscreen", "https://images.openbeautyfacts.org/images/products/333/787/558/8580/front_en.4.200.jpg"),
            Product("4", "Neutrogena Hydro Boost Water Gel", "Neutrogena", "Moisturizer", "https://images.openbeautyfacts.org/images/products/357/466/128/7303/front_en.6.200.jpg"),
            Product("5", "Paula's Choice 2% BHA Liquid Exfoliant", "Paula's Choice", "Exfoliant", "")
        )
        productAdapter.updateData(curatedProducts)
        binding.tvEmptyState.visibility = View.GONE
    }

    private fun showEmptyState(msg: String) {
        binding.tvEmptyState.text = msg
        binding.tvEmptyState.visibility = View.VISIBLE
    }

    private fun showAddToRoutineDialog(product: Product) {
        val options = arrayOf("Morning Routine ☀️", "Evening Routine 🌙")
        AlertDialog.Builder(this)
            .setTitle("Add '${product.name}' to routine")
            .setItems(options) { dialog, which ->
                val isMorning = (which == 0)
                val newStep = RoutineStep(
                    id = UUID.randomUUID().toString(),
                    title = if (product.category.isNotEmpty()) product.category else "Skincare Step",
                    productName = product.name,
                    isMorning = isMorning,
                    isCompleted = false
                )
                firestoreRepository.addRoutineStep(newStep)
                val timeOfDay = if (isMorning) "Morning" else "Evening"
                Toast.makeText(this, "Added to $timeOfDay routine!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
