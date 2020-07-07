package com.fidato.inventorymngmnt.ui.master.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.data.model.master.Category
import com.fidato.inventorymngmnt.databinding.RawCategoryBinding
import com.fidato.inventorymngmnt.utility.OnItemClickListner

class CategoryAdapter(
    var arylstCategory: ArrayList<Category>,
    val onItemClickListner: OnItemClickListner
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawCategoryBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_category,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arylstCategory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arylstCategory.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClickListner(position)
        }
    }

    fun setCategory(arylstCategory: ArrayList<Category>) {
        this.arylstCategory = arylstCategory
        notifyDataSetChanged()
    }

    fun addCategory(arylstCategory: ArrayList<Category>) {
        this.arylstCategory.addAll(arylstCategory)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RawCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(category: Category) {
            binding.category = category
            binding.executePendingBindings()
        }
    }

}