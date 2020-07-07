package com.fidato.inventorymngmnt.ui.master.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.data.model.master.SubCategory
import com.fidato.inventorymngmnt.databinding.RawSubCategoryBinding
import com.fidato.inventorymngmnt.utility.OnItemClickListner

class SubCategoryAdapter(
    var arylstSubCategory: ArrayList<SubCategory>,
    val onItemClickListner: OnItemClickListner
) :
    RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawSubCategoryBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_sub_category,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arylstSubCategory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arylstSubCategory.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClickListner(position)
        }
    }

    fun setSubCategory(arylstSubCategory: ArrayList<SubCategory>) {
        this.arylstSubCategory = arylstSubCategory
        notifyDataSetChanged()
    }

    fun addSubCategory(arylstSubCategory: ArrayList<SubCategory>) {
        this.arylstSubCategory.addAll(arylstSubCategory)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RawSubCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(subCategory: SubCategory) {
            binding.subCategory = subCategory
            binding.executePendingBindings()
        }
    }

}