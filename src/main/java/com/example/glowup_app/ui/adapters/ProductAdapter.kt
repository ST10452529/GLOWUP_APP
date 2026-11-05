package com.example.glowup_app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.glowup_app.R
import com.example.glowup_app.data.models.Product
import com.example.glowup_app.databinding.ItemProductBinding

class ProductAdapter(
    private var products: List<Product>,
    private val onAddClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        with(holder.binding) {
            tvProductName.text = product.name
            tvProductBrand.text = if (product.brand.isNotEmpty()) product.brand else "Skincare Brand"
            tvProductCategory.text = if (product.category.isNotEmpty()) product.category else "Skincare Product"

            if (product.imageUrl.isNotEmpty()) {
                Glide.with(ivProductImage.context)
                    .load(product.imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(ivProductImage)
            } else {
                ivProductImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            btnAddToRoutine.setOnClickListener {
                onAddClick(product)
            }
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
