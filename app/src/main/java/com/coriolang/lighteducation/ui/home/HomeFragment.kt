package com.coriolang.lighteducation.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.coriolang.lighteducation.R
import com.coriolang.lighteducation.databinding.FragmentHomeBinding
import com.coriolang.lighteducation.ui.auth.AuthViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val authViewModel:
            AuthViewModel by viewModels { AuthViewModel.Factory }

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

    private fun setupViews() {  }
}