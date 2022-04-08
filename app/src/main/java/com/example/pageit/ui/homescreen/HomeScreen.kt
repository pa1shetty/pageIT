package com.example.pageit.ui.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pageit.R
import com.example.pageit.data.module.ErrorCode
import com.example.pageit.data.module.StatusEnum
import com.example.pageit.databinding.FragmentHomeScreenBinding
import com.example.pageit.helper.workManager.SavePageDetails
import com.example.pageit.ui.homescreen.adapter.PageListAdapter
import com.example.pageit.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreen : Fragment() {
    private lateinit var homeScreenViewModel: HomeScreenViewModel

    @Inject
    lateinit var factory: HomeScreenViewModelFactory
    private lateinit var pageAdapter: PageListAdapter
    private lateinit var binding: FragmentHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeScreenViewModel = ViewModelProvider(this, factory)[HomeScreenViewModel::class.java]
        fetchUserDetails()
        fetchPages()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showPages()
        showUserDetails()
        setUpListenerCollector()
        setupSettingsClick()

    }

    //To get user name
    private fun fetchUserDetails() {
        lifecycleScope.launch(Dispatchers.IO) {
            homeScreenViewModel.getUserDetails()
        }
    }

    private fun showUserDetails() {
        lifecycleScope.launch {
            homeScreenViewModel.getUserName().collectLatest { name ->
                if (name != AppConstants.dataStoreDefaultValue) {
                    val welcomeMessage = getString(R.string.home_screen_welcome) + name
                    binding.tvUserName.text = welcomeMessage
                }
            }
        }
    }


    //to navigate to settings screen
    private fun setupSettingsClick() {
        binding.ivSettings.setOnClickListener {
            navigateTo(HomeScreenDirections.actionHomeScreenToSettingsScreen())
        }
    }


    //Set up all Listeners , observables collectors here
    private fun setUpListenerCollector() {
        //Handle pull down to refresh
        binding.srPages.setOnRefreshListener {
            fetchPages()
        }
        collectPageState()
        collectPageList()

    }

    //To handle page fetch state
    private fun collectPageState() {
        lifecycleScope.launch {
            homeScreenViewModel.pageFetchState.collect { requestResponse ->
                when (requestResponse.status) {
                    StatusEnum.NOT_REQUESTED -> {
                        binding.srPages.isRefreshing = false
                    }
                    StatusEnum.LOADING -> {
                        binding.srPages.isRefreshing = true
                    }
                    StatusEnum.SUCCESS -> {
                        binding.srPages.isRefreshing = false

                    }
                    else -> {
                        binding.srPages.isRefreshing = false
                        when (requestResponse.errorCode) {
                            ErrorCode.NO_INTERNET ->
                                showToast(getString(R.string.no_internet))
                            else -> {
                                showToast(getString(R.string.something_went_wrong))
                            }
                        }
                    }

                }
            }
        }
    }

    //Page list change will be collected here
    private fun collectPageList() {
        lifecycleScope.launch {
            homeScreenViewModel.getPages().collect { pages ->
                pageAdapter.submitList(pages)
                withContext(Dispatchers.Main) {
                    if (pages.isEmpty()) {
                        binding.rvPages.visibility = View.GONE
                        binding.clNoPages.visibility = View.VISIBLE
                    } else {
                        SavePageDetails.savePageDetailsWorker(requireContext())
                        binding.clNoPages.visibility = View.GONE
                        if (binding.rvPages.visibility == View.GONE) {
                            binding.rvPages.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    //Fetch pages from server
    private fun fetchPages() {
        lifecycleScope.launch(Dispatchers.IO) {
            homeScreenViewModel.fetchPages()
        }
    }

    //Get pages from db and display
    private fun showPages() {
        binding.rvPages.layoutManager = LinearLayoutManager(requireContext())
        pageAdapter =
            PageListAdapter(requireContext()) { pageModule ->
                navigateTo(
                    HomeScreenDirections.actionHomeScreenToPageDetailsScreen(
                        pageModule.page_id
                    )
                )
            }
        binding.rvPages.adapter = pageAdapter
    }

    private fun navigateTo(navDirections: NavDirections) {
        findNavController().navigate(
            navDirections
        )
    }

    private fun showToast(toastMessage: String) {
        Toast.makeText(
            requireContext(),
            toastMessage,
            Toast.LENGTH_SHORT
        ).show()
    }

}