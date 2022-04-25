package com.kmm.intermediatefirstsubmission.ui.pages.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kmm.intermediatefirstsubmission.R
import com.kmm.intermediatefirstsubmission.data.auth.viewmodel.AuthViewModel
import com.kmm.intermediatefirstsubmission.data.auth.viewmodel.SessionViewModel
import com.kmm.intermediatefirstsubmission.data.core.StateHandler
import com.kmm.intermediatefirstsubmission.databinding.FragmentLoginPageBinding
import com.kmm.intermediatefirstsubmission.utility.CommonFunction
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginPage : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentLoginPageBinding
    private val authViewModel by viewModel<AuthViewModel>()
    private val sessionViewModel by viewModel<SessionViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginPageBinding.inflate(layoutInflater)
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginPage_to_registerPage)
        }
        binding.btnSubmit.setOnClickListener(this)
        authViewModel.loginState.observe(viewLifecycleOwner) {
            when (it) {
                is StateHandler.Loading -> {
                    showLoading()
                }
                is StateHandler.Error -> {
                    hideLoading()
                    CommonFunction.showSnackBar(
                        binding.root,
                        requireContext(),
                        getString(R.string.login_failed) + it.message + "!",
                        true
                    )
                }
                is StateHandler.Success -> {
                    hideLoading()
                    CommonFunction.showSnackBar(
                        binding.root,
                        requireContext(),
                        getString(R.string.login_sucess),
                    )
                    it.data?.loginResult?.token?.let { token ->
                        sessionViewModel.saveToken(token)
                    }
                }
                else -> {}
            }
        }


        return binding.root
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSubmit.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.btnSubmit.isEnabled = true
    }

    override fun onClick(p0: View?) {
        if (binding.etEmail.error != null && binding.etPassword.error != null) return
        when {
            binding.etEmail.text?.isEmpty() == true -> {
                binding.etEmail.error = getString(R.string.email_not_valid)
                return
            }
            binding.etPassword.text?.isEmpty() == true || (binding.etPassword.text?.length
                ?: 0) < 6 -> {
                binding.etPassword.error = getString(R.string.password_error_warning)
                return
            }
        }
        authViewModel.login(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
    }

}