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
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_EDIT_PRODUCT
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_SUB_CAT_ID
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.data.model.master.Product
import com.fidato.inventorymngmnt.databinding.FragmentProductsBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.products.adapter.ProductAdapter
import com.fidato.inventorymngmnt.ui.products.viewmodel.ProductViewModel
import com.fidato.inventorymngmnt.utility.CategoryUnderlayButtonClickListner
import com.fidato.inventorymngmnt.utility.OnItemClickListner
import com.fidato.inventorymngmnt.utility.Status

class ProductsFragment : BaseFragment(), OnItemClickListner,
    View.OnClickListener, CategoryUnderlayButtonClickListner {

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
        setListener()
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
        viewModel.getSwipeHelper(this).attachToRecyclerView(binding.rcyclrvwProducts)
        viewModel.productAdapter = ProductAdapter(viewModel.arylstProduct, this)
        binding.rcyclrvwProducts.adapter = viewModel.productAdapter
    }

    private fun setListener() {
        binding.fabAddProduct.setOnClickListener(this)
    }

    private fun getBundleData() {
        if (arguments != null) {
            viewModel.subCatId = requireArguments().getInt(BUNDLE_SUB_CAT_ID, -1)
        }
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

    private fun navigateTo(productNavigation: ProductNavigation, bundle: Bundle?) {
        when (productNavigation) {
            ProductNavigation.ADD_EDIT_PRODUCT -> {
                view?.findNavController()
                    ?.navigate(
                        R.id.action_productsFragment_to_addEditProductFragment,
                        bundle
                    )
            }
            ProductNavigation.PRODUCT_DETAIL -> {
                view?.findNavController()
                    ?.navigate(R.id.action_productsFragment_to_productDetailFragment, bundle)
            }
        }
    }

    private fun navigateToProductAddEdit(isEdit: Boolean, position: Int) {
        var bundle: Bundle
        if (isEdit) {
            bundle = viewModel.getBundleToEditProduct(viewModel.arylstProduct[position])
        } else {
            bundle = bundleOf(BUNDLE_SUB_CAT_ID to viewModel.subCatId)
        }
        bundle.putBoolean(BUNDLE_EDIT_PRODUCT, isEdit)
        navigateTo(ProductNavigation.ADD_EDIT_PRODUCT, bundle)
    }

    private fun navigateToProductDetails() {
        var bundle = bundleOf(
            Constants.BUNDLE_PRODUCT_ID to viewModel.productId
        )
        navigateTo(ProductNavigation.PRODUCT_DETAIL, bundle)
    }

    override fun onItemClickListner(position: Int) {
        val product = viewModel.arylstProduct.get(position)
        Log.d(TAG, "Product : ${product.name}")
        viewModel.productId = product.id ?: -1
        navigateToProductDetails()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_add_product -> {
                navigateToProductAddEdit(false, -1)
            }
        }
    }

    override fun deleteUnderlayClicked(position: Int) {
        Log.d(TAG, "Delete Underlay Button clicked at : $position")
        viewModel.deleteProduct(position)
    }

    override fun editUnderlayClicked(position: Int) {
        navigateToProductAddEdit(true, position)
    }

}

enum class ProductNavigation {
    PRODUCT_DETAIL,
    ADD_EDIT_PRODUCT,
    ADD_EDIT_PRODUCT_VARIANT
}