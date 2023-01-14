package com.coriolang.lighteducation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.coriolang.lighteducation.R
import com.coriolang.lighteducation.databinding.FragmentAuthBinding
import kotlinx.coroutines.launch

class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding

    private val authViewModel:
            AuthViewModel by viewModels { AuthViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding
            .inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch { observeSignedInState() }
            launch { observeException() }
        }

        setupPasswordField()
        setupCreateButton()
        setupSignInButton()
    }

    private suspend fun observeSignedInState() {
        authViewModel.signedIn.collect { signedIn ->
            if (signedIn) {
                navigateToHome()
            }
        }
    }

    private fun navigateToHome() {
        val action = AuthFragmentDirections
            .actionAuthFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private suspend fun observeException() {
        authViewModel.exception.collect { message ->
            if (message.isNotEmpty()) {
                showToast(message)
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setupPasswordField() {
        binding.textFieldPassword.editText?.doOnTextChanged { text, start, before, count ->
            if (text != null && text.length < 8) {
                binding.textFieldPassword.error =
                    getString(R.string.eight_symbols)
            } else {
                binding.textFieldPassword.error = null
            }
        }
    }

    private fun setupCreateButton() {
        binding.buttonCreateAccount.setOnClickListener {
            navigateToSignUp()
        }
    }

    private fun navigateToSignUp() {
        val action = AuthFragmentDirections
            .actionAuthFragmentToSignupFragment()
        findNavController().navigate(action)
    }

    private fun setupSignInButton() {
        binding.buttonSignIn.setOnClickListener {
            val email = binding.textFieldEmail.editText?.text.toString()
            val password = binding.textFieldPassword.editText?.text.toString()

            if (password.length < 8) {
                showToast(getString(R.string.eight_symbols))
            } else {
                authViewModel.signIn(email, password)
            }
        }
    }
}