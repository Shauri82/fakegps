package com.shauri.fakegps

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat


class CircleSelectorView : View {
    val paint = Paint()
    val paintCircle = Paint()
    val bitmapPaint = Paint()
    var arc = 90.0
    val radius = context.resources.getDimension(R.dimen.radius);
    var mapBitmap: Bitmap? = null

    constructor(ctx: Context) : super(ctx) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    fun init() {
        paint.setColor(Color.BLACK)
        paint.setStrokeWidth(5f);
        paintCircle.isAntiAlias = true
        paintCircle.color = ContextCompat.getColor(context, R.color.colorPrimary)
        paintCircle.style = Paint.Style.STROKE
        paintCircle.strokeWidth = 3f
        // LayoutInflater.from(context).inflate(R.layout.view_circle_select, this)
        setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event != null) {
                    updateCurrentAngle(event.x, event.y)
                    invalidate()
                }
                return true;
            }

        })
       // bitmapPaint.alpha = 70
        mapBitmap = BitmapFactory.decodeResource(resources, R.drawable.map)?.getCircularBitmap()
    }

    private fun updateCurrentAngle(x: Float, y: Float) {
        val pointX: Float = x - measuredWidth / 2f
        val pointY: Float = y - measuredHeight / 2f
        var tan_x: Float
        var tan_y: Float
        var atan: Double

        //01: First quadrant - upper right corner area
        if (pointX >= 0 && pointY <= 0) {
            tan_x = pointX * -1
            tan_y = pointY * -1
            atan = Math.atan((tan_x / tan_y).toDouble())
            arc = Math.toDegrees(atan).toInt() + 90.0
        }

        //02: Second quadrant - upper left corner area
        if (pointX <= 0 && pointY <= 0) {
            tan_x = pointX
            tan_y = -pointY
            atan = Math.atan((tan_y / tan_x).toDouble())
            arc = Math.toDegrees(atan).toInt() - 180.0
        }

        //03: Third quadrant - lower left corner area
        if (pointX <= 0 && pointY >= 0) {
            tan_x = pointX
            tan_y = pointY
            atan = Math.atan((tan_x / tan_y).toDouble())
            if (Math.toDegrees(atan).toInt() >= 90.0) {
                arc = Math.toDegrees(atan).toInt() - (90.0)
            } else {
                arc = Math.toDegrees(atan).toInt() + 270.0
            }
        }

        //04: Fourth quadrant - lower right corner area
        if (pointX >= 0 && pointY >= 0) {
            tan_x = pointX
            tan_y = pointY
            atan = Math.atan((tan_y / tan_x).toDouble())
            arc = 360.0 - Math.toDegrees(atan).toInt()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val xDelta = (radius) * Math.cos(-Math.toRadians(arc)).toFloat()
        val xDeltaA = (radius) * Math.cos(-Math.toRadians(arc+5)).toFloat()
        val yDeltaA = (radius) * Math.sin(-Math.toRadians(arc+5)).toFloat()
        val xDeltaB = (radius) * Math.cos(-Math.toRadians(arc-5)).toFloat()
        val yDeltaB = (radius) * Math.sin(-Math.toRadians(arc-5)).toFloat()
        val yDelta = radius * Math.sin(-Math.toRadians(arc)).toFloat()
        val xCenter = measuredWidth / 2f
        val yCenter = measuredHeight / 2f
        if (mapBitmap != null) {
            canvas?.drawBitmap(mapBitmap!!, xCenter-radius, yCenter-radius, bitmapPaint)
        }
        canvas?.drawCircle(measuredWidth / 2f, measuredHeight / 2f, radius / 20f, paint);
        canvas?.drawCircle(measuredWidth / 2f, measuredHeight / 2f, radius, paintCircle);
        canvas?.drawLine(xCenter, yCenter, xCenter + xDelta/2, yCenter + yDelta/2, paint);
        canvas?.drawLine(xCenter + xDelta/2, yCenter + yDelta/2, xCenter + xDeltaA*0.45f, yCenter + yDeltaA*0.45f, paint);
        canvas?.drawLine(xCenter + xDelta/2, yCenter + yDelta/2, xCenter + xDeltaB*0.45f, yCenter + yDeltaB*0.45f, paint);
        canvas?.drawCircle(xCenter + xDelta, yCenter + yDelta, 20f, paint)
    }


    fun Bitmap.getCircularBitmap(config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap {
        // circle configuration


        val circlePaint = Paint().apply { isAntiAlias = true }
        val circleRadius = radius

        // output bitmap
        val outputBitmapPaint =
            Paint(circlePaint).apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN) }
        val outputBounds = Rect(0, 0, width, height)
        val output = Bitmap.createBitmap(width, height, config)

        return Canvas(output).run {
            drawCircle(circleRadius, circleRadius, circleRadius, circlePaint)
            drawBitmap(this@getCircularBitmap, outputBounds, outputBounds, outputBitmapPaint)
            output
        }
    }

}