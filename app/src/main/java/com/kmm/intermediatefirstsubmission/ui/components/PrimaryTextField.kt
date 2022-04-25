package com.kmm.intermediatefirstsubmission.ui.components

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.kmm.intermediatefirstsubmission.R

class PrimaryTextField : TextInputEditText, View.OnTouchListener {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        when {
            focused -> {
                clearButton.setTint(ContextCompat.getColor(context, R.color.skin_color))
            }
            !focused -> {
                clearButton.setTint(ContextCompat.getColor(context, R.color.cream))
            }
        }
    }

    init {

        setTextColor(ContextCompat.getColor(context, R.color.skin_color))
        setHintTextColor(ContextCompat.getColor(context, R.color.gray))
        clearButton =
                ContextCompat.getDrawable(context, R.drawable.ic_baseline_clear_24) as Drawable
        background =
                ContextCompat.getDrawable(context, R.drawable.bg_outlined_textfield) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                error = if (text?.isEmpty() == true) {

                    hint.toString() + context.getString(R.string.must_not_empty)
                } else {
                    hideClearIcon()
                    showClearIcon()
                    null
                }

            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }


    private var clearButton: Drawable

    private fun showClearIcon() {
        setEndIcon(icon = clearButton)
    }

    private fun hideClearIcon() {
        setEndIcon()
    }

    private fun setEndIcon(@Nullable icon: Drawable? = null) {
        setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        compoundDrawables[2]?.let {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClicked = false
            if (v?.layoutDirection == LAYOUT_DIRECTION_RTL) {
                clearButtonStart = (clearButton.intrinsicWidth + paddingStart).toFloat()
                when {
                    event!!.x <= clearButtonStart -> isClicked = true
                }
            } else {
                clearButtonEnd = (width - paddingEnd - clearButton.intrinsicWidth).toFloat()
                when {
                    event!!.x >= clearButtonEnd -> isClicked = true
                }
            }
            if (isClicked) {
                when (event!!.action) {
                    MotionEvent.ACTION_UP -> {
                        clearButton = ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_baseline_clear_24
                        ) as Drawable
                        text?.clear()
                        hideClearIcon()
                        error = hint.toString() + context.getString(R.string.must_not_empty)
                        return true
                    }
                }
            }
            return false
        }
        return false
    }


}