package com.daemon.won.keyboard.car;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义车牌键盘
 */
public class CarNumKeyboardView extends KeyboardView {
    private Paint mPaint;
    private Rect mBgRect;
    private Rect mBoundsRect;
    private Rect mIconRect;
    private Rect mTextRect;
    //数字键是否激活
    private boolean isNumberActive = true;
    //特殊键
    private List<Integer> mSpecialKeyList;
    //功能键
    private List<Integer> mFuncKeyList;
    //按键左右间距3dp，上下间距6dp
    private int xGapPx;
    private int yGapPx;
    //按键图标间距
    private int iconXGapPx;
    private int iconYGapPx;
    //省份文字大小
    private int mProvinceTextSize;
    //功能键文字大小
    private int mFuncTextSize;
    //常规文字大小
    private int mTextSize;

    /** 省份简称键盘 */
    private CarNumKeyboard mProvincesKeyBoard;

    /** 数字、字母的键盘 */
    private CarNumKeyboard mLettersKeyBoard;

    public static CarNumKeyboardView newInstance(Context context) {
        return new CarNumKeyboardView(context);
    }

    public CarNumKeyboardView(Context context) {
        super(context, null);
        init(context);
    }

    public CarNumKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CarNumKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mBgRect = new Rect();
        mBoundsRect = new Rect();
        mIconRect = new Rect();
        mTextRect = new Rect();
        mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        mPaint.setTypeface(font);
        mPaint.setAntiAlias(true);

        mSpecialKeyList = Arrays.asList(Keyboard.KEYCODE_CANCEL, Keyboard.KEYCODE_DELETE,
                "ABC".hashCode(), "中文".hashCode());
        mFuncKeyList = Arrays.asList("ABC".hashCode(), "中文".hashCode());

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 22, metrics);
        mFuncTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 17, metrics);
        mProvinceTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 19, metrics);

        xGapPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, metrics);
        yGapPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, metrics);
        iconXGapPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, metrics);
        iconYGapPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, metrics);
        //底部边距设置大一点
        int bottomPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, metrics);
        setPadding(0, 0, 0, bottomPadding);

        mProvincesKeyBoard = new CarNumKeyboard(context, R.xml.keyboard_car_num_provinces);
        mLettersKeyBoard = new CarNumKeyboard(context, R.xml.keyboard_car_num_letters);
        setKeyboard(mProvincesKeyBoard);
        setPreviewEnabled(false);
    }

    /** 切换到省份键盘 */
    public void switchToProvinces() {
        if (getKeyboard() == mProvincesKeyBoard) return;
        setKeyboard(mProvincesKeyBoard);
    }

    /** 切换到字母数字键盘 */
    public void switchToLetters() {
        if (getKeyboard() == mLettersKeyBoard) return;
        setKeyboard(mLettersKeyBoard);
    }

    /** 数字键是否启用 */
    public boolean isNumberActive() {
        return isNumberActive;
    }

    /** 启用/禁用数字键 */
    public void activeNumber(boolean active) {
        isNumberActive = active;
        List<Key> keys = mLettersKeyBoard.getKeys();
        if(keys == null || keys.isEmpty()) {
            return;
        }
        for (Key key : keys) {
            int code = key.codes[0];
            //0-9数字键
            if(code > 47 && code < 58 && key instanceof CarNumKey) {
                CarNumKey carNumKey = (CarNumKey) key;
                carNumKey.disable = !active;
            }
        }
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Keyboard keyboard = getKeyboard();
        if (keyboard == null) {
            return;
        }
        Context context = getContext();
        //绘制键盘背景
        getDrawingRect(mBgRect);
        mPaint.setColor(ContextCompat.getColor(context, R.color.won_keyboard_bg_color));
        canvas.drawRect(mBgRect, mPaint);

        List<Key> keys = keyboard.getKeys();
        if(keys == null || keys.isEmpty()) {
            return;
        }
        Resources resources = context.getResources();
        for (Key key : keys) {
            //绘制按键背景
            Drawable dr;
            if (mSpecialKeyList.contains(key.codes[0])) {
                dr = ResourcesCompat.getDrawable(resources, R.drawable.won_selector_btn_gray, null);
            } else {
                dr = ResourcesCompat.getDrawable(resources, R.drawable.won_selector_btn_white, null);
            }
            if(dr != null) {
                //按键按下状态
                int[] drawableState = key.getCurrentDrawableState();
                if (key.codes[0] != 0) {
                    dr.setState(drawableState);
                }
                mBoundsRect.set(key.x + xGapPx, key.y + yGapPx, key.x + key.width - xGapPx,
                        key.y + key.height - yGapPx);
                dr.setBounds(mBoundsRect);
                dr.draw(canvas);
            }
            //绘制文字
            if (key.label != null) {
                if (mFuncKeyList.contains(key.codes[0])) {
                    mPaint.setTextSize(mFuncTextSize);
                } else {
                    if(getKeyboard() == mProvincesKeyBoard) {
                        //当前若是省份键盘，把字调小一点
                        mPaint.setTextSize(mProvinceTextSize);
                    } else {
                        mPaint.setTextSize(mTextSize);
                    }
                }
                //按键文本常规的颜色
                int textColor = ContextCompat.getColor(context, R.color.won_keyboard_cell_text_color);
                //禁用按键的文本颜色
                if(key instanceof CarNumKey) {
                    CarNumKey carNumKey = (CarNumKey) key;
                    if(carNumKey.disable) {
                        textColor = ContextCompat.getColor(context, R.color.won_keyboard_cell_text_disable_color);
                    }
                }
                mPaint.setColor(textColor);
                mTextRect.set(key.x, key.y, key.x + key.width, key.y + key.height);
                Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
                int baseline = (mTextRect.top + mTextRect.bottom - fontMetrics.top - fontMetrics.bottom) / 2;
                canvas.drawText(key.label.toString(), mTextRect.centerX(), baseline, mPaint);
            }
            //绘制图标
            if (key.icon != null) {
                mIconRect.set(mBoundsRect.left + iconXGapPx, mBoundsRect.top + iconYGapPx,
                        mBoundsRect.right - iconXGapPx, mBoundsRect.bottom - iconYGapPx);
                key.icon.setBounds(mIconRect);
                key.icon.draw(canvas);
            }
        }
    }

}
