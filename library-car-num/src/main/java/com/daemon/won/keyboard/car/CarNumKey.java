package com.daemon.won.keyboard.car;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;

/**
 * 为了给键增加一个禁用的状态，重写getCurrentDrawableState方法
 */
public class CarNumKey extends Keyboard.Key {
    //是否禁用
    public boolean disable;

    private final static int[] KEY_STATE_NORMAL_ON = {
            android.R.attr.state_checkable,
            android.R.attr.state_checked
    };

    private final static int[] KEY_STATE_PRESSED_ON = {
            android.R.attr.state_pressed,
            android.R.attr.state_checkable,
            android.R.attr.state_checked
    };

    private final static int[] KEY_STATE_NORMAL_OFF = {
            android.R.attr.state_checkable
    };

    private final static int[] KEY_STATE_PRESSED_OFF = {
            android.R.attr.state_pressed,
            android.R.attr.state_checkable
    };

    private final static int[] KEY_STATE_NORMAL = {
            //android.R.attr.state_active,
            android.R.attr.state_activated
    };

    private final static int[] KEY_STATE_PRESSED = {
            android.R.attr.state_pressed,
            android.R.attr.state_activated
    };

    private final static int[] KEY_STATE_NO_ACTIVE = {
            //android.R.attr.state_active
    };


    public CarNumKey(Resources res, Keyboard.Row parent, int x, int y, XmlResourceParser parser) {
        super(res, parent, x, y, parser);
    }

    @Override
    public int[] getCurrentDrawableState() {
        int[] states = KEY_STATE_NORMAL;
        //开关键是否打开
        if (on) {
            if (pressed) {
                states = KEY_STATE_PRESSED_ON;
            } else {
                states = KEY_STATE_NORMAL_ON;
            }
        } else {
            //是否是开关键
            if (sticky) {
                if (pressed) {
                    states = KEY_STATE_PRESSED_OFF;
                } else {
                    states = KEY_STATE_NORMAL_OFF;
                }
            } else {
                //是否禁用
                if(disable) {
                    states = KEY_STATE_NO_ACTIVE;
                    //Log.d("test", "----->KEY_STATE_NO_ACTIVE " + codes[0]);
                } else {
                    //是否按下
                    if (pressed) {
                        states = KEY_STATE_PRESSED;
                    }
                    //Log.d("test", "----->KEY_STATE_NORMAL " + codes[0]);
                }
            }
        }
        return states;
    }
}
