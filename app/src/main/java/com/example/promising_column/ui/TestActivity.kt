package com.example.promising_column.ui

import android.os.Bundle
import android.text.*
import android.text.style.*
import android.view.animation.AnimationUtils
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentActivity
import com.example.promising_column.R

import android.util.Log
import com.example.promising_column.ui.richText.RichTextBlocks
import com.fenbi.android.common.RichTextParserBuilder
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


/**
 * TODO 简要描述
 * @author upshermiles @ Zebra Inc.
 * @since 01-26-2022
 */
class TestActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default)
        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction().add(R.id.root, TestFragment(TestVO(100, 35))).commitAllowingStateLoss()
        }

    }

    override fun onStart() {
        super.onStart()
        val textSwitch = findViewById<TextSwitcher>(R.id.ts_ts)
        val button = findViewById<AppCompatButton>(R.id.bt_bt)
        val textView = findViewById<TextView>(R.id.tv_span)
        textSwitch.setFactory {
            TextView(this)
        }
        textSwitch.setText("123")
        textSwitch.inAnimation = AnimationUtils.loadAnimation(this, R.anim.fragment_close_exit)
        textSwitch.outAnimation = AnimationUtils.loadAnimation(this, R.anim.fragment_close_enter)

        textView.setText("this is a sente\u0020nce ", TextView.BufferType.SPANNABLE)
        GlobalScope.launch(Dispatchers.IO) {
//            val client = OkHttpClient.Builder().build()
//            val service = Retrofit.Builder().apply {
//                client(client)
//                addConverterFactory(GsonConverterFactory.create())
//                baseUrl("https://conan.yuanfudao.biz")
//            }.build().create(EpisodeService::class.java)
//
//            val richTextBlocks = service.getRichText()?.execute()?.body()
//            if (richTextBlocks == null) {
//                Log.e("YLC", "null !!!!!")
//                return@launch
//            }

            val richTextBlocks = Gson().fromJson(exampleJson, RichTextBlocks::class.java)
            val output = RichTextParserBuilder().build().parse(richTextBlocks)
            textView.setText(output.first().spannable)
        }
    }

    interface EpisodeService {
        @GET("/conan-zvas-member/android/member-info")
        fun getRichText(): Call<RichTextBlocks?>?
    }

}