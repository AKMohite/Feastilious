package com.ak.feastit.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ak.feastit.R
import com.ak.feastit.databinding.ActivityMainBinding
import com.ak.feastit.utils.doOnApplyWindowInsets
import com.ak.feastit.utils.hide
import com.ak.feastit.utils.show
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigationrail.NavigationRailView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding as ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        edgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setupView()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController= navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.exploreFragment, R.id.collectionFragment, R.id.settingsFragment -> binding.mainBottomNavigation.show()
                else -> binding.mainBottomNavigation.hide()
            }
        }
        viewModel.reload()

        // TODO need to provide view checks?
        when(val navView = binding.mainBottomNavigation) {
            is BottomNavigationView -> navView.setupWithNavController(navController)
            is NavigationRailView -> navView.setupWithNavController(navController)
        }

    }

    private fun edgeToEdge() {
        // Default behavior, but we override the dark mode detection
        // because we enable a user to change a theme in the settings
        // and it has to be taken into the account here
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
//                detectDarkMode = { true },
            ),
            navigationBarStyle = SystemBarStyle.auto(
                // The default light scrim, as defined by androidx and the platform.
                // Taken from the EdgeToEdge.kt file.
                lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF),
                // The default dark scrim, as defined by androidx and the platform.
                // Taken from the EdgeToEdge.kt file.
                darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b),
//                detectDarkMode = { true },
            ),
        )
    }

    private fun setupView() {
        binding.root.doOnApplyWindowInsets { _, insets, _, margins ->
            val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            binding.root.updateLayoutParams<MarginLayoutParams> {
                bottomMargin = margins.bottom + inset
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


}