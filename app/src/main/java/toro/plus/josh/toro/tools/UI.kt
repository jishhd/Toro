package toro.plus.josh.toro.tools

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import com.google.android.material.textfield.TextInputLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import toro.plus.josh.toro.R


class UI {
    companion object {
        @JvmStatic
        fun showDeleteDialog(delete: Runnable) {
            delete.run()
        }

        @JvmStatic
        fun pop(context: Context, text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }

        @JvmStatic
        fun updateBackgroundColor(context: Context, view: View?, oldColor: Int, newColor: Int) {
            val valueAnimator = ValueAnimator.ofArgb(context.getColor(oldColor), context.getColor(newColor))
            valueAnimator.duration = context.resources.getInteger(R.integer.anim_duration).toLong()
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener {
                view?.setBackgroundColor(valueAnimator.animatedValue as Int)
            }
            valueAnimator.start()
        }

        @JvmStatic
        fun updateCardBackgroundColor(context: Context, view: CardView?, oldColor: Int, newColor: Int) {
            val valueAnimator = ValueAnimator.ofArgb(context.getColor(oldColor), context.getColor(newColor))
            valueAnimator.duration = context.resources.getInteger(R.integer.anim_duration).toLong()
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener {
                view?.setCardBackgroundColor(valueAnimator.animatedValue as Int)
            }
            valueAnimator.start()
        }

        @JvmStatic
        fun updateHighlightColor(context: Context, view: AppCompatEditText?, oldColor: Int, newColor: Int) {
            val valueAnimator = ValueAnimator.ofArgb(context.getColor(oldColor), context.getColor(newColor))
            valueAnimator.duration = context.resources.getInteger(R.integer.anim_duration).toLong()
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener {
                view?.highlightColor = valueAnimator.animatedValue as Int
            }
            valueAnimator.start()
        }

        @JvmStatic
        fun updateBoxStrokeColor(context: Context, view: TextInputLayout?, oldColor: Int, newColor: Int) {
            val valueAnimator = ValueAnimator.ofArgb(context.getColor(oldColor), context.getColor(newColor))
            valueAnimator.duration = context.resources.getInteger(R.integer.anim_duration).toLong()
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener {
                view?.boxStrokeColor = valueAnimator.animatedValue as Int
            }
            valueAnimator.start()
        }

        @JvmStatic
        fun updateTextColor(context: Context, view: TextView?, oldColor: Int, newColor: Int) {
            val valueAnimator = ValueAnimator.ofArgb(context.getColor(oldColor), context.getColor(newColor))
            valueAnimator.duration = context.resources.getInteger(R.integer.anim_duration).toLong()
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener {
                view?.setTextColor(valueAnimator.animatedValue as Int)
            }
            valueAnimator.start()
        }

        @JvmStatic
        fun updateTintListColor(context: Context, view: View?, oldColor: Int, newColor: Int) {
            val valueAnimator = ValueAnimator.ofArgb(context.getColor(oldColor), context.getColor(newColor))
            valueAnimator.duration = context.resources.getInteger(R.integer.anim_duration).toLong()
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener { if (view != null) {
                ViewCompat.setBackgroundTintList(view, ColorStateList.valueOf(valueAnimator.animatedValue as Int))
            }}
            valueAnimator.start()
        }

        @JvmStatic
        fun runLayoutAnimation(recyclerView: RecyclerView) {
            val context = recyclerView.context
            val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

            recyclerView.layoutAnimation = controller
            recyclerView.adapter?.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
        }
    }
}