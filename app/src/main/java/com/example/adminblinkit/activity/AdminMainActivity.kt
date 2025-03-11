package com.example.adminblinkit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.adminblinkit.R
import com.example.adminblinkit.databinding.ActivityAdminMainBinding
import com.example.adminblinkit.fragments.AddProductFragment
import com.example.adminblinkit.fragments.HomeFragment
import com.example.adminblinkit.fragments.OrderFragment

class AdminMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavigationUI.setupWithNavController(binding.bottomNavigationView,
            Navigation.findNavController(this, R.id.frameLayoutContainer)
        )

    }
}