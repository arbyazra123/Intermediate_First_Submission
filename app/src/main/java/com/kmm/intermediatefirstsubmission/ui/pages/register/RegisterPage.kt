package com.kmm.intermediatefirstsubmission.ui.pages.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kmm.intermediatefirstsubmission.R
import com.kmm.intermediatefirstsubmission.data.auth.viewmodel.AuthViewModel
import com.kmm.intermediatefirstsubmission.data.core.StateHandler
import com.kmm.intermediatefirstsubmission.databinding.FragmentRegisterPageBinding
import com.kmm.intermediatefirstsubmission.utility.CommonFunction
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RegisterPage : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentRegisterPageBinding
    private val authViewModel by sharedViewModel<AuthViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterPageBinding.inflate(layoutInflater)
        binding.btnSubmit.setOnClickListener(this)
        binding.tvAlreadyHaveAccount.setOnClickListener {
            findNavController().popBackStack()
        }
        authViewModel.registerState.observe(viewLifecycleOwner) {
            when (it) {
                is StateHandler.Loading -> {
                    showLoading()
                }
                is StateHandler.Error -> {
                    hideLoading()
                    CommonFunction.showSnackBar(
                        binding.root,
                        requireContext(),
                        getString(R.string.register_failed) + it.message + "!",
                        true
                    )
                }
                is StateHandler.Success -> {
                    hideLoading()
                    CommonFunction.showSnackBar(
                        binding.root,
                        requireContext(),
                        getString(R.string.register_success),
                    )
                    it.data?.error?.let { result ->
                        if (result) {
                            findNavController().navigate(R.id.action_registerPage_to_loginPage)
                        }
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
        if (binding.etEmail.error != null && binding.etPassword.error != null && binding.etName.error != null) return
        println("continue to register")
        when {
            binding.etName.text?.isEmpty() == true -> {
                binding.etName.error = getString(R.string.name_must_not_null)
                return
            }
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
        println("submit to register")
        authViewModel.register(
            name = binding.etName.text.toString(),
            email = binding.etEmail.text.toString(),
            password = binding.etPassword.text.toString()
        )
    }
}