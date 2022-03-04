package com.example.promising_column.ui.richText

import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned

/**
 * TODO 简要描述
 * @author upshermiles @ Zebra Inc.
 * @since 02-28-2022
 */
interface RichTextParser {
    fun parse(richTextBlocks: RichTextBlocks): List<RichTextData>
}

// native data structure
data class Range(
    val start: Int,
    val end: Int
) {
    fun size(): Int {
        return end - start
    }
}

data class RichTextRangeData(
    val range: Range,
    val entity: RichTextEntity,
)

data class RichTextData(
    val spannable: Spannable,
    val richTextEntities: List<RichTextRangeData>
)


// server data structure

data class RichTextBlocks(
    val blocks: List<RichTextBlock?>,
    val entityMap: Map<Long?, RichTextEntity?>?
)

data class RichTextBlock(
    val contents: List<RichTextContent?>?
)

data class RichTextContent(
    val ids: List<Long>?,
    val text: String?
)

data class RichTextEntity(
    val controlType: String?,
    val data: RichTextEntityData?
)

// todo 需要添加 deserializer
open class RichTextEntityData

enum class RichTextEnum(val desc: String) {
    // 有默认实现
    ITALIC("italic"),
    HOLLOW_OUT("hollowOut"),
    HIGHLIGHT("highlight"),
    SPACE("space"),

    // 没有默认实现， 需要在 Builder 中传入对应 Span
    NATUARL_SPELLING("naturalSpelling"),
}

