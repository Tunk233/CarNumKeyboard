package com.daemon.won.keyboard.car;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 黑色实心小圆点TextView
 */
public class PwdTextView extends AppCompatTextView {
    private float mRadius;
    private boolean isPwdType;
    private final Paint mPaint;
    private int mPointColor = Color.BLACK;

    public PwdTextView(Context context) {
        this(context, null);
    }

    public PwdTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PwdTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setPointColor(int color) {
        mPointColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isPwdType) {
            //画一个黑色的圆
            mPaint.setColor(mPointColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mPaint);
        }
    }

    public void clearPwd() {
        isPwdType = false;
        invalidate();
    }

    public void drawPwd(float radius) {
        isPwdType = true;
        if (radius == 0) {
            mRadius = getWidth() / 4;
        } else {
            mRadius = radius;
        }
        invalidate();
    }

}
