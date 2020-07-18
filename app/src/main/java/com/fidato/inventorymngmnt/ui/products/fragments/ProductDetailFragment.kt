package com.fidato.inventorymngmnt.ui.products.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.fidato.inventorymngmnt.utility.Status
import com.fidato.inventorymngmnt.utility.showToast

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
        binding.lifecycleOwner = this
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
        ).get(ProductDetailsViewModel::class.java)
        binding.vm = viewModel
        binding.product = viewModel.product
        binding.executePendingBindings()
    }

    private fun setupRecyclerView() {
        setupRVForSize()
        setupRVForColor()
    }

    private fun setupRVForSize() {
        viewModel.arylstProductSizeVarient = ArrayList<ProductVarient>()

        val linearLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rcyclrvwVarientSize.layoutManager = linearLayoutManager

        viewModel.productSizeVarientAdapter =
            ProductVariantAdapter(
                viewModel.arylstProductSizeVarient,
                true,
                this
            )
        binding.rcyclrvwVarientSize.adapter = viewModel.productSizeVarientAdapter
    }

    private fun setupRVForColor() {
        viewModel.arylstProductColorVarient = ArrayList<ProductVarient>()

        val linearLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rcyclrvwVarientColor.layoutManager = linearLayoutManager

        viewModel.productColorVarientAdapter =
            ProductVariantAdapter(
                viewModel.arylstProductColorVarient,
                false,
                this
            )
        binding.rcyclrvwVarientColor.adapter = viewModel.productColorVarientAdapter
    }

    private fun getBundleData() {
        viewModel.productId = arguments?.getInt(Constants.BUNDLE_PRODUCT_ID, -1)!!
    }

    private fun getData() {
        viewModel.getProductsById().observe(viewLifecycleOwner, Observer {
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

                    viewModel.product = it.data!!

                    setData()

                }
            }
        })
    }

    private fun setData() {
        binding.txtvwProductName.text = viewModel.product.name
        binding.txtvwProductDesc.text = viewModel.product.description

        setDataForProductSizeRV()
        setDataForProductColorRV(0)
    }

    private fun setDataForProductSizeRV() {
        val arrStrSize = viewModel.productVariantMapBySize.keys.toList() as ArrayList<String>
        viewModel.arylstProductSizeVarient = ArrayList<ProductVarient>()
        for (size in arrStrSize) {
            viewModel.arylstProductSizeVarient.add(ProductVarient(size = size))
        }

        if (!viewModel.arylstProductSizeVarient.isEmpty()) {
            selectSizeForAdapter(0)
            viewModel.productSizeVarientAdapter.setProductVariants(viewModel.arylstProductSizeVarient)
            binding.rcyclrvwVarientSize.visibility = View.VISIBLE
        } else {
            binding.rcyclrvwVarientSize.visibility = View.GONE
        }

    }

    private fun setDataForProductColorRV(position: Int) {
        if (!viewModel.arylstProductSizeVarient.isNullOrEmpty() && position >= 0) {
            viewModel.arylstProductColorVarient =
                viewModel.productVariantMapBySize.get(viewModel.arylstProductSizeVarient[position].size) as ArrayList<ProductVarient>
            selectColorForAdapter(0)
            setPrice(0)
            binding.rcyclrvwVarientColor.visibility = View.VISIBLE
        } else {
            binding.rcyclrvwVarientColor.visibility = View.GONE
        }
        viewModel.productColorVarientAdapter.setProductVariants(viewModel.arylstProductColorVarient)

    }

    private fun setPrice(position: Int) {
        if (!viewModel.arylstProductColorVarient.isNullOrEmpty()) {
            binding.txtvwProductPrice.text =
                viewModel.arylstProductColorVarient[position].price.toString()
        }
    }

    private fun selectColorForAdapter(position: Int) {
        for ((index, _) in viewModel.arylstProductColorVarient.withIndex()) {
            Log.d(TAG, "selectColorForAdapter:: index:$index, position:$position")
            viewModel.arylstProductColorVarient[index].selected = position == index
        }
        viewModel.productColorVarientAdapter.notifyDataSetChanged()
    }

    private fun selectSizeForAdapter(position: Int) {
        for ((index, _) in viewModel.arylstProductSizeVarient.withIndex()) {
            Log.d(TAG, "selectSizeForAdapter:: index:$index, position:$position")
            viewModel.arylstProductSizeVarient[index].selected = position == index
        }
        viewModel.productSizeVarientAdapter.notifyDataSetChanged()
    }

    override fun onProductVariantClickListner(position: Int, isForSize: Boolean) {
        if (isForSize) {
            selectSizeForAdapter(position)
            setDataForProductColorRV(position)
        } else {
            selectColorForAdapter(position)
            setPrice(position)
        }
    }

}