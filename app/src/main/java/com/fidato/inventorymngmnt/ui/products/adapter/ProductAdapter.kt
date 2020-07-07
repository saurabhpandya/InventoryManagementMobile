package com.fidato.inventorymngmnt.ui.products.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.data.model.master.Product
import com.fidato.inventorymngmnt.databinding.RawProductBinding
import com.fidato.inventorymngmnt.utility.OnItemClickListner

class ProductAdapter(
    var arylstProduct: ArrayList<Product>,
    val onItemClickListner: OnItemClickListner
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawProductBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_product,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arylstProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arylstProduct.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClickListner(position)
        }
    }

    fun setProduct(arylstProduct: ArrayList<Product>) {
        this.arylstProduct = arylstProduct
        notifyDataSetChanged()
    }

    fun addProduct(arylstProduct: ArrayList<Product>) {
        this.arylstProduct.addAll(arylstProduct)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RawProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }
    }
}