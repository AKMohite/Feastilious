// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.widgets.mealplan.week

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.ak.feastit.R
import timber.log.Timber

internal class WeekMealPlanWidgetProvider : AppWidgetProvider() {

  override fun onUpdate(
    context: Context,
    appWidgetManager: AppWidgetManager?,
    appWidgetIds: IntArray?,
  ) {
    super.onUpdate(context, appWidgetManager, appWidgetIds)
    Timber.d("WeekMealPlanWidgetProvider.onUpdate()")
    appWidgetIds?.forEach { appWidgetId ->
      updateWidget(context, appWidgetManager, appWidgetId)
    }
  }

  private fun updateWidget(
    context: Context,
    appWidgetManager: AppWidgetManager?,
    appWidgetId: Int,
  ) {
    val intent = Intent(context, WeekMealPlanWidgetService::class.java).apply {
      putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
      data = toUri(Intent.URI_INTENT_SCHEME).toUri()
    }
    val remoteView = RemoteViews(context.packageName, R.layout.widget_week_meal_plan_container).apply {
      setRemoteAdapter(R.id.widget_week_plan_recipes, intent)
      setEmptyView(R.id.widget_week_plan_recipes, R.id.widget_week_plan_recipes_empty_view)
      setInt(R.id.week_plan_container, "setBackgroundResource", R.drawable.bg_widget)
    }
    appWidgetManager?.updateAppWidget(appWidgetId, remoteView)
    appWidgetManager?.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_week_plan_recipes)
  }
}
