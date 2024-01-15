package com.daemon.won.keyboard.car;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import androidx.annotation.NonNull;


/**
 * 输入框键盘绑定帮助类
 */
public class CarNumHelper {
    private static final String PROVINCES = "京津渝沪冀晋辽吉黑苏浙皖闽赣鲁豫鄂湘粤琼川贵云陕甘青蒙桂宁新藏使领警学港澳";
    private final CarNumEditText mEditText;
    private final IKeyboardListener mListener;
    private final CarNumKeyboardView mKeyboardView;

    public CarNumHelper(@NonNull CarNumEditText editText, IKeyboardListener listener) {
        mEditText = editText;
        mListener = listener;
        mKeyboardView = CarNumKeyboardView.newInstance(editText.getContext());
        init();
    }

    /** 为EditText绑定车牌号输入键盘，并设置自定义键盘的特殊键的功能 */
    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        mKeyboardView.setOnKeyboardActionListener(new OnKeyboardActionAdapter(mEditText) {
            @Override
            public void close() {
                hideCustomKeyboard();
            }

            @Override
            public boolean onKeyEvent(int primaryCode, int[] keyCodes) {
                String s = mEditText.getText().toString();
                if ("ABC".hashCode() == primaryCode) {
                    mKeyboardView.switchToLetters();
                    return true;
                } else if ("中文".hashCode() == primaryCode) {
                    mKeyboardView.switchToProvinces();
                    return true;
                } else {
                    if(!mKeyboardView.isNumberActive() && primaryCode >= 48 && primaryCode <= 57) {
                        //数字键禁用了
                        return true;
                    }
                    // 除功能键以外的键
                    if (primaryCode != Keyboard.KEYCODE_DELETE && s.length() >= mEditText.getMaxCnt()) {
                        //车牌号到最大长度了
                        return true;
                    }
                    if (primaryCode == Keyboard.KEYCODE_DELETE && s.length() > 1) {
                        mKeyboardView.switchToLetters();
                        return false;
                    }
                    if (PROVINCES.contains(String.valueOf((char) primaryCode))) {
                        if (s.length() == 0) {
                            mKeyboardView.switchToLetters();
                        }
                        return false;
                    }
                    return super.onKeyEvent(primaryCode, keyCodes);
                }
            }
        });
        mEditText.setInputType(InputType.TYPE_NULL);
        //监听输入框的焦点事件：获得焦点显示自定义键盘；失去焦点收起自定义键盘
        mEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                hideSysKeyboard(mEditText);
                showCustomKeyboard();
            } else {
                hideCustomKeyboard();
            }
        });
        //监听物理返回键，收起自定义键盘
        mEditText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                hideCustomKeyboard();
                return true;
            }
            return false;
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length == 0) {
                    //当没有文字的时候键盘切换回省份
                    mKeyboardView.switchToProvinces();
                } else if (length == 1) {
                    mKeyboardView.activeNumber(false);
                } else if (length == 2) {
                    mKeyboardView.activeNumber(true);
                }
                IKeyboardListener keyboardListener = mEditText.mKeyboardListener;
                if (keyboardListener != null) {
                    keyboardListener.onNewInput(s.toString());
                }
                if (mListener != null) {
                    mListener.onNewInput(s.toString());
                }
            }
        });
    }

    /** 展示自定义键盘 */
    public void showCustomKeyboard() {
        if (mEditText == null) return;
        Object obj = mEditText.getTag(R.id.keyboard);
        final PopupWindow keyboardWindow;
        if (obj == null) {
            keyboardWindow = new PopupWindow(mKeyboardView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            keyboardWindow.setAnimationStyle(R.style.BottomPopupAnim);
            mEditText.setTag(R.id.keyboard, keyboardWindow);
        } else {
            keyboardWindow = ((PopupWindow) obj);
        }
        if (keyboardWindow.isShowing()) return;
        keyboardWindow.setOutsideTouchable(false);
        mKeyboardView.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (keyboardWindow.isShowing()) {
                    hideCustomKeyboard();
                    return true;
                }
            }
            return false;
        });
        Window window = getWindow(mEditText);
        if (window == null) return;
        View decorView = window.getDecorView();
        if (decorView == null) return;
        if (!isActivityRunning(mEditText.getContext())) return;
        // 横竖屏切换时，Activity的onCreate可能还未执行完毕，还拿不到decorView
        decorView.post(() -> {
            // 解决底部被导航栏遮挡问题
            keyboardWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            keyboardWindow.showAtLocation(decorView, Gravity.BOTTOM, 0, 0);
            keyboardWindow.update();
        });
    }

    /** 隐藏自定义键盘 */
    public void hideCustomKeyboard() {
        if (mEditText == null) return;
        if (!isActivityRunning(mEditText.getContext())) return;
        mEditText.clearFocus();
        Object tag = mEditText.getTag(R.id.keyboard);
        if (tag == null) return;
        if (tag instanceof PopupWindow) {
            PopupWindow window = (PopupWindow) tag;
            if (window.isShowing()) {
                window.dismiss();
            }
        }
    }

    /** 隐藏系统输入法 */
    private static void hideSysKeyboard(EditText et) {
        IBinder windowToken = et.getWindowToken();
        if (windowToken != null) {
            InputMethodManager imm = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private static Window getWindow(EditText et) {
        Context context = et.getContext();
        if (context instanceof Activity) {
            return ((Activity) context).getWindow();
        } else {
            Log.e("CarNumHelper", "EditText must have a Context which is a Activity.");
            return null;
        }
    }

    private static boolean isActivityRunning(Context context) {
        if (context == null) return false;
        if (context instanceof Activity) {
            return !((Activity) context).isFinishing();
        } else {
            return false;
        }
    }
}
