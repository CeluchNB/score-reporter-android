package com.noah.scorereporter.account.signup

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
import com.noah.scorereporter.account.profile.ProfileFragmentDirections
import com.noah.scorereporter.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModels()
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCreate.setOnClickListener {
            viewModel.signup(
                binding.inputFirstName.text.toString(),
                binding.inputLastName.text.toString(),
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            )
        }

        viewModel.user.observe(viewLifecycleOwner) { event ->
            val user = event?.getContentIfNotHandled()
            user?.let {
                binding.root.clearFocus()
                val imm: InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(getView()?.rootView?.windowToken, 0)

                val action = SignUpFragmentDirections.actionSignUpFragmentToProfileFragment(it)
                findNavController().navigate(action)
            }
        }
    }
}