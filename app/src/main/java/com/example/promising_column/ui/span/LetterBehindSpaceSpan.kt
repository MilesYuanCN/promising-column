package com.fenbi.android.zebraenglish.ui.span

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Spannable
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.MetricAffectingSpan
import android.text.style.ReplacementSpan
import androidx.core.text.toSpannable

/**
 * 调整两个字符间距 Span
 * @author hanrx @ Zebra Inc.
 * @since 01-20-2022
 */
class LetterBehindSpaceSpan(
    private val letterSpace: Int = 0
): ReplacementSpan() {

    private var spans : Array<CharacterStyle>? = null
    private var spannable: Spannable? = null

    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {

        val metrics = paint?.fontMetricsInt
        if (fm != null) {
            fm.top = metrics.top
            fm.ascent = metrics.ascent
            fm.descent = metrics.descent
            fm.bottom = metrics.bottom
        }
        val characterWidth = paint.measureText(text?.substring(end - 1, end) ?: "").toInt()
        val textWidth = paint.measureText(text?.substring(start, end) ?: "").toInt()
        return (characterWidth * 1 / 6) + textWidth
    }

    override fun draw(canvas: Canvas, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        text ?: return
        if (spannable.isNullOrEmpty()) {
            spannable = text.toSpannable()
        }
        if (spans.isNullOrEmpty()) {
            spannable?.let {
                spans = it.getSpans(start, end, CharacterStyle::class.java)
            }
        }
        spans?.let {
            for (span in it) {
                if (span is CharacterStyle && span !is MetricAffectingSpan && paint is TextPaint) {
                    span.updateDrawState(paint)
                }
            }
        }
        canvas.drawText(
            text, start, end, x, y.toFloat(), paint
        )
    }
}