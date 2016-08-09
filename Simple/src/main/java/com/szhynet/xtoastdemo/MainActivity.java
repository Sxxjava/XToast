package com.szhynet.xtoastdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.szhynet.widget.xtoast.XActivityToast;
import com.szhynet.widget.xtoast.XToast;
import com.szhynet.widget.xtoast.utils.Style;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1;
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
    }


    private void assignViews() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Style style = null;
        switch (view.getId()) {
            case R.id.btn1:
                style = Style.getCustom(this, Color.parseColor("#2B2B2B"),Color.parseColor("#A9B7C6"),Color.parseColor("#A9B7C6"),Color.parseColor("#A9B7C6"));
                XActivityToast.create(this, "自定义Style的Toast", XToast.Duration.MEDIUM_SHORT, style).show();
                break;
            case R.id.btn2:
                XActivityToast.create(this, "预设风格的Toast", XToast.Duration.MEDIUM_SHORT, XToast.Animations.FADE).show();
                break;
        }
    }
}
