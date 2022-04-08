package com.example.pageit.ui.settingsScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pageit.databinding.FragmentSettingsScreenBinding
import com.facebook.login.LoginManager


class SettingsScreen : Fragment() {
    private lateinit var binding: FragmentSettingsScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLogoutClick()
    }


    private fun setUpLogoutClick() {

        binding.loginButton.setOnClickListener {
            LoginManager.getInstance().logOut()
            navigateToHomeScreen()
        }
    }


    private fun navigateToHomeScreen() {
        findNavController().navigate(SettingsScreenDirections.actionSettingsScreenToLoginScreen())
    }

}