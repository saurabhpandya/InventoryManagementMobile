package com.fidato.inventorymngmnt.ui.supplier.fragment

import android.os.Bundle
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
import com.fidato.inventorymngmnt.constants.Constants
import com.fidato.inventorymngmnt.data.model.Supplier
import com.fidato.inventorymngmnt.data.supplier.SupplierNetworkDataProvider
import com.fidato.inventorymngmnt.databinding.FragmentSupplierBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.supplier.adapter.SupplierAdapter
import com.fidato.inventorymngmnt.ui.supplier.viewmodel.SupplierViewModel
import com.fidato.inventorymngmnt.utility.CategoryUnderlayButtonClickListner
import com.fidato.inventorymngmnt.utility.OnItemClickListner
import com.fidato.inventorymngmnt.utility.Status
import com.fidato.inventorymngmnt.utility.showToast

class SupplierFragment : BaseFragment(), OnItemClickListner, CategoryUnderlayButtonClickListner,
    View.OnClickListener {
    private val TAG = this::class.java.canonicalName

    private lateinit var binding: FragmentSupplierBinding
    private lateinit var viewModel: SupplierViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupplierBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        setListener()
//        getBundleData()
        setupObserver()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.requireActivity(),
            ViewModelFactory(
                SupplierNetworkDataProvider(RetrofitClient.SUPPPLIER_SERVICE),
                this.requireActivity().application
            )
        ).get(SupplierViewModel::class.java)
        binding.vm = viewModel
        binding.executePendingBindings()
    }


    private fun setupRecyclerView() {
        binding.rcyclrvwSuppliers.layoutManager = LinearLayoutManager(activity)
        binding.rcyclrvwSuppliers.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rcyclrvwSuppliers.setHasFixedSize(true)

        viewModel.supplierAdapter = SupplierAdapter(viewModel.getSupplierList(), this)
        viewModel.getSwipeHelper(this).attachToRecyclerView(binding.rcyclrvwSuppliers)
        binding.rcyclrvwSuppliers.adapter = viewModel.supplierAdapter
    }

    private fun setListener() {
        binding.fabAddSupplier.setOnClickListener(this)
    }

    private fun setupObserver() {
        viewModel.getSuppliers().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.prgrs.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.prgrs.visibility = View.GONE
                    activity?.showToast(it.message!!)
                }
                Status.SUCCESS -> {
                    binding.prgrs.visibility = View.GONE
                    viewModel.supplierAdapter.setSupplier(viewModel.getSupplierList())
                }
            }
        })
    }

    private fun navigateTo(customerNavigation: SupplierNavigation, bundle: Bundle) {
        when (customerNavigation) {
            SupplierNavigation.ADD_EDIT -> {
                view?.findNavController()
                    ?.navigate(R.id.action_supplierFragment_to_addEditSupplierFragment, bundle)
            }
            SupplierNavigation.DETAILS -> {
                view?.findNavController()
                    ?.navigate(R.id.action_supplierFragment_to_addEditSupplierFragment, bundle)
            }
        }

    }

    private fun navigateToAddSupplier() {
        val bundle = bundleOf(Constants.BUNDLE_EDIT_SUPPLIER to false)
        navigateTo(SupplierNavigation.ADD_EDIT, bundle)
    }

    private fun navigateToEditSupplier(supplier: Supplier) {
        val bundle = bundleOf(Constants.BUNDLE_EDIT_SUPPLIER to true)
        bundle.putParcelable(Constants.BUNDLE_SUPPLIER, supplier)
        navigateTo(SupplierNavigation.ADD_EDIT, bundle)
    }

    private fun navigateToSupplierDetail(supplier: Supplier) {
        val bundle = bundleOf(Constants.BUNDLE_SUPPLIER to supplier)
        navigateTo(SupplierNavigation.DETAILS, bundle)
    }

    override fun onItemClickListner(position: Int) {
        navigateToSupplierDetail(viewModel.getSupplierList()[position])
    }

    override fun deleteUnderlayClicked(position: Int) {

    }

    override fun editUnderlayClicked(position: Int) {
        navigateToEditSupplier(viewModel.getSupplierList()[position])
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_add_supplier -> {
                navigateToAddSupplier()
            }
        }
    }

}

enum class SupplierNavigation {
    ADD_EDIT,
    DETAILS
}