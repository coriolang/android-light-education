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
import com.coriolang.lighteducation.databinding.FragmentSignupBinding
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {

    private val authViewModel:
            AuthViewModel by viewModels { AuthViewModel.Factory }

    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding
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
    }

    private suspend fun observeSignedInState() {
        authViewModel.signedIn.collect { signedIn ->
            if (signedIn) {
                navigateToAuth()
            }
        }
    }

    private fun navigateToAuth() {
        val action = SignupFragmentDirections
            .actionSignupFragmentToAuthFragment()
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
            val displayName = binding.textFieldUsername.editText?.text.toString()
            val email = binding.textFieldEmail.editText?.text.toString()
            val password = binding.textFieldPassword.editText?.text.toString()

            if (password.length < 8) {
                showToast(getString(R.string.eight_symbols))
            } else {
                authViewModel.createUser(displayName, email, password)
            }
        }
    }
}