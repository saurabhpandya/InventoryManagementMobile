package com.fidato.inventorymngmnt.ui.customer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.base.BaseFragment
import com.fidato.inventorymngmnt.base.ViewModelFactory
import com.fidato.inventorymngmnt.data.customer.CustomerNetworkDataProvider
import com.fidato.inventorymngmnt.databinding.FragmentAddEditCustomerBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.customer.viewmodel.AddEditCustomerViewModel
import com.fidato.inventorymngmnt.utility.Status
import com.fidato.inventorymngmnt.utility.showToast

class AddEditCustomerFragment : BaseFragment() {
    private val TAG = this::class.java.canonicalName

    private lateinit var binding: FragmentAddEditCustomerBinding
    private lateinit var viewModel: AddEditCustomerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditCustomerBinding.inflate(inflater, container, false)
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
                CustomerNetworkDataProvider(RetrofitClient.CUSTOMER_SERVICE),
                this.requireActivity().application
            )
        ).get(AddEditCustomerViewModel::class.java)
        binding.vm = viewModel
        binding.customer = viewModel.customer
        binding.executePendingBindings()
    }

    private fun getBundleData() {
        viewModel.setBundleData(arguments)
        binding.customer = viewModel.customer
        if (viewModel.isEdit) {
            updateTitle(getString(R.string.title_edit_cust))
        } else {
            updateTitle(getString(R.string.title_add_cust))
        }
        if (viewModel.customer.id != null && viewModel.customer.id!! > 0)
            binding.tiedttxtCustId.setText("${viewModel.customer.id!!}")
        if (!viewModel.customer.name.isEmpty())
            binding.tiedttxtCustName.setText(viewModel.customer.name)
        if (!viewModel.customer.email.isEmpty())
            binding.tiedttxtCustEmail.setText(viewModel.customer.email)
        if (!viewModel.customer.mobile.isEmpty())
            binding.tiedttxtCustMobile.setText(viewModel.customer.mobile)
        if (!viewModel.customer.password.isEmpty())
            binding.tiedttxtCustPassword.setText(viewModel.customer.password)
    }

    private fun setupObserver() {
        viewModel.saveCustomerResponse.observe(viewLifecycleOwner, Observer {
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