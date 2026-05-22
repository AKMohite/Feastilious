// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.ui.features.settings

import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.ak.feastit.compose.R

internal sealed interface FeastTheme {
  val dataClassName: String
    get() = this::class.java.simpleName
  val title: Int
  val style: Int

  data class Default(
    @StringRes override val title: Int = R.string.feast_theme_default,
    @StyleRes override val style: Int = R.style.Theme_FeastIt,
  ) : FeastTheme

  data class Black(
    @StringRes override val title: Int = R.string.feast_theme_black,
    @StyleRes override val style: Int = R.style.Theme_Black,
  ) : FeastTheme

  data class Radioactive(
    @StringRes override val title: Int = R.string.feast_theme_radioactive,
    @StyleRes override val style: Int = R.style.Theme_Radioactive,
  ) : FeastTheme

  data class Cyberpunk(
    @StringRes override val title: Int = R.string.feast_theme_cyberpunk,
    @StyleRes override val style: Int = R.style.Theme_Cyberpunk,
  ) : FeastTheme

  data class Premium(
    @StringRes override val title: Int = R.string.feast_theme_premium,
    @StyleRes override val style: Int = R.style.Theme_Premium,
  ) : FeastTheme

  data class Blue(
    @StringRes override val title: Int = R.string.feast_theme_blue,
    @StyleRes override val style: Int = R.style.Theme_Blue,
  ) : FeastTheme

  data class Violet(
    @StringRes override val title: Int = R.string.feast_theme_violet,
    @StyleRes override val style: Int = R.style.Theme_Violet,
  ) : FeastTheme

  data class DevilRed(
    @StringRes override val title: Int = R.string.feast_theme_devil_red,
    @StyleRes override val style: Int = R.style.Theme_DevilRed,
  ) : FeastTheme

  data class Brown(
    @StringRes override val title: Int = R.string.feast_theme_brown,
    @StyleRes override val style: Int = R.style.Theme_Brown,
  ) : FeastTheme

  data class Teal(
    @StringRes override val title: Int = R.string.feast_theme_teal,
    @StyleRes override val style: Int = R.style.Theme_Teal,
  ) : FeastTheme

  data class Pink(
    @StringRes override val title: Int = R.string.feast_theme_pink,
    @StyleRes override val style: Int = R.style.Theme_Pink,
  ) : FeastTheme

  data class ClassicRed(
    @StringRes override val title: Int = R.string.feast_theme_classic_red,
    @StyleRes override val style: Int = R.style.Theme_ClassicRed,
  ) : FeastTheme

  data class Gold(
    @StringRes override val title: Int = R.string.feast_theme_gold,
    @StyleRes override val style: Int = R.style.Theme_Gold,
  ) : FeastTheme
}

internal val allAppThemes = listOf(
  FeastTheme.Default(),
  FeastTheme.Black(),
  FeastTheme.Premium(),
  FeastTheme.Blue(),
  FeastTheme.ClassicRed(),
  FeastTheme.Brown(),
  FeastTheme.Radioactive(),
  FeastTheme.Violet(),
  FeastTheme.DevilRed(),
  FeastTheme.Teal(),
  FeastTheme.Gold(),
  FeastTheme.Pink(),
  FeastTheme.Cyberpunk(),
)
