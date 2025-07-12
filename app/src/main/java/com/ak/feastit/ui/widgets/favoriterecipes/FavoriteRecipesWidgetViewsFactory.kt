// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.widgets.favoriterecipes

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.annotation.StringRes
import coil.ImageLoader
import coil.request.ImageRequest
import com.ak.feastit.R
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import timber.log.Timber

internal class FavoriteRecipesWidgetViewsFactory(
  private val context: Context,
  private val recipesRepository: RecipesRepository,
) : RemoteViewsService.RemoteViewsFactory {

  private val viewItems = mutableListOf<FavoriteWidgetType>()

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
        val loader = ImageLoader(context)
        val request =
          ImageRequest
            .Builder(context)
            .data(type.recipe.image)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()
//        TODO load images in lis
        /*val drawable = (loader.execute(request) as? SuccessResult)?.drawable
        val bitmap = (drawable as? BitmapDrawable)?.bitmap
        bitmap?.let {
          setImageViewBitmap(R.id.widget_fav_recipe_img, it)
          Timber.d("Image loaded")
        }*/
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
      val favorites = recipesRepository.observeFavoriteRecipes().firstOrNull() ?: return@runBlocking
      viewItems.clear()
      if (favorites.isNotEmpty()) {
        viewItems.add(FavoriteWidgetType.Header(R.string.favorites))
      }
      favorites.forEach { recipe ->
        viewItems.add(FavoriteWidgetType.Item(recipe))
      }
    }
  }

  override fun onDestroy() = Unit
}

private sealed interface FavoriteWidgetType {
  val id: Long
  data class Item(
    val recipe: Recipe,
    override val id: Long = recipe.id,
  ) : FavoriteWidgetType
  data class Header(
    @StringRes val title: Int,
    override val id: Long = 1L,
  ) : FavoriteWidgetType
}
