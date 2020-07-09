package com.fidato.inventorymngmnt.ui.master.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.base.BaseFragment
import com.fidato.inventorymngmnt.base.ViewModelFactory
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.databinding.FragmentAddEditCategoryBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.master.viewmodel.AddEditCategoryViewModel
import com.fidato.inventorymngmnt.utility.Resource
import com.fidato.inventorymngmnt.utility.Status
import com.fidato.inventorymngmnt.utility.hideKeyboard
import com.fidato.inventorymngmnt.utility.showToast

class AddEditCategoryFragment : BaseFragment() {

    private val TAG: String = AddEditCategoryFragment::class.java.canonicalName.toString()

    private lateinit var binding: FragmentAddEditCategoryBinding
    private lateinit var viewModel: AddEditCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditCategoryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setListner()
        observeData()
        getData()

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.requireActivity(),
            ViewModelFactory(
                MasterNetworkDataProvider(
                    RetrofitClient.MASTER_SERVICE
                ),
                this.requireActivity().application
            )
        ).get(AddEditCategoryViewModel::class.java)
        binding.vm = viewModel

    }

    private fun setListner() {
        binding.tiedttxtCatId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.errCatId.postValue("")
            }
        })
        binding.tiedttxtCatName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.errCatName.postValue("")
            }
        })
    }

    private fun observeData() {
        viewModel.saveCatResponse.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "saveCategory::$it")
            when (it.status) {
                Status.LOADING -> {
                    binding.prgrs.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.prgrs.visibility = View.GONE
                    binding.noData.visibility = View.GONE
                    if (it.message != null) {
                        viewModel.errCatName.postValue(it.message)
                    }
                }
                Status.SUCCESS -> {
                    val addEditResponse = it.data
                    if (addEditResponse != null) {
                        activity?.showToast(addEditResponse.message!!)
                    }

                    binding.prgrs.visibility = View.GONE
                    binding.noData.visibility = View.GONE
                    viewModel.saveCatResponse = MutableLiveData<Resource<CommonResponse>>()
                    activity?.hideKeyboard()
                    activity?.onBackPressed()

                }
            }
        })
    }

    private fun getData() {
        viewModel.setDataFromBundle(arguments)
        if (viewModel.isEdit) {
            updateTitle(requireActivity().getString(R.string.title_edit_cat))
            binding.tiedttxtCatId.isEnabled = false
        } else {
            updateTitle(requireActivity().getString(R.string.title_add_cat))
        }
        val category = viewModel.getCategory()
        binding.cateogry = category
        binding.executePendingBindings()
    }

    private fun saveCategory() {

    }
}