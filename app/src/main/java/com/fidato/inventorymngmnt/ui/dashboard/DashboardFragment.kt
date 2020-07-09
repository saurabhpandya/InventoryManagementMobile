package com.fidato.inventorymngmnt.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.base.BaseFragment
import com.fidato.inventorymngmnt.base.ViewModelFactory
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.databinding.FragmentDashboardBinding
import com.fidato.inventorymngmnt.networking.RetrofitClient
import com.fidato.inventorymngmnt.ui.dashboard.adapter.DashboardAdapter
import com.fidato.inventorymngmnt.ui.dashboard.viewmodel.DashboardViewModel
import com.fidato.inventorymngmnt.utility.OnItemClickListner
import com.fidato.inventorymngmnt.utility.showToast

class DashboardFragment : BaseFragment(), OnItemClickListner {
    private val TAG = DashboardFragment::class.java.canonicalName

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupRecyclerView()
//        getBundleData()
//        getData()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.requireActivity(),
            ViewModelFactory(
                MasterNetworkDataProvider(RetrofitClient.MASTER_SERVICE),
                this.requireActivity().application
            )
        ).get(DashboardViewModel::class.java)
        binding.vm = viewModel
    }

    private fun setupRecyclerView() {
        binding.rcyclrvwDashboard.layoutManager = LinearLayoutManager(activity)
        binding.rcyclrvwDashboard.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rcyclrvwDashboard.setHasFixedSize(true)
        viewModel.dashboardAdapter = DashboardAdapter(viewModel.getDashboardList(), this)
        binding.rcyclrvwDashboard.adapter = viewModel.dashboardAdapter
    }

    override fun onItemClickListner(position: Int) {
        when (position) {
            0 -> view?.findNavController()
                ?.navigate(R.id.action_dashboardFragment_to_categoryFragment, null)
            1 -> activity?.showToast("Under development")
            2 -> activity?.showToast("Under development")
        }
    }

}