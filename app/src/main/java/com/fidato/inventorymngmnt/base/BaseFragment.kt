package com.fidato.inventorymngmnt.base

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fidato.inventorymngmnt.MainActivity

open class BaseFragment : Fragment() {

    fun updateTitle(title: String?) {
        (activity as MainActivity).supportActionBar?.title = title ?: activity?.actionBar?.title
    }

    fun navigateUp() {
        findNavController().navigateUp()
    }

    fun onBackPressed(){
        activity?.onBackPressed()
    }

}