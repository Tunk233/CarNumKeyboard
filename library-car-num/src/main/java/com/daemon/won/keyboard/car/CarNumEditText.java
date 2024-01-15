package com.daemon.won.keyboard.car;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * 车牌号键盘的输入框 EditText
 */
public class CarNumEditText extends AppCompatEditText {
    private int mMaxCnt = 7;
    public IKeyboardListener mKeyboardListener;


    public CarNumEditText(Context context) {
        super(context);
    }

    public CarNumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CarNumEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setKeyboardListener(IKeyboardListener listener) {
        mKeyboardListener = listener;
    }

    public void setMaxCnt(int maxCnt) {
        mMaxCnt = maxCnt;
    }

    public int getMaxCnt() {
        return mMaxCnt;
    }
}
