package ru.skillbranch.devintensive.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.core.animation.doOnRepeat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.dpToPx
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.truncate

class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {
    companion object{
        private const val DEFAULT_BORDER_WIDTH = 2
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_SIZE = 40
        private val bgColors = arrayOf(
            Color.parseColor("#7BC862"),
            Color.parseColor("#E17076"),
            Color.parseColor("#FAA774"),
            Color.parseColor("#6EC9CB"),
            Color.parseColor("#65AADD"),
            Color.parseColor("#A695E7"),
            Color.parseColor("#EE7AAE"),
            Color.parseColor("#2196F3")
        )
    }
    @Px
    var borderWidth:Float = context.dpToPx(DEFAULT_BORDER_WIDTH)

    @ColorInt
    private var borderColor: Int = Color.WHITE
    private var initials: String = "??"

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val avatarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val initialsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val viewRect = Rect()
    private val borderRect = Rect()
    private var size = 0

    private var isAvatarMode = true
    private lateinit var srcBm:Bitmap
    init {
        if(attrs!=null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView)
            borderWidth = a.getDimension(R.styleable.AvatarImageView_aiv_borderWidth,
                context.dpToPx(DEFAULT_BORDER_WIDTH))
            borderColor = a.getColor(R.styleable.AvatarImageView_aiv_borderColor, DEFAULT_BORDER_COLOR)
            initials = a.getString(R.styleable.AvatarImageView_aiv_initials) ?: "??"
            a.recycle()
        }
        scaleType = ScaleType.CENTER_CROP
        setup()
        setOnLongClickListener{
            handleLongClick()
        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        if (isAvatarMode) prepareShader(width, height)
        Log.d("M_AvatarImageView", "setImageBitmap")
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (isAvatarMode) prepareShader(width, height)
        Log.d("M_AvatarImageView", "setImageDrawable")
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        if (isAvatarMode) prepareShader(width, height)
        Log.d("M_AvatarImageView", "setImageResource")
    }

    fun setInitials(initials: String){
        Log.d("M_AvatarImageView", "setInitials: $initials")
        this.initials = initials
        if (!isAvatarMode) invalidate()
    }
    fun setBorderColor(@ColorInt color: Int){
        Log.d("M_AvatarImageView", "setBorderColor: $color")
        borderColor = color
        borderPaint.color = borderColor
        invalidate()
    }

    fun setBorderWidth(@Dimension width: Int){
        Log.d("M_AvatarImageView", "setBorderWidth: $width")
        borderWidth = context.dpToPx(width)
        borderPaint.strokeWidth = borderWidth
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d("M_AvatarImageView", "onMeasure")
        val initSize = resolveDefaultSize(widthMeasureSpec)
        setMeasuredDimension(max(initSize, size), max(initSize, size))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d("M_AvatarImageView", "onSizeChanged")
        if (w==0) return
        with(viewRect){
            left = 0
            top = 0
        right = w
        bottom = h
        }
        with(borderRect){
            left = 0
            top = 0
            right = w
            bottom = h
        }
        prepareShader(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        //super.onDraw(canvas)
        Log.d("M_AvatarImageView", "onDraw")
        if (drawable != null && isAvatarMode){
            drawAvatar(canvas)
        }else{
            drawInitials(canvas)
        }
        val half = (borderWidth/2).toInt()
        borderRect.set(viewRect)
        borderRect.inset(half, half)
        canvas?.drawOval(borderRect.toRectF(), borderPaint)
    }

    override fun onSaveInstanceState(): Parcelable? {
        Log.d("M_AvatarImageView", "onSaveInstanceState $id")
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.isAvatarMode = isAvatarMode
        savedState.borderWidth = borderWidth
        savedState.borderColor = borderColor
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        Log.d("M_AvatarImageView", "onRestoreInstanceState $id")
        if (state is SavedState) {
            super.onRestoreInstanceState(state)
            isAvatarMode = state.isAvatarMode
            borderWidth = state.borderWidth
            borderColor = state.borderColor
            with(borderPaint){
                color = borderColor
                strokeWidth = borderWidth
            }
        }else {
            super.onRestoreInstanceState(state)
        }

    }

    private fun setup() {
        with(borderPaint){
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
            color = borderColor
        }
    }

    private fun prepareShader(w:Int, h:Int){
        if (w == 0 || drawable == null) return
        srcBm = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)
        avatarPaint.shader = BitmapShader(srcBm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

    }

    private fun resolveDefaultSize(spec: Int): Int {
        return when (MeasureSpec.getMode(spec)) {
            MeasureSpec.UNSPECIFIED -> context.dpToPx(DEFAULT_SIZE).toInt()
            MeasureSpec.AT_MOST -> MeasureSpec.getSize(spec)
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(spec)
            else -> MeasureSpec.getSize(spec)
        }
    }
    private fun drawAvatar(canvas:Canvas?){
        canvas?.drawOval(viewRect.toRectF(), avatarPaint)
    }
    private fun drawInitials(canvas: Canvas?){
        initialsPaint.color = initialsToColor(initials)
        canvas?.drawOval(viewRect.toRectF(), initialsPaint)
        with(initialsPaint){
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = height * 0.33f
        }
        val offsetY = (initialsPaint.descent() + initialsPaint.ascent())/2
        canvas?.drawText(initials, viewRect.exactCenterX(), viewRect.exactCenterY() - offsetY, initialsPaint)
    }
    private fun handleLongClick():Boolean{
        val va = ValueAnimator.ofInt(width, (width*1.2f).toInt()).apply {
            duration = 300
            interpolator = LinearInterpolator()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 1
        }
        va.addUpdateListener {
            size = it.animatedValue as Int
            requestLayout()
        }
        va.doOnRepeat { toggleMode() }
        va.start()
        return true
    }



    private fun toggleMode() {
        isAvatarMode = !isAvatarMode
        invalidate()
    }

    private fun initialsToColor(letters:String):Int{
        val b:Byte = letters[0].toByte()
        val len: Int = bgColors.size
        val d:Double = b/len.toDouble()
        val index: Int = ((d - truncate(d))*len).toInt()
        return bgColors[index]
    }

    private class SavedState : BaseSavedState, Parcelable{
        var isAvatarMode: Boolean = true
        var borderWidth:Float = 0f
        var borderColor:Int = 0
        constructor(superState: Parcelable?): super(superState)

        constructor(src: Parcel):super(src){
            isAvatarMode = src.readInt() == 1
            borderWidth = src.readFloat()
            borderColor = src.readInt()
        }

        override fun writeToParcel(dst: Parcel?, flags: Int) {
            super.writeToParcel(dst, flags)
            dst?.writeInt(if(isAvatarMode)1 else 0)
            dst?.writeFloat(borderWidth)
            dst?.writeInt(borderColor)
        }

        override fun describeContents()=0

        companion object CREATOR : Parcelable.Creator<SavedState>{
            override fun createFromParcel(parcel: Parcel) = SavedState(parcel)

            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
}