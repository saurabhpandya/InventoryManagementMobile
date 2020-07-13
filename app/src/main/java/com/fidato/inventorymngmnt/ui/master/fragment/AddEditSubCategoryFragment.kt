package com.fidato.inventorymngmnt.ui.master.fragment

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.fidato.inventorymngmnt.constants.Constants.Companion.ERR_NO_CAT
import com.fidato.inventorymngmnt.constants.Constants.Companion.ERR_NO_SUB_CAT
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.databinding.FragmentAddEditSubCategoryBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.master.viewmodel.AddEditSubCategoryViewModel
import com.fidato.inventorymngmnt.utility.Resource
import com.fidato.inventorymngmnt.utility.Status
import com.fidato.inventorymngmnt.utility.hideKeyboard
import com.fidato.inventorymngmnt.utility.showToast


class AddEditSubCategoryFragment : BaseFragment(), View.OnClickListener {
    private val TAG: String = AddEditSubCategoryFragment::class.java.canonicalName.toString()

    private lateinit var binding: FragmentAddEditSubCategoryBinding
    private lateinit var viewModel: AddEditSubCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditSubCategoryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setListener()
        observeData()
        getData()
        getCategories()
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
        ).get(AddEditSubCategoryViewModel::class.java)
        binding.vm = viewModel
    }

    private fun setListener() {

        binding.tiedttxtSubcatCategory.setOnClickListener(this)
        binding.tiedttxtSubcatSubCategory.setOnClickListener(this)

        binding.tiedttxtSubcatSubCategoryNew.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.errNewSubCat.postValue("")
            }
        })
        binding.tiedttxtSubcatCategory.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.errCat.postValue("")
            }
        })
        binding.tiedttxtSubcatCategory.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.errSubCat.postValue("")
            }
        })
    }

    private fun observeData() {
        viewModel.saveSubCatResponse.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "save Sub Category::$it")
            when (it.status) {
                Status.LOADING -> {
                    binding.prgrs.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.prgrs.visibility = View.GONE
                    binding.noData.visibility = View.GONE
                    if (it.message != null) {
                        viewModel.errNewSubCat.postValue(it.message)
                    }
                }
                Status.SUCCESS -> {
                    val addEditResponse = it.data
                    if (addEditResponse != null) {
                        activity?.showToast(addEditResponse.message!!)
                    }

                    binding.prgrs.visibility = View.GONE
                    binding.noData.visibility = View.GONE
                    viewModel.saveSubCatResponse = MutableLiveData<Resource<CommonResponse>>()
                    activity?.hideKeyboard()
                    activity?.onBackPressed()

                }
            }
        })
    }

    private fun getData() {
        viewModel.setDataFromBundle(arguments)
        if (viewModel.isEdit) {
            updateTitle(requireActivity().getString(R.string.title_edit_sub_cat))
        } else {
            updateTitle(requireActivity().getString(R.string.title_add_sub_cat))
        }

        binding.newSubCategory = viewModel.getNewSubCategory()
        binding.executePendingBindings()
    }

    private fun getCategories() {
        viewModel.getAllCategories().observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "categories::${it.status}")
            when (it.status) {
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Log.d(TAG, "categories::${it.message}")
                }
                Status.SUCCESS -> {
                    Log.d(TAG, "categories::${it.data}")
                    val categoryList = it.data
                    if (!categoryList.isNullOrEmpty()) {
                        val category = categoryList[viewModel.selCatIndex]
                        binding.tiedttxtSubcatCategory.setText(category.name)
                        getSubCategories(category.id!!, -1)
                    }
                }
            }
        })
    }

    private fun getSubCategories(catId: Int, subCatId: Int) {
        viewModel.getSubCategories(catId, subCatId).observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "sub categories::${it.status}")
            when (it.status) {
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Log.d(TAG, "sub categories::${it.message}")
                }
                Status.SUCCESS -> {
                    Log.d(TAG, "sub categories::${it.data}")
                    val subCategoryList = it.data
                    if (!subCategoryList.isNullOrEmpty()) {
                        val subCategory = subCategoryList[viewModel.selSubCatIndex]
                        binding.tiedttxtSubcatSubCategory.setText(subCategory.name)
                    }
                }
            }
        })
    }

    private fun showCategoryList() {
        AlertDialog.Builder(activity).apply {
            setTitle(R.string.hint_select_cat)
            setSingleChoiceItems(viewModel.categoryAdapter, viewModel.selCatIndex, null)
            setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    dialog.dismiss()
                    viewModel.selCatIndex = (dialog as AlertDialog).listView.checkedItemPosition
                    val selectedCat = viewModel.getCategoryList()[viewModel.selCatIndex]
                    Log.d(TAG, "Selected Category : ${selectedCat}")
                    viewModel.getNewSubCategory().catId = selectedCat.id!!
                    viewModel.getNewSubCategory().subCatId = 0
                    binding.tiedttxtSubcatCategory.setText(selectedCat.name)
                    binding.tiedttxtSubcatSubCategory.setText("")
                    getSubCategories(selectedCat.id!!, 0)
                })
            show()
        }
    }

    private fun showSubCategoryList() {
        AlertDialog.Builder(activity).apply {
            setTitle(R.string.hint_select_sub_cat)
            setSingleChoiceItems(viewModel.subCategoryAdapter, viewModel.selSubCatIndex, null)
            setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    dialog.dismiss()
                    viewModel.selSubCatIndex = (dialog as AlertDialog).listView.checkedItemPosition
                    val selectedSubCat = viewModel.getSubCategoryList()[viewModel.selSubCatIndex]
                    Log.d(TAG, "Selected Sub Category : ${selectedSubCat}")
                    viewModel.getNewSubCategory().catId = selectedSubCat.catId
                    if (!selectedSubCat.name.equals("none", true))
                        viewModel.getNewSubCategory().subCatId = selectedSubCat.id
                    else
                        viewModel.getNewSubCategory().subCatId = 0
                    binding.tiedttxtSubcatSubCategory.setText(selectedSubCat.name)
                })
            show()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tiedttxt_subcat_category -> {
                if (!viewModel.categoryAdapter.isEmpty) {
                    showCategoryList()
                } else {
                    viewModel.errCat.value = ERR_NO_CAT
                }

            }
            R.id.tiedttxt_subcat_sub_category -> {
                if (!viewModel.subCategoryAdapter.isEmpty) {
                    showSubCategoryList()
                } else {
                    viewModel.errSubCat.value = ERR_NO_SUB_CAT
                }

            }
        }
    }

}