package com.fidato.inventorymngmnt.ui.customer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.data.model.Customer
import com.fidato.inventorymngmnt.databinding.RawCustomerBinding
import com.fidato.inventorymngmnt.utility.OnItemClickListner

class CustomerAdapter(
    var arylstCustomer: ArrayList<Customer>,
    val onItemClickListner: OnItemClickListner
) :
    RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawCustomerBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_customer,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arylstCustomer.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arylstCustomer.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClickListner(position)
        }
    }

    fun setCustomer(arylstCustomer: ArrayList<Customer>) {
        this.arylstCustomer = arylstCustomer
        notifyDataSetChanged()
    }

    fun addCustomer(arylstCustomer: ArrayList<Customer>) {
        this.arylstCustomer.addAll(arylstCustomer)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RawCustomerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(customer: Customer) {
            binding.customer = customer
            binding.executePendingBindings()
        }
    }

}