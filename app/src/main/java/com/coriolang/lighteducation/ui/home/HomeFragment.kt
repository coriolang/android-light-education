package com.coriolang.lighteducation.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.coriolang.lighteducation.R
import com.coriolang.lighteducation.databinding.FragmentHomeBinding
import com.coriolang.lighteducation.ui.auth.AuthViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val authViewModel:
            AuthViewModel by viewModels { AuthViewModel.Factory }
    private val homeViewModel:
            HomeViewModel by viewModels { HomeViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupActionMenu()

        binding = FragmentHomeBinding
            .inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    private fun setupActionMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sign_out -> {
                        authViewModel.signOut()
                        navigateToAuth()

                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun navigateToAuth() {
        findNavController().navigate(
            R.id.action_global_authFragment
        )
    }

    private fun setupViews() {
        setupDirectionsCard()
        setupTopicsRecyclerView()
    }

    private fun setupDirectionsCard() {
        binding.cardDirections.setOnClickListener {
            navigateToSearch()
        }
    }

    private fun navigateToSearch() {
        val action = HomeFragmentDirections
            .actionHomeFragmentToSearchFragment()
        findNavController().navigate(action)
    }

    private fun setupTopicsRecyclerView() {
        val adapter = TopicAdapter { topicId ->
            val action = HomeFragmentDirections
                .actionHomeFragmentToTopicFragment(topicId)
            findNavController().navigate(action)
        }

        binding.recyclerViewTopics
            .layoutManager = LinearLayoutManager(context)
        binding.recyclerViewTopics.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            observeTopics(adapter)
        }
    }

    private suspend fun observeTopics(adapter: TopicAdapter) {
        homeViewModel.startListenTopics(authViewModel.userId)
        homeViewModel.topics.collect { topics ->
            adapter.submitList(topics)
        }
    }
}