package com.fidato.inventorymngmnt.ui.main.master.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fidato.inventorymngmnt.base.ViewModelFactory
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.data.model.master.Category
import com.fidato.inventorymngmnt.databinding.FragmentCategoryBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.main.master.adapter.CategoryAdapter
import com.fidato.inventorymngmnt.ui.main.master.viewmodel.CategoryViewModel
import com.fidato.inventorymngmnt.utility.OnItemClickListner
import com.fidato.inventorymngmnt.utility.Status

class CategoryFragment : Fragment(), OnItemClickListner {

    private val TAG: String = CategoryFragment::class.java.canonicalName.toString()

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModel: CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupRecyclerView()
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
        ).get(CategoryViewModel::class.java)
        binding.vm = viewModel
    }

    private fun setupRecyclerView() {
        viewModel.arylstCategory = ArrayList<Category>()
        binding.rcyclrvwCategories.layoutManager = LinearLayoutManager(activity)
        binding.rcyclrvwCategories.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rcyclrvwCategories.setHasFixedSize(true)
        viewModel.categoryAdapter = CategoryAdapter(viewModel.arylstCategory, this)
        binding.rcyclrvwCategories.adapter = viewModel.categoryAdapter
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
                    Log.d(TAG, "getCategory::${it.status}::${it.message}")
                }
            }
        })
    }

    override fun onItemClickListner(position: Int) {
        Log.d(TAG, "Category : ${viewModel.arylstCategory.get(position).name}")
    }

}