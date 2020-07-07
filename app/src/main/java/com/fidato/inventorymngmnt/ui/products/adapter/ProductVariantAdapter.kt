package com.fidato.inventorymngmnt.ui.products.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.data.model.master.ProductVarient
import com.fidato.inventorymngmnt.databinding.RawProductVariantBinding
import com.fidato.inventorymngmnt.utility.ProductVarientItemClickListner

class ProductVariantAdapter<T>(
    var arylstProductVariant: List<T>,
    var isForSize: Boolean,
    var onItemClickListner: ProductVarientItemClickListner
) :
    RecyclerView.Adapter<ProductVariantAdapter.ViewHolder<T>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<T> {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawProductVariantBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.raw_product_variant,
                parent,
                false
            )
        return ViewHolder<T>(
            binding,
            isForSize
        )

    }

    override fun getItemCount(): Int {
        return arylstProductVariant.size
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bindItems(arylstProductVariant.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListner.onProductVariantClickListner(position, isForSize)
        }
    }

    fun setProductVariants(arylstProductVariant: List<T>) {
        this.arylstProductVariant = arylstProductVariant
        notifyDataSetChanged()
    }

    class ViewHolder<T>(val binding: RawProductVariantBinding, val isForSize: Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItems(productVarient: T) {
            if (isForSize) {
                binding.txtvwVariant.text = "${productVarient}"
            } else {
                var productVariantMapping = productVarient as ProductVarient
                binding.txtvwVariant.text = productVariantMapping.color
            }
        }

    }
}