package com.fidato.inventorymngmnt.ui.master.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.base.BaseFragment
import com.fidato.inventorymngmnt.base.ViewModelFactory
import com.fidato.inventorymngmnt.constants.Constants
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.databinding.FragmentCategoryBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.master.adapter.CategoryAdapter
import com.fidato.inventorymngmnt.ui.master.viewmodel.CategoryViewModel
import com.fidato.inventorymngmnt.utility.CategoryUnderlayButtonClickListner
import com.fidato.inventorymngmnt.utility.OnItemClickListner
import com.fidato.inventorymngmnt.utility.Status
import com.fidato.inventorymngmnt.utility.showToast


class CategoryFragment : BaseFragment(), OnItemClickListner, CategoryUnderlayButtonClickListner,
    View.OnClickListener {

    private val TAG: String = CategoryFragment::class.java.canonicalName.toString()

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModel: CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        setupFab()
        getData()
        observeDeleteCategory()
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
        ).get(CategoryViewModel::class.java)
        binding.vm = viewModel
    }

    private fun setupRecyclerView() {
        binding.rcyclrvwCategories.layoutManager = LinearLayoutManager(activity)
        binding.rcyclrvwCategories.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rcyclrvwCategories.setHasFixedSize(true)

        viewModel.categoryAdapter = CategoryAdapter(viewModel.getCategoryList(), this)
        viewModel.getSwipeHelper(this).attachToRecyclerView(binding.rcyclrvwCategories)
        binding.rcyclrvwCategories.adapter = viewModel.categoryAdapter
    }

    private fun setupFab() {
        binding.fabAddCat.setOnClickListener(this)
    }

    private fun getData() {
        viewModel.getCategory().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.prgrs.visibility = View.VISIBLE
                    Log.d(TAG, "getCategory::${it.status}")
                }
                Status.SUCCESS -> {
                    binding.prgrs.visibility = View.GONE
                    Log.d(TAG, "getCategory::${it.status}::${it.data}")
                    if (!it.data.isNullOrEmpty()) {
                        viewModel.setCategoryData(it.data)
                        binding.rcyclrvwCategories.visibility = View.VISIBLE
                        binding.noData.visibility = View.GONE
                    } else {
                        binding.rcyclrvwCategories.visibility = View.GONE
                        binding.noData.visibility = View.VISIBLE
                    }
                }
                Status.ERROR -> {
                    binding.prgrs.visibility = View.GONE
                    binding.rcyclrvwCategories.visibility = View.GONE
                    binding.noData.visibility = View.VISIBLE
                    val txtvwNoData = binding.noData.findViewById<TextView>(R.id.txtvw_no_data)
                    txtvwNoData.text = it.message
                    Log.d(TAG, "getCategory::${it.status}::${it.message}")
                }
            }
        })
    }

    private fun observeDeleteCategory() {
        viewModel.deleteCatResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.prgrs.visibility = View.VISIBLE
                    Log.d(TAG, "deleteCategory::${it.status}")
                }
                Status.SUCCESS -> {
                    binding.prgrs.visibility = View.GONE
                    Log.d(TAG, "deleteCategory::${it.status}::${it.data}")
                    val deleteCatResponse = it.data
                    if (deleteCatResponse != null) {
                        context?.showToast(deleteCatResponse.message!!)
                        getData()
                    }
                }
                Status.ERROR -> {
                    binding.prgrs.visibility = View.GONE
                    Log.d(TAG, "deleteCategory::${it.status}::${it.message}")

                }
            }
        })
    }

    private fun navigateTo(navigation: CategoryNavigation, bundle: Bundle?) {
        when (navigation) {
            CategoryNavigation.SUB_CATEGORY -> {
                view?.findNavController()
                    ?.navigate(R.id.action_categoryFragment_to_subCategoryFragment, bundle)
            }
            CategoryNavigation.ADD_EDIT_CATEGORY -> {
                view?.findNavController()
                    ?.navigate(R.id.action_categoryFragment_to_addEditCategoryFragment, bundle)
            }
        }
    }

    override fun onItemClickListner(position: Int) {
        Log.d(TAG, "Category : ${viewModel.getCategoryList()[position].name}")
        val bundle = viewModel.getCategoryBundle(position)
        navigateTo(CategoryNavigation.SUB_CATEGORY, bundle)
    }

    override fun deleteUnderlayClicked(position: Int) {
        val category = viewModel.getCategoryList().get(position)
        Log.d(TAG, "Delete Underlay Button clicked for : ${category.name}")
        viewModel.deleteCategory(category)
    }

    override fun editUnderlayClicked(position: Int) {
        val bundle = viewModel.getCategoryBundle(position)
        bundle.putBoolean(Constants.BUNDLE_EDIT_CAT, true)
        navigateTo(CategoryNavigation.ADD_EDIT_CATEGORY, bundle)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_add_cat -> {
                navigateTo(CategoryNavigation.ADD_EDIT_CATEGORY, null)
            }
        }
    }

}

enum class CategoryNavigation {
    SUB_CATEGORY,
    ADD_EDIT_CATEGORY
}


