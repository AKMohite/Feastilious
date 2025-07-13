// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.widgets.favoriterecipes

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.ak.feastit.R
import timber.log.Timber

internal class FavoriteRecipesWidgetProvider : AppWidgetProvider() {

  override fun onUpdate(
    context: Context,
    appWidgetManager: AppWidgetManager?,
    appWidgetIds: IntArray?,
  ) {
    super.onUpdate(context, appWidgetManager, appWidgetIds)
    Timber.d("Update widget")
    appWidgetIds?.forEach { id ->
      updateWidget(context, appWidgetManager, id)
    }
  }

  private fun updateWidget(
    context: Context,
    appWidgetManager: AppWidgetManager?,
    widgetId: Int,
  ) {
    val intent = Intent(context, FavoriteRecipesWidgetService::class.java).apply {
      putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
      data = toUri(Intent.URI_INTENT_SCHEME).toUri()
    }
    val remoteView = RemoteViews(context.packageName, R.layout.widget_favorite_recipes_container).apply {
      setRemoteAdapter(R.id.widget_fav_recipes, intent)
      setEmptyView(R.id.widget_fav_recipes, R.id.widget_fav_recipes_empty_view)
      setInt(R.id.fav_recipes_container, "setBackgroundResource", R.drawable.bg_widget)
    }
    appWidgetManager?.updateAppWidget(widgetId, remoteView)
    appWidgetManager?.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_fav_recipes)
  }
}
