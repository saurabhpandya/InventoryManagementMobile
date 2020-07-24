package com.fidato.inventorymngmnt.ui.customer.fragment

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
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_CUSTOMER
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_EDIT_CUSTOMER
import com.fidato.inventorymngmnt.data.customer.CustomerNetworkDataProvider
import com.fidato.inventorymngmnt.data.model.Customer
import com.fidato.inventorymngmnt.databinding.FragmentCustomerBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.customer.adapter.CustomerAdapter
import com.fidato.inventorymngmnt.ui.customer.viewmodel.CustomerViewModel
import com.fidato.inventorymngmnt.utility.CategoryUnderlayButtonClickListner
import com.fidato.inventorymngmnt.utility.OnItemClickListner
import com.fidato.inventorymngmnt.utility.Status
import com.fidato.inventorymngmnt.utility.showToast

class CustomerFragment : BaseFragment(), OnItemClickListner, CategoryUnderlayButtonClickListner,
    View.OnClickListener {

    private val TAG = this::class.java.canonicalName

    private lateinit var binding: FragmentCustomerBinding
    private lateinit var viewModel: CustomerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomerBinding.inflate(inflater, container, false)
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
                CustomerNetworkDataProvider(RetrofitClient.CUSTOMER_SERVICE),
                this.requireActivity().application
            )
        ).get(CustomerViewModel::class.java)
        binding.vm = viewModel
        binding.executePendingBindings()
    }


    private fun setupRecyclerView() {
        binding.rcyclrvwCustomers.layoutManager = LinearLayoutManager(activity)
        binding.rcyclrvwCustomers.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rcyclrvwCustomers.setHasFixedSize(true)

        viewModel.customerAdapter = CustomerAdapter(viewModel.getCustomerList(), this)
        viewModel.getSwipeHelper(this).attachToRecyclerView(binding.rcyclrvwCustomers)
        binding.rcyclrvwCustomers.adapter = viewModel.customerAdapter
    }

    private fun setListener() {
        binding.fabAddCustomer.setOnClickListener(this)
    }

    private fun setupObserver() {
        viewModel.getCustomers().observe(viewLifecycleOwner, Observer {
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
                    viewModel.customerAdapter.setCustomer(viewModel.getCustomerList())
                }
            }
        })
    }

    private fun navigateTo(customerNavigation: CustomerNavigation, bundle: Bundle) {
        when (customerNavigation) {
            CustomerNavigation.ADD_EDIT -> {
                view?.findNavController()
                    ?.navigate(R.id.action_customerFragment_to_addEditCustomerFragment, bundle)
            }
            CustomerNavigation.DETAILS -> {
                view?.findNavController()
                    ?.navigate(R.id.action_customerFragment_to_addEditCustomerFragment, bundle)
            }
        }

    }

    private fun navigateToAddCustomer() {
        val bundle = bundleOf(BUNDLE_EDIT_CUSTOMER to false)
        navigateTo(CustomerNavigation.ADD_EDIT, bundle)
    }

    private fun navigateToEditCustomer(customer: Customer) {
        val bundle = bundleOf(BUNDLE_EDIT_CUSTOMER to true)
        bundle.putParcelable(BUNDLE_CUSTOMER, customer)
        navigateTo(CustomerNavigation.ADD_EDIT, bundle)
    }

    private fun navigateToCustomerDetail(customer: Customer) {
        val bundle = bundleOf(BUNDLE_CUSTOMER to customer)
        navigateTo(CustomerNavigation.DETAILS, bundle)
    }

    override fun onItemClickListner(position: Int) {
        navigateToCustomerDetail(viewModel.getCustomerList()[position])
    }

    override fun deleteUnderlayClicked(position: Int) {

    }

    override fun editUnderlayClicked(position: Int) {
        navigateToEditCustomer(viewModel.getCustomerList()[position])
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_add_customer -> {
                navigateToAddCustomer()
            }
        }
    }

}

enum class CustomerNavigation {
    ADD_EDIT,
    DETAILS
}