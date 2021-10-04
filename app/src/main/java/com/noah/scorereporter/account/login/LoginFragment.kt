package com.noah.scorereporter.account.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.noah.scorereporter.R
import com.noah.scorereporter.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonSignUp.setOnClickListener {
            navigateToSignUpFragment()
        }

        binding.buttonLogin.setOnClickListener {
            loginViewModel.login(
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            )
            binding.inputPassword.setText("")
        }

        loginViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                binding.root.clearFocus()
                val imm: InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(getView()?.rootView?.windowToken, 0)

                val action = LoginFragmentDirections.actionLoginFragmentToProfileFragment(it)
                this.findNavController().navigate(action)
            }
        }
    }

    private fun navigateToSignUpFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
        findNavController().navigate(action)
    }
}