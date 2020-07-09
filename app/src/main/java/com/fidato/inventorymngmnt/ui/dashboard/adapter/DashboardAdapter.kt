package com.fidato.inventorymngmnt.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.databinding.RawDashboardBinding
import com.fidato.inventorymngmnt.utility.OnItemClickListner

class DashboardAdapter(
    var arylstDashboard: ArrayList<String>,
    val onItemClickListner: OnItemClickListner
) :
    RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawDashboardBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_dashboard,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arylstDashboard.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arylstDashboard.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClickListner(position)
        }
    }

    fun setProduct(arylstDashboard: ArrayList<String>) {
        this.arylstDashboard = arylstDashboard
        notifyDataSetChanged()
    }

    fun addProduct(arylstDashboard: ArrayList<String>) {
        this.arylstDashboard.addAll(arylstDashboard)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RawDashboardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(dashboard: String) {
            binding.dashboard = dashboard
            binding.executePendingBindings()
        }
    }
}