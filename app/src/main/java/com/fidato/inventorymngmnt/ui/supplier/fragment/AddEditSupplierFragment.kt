package com.fidato.inventorymngmnt.ui.supplier.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.base.BaseFragment
import com.fidato.inventorymngmnt.base.ViewModelFactory
import com.fidato.inventorymngmnt.data.supplier.SupplierNetworkDataProvider
import com.fidato.inventorymngmnt.databinding.FragmentAddEditSupplierBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.supplier.viewmodel.AddEditSupplierViewModel
import com.fidato.inventorymngmnt.utility.Status
import com.fidato.inventorymngmnt.utility.showToast

class AddEditSupplierFragment : BaseFragment() {
    private val TAG = this::class.java.canonicalName

    private lateinit var binding: FragmentAddEditSupplierBinding
    private lateinit var viewModel: AddEditSupplierViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditSupplierBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
//        setListener()
        getBundleData()
        setupObserver()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.requireActivity(),
            ViewModelFactory(
                SupplierNetworkDataProvider(RetrofitClient.SUPPPLIER_SERVICE),
                this.requireActivity().application
            )
        ).get(AddEditSupplierViewModel::class.java)
        binding.vm = viewModel
        binding.supplier = viewModel.supplier
        binding.executePendingBindings()
    }

    private fun getBundleData() {
        viewModel.setBundleData(arguments)
        binding.supplier = viewModel.supplier
        if (viewModel.isEdit) {
            updateTitle(getString(R.string.title_edit_sup))
        } else {
            updateTitle(getString(R.string.title_add_sup))
        }
        if (viewModel.supplier.id != null && viewModel.supplier.id!! > 0)
            binding.tiedttxtSupId.setText("${viewModel.supplier.id!!}")
        if (!viewModel.supplier.name.isEmpty())
            binding.tiedttxtSupName.setText(viewModel.supplier.name)
        if (!viewModel.supplier.email.isEmpty())
            binding.tiedttxtSupEmail.setText(viewModel.supplier.email)
        if (!viewModel.supplier.password.isEmpty())
            binding.tiedttxtSupPassword.setText(viewModel.supplier.password)
    }

    private fun setupObserver() {
        viewModel.saveSupplierResponse.observe(viewLifecycleOwner, Observer {
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
                    activity?.showToast(it.data?.message!!)
                    onBackPressed()
                }
            }
        })
    }

}