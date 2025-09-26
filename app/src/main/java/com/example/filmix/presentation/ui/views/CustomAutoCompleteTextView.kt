package com.example.filmix.presentation.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView

class CustomAutoCompleteTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.autoCompleteTextViewStyle
) : AppCompatAutoCompleteTextView(context, attrs, defStyleAttr) {

    override fun performFiltering(text: CharSequence?, keyCode: Int) {
        super.performFiltering(text, keyCode)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: android.graphics.Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            if (text?.isEmpty() == true) {
                performFiltering("", 0)
            } else {
                performFiltering(text, 0)
            }
        }
    }
}
