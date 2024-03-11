package com.privatepe.app.Zego

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.*
import android.widget.RelativeLayout

class FloatView(context1:Context,width1:Int,height1:Int) {

    val context:Context=context1
    val width:Int=width1
    val height:Int=height1



    fun initGestureListener(view: RelativeLayout?) {
        Log.e("cjajafa","Yes1");
        val detector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                Log.e("cjajafa","Yes2");

                view!!.performClick()
                return false
            }

            override fun onDown(e: MotionEvent): Boolean {
                Log.e("cjajafa","Yes3");

                return true
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                Log.e("cjajafa","Yes4");

                val params = view?.layoutParams
                if (params is RelativeLayout.LayoutParams) {
                    val offsetX = if (isRTL) (e2.x - (e1?.x ?: 0f)) else ((e1?.x ?: 0f) - e2.x)

                    val layoutParams = view.layoutParams as RelativeLayout.LayoutParams
                    val newX = (layoutParams.marginEnd + offsetX).toInt()
                    val newY = (layoutParams.topMargin + (e2.y - (e1?.y ?: 0f))).toInt()
                    if (newX >= 0 && newX <= width - view.width && newY >= 0 && newY <= (height-100) - view.height) {
                        layoutParams.marginEnd = newX
                        layoutParams.topMargin = newY
                        view.layoutParams = layoutParams
                    }
                }
                return true
            }
        })
        view!!.setOnTouchListener { v, event -> detector.onTouchEvent(event) }
    }
    private val isRTL: Boolean
        get() {
            val configuration = context.resources.configuration
            val layoutDirection = configuration.layoutDirection
            return layoutDirection == View.LAYOUT_DIRECTION_RTL
        }
}
