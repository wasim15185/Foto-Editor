package com.android.demoeditor.customView


import android.annotation.SuppressLint
import android.content.Context
import com.android.demoeditor.R
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt


class RulerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArr: Int = 0
) : View(context, attrs, defStyleArr) {

    private val TAG = RulerView::class.java.simpleName

    private var isEnabelReDraw = true


    init {
        init(attrs);
    }


    var min: Float = -100f
    var max: Float = 100f
    var value: Float = 0f
//        set(value) {
//            if (value <= min || value >= max) {
//                throw Exception("Value Must be under ${min} & ${max}")
//            }
//
//            field = value
//        }

    var step: Float = 1.0f


    /**
     * [min] is for minimum value of RulerView
     * [max] is for maximum value of RulerView
     * [value] is for recent value of RulerView
     */


    private val mCanvasClipBounds: Rect = Rect()

    private lateinit var mScrollingListener: ScrollingListener
    private var mLastTouchedPosition = 0f

    private var mProgressLinePaint: Paint? = null
    private var mProgressMiddleLinePaint: Paint? = null
    private var mProgressLineWidth = 0
    private var mProgressLineHeight: Int = 0
    private var mProgressLineMargin = 0

    private var mScrollStarted = false
    private var mTotalScrollDistance = 0f

    private var mMiddleLineColor = 0
    private var mProgressLineColor = 0


    /**
     * [setScrollingListener] is Scroll-Listener
     */
    fun setScrollingListener(scrollingListener: ScrollingListener) {
        mScrollingListener = scrollingListener
    }


    /**
     * [setMiddleLineColor] is for setting the [mMiddleLineColor]
     */
    fun setMiddleLineColor(@ColorInt middleLineColor: Int) {
        mMiddleLineColor = middleLineColor
        mProgressMiddleLinePaint?.color = mMiddleLineColor
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        /**
         * [MotionEvent.ACTION_DOWN] is fired when "User Touch the finger to screen"
         * [MotionEvent.ACTION_UP] is fired when "User pull up the finger from screen"
         * [MotionEvent.ACTION_MOVE] is fired when "When use move the finger on the screen"
         */

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> mLastTouchedPosition = event.x
            MotionEvent.ACTION_UP -> {
                mScrollStarted = false
                mScrollingListener.onScrollEnd()
            }
            MotionEvent.ACTION_MOVE -> {

                /**
                 * if [mLastTouchedPosition] > [event.x] then You are Scrolling Right to Left
                 * if [mLastTouchedPosition] < [event.x] then You are Scrolling Left to Right
                 */

                if (mLastTouchedPosition > event.x) rightToLeft()

                if (mLastTouchedPosition < event.x) leftToRight()


                val distance = event.x - mLastTouchedPosition
                if (distance != 0f) {
                    if (!mScrollStarted) {
                        mScrollStarted = true;
                        mScrollingListener.onScrollStart()
                    }
                    onScrollEvent(event, distance);
                }

            }
        }

        return true;
    }

    private fun leftToRight() {
        value = value.round(value, 4)

        if (value > min) {
            isEnabelReDraw = true
            value -= step
        } else {
            isEnabelReDraw = false
        }


    }

    private fun rightToLeft() {
        value = value.round(value, 4)

        if (value < max) {
            isEnabelReDraw = true
            value += step
        } else {
            isEnabelReDraw = false
        }

    }


    private fun onScrollEvent(event: MotionEvent, distance: Float) {
        if (isEnabelReDraw) {
            mTotalScrollDistance -= distance;
            postInvalidate();
            mLastTouchedPosition = event.x;
            mScrollingListener.onScroll(-distance, mTotalScrollDistance, value)
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.getClipBounds(mCanvasClipBounds);

        /**
         * [linesCount] means how many line will show in view here this count is 31
         */

        val linesCount = mCanvasClipBounds.width() / (mProgressLineWidth + mProgressLineMargin)
        val deltaX = mTotalScrollDistance % (mProgressLineMargin + mProgressLineWidth).toFloat()

        for (i in 0 until linesCount) {
            /**
             * This When Part is for ProgressLine Opacity
             */
            when {
                i < linesCount / 4 -> {
                    mProgressLinePaint!!.alpha = (255 * (i / (linesCount / 4).toFloat())).toInt()
                }
                i > linesCount * 3 / 4 -> {
                    mProgressLinePaint!!.alpha =
                        (255 * ((linesCount - i) / (linesCount / 4).toFloat())).toInt()
                }
                else -> {
                    mProgressLinePaint!!.alpha = 255
                }
            }

            /**This is for draw progress-line*/

            canvas!!.drawLine(
                -deltaX + mCanvasClipBounds.left + i * (mProgressLineWidth + mProgressLineMargin),
                mCanvasClipBounds.centerY() - mProgressLineHeight / 4.0f,
                -deltaX + mCanvasClipBounds.left + i * (mProgressLineWidth + mProgressLineMargin),
                mCanvasClipBounds.centerY() + mProgressLineHeight / 4.0f, mProgressLinePaint!!
            )
        }
        /**This is for draw mid-Line*/
        canvas!!.drawLine(
            mCanvasClipBounds.centerX().toFloat(),
            mCanvasClipBounds.centerY() - mProgressLineHeight / 2.0f,
            mCanvasClipBounds.centerX().toFloat(),
            mCanvasClipBounds.centerY() + mProgressLineHeight / 2.0f,
            mProgressMiddleLinePaint!!
        )


    }


    interface ScrollingListener {
        fun onScrollStart()

        fun onScroll(delta: Float, totalDistance: Float, currentVal: Float)

        fun onScrollEnd()
    }


    @SuppressLint("ResourceAsColor")
    private fun init(attrs: AttributeSet?) {
        /**
         * [mMiddleLineColor] is for Middle Line Color for RulerView
         *
         * [mProgressLineColor] is for Progress Line Color for RulerView
         * [mProgressLineWidth] is for width of Progress-Line
         * [mProgressLineHeight] is for height of Progress-Line
         * [mProgressLineMargin] is for margin of Progress-Line
         * [mProgressLinePaint] is for Paint Object of Progress-Line
         *
         */


        attrs?.let {

            /** Here we are getting the attributes which is write in XML */
            val attributes = context.obtainStyledAttributes(it, R.styleable.RulerView)

            /** Here we are setting the value inside [mMiddleLineColor] and [mProgressLineColor] */
            try {

                /** attributes.getColor(___,___) is a method
                 * where 1st parameter of that method is given in XML through [app:rulerMidlineColor] and
                 * 2nd parameter is default color it means in-case if you do not give color through XML
                 * then it automatically retrieve [R.color.ruler_view_color_widget_rotate_mid_line]
                 */
                mMiddleLineColor = attributes.getColor(
                    R.styleable.RulerView_rulerMidlineColor,
                    getColor(R.color.ruler_view_color_widget_rotate_mid_line)
                )

                /** attributes.getColor(___,___) is a method
                 * where 1st parameter of that method is given in XML through [app:rulerStickColor] and
                 * 2nd parameter is default color it means in-case if you do not give color through XML
                 * then it automatically retrieve [R.color.ruler_view_color_progress_wheel_line]
                 */

                mProgressLineColor = attributes.getColor(
                    R.styleable.RulerView_rulerStickColor,
                    getColor(R.color.ruler_view_color_progress_wheel_line)
                )

            } finally {
                attributes.recycle()
            }

        }


        mProgressLineWidth =
            context.resources.getDimensionPixelSize(R.dimen.ucrop_width_horizontal_wheel_progress_line)
        mProgressLineHeight =
            context.resources.getDimensionPixelSize(R.dimen.ucrop_height_horizontal_wheel_progress_line)
        mProgressLineMargin =
            context.resources.getDimensionPixelSize(R.dimen.ucrop_margin_horizontal_wheel_progress_line)

        mProgressLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = mProgressLineWidth.toFloat()
            color = mProgressLineColor

        }

        mProgressMiddleLinePaint = Paint(mProgressLinePaint).apply {
            color = mMiddleLineColor
            strokeCap = Paint.Cap.ROUND
            strokeWidth =
                context.resources.getDimensionPixelSize(R.dimen.ucrop_width_middle_wheel_progress_line)
                    .toFloat()
        }


    }


    private fun getColor(colorId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(colorId, context.theme)
        } else {
            resources.getColor(colorId)
        }
    }


    private fun Float.round(
        number: Float,
        afterPointDecimal: Int
    ): Float {

        return String.format("%.${afterPointDecimal}f", number).toFloat()
    }

}