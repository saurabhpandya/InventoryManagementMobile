package com.fidato.inventorymngmnt.ui.products.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.base.BaseFragment
import com.fidato.inventorymngmnt.base.ViewModelFactory
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.databinding.FragmentAddEditProductVariantBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.products.viewmodel.AddEditProductVariantViewModel
import com.fidato.inventorymngmnt.utility.Status
import com.fidato.inventorymngmnt.utility.showToast

class AddEditProductVariantFragment : BaseFragment() {
    private val TAG: String = this::class.java.canonicalName.toString()

    private lateinit var binding: FragmentAddEditProductVariantBinding
    private lateinit var viewModel: AddEditProductVariantViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditProductVariantBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setListener()
        getData()
        setData()

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.requireActivity(),
            ViewModelFactory(
                MasterNetworkDataProvider(RetrofitClient.MASTER_SERVICE),
                this.requireActivity().application
            )
        ).get(AddEditProductVariantViewModel::class.java)
        binding.vm = viewModel
        binding.productVariant = viewModel.productVariant
        binding.executePendingBindings()
    }

    private fun setListener() {

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            Log.d(TAG, "onBackPressedDispatcher")
//            navigateUp()
//        }

        viewModel.saveProductVariantResponse.observe(viewLifecycleOwner, Observer {
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

    private fun getData() {
        viewModel.getBundleData(requireArguments())
        binding.productVariant = viewModel.productVariant
        binding.executePendingBindings()
    }

    private fun setData() {
        var title = ""
        if (viewModel.isEdit) {
            title = getString(R.string.title_edit_variant)
            val pVPrice = viewModel.productVariant.price
            val pvQuantity = viewModel.productVariant.quantity
            val pvColor = viewModel.productVariant.color
            val pVSize = viewModel.productVariant.size
            if (pVPrice != null && pVPrice > 0) {
                binding.tiedttxtPrdctPrice.setText("$pVPrice")
            }
            if (pvQuantity != null && pvQuantity > 0) {
                binding.tiedttxtPrdctQnty.setText("$pvQuantity")
            }
            if (!pvColor.isNullOrEmpty()) {
                binding.tiedttxtPrdctColor.setText("$pvColor")
            }
            if (!pVSize.isNullOrEmpty()) {
                binding.tiedttxtPrdctSize.setText("$pVSize")
            }
            binding.btnAddPrdctVariant.setText(R.string.btn_update)
        } else {
            title = getString(R.string.title_add_variant)
            binding.btnAddPrdctVariant.setText(R.string.btn_save)
        }
        updateTitle(title)
        val pName = viewModel.productName
        if (!pName.isNullOrEmpty())
            binding.txtvwPrdctName.text = pName
    }


}