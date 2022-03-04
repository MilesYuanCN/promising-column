package com.example.promising_column.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.util.logging.Logger

/**
 * @author yuanlucheng @ Zebra Inc.
 * @since 01-26-2022
 */
class TestFragment(var test: TestVO): Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("YLC", "${test.integer} &&&&  $this")
    }
}

data class TestVO(
    val integer: Int,
    val loader: Long
)
