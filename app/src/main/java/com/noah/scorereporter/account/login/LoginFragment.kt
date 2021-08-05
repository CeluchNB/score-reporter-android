package com.noah.scorereporter.account.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.noah.scorereporter.account.UserProfileDataSource
import com.noah.scorereporter.account.UserProfileRepository
import com.noah.scorereporter.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater)


        return binding.root
    }
}