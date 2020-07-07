package com.fidato.inventorymngmnt.ui.products.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fidato.inventorymngmnt.base.BaseFragment
import com.fidato.inventorymngmnt.base.ViewModelFactory
import com.fidato.inventorymngmnt.constants.Constants
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.data.model.master.ProductVarient
import com.fidato.inventorymngmnt.databinding.FragmentProductDetailBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.products.adapter.ProductVariantAdapter
import com.fidato.inventorymngmnt.ui.products.viewmodel.ProductDetailsViewModel
import com.fidato.inventorymngmnt.utility.ProductVarientItemClickListner

class ProductDetailFragment : BaseFragment(), ProductVarientItemClickListner {

    private val TAG: String = ProductDetailFragment::class.java.canonicalName.toString()

    private lateinit var binding: FragmentProductDetailBinding
    private lateinit var viewModel: ProductDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        getBundleData()
//        getData()
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
        ).get(ProductDetailsViewModel::class.java)
        binding.vm = viewModel
    }

    private fun setupRecyclerView() {
        viewModel.arylstProductColorVarient = ArrayList<ProductVarient>()
        binding.rcyclrvwVarientSize.layoutManager = LinearLayoutManager(activity)
        binding.rcyclrvwVarientSize.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rcyclrvwVarientSize.setHasFixedSize(true)
        viewModel.productColorVarientAdapter =
            ProductVariantAdapter(
                viewModel.arylstProductColorVarient,
                true,
                this
            )
        binding.rcyclrvwVarientSize.adapter = viewModel.productColorVarientAdapter
    }

    private fun getBundleData() {
        viewModel.productId = arguments?.getInt(Constants.BUNDLE_PRODUCT_ID, -1)!!
    }

    override fun onProductVariantClickListner(position: Int, isForSize: Boolean) {

    }

}