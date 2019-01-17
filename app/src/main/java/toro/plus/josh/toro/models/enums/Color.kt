package toro.plus.josh.toro.models.enums

import toro.plus.josh.toro.R

enum class Color(
    val colorPale: Int,
    val colorLight: Int,
    val color: Int,
    val colorDark: Int,
    val colorAccent: Int
) {
    RED(
        R.color.pale_red,
        R.color.light_red,
        R.color.red,
        R.color.dark_red,
        R.color.accent_red
    ),
    ORANGE(
        R.color.pale_orange,
        R.color.light_orange,
        R.color.orange,
        R.color.dark_orange,
        R.color.accent_orange
    ),
    YELLOW(
        R.color.pale_yellow,
        R.color.light_yellow,
        R.color.yellow,
        R.color.dark_yellow,
        R.color.accent_yellow
    ),
    GREEN(
        R.color.pale_green,
        R.color.light_green,
        R.color.green,
        R.color.dark_green,
        R.color.accent_green
    ),
    BLUE(
        R.color.pale_blue,
        R.color.light_blue,
        R.color.blue,
        R.color.dark_blue,
        R.color.accent_blue
    ),
    PURPLE(
        R.color.pale_purple,
        R.color.light_purple,
        R.color.purple,
        R.color.dark_purple,
        R.color.accent_purple
    ),
}