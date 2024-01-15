package com.daemon.won.demo.keyboard.car;

import android.content.Context;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

@Deprecated
public class MyToolBar extends Toolbar {
    private TextView centerTitleTv;

    public MyToolBar(Context context) {
        super(context);
        init();
    }

    public MyToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addView(centerTitleTv = createCenterTitle());
        setPadding(0, getStatusBarHeight(getContext()), 0, 0);
    }

    public MyToolBar setNavIcon(int resId) {
        setNavigationIcon(resId);
        return this;
    }

    public MyToolBar setNavigationListener(OnClickListener onClickListener) {
        setNavigationOnClickListener(onClickListener);
        return this;
    }

    public MyToolBar setCenterTitle(String title, int textSize, int colorRes) {
        centerTitleTv.setText(TextUtils.isEmpty(title) ? "" : title);
        centerTitleTv.setTextSize(2, (float)textSize);
        centerTitleTv.setTextColor(getResources().getColor(colorRes));
        centerTitleTv.setGravity(17);
        return this;
    }

    private TextView createCenterTitle() {
        TextView centerTitleTv = new TextView(getContext());
        centerTitleTv.setTextSize(2, 18.0F);
        //centerTitleTv.setTextColor(getResources().getColor(R.color.title_text_color));
        LayoutParams params = new LayoutParams(-2, -1);
        params.gravity = 17;
        centerTitleTv.setLayoutParams(params);
        centerTitleTv.setSingleLine();
        centerTitleTv.setEllipsize(TruncateAt.END);
        centerTitleTv.setMaxWidth(getScreenWidth(getContext()) - dp2px(getContext(), 130.0F));
        return centerTitleTv;
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    private int dp2px(Context context, float dpVal) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }
}
