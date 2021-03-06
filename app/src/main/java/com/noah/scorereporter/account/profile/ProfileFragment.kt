package com.noah.scorereporter.account.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.noah.scorereporter.R
import com.noah.scorereporter.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.logoutSuccess.observe(viewLifecycleOwner) { success ->
            if (success.getContentIfNotHandled() == true) {
                this.findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
        }

        val bundle = arguments
        if (bundle != null) {
            val args = ProfileFragmentArgs.fromBundle(bundle)
            val userProfile = args.userProfile
            userProfile?.let {
                viewModel.setUserProfile(it)
            }
        }

        if (!viewModel.hasSavedToken()) {
            this.findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        } else {
            if (viewModel.userProfile.value == null) {
                viewModel.fetchUserProfile()
            }
        }
    }
}