package com.fenbi.android.common

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.example.promising_column.ui.richText.*
import com.fenbi.android.zebraenglish.ui.span.LetterBehindSpaceSpan
import java.util.*

/**
 * 解析富文本结构， 输出能用的东西
 * @author yuanlucheng @ Zebra Inc.
 * @since 02-25-2022
 */

class RichTextParserImpl(customSpanList: Map<RichTextEnum, SpannableProcessor>) : RichTextParser {

    private val defaultItalicRichTextConfig: SpannableProcessor = { spannableStringBuilder, richTextRangeData ->
        if (richTextRangeData.range.size() <= 0) {
            spannableStringBuilder
        } else {
            spannableStringBuilder.setSpan(
                StyleSpan(Typeface.ITALIC),
                richTextRangeData.range.start,
                richTextRangeData.range.end,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            spannableStringBuilder.setSpan(
                LetterBehindSpaceSpan(),
                richTextRangeData.range.end - 1,
                richTextRangeData.range.end,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            spannableStringBuilder
        }
    }

    // todo
    val supportRichTextMap = mapOf<RichTextEnum, SpannableProcessor>(
        RichTextEnum.ITALIC to defaultItalicRichTextConfig,
        RichTextEnum.HIGHLIGHT to ForegroundColorSpan(Color.GREEN).toSomething(),
    )


    init {
        (supportRichTextMap as MutableMap<RichTextEnum, SpannableProcessor>).putAll(customSpanList)
    }

    /**
     * 感觉职责：
     * * 提供 Spannable
     * * 提供成组信息
     * * 解析数据结构， 返回比较干净的数据结构
     */
    override fun parse(richTextBlocks: RichTextBlocks): List<RichTextData> {
        return richTextBlocks.blocks.map {
            val output = parseSingleBlock(it, richTextBlocks.entityMap)
            RichTextData(
                spannable = output.first,
                richTextEntities = output.second
            )
        }
    }

    private fun parseSingleBlock(
        richTextBlock: RichTextBlock?,
        entityMap: Map<Long?, RichTextEntity?>?
    ): Pair<Spannable, List<RichTextRangeData>> {
        if (richTextBlock == null || richTextBlock.contents.isNullOrEmpty() || entityMap.isNullOrEmpty()) {
            logE("invalid data") // todo
            return SpannableString("") to emptyList()
        }

        // 1. 成组，找到每个 SpanInstance 的 start & end
        val idToRangeMap = mutableMapOf<Long, RichTextRangeData>()
        val spannableStringBuilder = SpannableStringBuilder()
        richTextBlock.contents.forEach content@{ content ->
            if (content?.text == null) {
                logE("parse error content is null")
                return@content
            }
            val startIndex = spannableStringBuilder.length // 以原先string 长度为起点（Inclusive）
            spannableStringBuilder.append(content.text)
            val endIndex = spannableStringBuilder.length // 以 append 后 string 长度为终点（Exclusive）
            content.ids?.forEach { id ->
                val richTextEntity = entityMap[id]
                if (richTextEntity == null) {
                    logE("richTextEntity is null, diacard this id")
                    return@forEach
                }

                if (idToRangeMap.containsKey(id)) {
                    val rangeTextRangeData = idToRangeMap[id] ?: return@forEach
                    val previousEndIndex = rangeTextRangeData.range.end
                    if (startIndex == previousEndIndex) {
                        // 两个相同 id 紧挨着， 成组
                        idToRangeMap[id] = RichTextRangeData(
                            Range(rangeTextRangeData.range.start, endIndex),
                            richTextEntity
                        )
                    } else {
                        // 两个相同 id 中间有间隔（不应出现），将前面信息覆盖掉
                        logE("same id not continuous, id:$id")
                        idToRangeMap[id] =
                            RichTextRangeData(
                                Range(startIndex, endIndex),
                                richTextEntity,
                            )
                    }
                } else {
                    idToRangeMap[id] =
                        RichTextRangeData(Range(startIndex, endIndex), richTextEntity)
                }
            }
        }

        // 2. 拆分出支持列表和不支持列表，解析器处理支持枚举， 返回未解析枚举
        val invalidRichTextData = mutableMapOf<Long, RichTextRangeData>()
        val validRichTextData = mutableMapOf<Long, RichTextRangeData>()

        idToRangeMap.forEach { (l, richTextRangeData) ->
            if (supportRichTextMap.containsKey(
                    // todo 这个 valueOf 会Exception
                    RichTextEnum.valueOf(
                        richTextRangeData.entity.controlType ?: ""
                    )
                )
            ) {
                validRichTextData[l] = richTextRangeData
            } else {
                invalidRichTextData[l] = richTextRangeData
            }

        }

        // 3. 对 valid 的类型设置 span
        validRichTextData.forEach {
            val entity = it.value.entity
            if (entity.controlType == null) {
                logE("can't find entity, id:${it.key}")
                return@forEach
            }
            supportRichTextMap[RichTextEnum.valueOf(entity.controlType)]?.let { callback ->
                callback.invoke(spannableStringBuilder, it.value)
            }
        }

        return spannableStringBuilder to invalidRichTextData.values.toList()
    }

    // todo 整个 Builder
    private fun log(msg: String) {
        // todo implement
    }

    private fun logE(msg: String, t: Throwable? = null) {

    }

    companion object {
        private const val TAG = "RichTextParserImpl"
    }
}

class RichTextParserBuilder() {

    private val customSpanList = mutableMapOf<RichTextEnum, SpannableProcessor>()

    fun addSpan(richTextEnum: RichTextEnum, spannableProcessor: SpannableProcessor) {
        customSpanList[richTextEnum] = spannableProcessor
    }

    fun addSpan(richTextEnum: RichTextEnum, span: Any) {
        customSpanList[richTextEnum] = span.toSomething()
    }

    fun build(): RichTextParser {
        return RichTextParserImpl(customSpanList)
    }

}

/** todo 依赖了以下前提
 * 1. 相同 id 不间隔 （如果间隔的话， 需要保存 List<Range>， 要不然一个 Range 就行
 * 2. 相同 id 不跨 block
 * 3. 同一 content 里面没有相同 id
 **/


private typealias SpannableProcessor = (spannableStringBuilder: SpannableStringBuilder, richTextRangeData: RichTextRangeData) -> SpannableStringBuilder

fun Any.toSomething(): SpannableProcessor =
    { span, richTextRangeData ->
        span.setSpan(
            this,
            richTextRangeData.range.start,
            richTextRangeData.range.end,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        span
    }
