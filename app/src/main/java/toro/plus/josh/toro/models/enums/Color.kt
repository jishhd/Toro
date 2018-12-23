package toro.plus.josh.toro.models.enums

import toro.plus.josh.toro.R

enum class Color(val color: Int, val colorDark: Int, val colorAccent: Int) {
    RED(
        R.color.red,
        R.color.dark_red,
        R.color.accent_red
    ),
    ORANGE(
        R.color.orange,
        R.color.dark_orange,
        R.color.accent_orange
    ),
    YELLOW(
        R.color.yellow,
        R.color.dark_yellow,
        R.color.accent_yellow
    ),
    GREEN(
        R.color.green,
        R.color.dark_green,
        R.color.accent_green
    ),
    BLUE(
        R.color.blue,
        R.color.dark_blue,
        R.color.accent_blue
    ),
    PURPLE(
        R.color.purple,
        R.color.dark_purple,
        R.color.accent_purple
    ),
}