package com.fidato.inventorymngmnt.utility

interface OnItemClickListner {
    fun onItemClickListner(position: Int)
}

interface ProductVarientItemClickListner {
    fun onProductVariantClickListner(position: Int, isForSize: Boolean)
}