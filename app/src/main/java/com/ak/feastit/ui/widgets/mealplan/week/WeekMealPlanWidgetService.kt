// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.widgets.mealplan.week

import android.content.Intent
import android.widget.RemoteViewsService
import com.mak.feastit.domain.repository.WidgetRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class WeekMealPlanWidgetService : RemoteViewsService() {

  @Inject lateinit var widgetRepository: WidgetRepository

  override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory? {
    return WeekMealPlanRemoteViewsFactory(applicationContext, widgetRepository)
  }
}
