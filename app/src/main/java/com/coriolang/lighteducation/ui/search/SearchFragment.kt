package com.coriolang.lighteducation.ui.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.coriolang.lighteducation.R
import com.coriolang.lighteducation.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val searchViewModel:
            SearchViewModel by viewModels { SearchViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupActionMenu()

        binding = FragmentSearchBinding
            .inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    private fun setupActionMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> {
                        setupSearchView(menuItem)
                        true
                    }
                    R.id.action_filter -> {
                        val filtersBottomSheet =
                            FiltersBottomSheet(searchViewModel)

                        filtersBottomSheet.show(
                            parentFragmentManager,
                            FiltersBottomSheet.TAG
                        )

                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupSearchView(menuItem: MenuItem) {
        val searchView = (menuItem.actionView as SearchView)

        searchView.setOnQueryTextListener(object : OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewModel.name = if (
                    query?.isEmpty() == true
                    || query?.isBlank() == true
                ) {

                    null
                } else {
                    query
                }

                searchViewModel.getDirections()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchViewModel.name = if (
                    newText?.isEmpty() == true
                    || newText?.isBlank() == true
                ) {

                    null
                } else {
                    newText
                }

                searchViewModel.getDirections()

                return true
            }
        })
    }

    private fun setupViews() {
        setupDirectionsRecyclerView()
    }

    private fun setupDirectionsRecyclerView() {
        val adapter = DirectionAdapter { directionId ->
            val action = SearchFragmentDirections
                .actionSearchFragmentToDirectionFragment(directionId)
            findNavController().navigate(action)
        }

        binding.recyclerViewSearchResults
            .layoutManager = LinearLayoutManager(context)
        binding.recyclerViewSearchResults.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            observeDirections(adapter)
        }
    }

    private suspend fun observeDirections(adapter: DirectionAdapter) {
        searchViewModel.getDirections()
        searchViewModel.directions.collect { directions ->
            adapter.submitList(directions)
        }
    }
}