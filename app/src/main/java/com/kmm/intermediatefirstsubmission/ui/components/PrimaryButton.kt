package com.kmm.intermediatefirstsubmission.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.kmm.intermediatefirstsubmission.R

class PrimaryButton : AppCompatButton {

    private var customText: String = text.toString()
    private var enabledState: Drawable =
            ContextCompat.getDrawable(context, R.drawable.bg_primary_button) as Drawable
    private var disabledState: Drawable =
            ContextCompat.getDrawable(context, R.drawable.bg_primary_button_disabled) as Drawable


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledState else disabledState
        text = if (isEnabled) customText else ""
        gravity = Gravity.CENTER
    }

}