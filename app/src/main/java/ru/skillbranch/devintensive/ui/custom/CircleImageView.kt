package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toBitmap
import ru.skillbranch.devintensive.R

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr:Int = 0
):ImageView(context, attrs, defStyleAttr)
{
    private var borderWidth:Int = 0
    private var borderColor:Int = 0
    init {
        scaleType = ScaleType.CENTER_CROP
        if(attrs!=null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth, 2.0f).toInt()
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, Color.WHITE)
            a.recycle()
        }
    }
    fun getBorderWidth():Int{
        return borderWidth
    }
    fun setBorderWidth(width:Int){
        borderWidth = width
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

    override fun onDraw(canvas: Canvas?) {
        val dr = drawable

        //super.onDraw(canvas)

        val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val pDst = Paint()
        val pSrc = Paint()
        val pBorder = Paint()
        pSrc.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        val bitmap = createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val bitMaskCanvas = Canvas(bitmap)

        maskPaint.style = Paint.Style.FILL_AND_STROKE
        maskPaint.color = Color.BLUE
        bitMaskCanvas.drawCircle(width.toFloat()/2, height.toFloat()/2, height.toFloat()/2, maskPaint)

        canvas?.drawBitmap(dr.toBitmap(width,height, Bitmap.Config.ARGB_8888), 0f, 0f, pDst)//dst


        canvas?.drawBitmap(bitmap, 0f, 0f, pSrc)//src
        pBorder.style = Paint.Style.STROKE
        pBorder.strokeWidth = borderWidth.toFloat()
        pBorder.color = borderColor
        canvas?.drawCircle(width.toFloat()/2, height.toFloat()/2, height.toFloat()/2-borderWidth/2, pBorder)
    }

}