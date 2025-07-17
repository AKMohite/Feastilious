// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ak.feastit.R
import com.ak.feastit.databinding.ActivityMainBinding
import com.ak.feastit.ui.settings.appearance.allAppThemes
import com.ak.feastit.utils.doOnApplyWindowInsets
import com.ak.feastit.utils.hide
import com.ak.feastit.utils.show
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.DynamicColors
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigationrail.NavigationRailView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

private const val EXTRA_SHORTCUT_FAVORITE = "extraShortcutFavorite"
private const val EXTRA_SHORTCUT_MEAL_PLAN = "extraShortcutMealPlan"
private const val EXTRA_SHORTCUT_SHOPPING = "extraShortcutShopping"
private const val EXTRA_SHORTCUT_SEARCH = "extraShortcutSearch"

@AndroidEntryPoint
internal class MainActivity : AppCompatActivity() {

  private var baseBinding: ActivityMainBinding? = null
  private val binding: ActivityMainBinding
    get() = baseBinding as ActivityMainBinding

  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
//    setPreferenceUIConfiguration()
    edgeToEdge()
    super.onCreate(savedInstanceState)
    baseBinding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setupView()

    val navController = getNavigationController()
    navController.addOnDestinationChangedListener { _, destination, _ ->
      when (destination.id) {
        R.id.exploreFragment, R.id.collectionFragment, R.id.settingsFragment -> binding.mainBottomNavigation.show()
        else -> binding.mainBottomNavigation.hide()
      }
    }
    viewModel.reload()

    // TODO need to provide view checks?
    when (val navView = binding.mainBottomNavigation) {
      is BottomNavigationView -> navView.setupWithNavController(navController)
      is NavigationRailView -> navView.setupWithNavController(navController)
      is NavigationView -> navView.setupWithNavController(navController)
    }

        /*onBackPressedDispatcher.addCallback(
            owner = this,
            onBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
//                if (getCurrentFragment().onBackPressed() == false) {
                    if (!navController.popBackStack()) {
                        finish()
                    }
//                }
                }
            }
        )*/
    handleAppShortcut(intent)
    loadDynamicShortcut()
  }

  private fun setPreferenceUIConfiguration() {
    lifecycleScope.launch(Dispatchers.IO) {
      val sharedPrefs = getSharedPreferences(getString(R.string.preference_file_name), MODE_PRIVATE)
//      val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
      val isDynamic = sharedPrefs.getBoolean(getString(R.string.preference_key_dynamic_theme), false)
      if (isDynamic && DynamicColors.isDynamicColorAvailable()) {
        Timber.d("Dynamic theme set")
        withContext(Dispatchers.Main) { DynamicColors.applyToActivityIfAvailable(this@MainActivity) }
      } else {
        val appThemes = allAppThemes
        val themeName = sharedPrefs.getString(getString(R.string.preference_key_theme), null)
        val theme = appThemes.firstOrNull { it.dataClassName == themeName } ?: appThemes.first()
        Timber.d("Set app theme: ${theme.dataClassName}")
        withContext(Dispatchers.Main) { setTheme(theme.style) }
      }
    }
  }

  private fun getNavigationController(): NavController {
    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    val navController = navHostFragment.navController
    return navController
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    Timber.d("Intent received: ${intent != null}")
    handleAppShortcut(intent)
    handleNotification(intent)
  }

  private fun handleNotification(intent: Intent?) {
    if (intent == null) return
  }

  private fun handleAppShortcut(intent: Intent?) {
    Timber.d("Intent received: ${intent != null}")
    if (intent == null || intent.extras == null) return
    Timber.d("On app's shortcut click: ${intent.extras} with action ${intent.action}")
//        TODO app navigation is crashing as destinations are from different screens
    when {
      intent.extras?.containsKey(EXTRA_SHORTCUT_FAVORITE) == true -> {
        selectBottomNavigationItem(R.id.collectionFragment)
        getNavigationController().navigate(R.id.collection_to_favorites)
      }

      intent.extras?.containsKey(EXTRA_SHORTCUT_MEAL_PLAN) == true -> {
        selectBottomNavigationItem(R.id.collectionFragment)
        getNavigationController().navigate(R.id.collection_to_meal_planner)
      }

      intent.extras?.containsKey(EXTRA_SHORTCUT_SHOPPING) == true -> {
        selectBottomNavigationItem(R.id.collectionFragment)
        getNavigationController().navigate(R.id.collection_to_shopping)
      }

      intent.extras?.containsKey(EXTRA_SHORTCUT_SEARCH) == true -> {
        selectBottomNavigationItem(R.id.exploreFragment)
        getNavigationController().navigate(R.id.explore_to_search)
      }
    }
  }

  private fun selectBottomNavigationItem(@IdRes menuItem: Int) {
    when (val navView = binding.mainBottomNavigation) {
      is BottomNavigationView -> navView.selectedItemId = menuItem
      is NavigationRailView -> navView.selectedItemId = menuItem
      is NavigationView -> navView.setCheckedItem(menuItem)
    }
  }

  private fun loadDynamicShortcut() {
//        TODO load dynamic shortcut if today's meal is available
        /*val intent= Intent().apply {
            putExtra("mealId", "meal-recipe-id")
        }
        val shortcut = ShortcutInfoCompat.Builder(this, "today_plan_id")
            .setShortLabel("Recipe name")
            .setLongLabel("Recipe desc")
            .setIcon(IconCompat.createWithResource(this, R.drawable.ic_repeat))
            .setIntent(intent)
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(this, shortcut)*/
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
    binding.mainBottomNavigation.doOnApplyWindowInsets { insetView, insets, _, margins ->
      val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
      insetView.updateLayoutParams<MarginLayoutParams> {
        bottomMargin = margins.bottom + inset
      }
    }
  }

  private fun showSnackBar(
    @StringRes message: Int,
    @StringRes actionText: Int? = null,
    action: (() -> Unit)? = null,
  ) {
    val snackBar = Snackbar.make(
      binding.root,
      message,
      Snackbar.LENGTH_SHORT,
    )
    if (actionText != null && action != null) {
      val actionListener: (View) -> Unit = { action() }
      snackBar.setAction(getString(actionText), actionListener)
    }
    snackBar.show()
  }

  override fun onDestroy() {
    baseBinding = null
    super.onDestroy()
  }
}
