package com.daemon.won.keyboard.car;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;

/**
 * 自定义车牌输入框
 * View的层级分为上下两层，上层为TextView的方框显示单个字符，下层为EditText，接收输入
 */
public class CarNumView extends RelativeLayout {
    private static final float DEFAULT_SIZE = 50.0f;

    //显示在上层的单个字符方框TextView数组
    private TextView[] mTextViewArray;
    //下层EditText，接收输入
    private CarNumEditText mEditText;
    //TextView的容器
    private LinearLayout mContainerText;
    //输入框数量
    private int mBoxCnt = 8;
    //输入框的宽度
    private int mBoxWidth;
    //输入框的高度
    private int mBoxHeight;
    //输入框分割线
    private Drawable mEtDividerDrawable;
    //输入框文字颜色
    private int mTextColor;
    //输入框文字大小
    private int mTextSize;
    //输入框获取焦点时背景
    private Drawable mBgDrawableFocus;
    //输入框没有焦点时背景
    private Drawable mBgDrawableNormal;


    public CarNumView(Context context) {
        this(context, null);
    }

    public CarNumView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarNumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.won_layout_car_num, this);
        mContainerText = findViewById(R.id.container_car_num);
        mEditText = findViewById(R.id.et_car_num);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CarNumView, defStyleAttr, 0);
        mBoxWidth = typedArray.getDimensionPixelSize(R.styleable.CarNumView_box_width, dp2px(context, DEFAULT_SIZE));
        mBoxHeight = typedArray.getDimensionPixelSize(R.styleable.CarNumView_box_height, dp2px(context, DEFAULT_SIZE));
        mEtDividerDrawable = typedArray.getDrawable(R.styleable.CarNumView_box_divider_drawable);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.CarNumView_box_text_size, sp2px(context, 16));
        mTextColor = typedArray.getColor(R.styleable.CarNumView_box_text_color, Color.BLACK);
        mBgDrawableFocus = typedArray.getDrawable(R.styleable.CarNumView_box_bg_focus);
        mBgDrawableNormal = typedArray.getDrawable(R.styleable.CarNumView_box_bg_normal);
        //释放资源
        typedArray.recycle();
        Resources resources = context.getResources();
        //当xml中未配置时 这里进行初始配置默认图片
        if (mEtDividerDrawable == null) {
            mEtDividerDrawable = ResourcesCompat.getDrawable(resources, R.drawable.won_shape_divider_car_num, null);
        }
        if (mBgDrawableFocus == null) {
            mBgDrawableFocus = ResourcesCompat.getDrawable(resources, R.drawable.won_shape_box_bg_focus, null);
        }
        if (mBgDrawableNormal == null) {
            mBgDrawableNormal = ResourcesCompat.getDrawable(resources, R.drawable.won_shape_box_bg_normal, null);
        }
        initUI();
    }

    private void initUI() {
        Context context = getContext();
        //将光标隐藏
        mEditText.setCursorVisible(false);
        //最大输入长度
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mBoxCnt)});
        //设置分割线的宽度
        if (mEtDividerDrawable != null) {
            mEtDividerDrawable.setBounds(0, 0, mEtDividerDrawable.getMinimumWidth(), mEtDividerDrawable.getMinimumHeight());
            mContainerText.setDividerDrawable(mEtDividerDrawable);
        }
        mTextViewArray = new TextView[mBoxCnt];
        for (int i = 0; i < mTextViewArray.length; i++) {
            TextView textView = new TextView(context);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            textView.setTextColor(mTextColor);
            textView.setWidth(mBoxWidth);
            textView.setHeight(mBoxHeight);
            if (i == 0) {
                textView.setBackground(mBgDrawableFocus);
            } else {
                textView.setBackground(mBgDrawableNormal);
            }
            textView.setGravity(Gravity.CENTER);
            textView.setFocusable(false);
            mTextViewArray[i] = textView;
        }
        for(int i = 0; i < mTextViewArray.length; i++) {
            TextView tv = mTextViewArray[i];
            mContainerText.addView(tv);
            //车牌第二位后面的圆点分隔符
            if(i == 1) {
                int pointSize = dp2px(context, 8);
                int radius = dp2px(context, 2);
                PwdTextView pointView = new PwdTextView(context);
                pointView.setWidth(pointSize);
                pointView.setHeight(pointSize);
                pointView.setPointColor(Color.parseColor("#aeaeae"));
                pointView.drawPwd(radius);
                mContainerText.addView(pointView);
            }
        }
        mEditText.setKeyboardListener(this::onTextChange);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mHeightMeasureSpec = heightMeasureSpec;
        int heightMode = MeasureSpec.getMode(mHeightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            mHeightMeasureSpec = MeasureSpec.makeMeasureSpec(dp2px(getContext(), DEFAULT_SIZE), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, mHeightMeasureSpec);
    }

    private void onTextChange(String newInput) {
        int size = newInput.length();
        for (int i = 0; i < mTextViewArray.length; i++) {
            TextView tv = mTextViewArray[i];
            //设置方框样式，下个输入焦点特殊样式
            if(i == size) {
                tv.setBackground(mBgDrawableFocus);
            } else {
                tv.setBackground(mBgDrawableNormal);
            }
            //设置单个的文本
            if(i < size) {
                tv.setText(String.valueOf(newInput.charAt(i)));
            } else {
                tv.setText("");
            }
        }
    }

    /** 获取输入文本 */
    public String getInputContent() {
        StringBuilder sb = new StringBuilder();
        for (TextView tv : mTextViewArray) {
            sb.append(tv.getText().toString().trim());
        }
        return sb.toString();
    }

    /** 删除输入内容 */
    public void clearInputContent() {
        for (int i = 0; i < mTextViewArray.length; i++) {
            if (i == 0) {
                mTextViewArray[i].setBackground(mBgDrawableFocus);
            } else {
                mTextViewArray[i].setBackground(mBgDrawableNormal);
            }
            mTextViewArray[i].setText("");
        }
    }

    /** 获取输入的EditText */
    public CarNumEditText getEditText() {
        return mEditText;
    }

    public int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }

    public int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, context.getResources().getDisplayMetrics());
    }

}
