package com.fenbi.android.zebraenglish.ui.span

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Spannable
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.MetricAffectingSpan
import android.text.style.ReplacementSpan
import android.text.style.UpdateLayout
import androidx.core.text.toSpannable

/**
 * 调整两个字符间距 Span
 * @author hanrx @ Zebra Inc.
 * @since 01-20-2022
 */
class MyLetterBehindSpaceSpan(
) : MetricAffectingSpan(), UpdateLayout {

    private var spans: Array<CharacterStyle>? = null
    private var spannable: Spannable? = null

    override fun updateDrawState(p0: TextPaint?) {
        apply(p0)
    }

    override fun updateMeasureState(p0: TextPaint) {
        apply(p0)
    }

    private fun apply(tp: TextPaint?) {
        tp?.letterSpacing = (tp?.textSize ?: 0f) / 6f
        tp?.textAlign = Paint.Align.CENTER
    }
}