package com.kmm.intermediatefirstsubmission.ui.pages.landing.story

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kmm.intermediatefirstsubmission.R
import com.kmm.intermediatefirstsubmission.data.core.StateHandler
import com.kmm.intermediatefirstsubmission.data.story.model.ListStoryResponseItem
import com.kmm.intermediatefirstsubmission.data.story.view_model.StoryViewModel
import com.kmm.intermediatefirstsubmission.databinding.FragmentStoryPageBinding
import com.kmm.intermediatefirstsubmission.databinding.StoryItemBinding
import com.kmm.intermediatefirstsubmission.ui.adapter.IOnStoryItemClick
import com.kmm.intermediatefirstsubmission.ui.adapter.LoadingStateAdapter
import com.kmm.intermediatefirstsubmission.ui.adapter.StoryListViewAdapter
import com.kmm.intermediatefirstsubmission.ui.adapter.StoryListViewPagingAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class StoryPage : Fragment(), IOnStoryItemClick {
    private lateinit var binding: FragmentStoryPageBinding
    private val storyViewModel by sharedViewModel<StoryViewModel>()
    private lateinit var storyListViewAdapter: StoryListViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentStoryPageBinding.inflate(layoutInflater)
        storyViewModel.getStories("1")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.swipe.setOnRefreshListener {
            storyViewModel.getStories("1")
        }
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_storyPage_to_addStoryPage)
        }
        val adapter = StoryListViewPagingAdapter(this)
        getDataWithPaging(adapter)
//        getData()
        return binding.root
    }

    private fun getData() {
        storyViewModel.storyViewState.observe(viewLifecycleOwner) {

            when (it) {
                is StateHandler.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is StateHandler.Success -> {
                    binding.swipe.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    it.data?.listStory?.let { stories ->
                        if (storyViewModel.checkIfStoryHasUpdate()) {
                            binding.rv.smoothScrollToPosition(0)
                        }
                        val adapter = StoryListViewAdapter(this)
                        adapter.setData(ArrayList(stories))
                        storyListViewAdapter = adapter
                        binding.rv.adapter = storyListViewAdapter

                    }
                }
                is StateHandler.Error -> {
                    binding.swipe.isRefreshing = false
                    binding.progressBar.visibility = View.GONE

                }
                else -> {

                }
            }
        }
    }

    private fun getDataWithPaging(adapter: StoryListViewPagingAdapter) {
        binding.rv.adapter =
                adapter.withLoadStateFooter(LoadingStateAdapter(retry = { adapter.retry() }))
        storyViewModel.getStoriesWithPaging().observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onStop() {
        super.onStop()
        storyViewModel.getStories("1")
    }


    override fun onClick(binding: StoryItemBinding, story: ListStoryResponseItem) {
        val bundle = bundleOf(DetailStoryPage.DATA to story)
        val extras = FragmentNavigatorExtras(
            binding.storyImageView to "${story.id}image",
            binding.descTextView to "${story.id}desc",
            binding.createdDate to "${story.id}date",
            binding.nameTextView to "${story.id}name",
        )
        findNavController().navigate(R.id.detailStoryPage, bundle, null, extras)
    }
}