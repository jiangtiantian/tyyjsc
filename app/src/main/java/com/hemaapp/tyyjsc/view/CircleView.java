package com.hemaapp.tyyjsc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class CircleView extends View {

	private float ratio = 1.0f;

	private Paint paint, textpaint, p;
	private RectF area;
	private int value = 0;

	private Context context = null;// 上下文
	private DisplayMetrics dm = null;
	private int baseLine = 0;

	public CircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public CircleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleView(Context context) {
		this(context, null);
	}

	public void setProgress(int value) {
		this.value = value;
		invalidate();
	}

	public void init() {

		WindowManager window = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		dm = new DisplayMetrics();
		window.getDefaultDisplay().getMetrics(dm);

		// 动态画笔
		paint = new Paint();
		paint.setStrokeWidth(5f);
		paint.setColor(Color.RED);
		paint.setStyle(Style.STROKE);//画边框
		paint.setAntiAlias(true);

		p = new Paint();
		p.setStrokeWidth(5f);
		p.setColor(Color.GRAY);
		p.setAntiAlias(true);

		textpaint = new Paint();
		textpaint.setTextSize(50f);
		textpaint.setColor(Color.RED);
		textpaint.setTextAlign(Paint.Align.CENTER);

		area = new RectF((int) (dm.widthPixels * 0.3),
				(int) (dm.widthPixels * 0.2), (int) (dm.widthPixels * 0.7),
				(int) (dm.widthPixels * 0.6));


		paint.setTextAlign(Paint.Align.CENTER);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);// 画布背景，即控件背景为透明
		canvas.drawOval(area, p);// 在画布上画背景为灰色的圆形
		canvas.drawArc(area, 120, 360 * value / 100, false, paint);// 在圆形外围画红色边框
		FontMetricsInt fontMetrics = textpaint.getFontMetricsInt();
		baseLine = (int) (area.bottom + area.top - fontMetrics.bottom - fontMetrics.top) / 2;
		canvas.drawText(value + "%", area.centerX(), baseLine,
				textpaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// 父容器传过来的宽度方向上的模式
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		// 父容器传过来的高度方向上的模式
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		// 父容器传过来的宽度的值
		int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
				- getPaddingRight();
		// 父容器传过来的高度的值
		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft()
				- getPaddingRight();

		if (widthMode == MeasureSpec.EXACTLY
				&& heightMode != MeasureSpec.EXACTLY && ratio != 0.0f) {
			// 判断条件为，宽度模式为Exactly，也就是填充父窗体或者是指定宽度；
			// 且高度模式不是Exaclty，代表设置的既不是fill_parent也不是具体的值，于是需要具体测量
			// 且图片的宽高比已经赋值完毕，不再是0.0f
			// 表示宽度确定，要测量高度
			height = (int) (width / ratio + 0.5f);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);
		} else if (widthMode != MeasureSpec.EXACTLY
				&& heightMode == MeasureSpec.EXACTLY && ratio != 0.0f) {
			// 判断条件跟上面的相反，宽度方向和高度方向的条件互换
			// 表示高度确定，要测量宽度
			width = (int) (height * ratio + 0.5f);

			widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
					MeasureSpec.EXACTLY);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
