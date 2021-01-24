package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import ru.skillbranch.devintensive.R
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr:Int = 0
):ImageView(context, attrs, defStyleAttr)
{
    private var borderWidth:Float = 0f
    private var borderColor:Int = 0
    private var bgColor:Int = 0
    private var initials:String? = null
    init {
        scaleType = ScaleType.CENTER_CROP
        if(attrs!=null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth, 2.0f)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, Color.WHITE)
            bgColor = a.getColor(R.styleable.CircleImageView_cv_backgroundColor, Color.WHITE)
            initials = a.getString(R.styleable.CircleImageView_cv_initials)
            a.recycle()
        }
    }
    fun getBorderWidth():Int{
        return pxToDp(borderWidth).toInt()
    }
    fun setBorderWidth(width:Int){
        borderWidth = dpToPx(width.toFloat())
    }
    fun getBorderColor():Int{
        return borderColor
    }
    fun setBorderColor (hex:String){
        borderColor = Color.parseColor(hex)
    }

    fun setBorderColor(colorId:Int){
        borderColor = resources.getColor(colorId, context.theme)
    }

    override fun setBackgroundColor(color: Int) {
        bgColor = color
    }

    override fun onDraw(canvas: Canvas?) {
        var dr = drawable
        if(dr == null) dr = stringToBitmap(initials)
        //super.onDraw(canvas)
        val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val pDst = Paint()
        val pSrc = Paint()
        val size = min(height,width)
        val pBorder = Paint(Paint.ANTI_ALIAS_FLAG)
        pSrc.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        val bitmap = createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val bitMaskCanvas = Canvas(bitmap)
        pDst.color = bgColor
        pDst.style = Paint.Style.FILL
        canvas?.drawCircle(size.toFloat()/2, size.toFloat()/2, size.toFloat()/2-borderWidth/2, pDst)

        maskPaint.style = Paint.Style.FILL_AND_STROKE
        maskPaint.color = Color.BLACK
        bitMaskCanvas.drawCircle(size.toFloat()/2, size.toFloat()/2, size.toFloat()/2, maskPaint)

        canvas?.drawBitmap(dr.toBitmap(size,size, Bitmap.Config.ARGB_8888), 0f, 0f, pDst)//dst


        canvas?.drawBitmap(bitmap, 0f, 0f, pSrc)//src
        pBorder.style = Paint.Style.STROKE
        pBorder.strokeWidth = borderWidth.toFloat()
        pBorder.color = borderColor
        canvas?.drawCircle(size.toFloat()/2, size.toFloat()/2, size.toFloat()/2-borderWidth/2, pBorder)
    }
    fun stringToBitmap(s:String?):BitmapDrawable{
        val paint = Paint()
        val bitmap = createBitmap(500, 500, Bitmap.Config.ARGB_8888)
        val bitmapCanvas = Canvas(bitmap)
        paint.textSize = 250f
        paint.textAlign = Paint.Align.CENTER
        paint.color =Color.WHITE
        paint.style = Paint.Style.FILL_AND_STROKE
        bitmapCanvas.drawText(s ?: "", 250f, 210+paint.textSize/2, paint)
        return bitmap.toDrawable(resources)
    }
    fun pxToDp(px:Float):Float{
        return px*160f/resources.displayMetrics.densityDpi
    }
    fun dpToPx(dp:Float):Float{
        return dp*resources.displayMetrics.densityDpi/160f
    }
}