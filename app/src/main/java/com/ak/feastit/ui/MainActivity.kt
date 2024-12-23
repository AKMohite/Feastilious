package com.ak.feastit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ak.feastit.R
import com.ak.feastit.databinding.ActivityMainBinding
import com.ak.feastit.utils.hide
import com.ak.feastit.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding as ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController= navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment, R.id.onBoardingFragment -> binding.mainBottomNavigation.hide()
                else -> binding.mainBottomNavigation.show()
            }
        }

        binding.mainBottomNavigation.setupWithNavController(navController)

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


}