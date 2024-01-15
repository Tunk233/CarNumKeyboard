package com.daemon.won.keyboard.car;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;

/**
 * 为了给键增加一个禁用的状态，重写createKeyFromXml方法
 */
public class CarNumKeyboard extends Keyboard {

    public CarNumKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public CarNumKeyboard(Context context, int layoutTemplateResId, CharSequence characters,
                          int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }

    @Override
    protected Key createKeyFromXml(Resources res, Row parent, int x, int y, XmlResourceParser parser) {
        return new CarNumKey(res, parent, x, y, parser);
    }

}
