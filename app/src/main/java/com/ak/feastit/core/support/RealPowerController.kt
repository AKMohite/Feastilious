// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.core.support

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.PowerManager
import androidx.core.content.getSystemService
import com.ak.feastit.R
import com.ak.feastit.utils.getAppPreferences
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.withContext

internal class RealPowerController @Inject constructor(
  @ApplicationContext private val context: Context,
  private val dispatcherProvider: DispatcherProvider,
) : PowerController {

  private val powerManager: PowerManager? by lazy { context.getSystemService() }
  private val connectivityManager: ConnectivityManager? by lazy { context.getSystemService() }
  private val preference: SharedPreferences by lazy { context.getAppPreferences() }

  override suspend fun needToSaveData(): Boolean {
    return (powerManager?.isPowerSaveMode == true) || isBackgroundDataRestricted() || hasUserEnabledDataSaver()
  }

  private suspend fun hasUserEnabledDataSaver(): Boolean = withContext(dispatcherProvider.io) {
    preference.getBoolean(context.getString(R.string.preference_key_data_saver), false)
  }

  private fun isBackgroundDataRestricted(): Boolean {
    return connectivityManager?.restrictBackgroundStatus == ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED
  }
}

interface PowerController {
  suspend fun needToSaveData(): Boolean
}
