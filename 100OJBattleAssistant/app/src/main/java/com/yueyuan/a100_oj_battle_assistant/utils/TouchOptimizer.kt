package com.yueyuan.a100_oj_battle_assistant.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

object TouchOptimizer {
    
    /**
     * Optimize touch targets for mobile devices
     * Ensures minimum 48dp touch target size as per Material Design guidelines
     */
    fun optimizeTouchTargets(rootView: ViewGroup) {
        val minTouchTargetSize = dpToPx(rootView.context, 48f).toInt()
        
        fun optimizeView(view: View) {
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    optimizeView(view.getChildAt(i))
                }
            } else {
                // Ensure minimum touch target size
                val layoutParams = view.layoutParams
                if (layoutParams.width > 0 && layoutParams.width < minTouchTargetSize) {
                    layoutParams.width = minTouchTargetSize
                }
                if (layoutParams.height > 0 && layoutParams.height < minTouchTargetSize) {
                    layoutParams.height = minTouchTargetSize
                }
                view.layoutParams = layoutParams
            }
        }
        
        optimizeView(rootView)
    }
    
    /**
     * Add ripple effect to clickable views for better visual feedback
     */
    fun addRippleEffect(view: View) {
        val context = view.context
        val typedArray = context.obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackground))
        val backgroundResource = typedArray.getResourceId(0, 0)
        typedArray.recycle()
        
        if (backgroundResource != 0) {
            ViewCompat.setBackground(view, context.getDrawable(backgroundResource))
        }
    }
    
    /**
     * Handle keyboard insets for better input experience
     */
    fun handleKeyboardInsets(rootView: View, scrollView: View) {
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, windowInsets ->
            val imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime())
            val systemInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            
            // Adjust padding based on keyboard visibility
            scrollView.updatePadding(bottom = imeInsets.bottom)
            view.updatePadding(
                left = systemInsets.left,
                top = systemInsets.top,
                right = systemInsets.right,
                bottom = if (imeInsets.bottom > 0) 0 else systemInsets.bottom
            )
            
            windowInsets
        }
    }
    
    /**
     * Optimize EditText for mobile input
     */
    fun optimizeEditText(editText: EditText) {
        // Prevent text selection on single tap for number inputs
        if (editText.inputType and android.text.InputType.TYPE_CLASS_NUMBER != 0) {
            editText.setOnClickListener {
                editText.selectAll()
            }
        }
        
        // Add content description for accessibility
        if (editText.contentDescription.isNullOrEmpty()) {
            editText.contentDescription = editText.hint
        }
    }
    
    /**
     * Add haptic feedback for button interactions
     */
    fun addHapticFeedback(view: View) {
        view.setOnClickListener { originalClickListener ->
            view.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            (originalClickListener as? View.OnClickListener)?.onClick(view)
        }
    }
    
    private fun dpToPx(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}