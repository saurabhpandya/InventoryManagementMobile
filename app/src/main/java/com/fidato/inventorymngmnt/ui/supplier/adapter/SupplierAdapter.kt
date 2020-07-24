package com.fidato.inventorymngmnt.ui.supplier.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.data.model.Supplier
import com.fidato.inventorymngmnt.databinding.RawSupplierBinding
import com.fidato.inventorymngmnt.utility.OnItemClickListner

class SupplierAdapter(
    var arylstSupplier: ArrayList<Supplier>,
    val onItemClickListner: OnItemClickListner
) :
    RecyclerView.Adapter<SupplierAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawSupplierBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_supplier,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arylstSupplier.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arylstSupplier.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClickListner(position)
        }
    }

    fun setSupplier(arylstSupplier: ArrayList<Supplier>) {
        this.arylstSupplier = arylstSupplier
        notifyDataSetChanged()
    }

    fun addSupplier(arylstSupplier: ArrayList<Supplier>) {
        this.arylstSupplier.addAll(arylstSupplier)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RawSupplierBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(supplier: Supplier) {
            binding.supplier = supplier
            binding.executePendingBindings()
        }
    }

}