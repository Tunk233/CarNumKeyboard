# CarNumKeyboard

Android输入车牌号的自定义键盘及输入方框。

## 效果展示
![效果展示](./art/capture.mp4)

未输入时的方框及键盘状态:
![未输入时的方框及键盘状态](./art/pic-1.png)

输入第二位城市编码时禁用数字键盘:
![禁用数字键盘](./art/pic-2.png)

输完城市编码再启用数字键盘:
![启用数字键盘](./art/pic-3.png)

默认是非新能源的7位车牌:
![7位车牌](./art/pic-4.png)

点击加号变成新能源8位车牌:
![新能源8位车牌](./art/pic-5.png)


## 使用方式

参照demo中的使用方式，建议直接源码依赖，方便自己按需求修改:
```groovy
implementation project(path: ':library-car-num')
```

偷了个懒:)，最后的Add按钮是按尾部对齐直接盖在输入框上的，正好挡住了第8位输入框：

```xml
<androidx.constraintlayout.widget.ConstraintLayout
    android:id="@+id/container_top"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <com.daemon.won.keyboard.car.CarNumView
        android:id="@+id/car_num_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintTop_toBottomOf="@id/tv_tips"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        android:layout_marginTop="20dp"
        app:box_text_color="@color/black_03"
        app:box_text_size="20sp"
        app:box_width="36dp"/>
    <TextView
        android:id="@+id/tv_btn_add"
        android:layout_width="36dp"
        android:layout_height="50dp"
        app:layout_constraintTop_toTopOf="@id/car_num_view"
        app:layout_constraintBottom_toBottomOf="@id/car_num_view"
        app:layout_constraintRight_toRightOf="@id/car_num_view"
        android:gravity="center"
        android:text="+"
        android:textSize="22sp"
        android:textColor="@color/gray_ae"
        android:background="@drawable/won_layer_list_add_bg"/>
</androidx.constraintlayout.widget.ConstraintLayout>
```

## 注意事项

仅供学习使用，禁止用于商业场景。
要求创建CarNumView所持有的Context是Activity, 否则无法正常弹出车牌号输入法，暂时未修复这个问题。

## 致谢
感谢[VehicleEditText](https://github.com/relish-wang/VehicleEditText) 及 [VerificationCodeView](https://github.com/JackTuoTuo/VerificationCodeView) ，在他们的基础上调整修改，整合出此版本的车牌输入框及键盘。