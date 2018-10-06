package foodfacts.bevilacqua.com.foodfacts.ui

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import foodfacts.bevilacqua.com.foodfacts.R
import foodfacts.bevilacqua.com.foodfacts.databinding.ProductViewItemBinding
import foodfacts.bevilacqua.com.foodfacts.model.Product

internal class ProductRecyclerViewAdapter(private val productClickCallback: ProductClickCallback?) :
        RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductViewHolder>() {

    private var values: List<Product> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = DataBindingUtil
                .inflate<ProductViewItemBinding>(LayoutInflater.from(parent.context), R.layout.product_view_item,
                        parent, false)
        binding.callback = productClickCallback
        return ProductViewHolder(binding)
    }

    override fun getItemCount() = values.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.binding.product = values[position]

        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.ic_barcode)
        requestOptions.error(R.drawable.ic_broken_image)

        Glide.with(holder.itemView.context)
                .load(values[position].imageUrl)
                .apply(requestOptions)
                .into(holder.binding.productImageView)

        holder.binding.executePendingBindings()
    }

    fun submitList(productList: List<Product>) {
        values = productList
    }

    inner class ProductViewHolder(val binding: ProductViewItemBinding) : RecyclerView.ViewHolder(binding.root)
}