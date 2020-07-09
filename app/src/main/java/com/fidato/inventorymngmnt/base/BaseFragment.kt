package com.fidato.inventorymngmnt.base

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.fidato.inventorymngmnt.MainActivity

open class BaseFragment : Fragment() {

    fun updateTitle(title: String?) {
        (activity as MainActivity).supportActionBar?.title = title ?: activity?.actionBar?.title
    }

}