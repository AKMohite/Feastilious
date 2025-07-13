// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.widgets.favoriterecipes

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.ak.feastit.R
import com.mak.feastit.domain.model.FavoriteWidgetType
import com.mak.feastit.domain.repository.WidgetRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import timber.log.Timber

internal class FavoriteRecipesWidgetViewsFactory(
  private val context: Context,
  private val widgetRepository: WidgetRepository,
) : RemoteViewsService.RemoteViewsFactory {

  private val viewItems = mutableListOf<FavoriteWidgetType>()
  private var job: Job? = null

  override fun getCount(): Int = viewItems.count()

  override fun getItemId(position: Int): Long = viewItems[position].id

  override fun getLoadingView(): RemoteViews? = RemoteViews(context.packageName, R.layout.widget_loading_item)

  override fun getViewAt(position: Int): RemoteViews? {
    return when (val type = viewItems[position]) {
      is FavoriteWidgetType.Header -> createHeaderRemoteView(type)
      is FavoriteWidgetType.Item -> createItemRemoteView(type)
    }
  }

  private fun createItemRemoteView(type: FavoriteWidgetType.Item): RemoteViews {
    return RemoteViews(context.packageName, R.layout.widget_favorite_recipe).apply {
      setTextViewText(R.id.widget_fav_recipe_title, type.recipe.name)
      try {
        Timber.d("Loading image for ${type.recipe.image}")
        val loader = ImageLoader(context)
        val request =
          ImageRequest
            .Builder(context)
            .data(type.recipe.image)
            .size(context.resources.getDimensionPixelSize(R.dimen.icon_size), context.resources.getDimensionPixelSize(R.dimen.icon_size))
            .allowHardware(false) // Disable hardware bitmaps.
            .build()
        val drawable = runBlocking { (loader.execute(request) as? SuccessResult)?.drawable }
        val bitmap = (drawable as? BitmapDrawable)?.bitmap
        bitmap?.let {
          Timber.d("Image loaded")
          setImageViewBitmap(R.id.widget_fav_recipe_img, it)
        }
      } catch (t: Throwable) {
        Timber.e(t, "Cannot load image ${type.recipe.image}")
      }
    }
  }

  private fun createHeaderRemoteView(type: FavoriteWidgetType.Header): RemoteViews {
    return RemoteViews(context.packageName, R.layout.widget_header).apply {
      setTextViewText(R.id.widget_header_title, context.getString(type.title))
    }
  }

  override fun getViewTypeCount(): Int = 3

  override fun hasStableIds(): Boolean = true

  override fun onCreate() = Unit

  override fun onDataSetChanged() {
    runBlocking {
      val favorites = widgetRepository.getFavoriteRecipes()
      viewItems.clear()
      if (favorites.isNotEmpty()) {
        viewItems.add(FavoriteWidgetType.Header(R.string.favorites))
      }
      favorites.forEach { recipe ->
        viewItems.add(FavoriteWidgetType.Item(recipe))
      }
    }
  }

  override fun onDestroy() {
    job?.cancel()
  }
}
