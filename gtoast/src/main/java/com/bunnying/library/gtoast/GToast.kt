package com.bunnying.library.gtoast

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import kotlin.math.min

@SuppressLint("ShowToast")
object GToast {
    const val LENGTH_SHORT = Toast.LENGTH_SHORT
    const val LENGTH_LONG = Toast.LENGTH_LONG
    private var toast: Toast? = null

    class Builder(private val context: Context?) {
        private var duration: Int = LENGTH_SHORT
        private var text: String? = null
        private var icon: Drawable? = null
        private var gravity: Int? = null
        private var offsetX: Int = 0
        private var offsetY: Int = 0
        private var colorBackground: Int? = null
        private var colorText: Int? = null
        private var colorIcon: Int? = null

        fun create(): Toast {
            val t = Toast.makeText(context, null, duration).apply {
                if(this@Builder.gravity != null || offsetX != 0 || offsetY != 0) {
                    this.setGravity(this@Builder.gravity ?: gravity, offsetX, offsetY)
                }
            }
            val v = View.inflate(context, R.layout.gtoast, null)
            v.findViewById<MaterialCardView?>(R.id.cardViewToast)?.apply {
                this@Builder.colorBackground?.let {
                    this.setCardBackgroundColor(it)
                    if(Color.alpha(it) != 255) {
                        this.cardElevation = 0f
                    }
                }
                this.addOnLayoutChangeListener { v, left, top, right, bottom, _, _, _, _ ->
                    this.radius = when (v.findViewById<AppCompatTextView?>(R.id.textViewToast)?.lineCount) {
                            0, 1 -> min(right - left, bottom - top) * 0.5f
                            else -> dpToPx(context, 24).toFloat()
                        }
                }
            }
            v.findViewById<AppCompatImageView?>(R.id.imageViewToast)?.apply {
                if(this@Builder.icon == null) this.visibility = View.GONE
                this.setImageDrawable(this@Builder.icon?.mutate()?.apply {
                    this@Builder.colorIcon?.let {
                        this.setColorFilter(it, PorterDuff.Mode.SRC_IN)
                    }
                })
            }
            v.findViewById<AppCompatTextView?>(R.id.textViewToast)?.apply {
                colorText?.let { this.setTextColor(it) }
                this.text = this@Builder.text
                if(text.isNullOrBlank()) {
                    this.visibility = View.GONE
                }
            }
            t.view = v
            return t
        }
        fun show(): Toast {
            val t = create()
            toast = t
            t.show()
            return t
        }
        fun shot(): Toast { //cancel before toast
            toast?.cancel()
            return this.show()
        }

        fun success(@StringRes textRes: Int, gravity: Int? = null): Builder
                = success(context?.getString(textRes), gravity)
        fun success(text: String?, gravity: Int? = null): Builder {
            this.text = text
            this.gravity = gravity
            this.icon = getDrawable(context, R.drawable.ic_gtoast_check_black_24dp)
            this.colorIcon = Color.WHITE
            this.colorText = Color.WHITE
            this.colorBackground = getColor(context, R.color.colorToastSuccess)
            return this
        }

        fun error(@StringRes textRes: Int, gravity: Int? = null): Builder
                = error(context?.getString(textRes), gravity)
        fun error(text: String?, gravity: Int? = null): Builder {
            this.text = text
            this.gravity = gravity
            this.icon =  getDrawable(context, R.drawable.ic_gtoast_close_black_24dp)
            this.colorIcon = Color.WHITE
            this.colorText = Color.WHITE
            this.colorBackground = getColor(context, R.color.colorToastError)
            return this
        }

        fun info(@StringRes textRes: Int, gravity: Int? = null): Builder
                = info(context?.getString(textRes), gravity)
        fun info(text: String?, gravity: Int? = null): Builder {
            this.text = text
            this.gravity = gravity
            this.icon = getDrawable(context, R.drawable.ic_gtoast_info_outline_black_24dp)
            this.colorIcon = Color.WHITE
            this.colorText = Color.WHITE
            this.colorBackground = getColor(context, R.color.colorToastInfo)
            return this
        }

        fun normal(@StringRes textRes: Int, @DrawableRes iconRes: Int? = null, gravity: Int? = null): Builder
                = normal(context?.getString(textRes), iconRes, gravity)
        fun normal(text: String?, @DrawableRes iconRes: Int? = null, gravity: Int? = null): Builder {
            this.text = text
            this.gravity = gravity
            this.icon = if(iconRes != null && iconRes != 0) {
                getDrawable(context, iconRes)
            } else {
                null
            }
            if(iconRes != null && iconRes != 0) {
                this.icon = getDrawable(context, iconRes)
            }
            this.colorIcon = Color.WHITE
            this.colorText = Color.WHITE
            this.colorBackground = getColor(context, R.color.colorToastNormal)
            return this
        }

        fun heart(@StringRes textRes: Int, iconRes: Int = R.drawable.ic_gtoast_favorite_red_24dp): Builder
                = heart(context?.getString(textRes), iconRes)
        fun heart(text: String?, iconRes: Int = R.drawable.ic_gtoast_favorite_red_24dp): Builder {
            this.text = text
            this.setIcon(iconRes)
            this.setGravity(Gravity.TOP, 0, dpToPx(context, 54))
            this.duration = LENGTH_LONG
            this.colorText = Color.BLACK
            this.colorBackground = Color.WHITE
            return this
        }

        fun setText(@StringRes textRes: Int): Builder
                = setText(context?.getString(textRes))
        fun setText(text: String?): Builder {
            this.text = text
            return this
        }

        fun setIcon(@DrawableRes iconRes: Int): Builder
                = setIcon(getDrawable(context, iconRes))
        fun setIcon(icon: Drawable?): Builder {
            this.icon = icon
            return this
        }

        fun setGravity(gravity: Int, offsetX: Int = 0, offsetY: Int = 0): Builder {
            this.gravity = gravity
            this.offsetX = offsetX
            this.offsetY = offsetY
            return this
        }

        fun setDuration(duration: Int): Builder {
            this.duration = duration
            return this
        }

        fun setBackgroundColorRes(@ColorRes colorRes: Int): Builder
                = setBackgroundColor(getColor(context, colorRes))
        fun setBackgroundColor(@ColorInt color: Int?): Builder {
            this.colorBackground = color
            return this
        }

        fun setIconColorRes(@ColorRes colorRes: Int): Builder
                = setIconColor(getColor(context, colorRes))
        fun setIconColor(@ColorInt color: Int?): Builder {
            this.colorIcon = color
            return this
        }

        fun setTextColorRes(@ColorRes colorRes: Int): Builder
                = setTextColor(getColor(context, colorRes))
        fun setTextColor(@ColorInt color: Int?): Builder {
            this.colorText = color
            return this
        }

        fun setIconTextColorRes(@ColorRes colorRes: Int): Builder
                = setIconTextColor(getColor(context, colorRes))
        fun setIconTextColor(@ColorInt color: Int?): Builder {
            this.colorIcon = color
            this.colorText = color
            return this
        }
    }

    private fun getDrawable(context: Context?, @DrawableRes drawableRes: Int): Drawable? {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> context?.getDrawable(drawableRes)
            context != null -> ContextCompat.getDrawable(context, drawableRes)
            else -> null
        }
    }
    private fun getColor(context: Context?, @ColorRes colorRes: Int): Int? {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> context?.getColor(colorRes)
            context != null -> ContextCompat.getColor(context, colorRes)
            else -> null
        }
    }

    private fun dpToPx(context: Context?, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context?.resources?.displayMetrics).toInt()
    }
}