package com.fidato.inventorymngmnt.ui.products.fragments

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
import com.fidato.inventorymngmnt.constants.Constants
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_SUB_CAT_ID
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.data.model.master.Product
import com.fidato.inventorymngmnt.databinding.FragmentProductsBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.products.adapter.ProductAdapter
import com.fidato.inventorymngmnt.ui.products.viewmodel.ProductViewModel
import com.fidato.inventorymngmnt.utility.OnItemClickListner
import com.fidato.inventorymngmnt.utility.Status

class ProductsFragment : BaseFragment(), OnItemClickListner {

    private val TAG: String = ProductsFragment::class.java.canonicalName.toString()

    private lateinit var binding: FragmentProductsBinding
    private lateinit var viewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
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
        ).get(ProductViewModel::class.java)
        binding.vm = viewModel
    }

    private fun setupRecyclerView() {
        viewModel.arylstProduct = ArrayList<Product>()
        binding.rcyclrvwProducts.layoutManager = LinearLayoutManager(activity)
        binding.rcyclrvwProducts.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rcyclrvwProducts.setHasFixedSize(true)
        viewModel.productAdapter = ProductAdapter(viewModel.arylstProduct, this)
        binding.rcyclrvwProducts.adapter = viewModel.productAdapter
    }

    private fun getBundleData() {
        viewModel.subCatId = arguments?.getInt(BUNDLE_SUB_CAT_ID, -1)!!
    }

    private fun getData() {
        viewModel.getProductsBySubCatId().observe(viewLifecycleOwner, Observer {

            when (it.status) {
                Status.LOADING -> {
                    binding.prgrs.visibility = View.VISIBLE
                    Log.d(TAG, "getProductsBySubCatId::${it.status}")
                }
                Status.SUCCESS -> {
                    binding.prgrs.visibility = View.GONE
                    Log.d(TAG, "getProductsBySubCatId::${it.status}::${it.data}")
                    if (!it.data.isNullOrEmpty()) {
                        viewModel.setProductData(it.data)
                        binding.rcyclrvwProducts.visibility = View.VISIBLE
                        binding.noData.visibility = View.GONE
                    } else {
                        binding.rcyclrvwProducts.visibility = View.GONE
                        binding.noData.visibility = View.VISIBLE
                    }
                }
                Status.ERROR -> {
                    binding.prgrs.visibility = View.GONE
                    binding.noData.visibility = View.VISIBLE
                    Log.d(TAG, "getProductsBySubCatId::${it.status}::${it.message}")
                }
            }
        })
    }


    private fun navigateToProductDetails() {
        var bundle = bundleOf(
            Constants.BUNDLE_PRODUCT_ID to viewModel.productId
        )
        view?.findNavController()
            ?.navigate(R.id.action_productsFragment_to_productDetailFragment, bundle)
    }

    override fun onItemClickListner(position: Int) {
        val product = viewModel.arylstProduct.get(position)
        Log.d(TAG, "Product : ${product}")
        viewModel.productId = product.id
        navigateToProductDetails()
    }

}