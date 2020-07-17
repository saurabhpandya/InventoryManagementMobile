package com.fidato.inventorymngmnt.ui.products.fragments

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.databinding.FragmentAddEditProductBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.products.adapter.AddProductVariantAdapter
import com.fidato.inventorymngmnt.ui.products.viewmodel.AddEditProductViewModel
import com.fidato.inventorymngmnt.utility.CategoryUnderlayButtonClickListner
import com.fidato.inventorymngmnt.utility.OnItemClickListner
import com.fidato.inventorymngmnt.utility.Status
import com.fidato.inventorymngmnt.utility.showToast

class AddEditProductFragment : BaseFragment(), OnItemClickListner,
    CategoryUnderlayButtonClickListner, View.OnClickListener {
    private val TAG: String = AddEditProductFragment::class.java.canonicalName.toString()

    private lateinit var binding: FragmentAddEditProductBinding
    private lateinit var viewModel: AddEditProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditProductBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewModel()
        setupRecyclerView()
        setListener()
        getData()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.requireActivity(),
            ViewModelFactory(
                MasterNetworkDataProvider(RetrofitClient.MASTER_SERVICE),
                this.requireActivity().application
            )
        ).get(AddEditProductViewModel::class.java)
        binding.vm = viewModel
        binding.product = viewModel.product
        binding.executePendingBindings()
    }

    private fun setupRecyclerView() {

        binding.rcyclrvwProductVariant.layoutManager = LinearLayoutManager(activity)
        binding.rcyclrvwProductVariant.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rcyclrvwProductVariant.setHasFixedSize(true)
        viewModel.getSwipeHelper(this).attachToRecyclerView(binding.rcyclrvwProductVariant)
        viewModel.addProductVariantAdap =
            AddProductVariantAdapter(viewModel.product.productVariantMapping, this)
        binding.rcyclrvwProductVariant.adapter = viewModel.addProductVariantAdap
    }

    private fun setListener() {
        binding.btnAddProductVariant.setOnClickListener(this)
        binding.tiedttxtPrdctSubCat.setOnClickListener(this)
    }

    private fun getData() {

        getBundleData()
        viewModel.getProduct().observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "getProduct: Status: ${it.status}")
            when (it.status) {
                Status.LOADING -> {
                    binding.prgrs.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    Log.d(TAG, "getProduct: Error Message: ${it.message}")
                    binding.prgrs.visibility = View.GONE
                    activity?.showToast(it.message!!)
                }
                Status.SUCCESS -> {
                    Log.d(TAG, "getProduct: Data: ${it.data}")
                    binding.prgrs.visibility = View.GONE
                    viewModel.product = it.data!!
                    binding.tiedttxtPrdctName.setText(viewModel.product.name)
                    binding.tiedttxtPrdctDesc.setText(viewModel.product.description)
                    viewModel.addProductVariantAdap.setProductVariant(viewModel.product.productVariantMapping)
                    getSubCategory()
                }
            }
        })
    }

    private fun getSubCategory() {
        viewModel.getSubCategory().observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "getSubCategory: Status: ${it.status}")
            when (it.status) {
                Status.LOADING -> {
                    binding.prgrs.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    Log.d(TAG, "getSubCategory: Error Message: ${it.message}")
                    binding.prgrs.visibility = View.GONE
                    activity?.showToast(it.message!!)
                }
                Status.SUCCESS -> {
                    Log.d(TAG, "getSubCategory: Data: ${it.data}")
                    binding.prgrs.visibility = View.GONE
                    viewModel.subCategories = it.data!!
                    if (!viewModel.subCategories.isNullOrEmpty() && viewModel.selSubCatIndex >= 0)
                        binding.tiedttxtPrdctSubCat.setText(viewModel.subCategories[viewModel.selSubCatIndex].name)

                }
            }
        })

        viewModel.saveProductResponse.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "saveProductResponse: Status: ${it.status}")
            when (it.status) {
                Status.LOADING -> {
                    binding.prgrs.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    Log.d(TAG, "saveProductResponse: Error Message: ${it.message}")
                    binding.prgrs.visibility = View.GONE
                    activity?.showToast(it.message!!)
                }
                Status.SUCCESS -> {
                    Log.d(TAG, "saveProductResponse: Data: ${it.data}")
                    binding.prgrs.visibility = View.GONE
                    activity?.showToast(it.data?.message!!)
                    onBackPressed()
                }
            }
        })
    }

    private fun getBundleData() {
        viewModel.setBundleData(arguments)
    }

    private fun navigateTo(productNavigation: ProductNavigation, bundle: Bundle?) {
        when (productNavigation) {
            ProductNavigation.ADD_EDIT_PRODUCT_VARIANT -> {
                view?.findNavController()
                    ?.navigate(
                        R.id.action_addEditProductFragment_to_addEditProductVariantFragment,
                        bundle
                    )
            }
        }
    }

    private fun navigateToAddProductVariant() {
        val bundle = bundleOf(Constants.BUNDLE_EDIT_PRODUCT_VARIANT to false)
        bundle.putInt(Constants.BUNDLE_PRODUCT_ID, viewModel.product.id!!)
        bundle.putString(Constants.BUNDLE_PRODUCT_NAME, viewModel.product.name!!)
        navigateTo(ProductNavigation.ADD_EDIT_PRODUCT_VARIANT, bundle)
    }

    private fun navigateToEditProductVariant(productVarientIndex: Int) {
        val bundle = bundleOf(Constants.BUNDLE_EDIT_PRODUCT_VARIANT to true)
        bundle.putParcelable(
            Constants.BUNDLE_PRODUCT_VARIANT,
            viewModel.product.productVariantMapping[productVarientIndex]
        )
        bundle.putString(Constants.BUNDLE_PRODUCT_NAME, viewModel.product.name!!)
        navigateTo(ProductNavigation.ADD_EDIT_PRODUCT_VARIANT, bundle)
    }

    private fun showSubCategoryList() {
        AlertDialog.Builder(activity).apply {
            setTitle(R.string.hint_select_sub_cat)
            setSingleChoiceItems(viewModel.subCategoryAdapter, viewModel.selSubCatIndex, null)
            setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    dialog.dismiss()
                    viewModel.selSubCatIndex = (dialog as AlertDialog).listView.checkedItemPosition

                    if (viewModel.selSubCatIndex >= 0) {
                        val selectedSubCat =
                            viewModel.subCategories[viewModel.selSubCatIndex]
                        Log.d(TAG, "Selected Sub Category : ${selectedSubCat}")
                        viewModel.product.subCatId = selectedSubCat.id
                        binding.tiedttxtPrdctSubCat.setText(selectedSubCat.name)
                    }
                })
            show()
        }
    }

    override fun onItemClickListner(position: Int) {

    }

    override fun deleteUnderlayClicked(position: Int) {

    }

    override fun editUnderlayClicked(position: Int) {
        navigateToEditProductVariant(position)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_product_variant -> {
                navigateToAddProductVariant()
            }
            R.id.tiedttxt_prdct_sub_cat -> {
                showSubCategoryList()
            }
        }
    }

}