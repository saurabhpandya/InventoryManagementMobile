package com.fidato.inventorymngmnt.ui.products.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.data.model.master.ProductVarient
import com.fidato.inventorymngmnt.databinding.RawAddProductVariantBinding
import com.fidato.inventorymngmnt.utility.OnItemClickListner

class AddProductVariantAdapter(
    var arylstProductVariant: ArrayList<ProductVarient>,
    val onItemClickListner: OnItemClickListner
) :
    RecyclerView.Adapter<AddProductVariantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawAddProductVariantBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_add_product_variant,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arylstProductVariant.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arylstProductVariant.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClickListner(position)
        }
    }

    fun setProductVariant(arylstProductVariant: ArrayList<ProductVarient>) {
        this.arylstProductVariant = arylstProductVariant
        notifyDataSetChanged()
    }

    fun addProductVariant(arylstProductVariant: ArrayList<ProductVarient>) {
        this.arylstProductVariant.addAll(arylstProductVariant)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RawAddProductVariantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(productVariant: ProductVarient) {
            binding.txtvwPrdctColor.text = "Color: ${productVariant.color}"
            binding.txtvwPrdctId.text = "Vairant Id: ${productVariant.id}"
            binding.txtvwPrdctSize.text = "Size: ${productVariant.size}"
            binding.txtvwPrdctPrice.text = "Price: ${productVariant.price}"
//            binding.productVariant = productVariant
//            binding.executePendingBindings()
        }
    }
}