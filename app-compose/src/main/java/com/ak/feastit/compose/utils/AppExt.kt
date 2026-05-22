// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.utils

import android.content.Context
import android.content.SharedPreferences
import com.ak.feastit.compose.R

internal fun Context.getAppPreferences(): SharedPreferences {
  return this.getSharedPreferences(this.getString(R.string.settings_preferences_file_name), Context.MODE_PRIVATE)
}
