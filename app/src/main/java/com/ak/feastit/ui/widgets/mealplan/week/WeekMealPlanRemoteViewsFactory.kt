// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.widgets.mealplan.week

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.ak.feastit.R
import com.mak.feastit.domain.model.WeekMealPlanSection
import com.mak.feastit.domain.repository.WidgetRepository
import kotlinx.coroutines.runBlocking
import timber.log.Timber

internal class WeekMealPlanRemoteViewsFactory(
  private val context: Context,
  private val widgetRepository: WidgetRepository,
) : RemoteViewsService.RemoteViewsFactory {

  private val viewItems = mutableListOf<WeekMealPlanSection>()

  override fun getCount(): Int = viewItems.size

  override fun getItemId(position: Int): Long {
    return when (val item = viewItems[position]) {
      is WeekMealPlanSection.DayHeader -> {
        item.day.hashCode().toLong()
      }

      is WeekMealPlanSection.MealRecipe -> {
        item.meal.id
      }
    }
  }

  override fun getLoadingView(): RemoteViews? = RemoteViews(context.packageName, R.layout.widget_loading_item)

  override fun getViewAt(position: Int): RemoteViews? {
    return when (val item = viewItems[position]) {
      is WeekMealPlanSection.DayHeader -> createHeaderRemoteView(item)
      is WeekMealPlanSection.MealRecipe -> createMealRemoteView(item)
    }
  }

  private fun createMealRemoteView(item: WeekMealPlanSection.MealRecipe): RemoteViews {
    return RemoteViews(context.packageName, R.layout.widget_meal_plan_recipe).apply {
      setTextViewText(R.id.widget_recipe_name, item.meal.name)
      setTextViewText(R.id.widget_preparation_time, item.meal.displayablePreparationTime())
      try {
        Timber.d("Loading image for ${item.meal.image}")
        val loader = ImageLoader(context)
        val request =
          ImageRequest
            .Builder(context)
            .data(item.meal.image)
            .size(context.resources.getDimensionPixelSize(R.dimen.icon_size), context.resources.getDimensionPixelSize(R.dimen.icon_size))
            .allowHardware(false) // Disable hardware bitmaps.
            .build()
        val drawable = runBlocking { (loader.execute(request) as? SuccessResult)?.drawable }
        val bitmap = (drawable as? BitmapDrawable)?.bitmap
        bitmap?.let {
          Timber.d("Image loaded")
          setImageViewBitmap(R.id.widget_recipe_img, it)
        }
      } catch (t: Throwable) {
        Timber.e(t, "Cannot load image ${item.meal.image}")
      }
    }
  }

  private fun createHeaderRemoteView(item: WeekMealPlanSection.DayHeader): RemoteViews {
    return RemoteViews(context.packageName, R.layout.widget_day_meal_plan_recipe).apply {
      setTextViewText(R.id.widget_day_tv, item.day.day)
      setTextViewText(R.id.widget_toggle_icon, item.day.noOfMeal)
      val visibility = if (!item.day.noOfMeal.isEmpty()) View.VISIBLE else View.GONE
      setViewVisibility(R.id.widget_toggle_icon, visibility)
    }
  }

  override fun getViewTypeCount(): Int = 3

  override fun hasStableIds(): Boolean = true

  override fun onCreate() = Unit

  override fun onDataSetChanged() {
    runBlocking {
      val mealPlans = widgetRepository.getWeekMealPlans()
      viewItems.clear()
      viewItems.addAll(mealPlans)
    }
  }

  override fun onDestroy() = Unit
}
