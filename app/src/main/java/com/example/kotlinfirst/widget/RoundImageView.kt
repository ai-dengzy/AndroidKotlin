package com.example.kotlinfirst.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.example.kotlinfirst.R

class RoundImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : AppCompatImageView(context, attrs, defStyleAttr) {
  internal var width: Float = 0.toFloat()
  internal var height: Float = 0.toFloat()
  private val defaultRadius = 0
  private var radius: Int = 0
  private var leftTopRadius: Int = 0
  private var rightTopRadius: Int = 0
  private var rightBottomRadius: Int = 0
  private var leftBottomRadius: Int = 0

  constructor(context: Context) : this(context, null) {
    init(context, null)
  }

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
    init(context, attrs)
  }

  init {
    init(context, attrs)
  }

  private fun init(context: Context, attrs: AttributeSet?) {
    if (Build.VERSION.SDK_INT < 18) {
      setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }
    // 读取配置
    val array = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView)
    radius = array.getDimensionPixelOffset(R.styleable.RoundImageView_radius, defaultRadius)
    leftTopRadius = array.getDimensionPixelOffset(R.styleable.RoundImageView_left_top_radius, defaultRadius)
    rightTopRadius = array.getDimensionPixelOffset(R.styleable.RoundImageView_right_top_radius, defaultRadius)
    rightBottomRadius = array.getDimensionPixelOffset(R.styleable.RoundImageView_right_bottom_radius, defaultRadius)
    leftBottomRadius = array.getDimensionPixelOffset(R.styleable.RoundImageView_left_bottom_radius, defaultRadius)
    //如果四个角的值没有设置，那么就使用通用的radius的值。
    if (defaultRadius == leftTopRadius) {
      leftTopRadius = radius
    }
    if (defaultRadius == rightTopRadius) {
      rightTopRadius = radius
    }
    if (defaultRadius == rightBottomRadius) {
      rightBottomRadius = radius
    }
    if (defaultRadius == leftBottomRadius) {
      leftBottomRadius = radius
    }

  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    width = getWidth().toFloat()
    height = getHeight().toFloat()
  }

  fun setRoundRadius(leftTopRadius: Int, leftBottomRadius: Int, rightTopRadius: Int, rightBottomRadius: Int) {
    this.leftTopRadius = leftTopRadius
    this.leftBottomRadius = leftBottomRadius
    this.rightTopRadius = rightTopRadius
    this.rightBottomRadius = rightBottomRadius
  }

  override fun onDraw(canvas: Canvas) {
    //这里做下判断，只有图片的宽高大于设置的圆角距离的时候才进行裁剪
    val maxLeft = Math.max(leftTopRadius, leftBottomRadius)
    val maxRight = Math.max(rightTopRadius, rightBottomRadius)
    val minWidth = maxLeft + maxRight
    val maxTop = Math.max(leftTopRadius, rightTopRadius)
    val maxBottom = Math.max(leftBottomRadius, rightBottomRadius)
    val minHeight = maxTop + maxBottom
    if (width >= minWidth && height > minHeight) {
      val path = Path()
      //四个角：右上，右下，左下，左上
      path.moveTo(leftTopRadius.toFloat(), 0f)
      path.lineTo(width - rightTopRadius, 0f)
      path.quadTo(width, 0f, width, rightTopRadius.toFloat())
      path.lineTo(width, height - rightBottomRadius)
      path.quadTo(width, height, width - rightBottomRadius, height)
      path.lineTo(leftBottomRadius.toFloat(), height)
      path.quadTo(0f, height, 0f, height - leftBottomRadius)
      path.lineTo(0f, leftTopRadius.toFloat())
      path.quadTo(0f, 0f, leftTopRadius.toFloat(), 0f)
      canvas.clipPath(path)
    }
    super.onDraw(canvas)

  }

}