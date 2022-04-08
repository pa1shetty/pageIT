package com.example.pageit.ui.pageDetailsScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.pageit.R
import com.example.pageit.data.database.module.FeedModule
import com.example.pageit.data.module.ErrorCode
import com.example.pageit.data.module.StatusEnum
import com.example.pageit.databinding.FragmentPageDetailsScreenBinding
import com.example.pageit.ui.pageDetailsScreen.adapter.FeedListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PageDetailsScreen : Fragment() {
    private val safeArgs: PageDetailsScreenArgs by navArgs()


    private lateinit var pageDetailsScreenViewModel: PageDetailsScreenViewModel

    private lateinit var binding: FragmentPageDetailsScreenBinding

    private val feedAdapter = FeedListAdapter()

    @Inject
    lateinit var factory: PageDetailsScreenViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageDetailsScreenViewModel =
            ViewModelProvider(this, factory)[PageDetailsScreenViewModel::class.java]
        fetchFeeds()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageDetailsScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showPageDetails()
        showFeeds()
        setUpListenerCollector()
    }

    //Set up all Listeners , observables collectors here
    private fun setUpListenerCollector() {
        //Swipe to refresh
        binding.srFeeds.setOnRefreshListener {
            fetchFeeds()
        }
        feedStatusCollector()
        feedCollector()
    }

    private fun feedStatusCollector() {
        lifecycleScope.launch {
            pageDetailsScreenViewModel.feedFetchState.collect { requestResponse ->
                when (requestResponse.status) {
                    StatusEnum.NOT_REQUESTED -> {
                        binding.srFeeds.isRefreshing = false
                    }
                    StatusEnum.LOADING -> {
                        binding.srFeeds.isRefreshing = true
                    }
                    StatusEnum.SUCCESS -> {
                        binding.srFeeds.isRefreshing = false
                    }
                    else -> {
                        binding.srFeeds.isRefreshing = false
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

    //Collect feed changes
    private fun feedCollector() {
        lifecycleScope.launch {
            pageDetailsScreenViewModel.getFeeds(safeArgs.pageId).collect { feeds ->
                feedAdapter.submitList(feeds)
                withContext(Dispatchers.Main) {
                    handleFeedUI(feeds)
                }
            }
        }
    }


    //To set up feed ui
    private fun showFeeds() {
        binding.rvFeed.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFeed.adapter = feedAdapter
    }

    //to create feed ui
    private fun handleFeedUI(feeds: List<FeedModule>) {
        if (feeds.isEmpty()) {
            binding.rvFeed.visibility = View.GONE
            binding.clNoFeed.visibility = View.VISIBLE
        } else {
            binding.clNoFeed.visibility = View.GONE
            if (binding.rvFeed.visibility == View.GONE) {
                binding.rvFeed.visibility = View.VISIBLE
            }
        }
    }

    //Fetch feeds from Server
    private fun fetchFeeds() {
        lifecycleScope.launch(Dispatchers.IO) {
            pageDetailsScreenViewModel.fetchFeeds(safeArgs.pageId)
        }
    }

    //Common method to to show toast
    private fun showToast(toastMessage: String) {
        Toast.makeText(
            requireContext(),
            toastMessage,
            Toast.LENGTH_SHORT
        ).show()
    }

    //Show page details
    private fun showPageDetails() {
        lifecycleScope.launch {
            pageDetailsScreenViewModel.getPageDetail(safeArgs.pageId).collect { page ->
                binding.tvPageName.text = page.name
                binding.tvPageCategory.text = page.category
                page.username?.let { username ->
                    val atUsername = "@$username"
                    binding.tvPageCategory.text = atUsername
                }
                Glide.with(requireContext())
                    .load(page.picture)
                    .circleCrop()
                    .into(binding.ivPagePicture)
                binding.tvFollowers.text =
                    getString(R.string.followers, page.followers_count.toString())
                page.cover?.let { cover ->
                    Glide.with(requireContext())
                        .load(cover)
                        .centerCrop()
                        .into(binding.ivCoverPic)
                }
            }
        }

    }
}