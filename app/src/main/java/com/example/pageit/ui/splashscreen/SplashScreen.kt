package com.example.pageit.ui.splashscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pageit.databinding.FragmentSplashScreenBinding
import com.facebook.AccessToken


@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding

    //Check if user logged with fb
    private fun isUserLogged(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    //If not logged in go to login Page.
    private fun navigateToLoginScreen() {
        findNavController().navigate(SplashScreenDirections.actionSplashScreenToLoginScreen())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isUserLogged()) {
            navigateToHomeScreen()
        } else {
            navigateToLoginScreen()
        }
    }

    private fun navigateToHomeScreen() {
        findNavController().navigate(SplashScreenDirections.actionSplashScreenToHomeScreen())
    }
}