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
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_SUB_CAT_ID
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.data.model.master.SubCategory
import com.fidato.inventorymngmnt.databinding.FragmentSubCategoryBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.master.adapter.SubCategoryAdapter
import com.fidato.inventorymngmnt.ui.master.viewmodel.SubCategoryViewModel
import com.fidato.inventorymngmnt.utility.OnItemClickListner
import com.fidato.inventorymngmnt.utility.Status

class SubCategoryFragment : BaseFragment(), OnItemClickListner {

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
        setupRecyclerView()
        getBundleData()
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
        ).get(SubCategoryViewModel::class.java)
        binding.vm = viewModel
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

    private fun navigateToProductList() {
        var bundle = bundleOf(
            BUNDLE_SUB_CAT_ID to viewModel.subCatId
        )
        view?.findNavController()
            ?.navigate(R.id.action_subCategoryFragment_to_productsFragment, bundle)
    }

    override fun onItemClickListner(position: Int) {
        val subCategory = viewModel.arylstSubCategory.get(position)
        Log.d(TAG, "Sub Category : ${subCategory}")
        if (subCategory.subCatId != null) {
            getSubCategories(subCategory.catId!!, subCategory.id)
        } else {
            Log.d(TAG, "End of sub category")
        }

    }

}