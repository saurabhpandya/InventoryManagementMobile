package com.fidato.inventorymngmnt.ui.master.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.base.BaseFragment
import com.fidato.inventorymngmnt.base.ViewModelFactory
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_CAT_ID
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_CAT_NAME
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_EDIT_SUB_CAT
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_SUB_CAT_ID
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.data.model.master.SubCategory
import com.fidato.inventorymngmnt.databinding.FragmentSubCategoryBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.master.adapter.SubCategoryAdapter
import com.fidato.inventorymngmnt.ui.master.viewmodel.SubCategoryViewModel
import com.fidato.inventorymngmnt.utility.CategoryUnderlayButtonClickListner
import com.fidato.inventorymngmnt.utility.OnItemClickListner
import com.fidato.inventorymngmnt.utility.Status
import com.fidato.inventorymngmnt.utility.showToast

class SubCategoryFragment : BaseFragment(), OnItemClickListner, View.OnClickListener,
    CategoryUnderlayButtonClickListner {

    private val TAG: String = SubCategoryFragment::class.java.canonicalName.toString()

    private lateinit var binding: FragmentSubCategoryBinding
    private lateinit var viewModel: SubCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setListener()
        setupRecyclerView()
        getBundleData()
        getData()
        observeDeleteSubCategory()
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
        ).get(SubCategoryViewModel::class.java)
        binding.vm = viewModel
    }

    private fun setListener() {
        binding.fabAddSubCat.setOnClickListener(this)
    }

    private fun setupRecyclerView() {
        viewModel.arylstSubCategory = ArrayList<SubCategory>()
        binding.rcyclrvwSubCategories.layoutManager = LinearLayoutManager(activity)
        binding.rcyclrvwSubCategories.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rcyclrvwSubCategories.setHasFixedSize(true)
        viewModel.getSwipeHelper(this).attachToRecyclerView(binding.rcyclrvwSubCategories)
        viewModel.subCategoryAdapter = SubCategoryAdapter(viewModel.arylstSubCategory, this)
        binding.rcyclrvwSubCategories.adapter = viewModel.subCategoryAdapter
    }

    private fun getBundleData() {
        viewModel.catId = arguments?.getInt(BUNDLE_CAT_ID, -1)!!
        viewModel.catName = arguments?.getString(BUNDLE_CAT_NAME, "")!!
    }

    private fun getData() {
        viewModel.getSubCategoryByCatId(viewModel.catId).observe(viewLifecycleOwner, Observer {

            when (it.status) {
                Status.LOADING -> {
                    binding.prgrs.visibility = View.VISIBLE
                    Log.d(TAG, "getSubCategoryByCatId::${it.status}")
                }
                Status.SUCCESS -> {
                    binding.prgrs.visibility = View.GONE
                    Log.d(TAG, "getSubCategoryByCatId::${it.status}::${it.data}")
                    if (!it.data.isNullOrEmpty()) {
                        viewModel.setSubCategoryData(it.data)
                        binding.rcyclrvwSubCategories.visibility = View.VISIBLE
                        binding.noData.visibility = View.GONE
                    } else {
                        binding.rcyclrvwSubCategories.visibility = View.GONE
                        binding.noData.visibility = View.VISIBLE
                    }

                }
                Status.ERROR -> {
                    binding.prgrs.visibility = View.GONE
                    Log.d(TAG, "getSubCategoryByCatId::${it.status}::${it.message}")
                }
            }
        })
    }

    private fun getSubCategories(catId: Int, subCatId: Int) {
        viewModel.subCatId = subCatId
        viewModel.getSubCategoryBySubCatId(catId, subCatId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.prgrs.visibility = View.VISIBLE
                    Log.d(TAG, "getSubCategoryByCatId::${it.status}")
                }
                Status.SUCCESS -> {
                    binding.prgrs.visibility = View.GONE
                    Log.d(TAG, "getSubCategoryByCatId::${it.status}::${it.data}")
                    if (!it.data.isNullOrEmpty()) {
                        viewModel.setSubCategoryData(it.data)
                        binding.rcyclrvwSubCategories.visibility = View.VISIBLE
                    } else {
//                        binding.rcyclrvwSubCategories.visibility = View.GONE
                        navigateToProductList()
                    }
                }
                Status.ERROR -> {
                    binding.prgrs.visibility = View.GONE
                    Log.d(TAG, "getSubCategoryByCatId::${it.status}::${it.message}")
                }
            }
        })
    }

    private fun navigateTo(subCatNavigation: SubCategoryNavigation, bundle: Bundle?) {
        when (subCatNavigation) {
            SubCategoryNavigation.ADD_EDIT_SUB_CATEGORY -> {
                view?.findNavController()
                    ?.navigate(
                        R.id.action_subCategoryFragment_to_addEditSubCategoryFragment,
                        bundle
                    )
            }
            SubCategoryNavigation.SUB_CATEGORY -> {
            }
            SubCategoryNavigation.PRODUCT -> {
                view?.findNavController()
                    ?.navigate(R.id.action_subCategoryFragment_to_productsFragment, bundle)
            }
        }
    }

    private fun navigateToProductList() {
        var bundle = bundleOf(
            BUNDLE_SUB_CAT_ID to viewModel.subCatId
        )
        navigateTo(SubCategoryNavigation.PRODUCT, bundle)
    }

    private fun observeDeleteSubCategory() {
        viewModel.deleteSubCatResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.prgrs.visibility = View.VISIBLE
                    Log.d(TAG, "deleteSubCategory::${it.status}")
                }
                Status.SUCCESS -> {
                    binding.prgrs.visibility = View.GONE
                    Log.d(TAG, "deleteSubCategory::${it.status}::${it.data}")
                    val deleteSubCatResponse = it.data
                    if (deleteSubCatResponse != null) {
                        context?.showToast(deleteSubCatResponse.message!!)
                        getData()
                    }
                }
                Status.ERROR -> {
                    binding.prgrs.visibility = View.GONE
                    Log.d(TAG, "deleteSubCategory::${it.status}::${it.message}")

                }
            }
        })
    }

    override fun onItemClickListner(position: Int) {
        viewModel.selectedSubCat = viewModel.arylstSubCategory.get(position)
        Log.d(TAG, "Sub Category : ${viewModel.selectedSubCat}")
        if (viewModel.selectedSubCat?.subCatId != null) {
            getSubCategories(viewModel.selectedSubCat?.catId!!, viewModel.selectedSubCat?.id!!)
        } else {
            Log.d(TAG, "End of sub category")
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_add_sub_cat -> {
                val addSubCatBundle = viewModel.getCatBundleToAddSubCategory()
                addSubCatBundle.putBoolean(BUNDLE_EDIT_SUB_CAT, false)
                navigateTo(SubCategoryNavigation.ADD_EDIT_SUB_CATEGORY, addSubCatBundle)
            }
        }
    }

    override fun deleteUnderlayClicked(position: Int) {
        val subCategory = viewModel.arylstSubCategory[position]
        Log.d(TAG, "Delete Underlay Button clicked for : ${subCategory.name}")
        viewModel.deleteSubCategory(subCategory)
    }

    override fun editUnderlayClicked(position: Int) {
        val editSubCatBundle = viewModel.getSubCategoryBundle(position)
        editSubCatBundle.putBoolean(BUNDLE_EDIT_SUB_CAT, true)
        navigateTo(SubCategoryNavigation.ADD_EDIT_SUB_CATEGORY, editSubCatBundle)
    }
}

enum class SubCategoryNavigation {
    SUB_CATEGORY,
    PRODUCT,
    ADD_EDIT_SUB_CATEGORY
}