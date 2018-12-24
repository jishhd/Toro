package toro.plus.josh.toro.tools

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import com.google.android.material.textfield.TextInputLayout

class UI {
    companion object {
        const val COLOR_ANIM_MS = 250L

        @JvmStatic
        fun showDeleteDialog(delete: Runnable) {
            delete.run()
        }

        @JvmStatic
        fun updateBackgroundColor(context: Context, view: View?, oldColor: Int, newColor: Int) {
            val valueAnimator = ValueAnimator.ofArgb(context.getColor(oldColor), context.getColor(newColor))
            valueAnimator.duration = UI.COLOR_ANIM_MS
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener {
                view?.setBackgroundColor(valueAnimator.animatedValue as Int)
            }
            valueAnimator.start()
        }

        @JvmStatic
        fun updateHighlightColor(context: Context, view: AppCompatEditText?, oldColor: Int, newColor: Int) {
            val valueAnimator = ValueAnimator.ofArgb(context.getColor(oldColor), context.getColor(newColor))
            valueAnimator.duration = UI.COLOR_ANIM_MS
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener {
                view?.highlightColor = valueAnimator.animatedValue as Int
            }
            valueAnimator.start()
        }

        @JvmStatic
        fun updateBoxStrokeColor(context: Context, view: TextInputLayout?, oldColor: Int, newColor: Int) {
            val valueAnimator = ValueAnimator.ofArgb(context.getColor(oldColor), context.getColor(newColor))
            valueAnimator.duration = UI.COLOR_ANIM_MS
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener {
                view?.boxStrokeColor = valueAnimator.animatedValue as Int
            }
            valueAnimator.start()
        }

        @JvmStatic
        fun updateTextColor(context: Context, view: TextView?, oldColor: Int, newColor: Int) {
            val valueAnimator = ValueAnimator.ofArgb(context.getColor(oldColor), context.getColor(newColor))
            valueAnimator.duration = UI.COLOR_ANIM_MS
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener {
                view?.setTextColor(valueAnimator.animatedValue as Int)
            }
            valueAnimator.start()
        }

        @JvmStatic
        fun updateTintListColor(context: Context, view: View?, oldColor: Int, newColor: Int) {
            val valueAnimator = ValueAnimator.ofArgb(context.getColor(oldColor), context.getColor(newColor))
            valueAnimator.duration = UI.COLOR_ANIM_MS
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener { if (view != null) {
                ViewCompat.setBackgroundTintList(view, ColorStateList.valueOf(valueAnimator.animatedValue as Int))
            }}
            valueAnimator.start()
        }
    }
}