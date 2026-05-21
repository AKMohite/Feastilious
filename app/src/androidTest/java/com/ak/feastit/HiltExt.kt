// Copyright 2026, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit

import android.content.Intent
import android.os.Bundle
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.EmptyFragmentActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider

// inline fun <reified T : Fragment> launchFragmentInHiltContainer (){
//  val scenario = launchActivity<HiltTestActivity>()
//  scenario.onActivity { activity ->
//    val fragment = ShoppingFragment()
//    activity.supportFragmentManager.beginTransaction()
//      .replace(android.R.id.content, fragment)
//      .commitNow()
//  }
// }

inline fun <reified T : Fragment> launchFragmentInHiltContainer(
  fragmentArgs: Bundle? = null,
  themeResId: Int = androidx.appcompat.R.style.Theme_AppCompat_Empty,
  crossinline action: Fragment.() -> Unit = {},
) {
  val startActivityIntent = Intent(
    ApplicationProvider.getApplicationContext(),
    HiltTestActivity::class.java,
  ).putExtra(
    EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY,
    themeResId,
  )

  ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity { activity ->
    val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
      Preconditions.checkNotNull(T::class.java.classLoader),
      T::class.java.name,
    )
    fragment.arguments = fragmentArgs
    activity.supportFragmentManager
      .beginTransaction()
      .add(android.R.id.content, fragment, "")
      .commitNow()

    fragment.action()
  }
}

inline fun <reified T : Fragment> launchFragmentInHiltContainer(
  fragmentArgs: Bundle? = null,
  themeResId: Int = androidx.appcompat.R.style.Theme_AppCompat_Empty,
  factory: FragmentFactory,
  crossinline action: Fragment.() -> Unit = {},
) {
  val startActivityIntent = Intent(
    ApplicationProvider.getApplicationContext(),
    HiltTestActivity::class.java,
  ).putExtra(EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY, themeResId)

  ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity { activity ->
    activity.supportFragmentManager.fragmentFactory = factory
    val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
      Preconditions.checkNotNull(T::class.java.classLoader),
      T::class.java.name,
    )
    fragment.arguments = fragmentArgs

    activity.supportFragmentManager
      .beginTransaction()
      .add(android.R.id.content, fragment, "")
      .commitNow()

    fragment.action()
  }
}
