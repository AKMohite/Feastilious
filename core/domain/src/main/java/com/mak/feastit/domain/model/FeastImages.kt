// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.model

data class RecipeImage(
  val id: Long,
  val extension: String,
  private var size: RecipeImageSize,
  val type: ImageType,
) {

  private fun setImageSize(size: RecipeImageSize) {
    this.size = size
  }

  fun setSaver() {
    when (type) {
      ImageType.BANNER -> setImageSize(RecipeImageSize.SMALL)
      ImageType.CELL -> setImageSize(RecipeImageSize.EXTRA_SMALL)
    }
  }

  fun getResource(): String {
    return "https://img.spoonacular.com/recipes/$id-${size.width}x${size.height}.$extension"
  }
}

enum class RecipeImageSize(val width: Int, val height: Int) {
  ICON(90, 90),
  EXTRA_SMALL(240, 150),
  SMALL(312, 150),
  MEDIUM(312, 231),
  LARGE(480, 360),
  EXTRA_LARGE(556, 370),
  EXTRA_EXTRA_LARGE(636, 393),
}

enum class ImageType(val key: String) {
  BANNER("banner"),
  CELL("cell"),
}
