// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.settings.appearance

import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.ak.feastit.R

internal sealed interface FeastTheme {
  val name: String
  val title: Int

//  val primaryColor: Int
//  val secondaryColor: Int
//  val tertiaryColor: Int
//  val surfaceColor: Int
//  val errorColor: Int
  val style: Int

  data class Default(
    override val name: String = Default::class.java.name,
    @StringRes override val title: Int = R.string.feast_theme_default,
//    @ColorRes override val primaryColor: Int = R.color.md_theme_primary,
//    @ColorRes override val secondaryColor: Int = R.color.md_theme_secondary,
//    @ColorRes override val tertiaryColor: Int = R.color.md_theme_tertiary,
//    @ColorRes override val surfaceColor: Int = R.color.md_theme_surface,
//    @ColorRes override val errorColor: Int = R.color.md_theme_error,
    @StyleRes override val style: Int = R.style.Theme_FeastIt,
  ) : FeastTheme

  data class Black(
    override val name: String = Black::class.java.name,
    @StringRes override val title: Int = R.string.feast_theme_black,
//    @ColorRes override val primaryColor: Int = R.color.md_black_theme_primary,
//    @ColorRes override val secondaryColor: Int = R.color.md_black_theme_secondary,
//    @ColorRes override val tertiaryColor: Int = R.color.md_black_theme_tertiary,
//    @ColorRes override val surfaceColor: Int = R.color.md_black_theme_surface,
//    @ColorRes override val errorColor: Int = R.color.md_black_theme_error,
    @StyleRes override val style: Int = R.style.Theme_Black,
  ) : FeastTheme

  data class Radioactive(
    override val name: String = Radioactive::class.java.name,
    @StringRes override val title: Int = R.string.feast_theme_radioactive,
//    @ColorRes override val primaryColor: Int = R.color.md_radioactive_theme_primary,
//    @ColorRes override val secondaryColor: Int = R.color.md_radioactive_theme_secondary,
//    @ColorRes override val tertiaryColor: Int = R.color.md_radioactive_theme_tertiary,
//    @ColorRes override val surfaceColor: Int = R.color.md_radioactive_theme_surface,
//    @ColorRes override val errorColor: Int = R.color.md_radioactive_theme_error,
    @StyleRes override val style: Int = R.style.Theme_Radioactive,
  ) : FeastTheme

  data class Cyberpunk(
    override val name: String = Cyberpunk::class.java.name,
    @StringRes override val title: Int = R.string.feast_theme_cyberpunk,
//    @ColorRes override val primaryColor: Int = R.color.md_cyberpunk_theme_primary,
//    @ColorRes override val secondaryColor: Int = R.color.md_cyberpunk_theme_secondary,
//    @ColorRes override val tertiaryColor: Int = R.color.md_cyberpunk_theme_tertiary,
//    @ColorRes override val surfaceColor: Int = R.color.md_cyberpunk_theme_surface,
//    @ColorRes override val errorColor: Int = R.color.md_cyberpunk_theme_error,
    @StyleRes override val style: Int = R.style.Theme_Cyberpunk,
  ) : FeastTheme

  data class Premium(
    override val name: String = Premium::class.java.name,
    @StringRes override val title: Int = R.string.feast_theme_premium,
//    @ColorRes override val primaryColor: Int = R.color.md_premium_theme_primary,
//    @ColorRes override val secondaryColor: Int = R.color.md_premium_theme_secondary,
//    @ColorRes override val tertiaryColor: Int = R.color.md_premium_theme_tertiary,
//    @ColorRes override val surfaceColor: Int = R.color.md_premium_theme_surface,
//    @ColorRes override val errorColor: Int = R.color.md_premium_theme_error,
    @StyleRes override val style: Int = R.style.Theme_Premium,
  ) : FeastTheme

  data class Blue(
    override val name: String = Blue::class.java.name,
    @StringRes override val title: Int = R.string.feast_theme_blue,
//    @ColorRes override val primaryColor: Int = R.color.md_blue_theme_primary,
//    @ColorRes override val secondaryColor: Int = R.color.md_blue_theme_secondary,
//    @ColorRes override val tertiaryColor: Int = R.color.md_blue_theme_tertiary,
//    @ColorRes override val surfaceColor: Int = R.color.md_blue_theme_surface,
//    @ColorRes override val errorColor: Int = R.color.md_blue_theme_error,
    @StyleRes override val style: Int = R.style.Theme_Blue,
  ) : FeastTheme

  data class Violet(
    override val name: String = Violet::class.java.name,
    @StringRes override val title: Int = R.string.feast_theme_violet,
//    @ColorRes override val primaryColor: Int = R.color.md_violet_theme_primary,
//    @ColorRes override val secondaryColor: Int = R.color.md_violet_theme_secondary,
//    @ColorRes override val tertiaryColor: Int = R.color.md_violet_theme_tertiary,
//    @ColorRes override val surfaceColor: Int = R.color.md_violet_theme_surface,
//    @ColorRes override val errorColor: Int = R.color.md_violet_theme_error,
    @StyleRes override val style: Int = R.style.Theme_Violet,
  ) : FeastTheme

  data class DevilRed(
    override val name: String = DevilRed::class.java.name,
    @StringRes override val title: Int = R.string.feast_theme_devil_red,
//    @ColorRes override val primaryColor: Int = R.color.md_devil_red_theme_primary,
//    @ColorRes override val secondaryColor: Int = R.color.md_devil_red_theme_secondary,
//    @ColorRes override val tertiaryColor: Int = R.color.md_devil_red_theme_tertiary,
//    @ColorRes override val surfaceColor: Int = R.color.md_devil_red_theme_surface,
//    @ColorRes override val errorColor: Int = R.color.md_devil_red_theme_error,
    @StyleRes override val style: Int = R.style.Theme_DevilRed,
  ) : FeastTheme

  data class Brown(
    override val name: String = Brown::class.java.name,
    @StringRes override val title: Int = R.string.feast_theme_brown,
//    @ColorRes override val primaryColor: Int = R.color.md_brown_theme_primary,
//    @ColorRes override val secondaryColor: Int = R.color.md_brown_theme_secondary,
//    @ColorRes override val tertiaryColor: Int = R.color.md_brown_theme_tertiary,
//    @ColorRes override val surfaceColor: Int = R.color.md_brown_theme_surface,
//    @ColorRes override val errorColor: Int = R.color.md_brown_theme_error,
    @StyleRes override val style: Int = R.style.Theme_Brown,
  ) : FeastTheme

  data class Teal(
    override val name: String = Teal::class.java.name,
    @StringRes override val title: Int = R.string.feast_theme_teal,
//    @ColorRes override val primaryColor: Int = R.color.md_teal_theme_primary,
//    @ColorRes override val secondaryColor: Int = R.color.md_teal_theme_secondary,
//    @ColorRes override val tertiaryColor: Int = R.color.md_teal_theme_tertiary,
//    @ColorRes override val surfaceColor: Int = R.color.md_teal_theme_surface,
//    @ColorRes override val errorColor: Int = R.color.md_teal_theme_error,
    @StyleRes override val style: Int = R.style.Theme_Teal,
  ) : FeastTheme

  data class Pink(
    override val name: String = Pink::class.java.name,
    @StringRes override val title: Int = R.string.feast_theme_pink,
//    @ColorRes override val primaryColor: Int = R.color.md_pink_theme_primary,
//    @ColorRes override val secondaryColor: Int = R.color.md_pink_theme_secondary,
//    @ColorRes override val tertiaryColor: Int = R.color.md_pink_theme_tertiary,
//    @ColorRes override val surfaceColor: Int = R.color.md_pink_theme_surface,
//    @ColorRes override val errorColor: Int = R.color.md_pink_theme_error,
    @StyleRes override val style: Int = R.style.Theme_Pink,
  ) : FeastTheme

  data class ClassicRed(
    override val name: String = ClassicRed::class.java.name,
    @StringRes override val title: Int = R.string.feast_theme_classic_red,
//    @ColorRes override val primaryColor: Int = R.color.md_red_theme_primary,
//    @ColorRes override val secondaryColor: Int = R.color.md_red_theme_secondary,
//    @ColorRes override val tertiaryColor: Int = R.color.md_red_theme_tertiary,
//    @ColorRes override val surfaceColor: Int = R.color.md_red_theme_surface,
//    @ColorRes override val errorColor: Int = R.color.md_red_theme_error,
    @StyleRes override val style: Int = R.style.Theme_ClassicRed,
  ) : FeastTheme

  data class Gold(
    override val name: String = Gold::class.java.name,
    @StringRes override val title: Int = R.string.feast_theme_gold,
//    @ColorRes override val primaryColor: Int = R.color.md_gold_theme_primary,
//    @ColorRes override val secondaryColor: Int = R.color.md_gold_theme_secondary,
//    @ColorRes override val tertiaryColor: Int = R.color.md_gold_theme_tertiary,
//    @ColorRes override val surfaceColor: Int = R.color.md_gold_theme_surface,
//    @ColorRes override val errorColor: Int = R.color.md_gold_theme_error,
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
