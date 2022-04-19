package com.example.promising_column.surface

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.SurfaceHolder

import android.view.SurfaceView
import java.lang.Exception


class SurfaceViewSinFun(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback, Runnable {

    //绘图的Canvas
    private var mCanvas: Canvas? = null

    //子线程标志位
    private var mIsDrawing = false
    private var x = 0
    private var y = 0
    private val mPaint: Paint
    private val mPath: Path

    constructor(context: Context?) : this(context, null) {}
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {}

    override fun surfaceCreated(holder: SurfaceHolder) {
        mIsDrawing = true
        Thread(this).start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {


    }
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mIsDrawing = false
    }

    override fun run() {
        while (mIsDrawing) {
            drawSomething()
            x += 1
            y = (50 * Math.sin(2 * x * Math.PI / 180) + 100).toInt()
            //加入新的坐标点
            mPath.lineTo(x.toFloat(), y.toFloat())
        }
    }

    private fun drawSomething() {
        try {
            //获得canvas对象
            mCanvas = holder?.lockCanvas()
            //绘制背景
            mCanvas?.drawColor(Color.WHITE)
            //绘制路径
            mCanvas?.drawPath(mPath, mPaint)
        } catch (e: Exception) {
        } finally {
            if (mCanvas != null) {
                //释放canvas对象并提交画布
                holder?.unlockCanvasAndPost(mCanvas)
            }
        }
    }

    /**
     * 初始化View
     */
    private fun initView() {
        holder.addCallback(this)
        isFocusable = true
        keepScreenOn = true
        isFocusableInTouchMode = true
    }

    init {
        mPaint = Paint()
        mPaint.color = Color.BLACK
        mPaint.style = Paint.Style.STROKE
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = 5F
        mPath = Path()
        //路径起始点(0, 100)
        mPath.moveTo(0F, 100F)
        initView()
    }
}