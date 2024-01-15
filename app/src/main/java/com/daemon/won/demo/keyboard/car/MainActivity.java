package com.daemon.won.demo.keyboard.car;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.daemon.won.keyboard.car.CarNumView;
import com.daemon.won.keyboard.car.CarNumHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_num);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        MyToolBar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavIcon(R.drawable.won_ic_back);
        toolbar.setNavigationListener(v -> finish());
        toolbar.setCenterTitle("车牌键盘", 19, R.color.white);

        Button btnSubmit = findViewById(R.id.btn_submit);
        CarNumView carNumView = findViewById(R.id.car_num_view);
        TextView tvBtnAdd = findViewById(R.id.tv_btn_add);

        tvBtnAdd.setOnClickListener(v -> {
            tvBtnAdd.setVisibility(View.GONE);
            //允许输入的最大字符长度变为8个
            carNumView.getEditText().setMaxCnt(8);
        });

        btnSubmit.setEnabled(false);
        btnSubmit.setOnClickListener(v -> {
            String carNumber = carNumView.getInputContent();
            if(!carNumber.isEmpty()) {
                Toast.makeText(this, carNumber, Toast.LENGTH_LONG).show();
            }
        });

        CarNumHelper carNumHelper = new CarNumHelper(carNumView.getEditText(),
                newInput -> btnSubmit.setEnabled(newInput.length() >= 7));
        carNumHelper.showCustomKeyboard();
    }
}
