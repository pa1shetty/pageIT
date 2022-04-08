package com.example.pageit.ui.loginscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pageit.R
import com.example.pageit.databinding.FragmentLoginScreenBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginScreen : Fragment() {
    private lateinit var binding: FragmentLoginScreenBinding
    private val callbackManager = CallbackManager.Factory.create()
    private lateinit var loginScreenViewModel: LoginScreenViewModel

    @Inject
    lateinit var factory: LoginScreenViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginScreenViewModel = ViewModelProvider(this, factory)[LoginScreenViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleFBLogin()
        hideLoading()
    }

    private fun handleFBLogin() {
        binding.loginButton.setPermissions(
            listOf(
                "pages_manage_metadata",
                "pages_read_engagement",
                "pages_show_list", "business_management"
            )
        )
        binding.loginButton.setOnClickListener {
            showLoading()
        }
        binding.loginButton.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onCancel() {
                hideLoading()
                showToast(getString(R.string.error))
            }

            override fun onError(error: FacebookException) {
                hideLoading()
                showToast(getString(R.string.error))
            }

            override fun onSuccess(result: LoginResult) {
                showToast(getString(R.string.log_in_success))
                lifecycleScope.launch {
                    saveUserDetails(result.accessToken.token, result.accessToken.userId)
                    navigateToHomeScreen()
                }

            }
        })
    }

    private suspend fun saveUserDetails(token: String, userId: String) {
        loginScreenViewModel.saveFbUserAccessToken(token)
        loginScreenViewModel.saveFbUserId(userId)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToHomeScreen() {
        findNavController().navigate(LoginScreenDirections.actionLoginScreenToHomeScreen())
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.vLoading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.vLoading.visibility = View.GONE
    }
}