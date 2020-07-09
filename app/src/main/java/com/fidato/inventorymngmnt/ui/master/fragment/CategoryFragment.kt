package com.fidato.inventorymngmnt.ui.master.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.base.BaseFragment
import com.fidato.inventorymngmnt.base.ViewModelFactory
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_CAT_ID
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_CAT_NAME
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.data.model.master.Category
import com.fidato.inventorymngmnt.databinding.FragmentCategoryBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.master.adapter.CategoryAdapter
import com.fidato.inventorymngmnt.ui.master.viewmodel.CategoryViewModel
import com.fidato.inventorymngmnt.utility.OnItemClickListner
import com.fidato.inventorymngmnt.utility.Status
import com.fidato.inventorymngmnt.utility.SwipeHelper


class CategoryFragment : BaseFragment(), OnItemClickListner {

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
        val swipeHelper = object : SwipeHelper(activity) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>
            ) {

                underlayButtons.add(
                    SwipeHelper.UnderlayButton("Edit",
                        0,
                        ContextCompat.getColor(activity!!, R.color.btn_edit),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {

                            }
                        })
                )

                underlayButtons.add(
                    SwipeHelper.UnderlayButton("Delete",
                        0,
                        ContextCompat.getColor(activity!!, R.color.btn_delete),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {

                            }
                        })
                )

            }

        }
//        ItemTouchHelper(SwipeToDeleteCallback(requireActivity(),viewModel.categoryAdapter))
        swipeHelper.attachToRecyclerView(binding.rcyclrvwCategories)
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
                    binding.noData.visibility = View.VISIBLE
                    val txtvwNoData = binding.noData.findViewById<TextView>(R.id.txtvw_no_data)
                    txtvwNoData.text = it.message
                    Log.d(TAG, "getCategory::${it.status}::${it.message}")
                }
            }
        })
    }

    override fun onItemClickListner(position: Int) {
        Log.d(TAG, "Category : ${viewModel.arylstCategory.get(position).name}")
        val category = viewModel.arylstCategory[position]

        var bundle = bundleOf(
            BUNDLE_CAT_ID to category.id,
            BUNDLE_CAT_NAME to category.name
        )
        view?.findNavController()
            ?.navigate(R.id.action_categoryFragment_to_subCategoryFragment, bundle)
    }
}

