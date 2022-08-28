package com.stars.soduko.ui.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.stars.soduko.R;
import com.stars.soduko.model.Soduko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SodukoItemView extends FrameLayout {
    public SodukoItemView(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_soduko_item, this, true);
    }

    public SodukoItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_soduko_item, this, true);
        int[] RESID = new int[]{
                R.id.tv_left_1,
                R.id.tv_left_2,
                R.id.tv_left_3,
                R.id.tv_left_4,
                R.id.tv_left_5,
                R.id.tv_left_6,
                R.id.tv_left_7,
                R.id.tv_left_8,
                R.id.tv_left_9,
        };
        float itemWidth = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(32))/9.0f/3.0f;
        float offsetX = 0;
        float offsetY = 0;
        for (int i = 0;i<9;i++) {
            TextView leftView = findViewById(RESID[i]);
            leftView.setText(""+(i+1));
            leftView.getLayoutParams().width = (int) itemWidth;
            leftView.getLayoutParams().height = (int) itemWidth;
            ((FrameLayout.LayoutParams)leftView.getLayoutParams()).setMargins((int)offsetX, (int)offsetY, 0 , 0);
            if (i == 2 || i == 5) {
                offsetY += itemWidth;
                offsetX = 0;
            } else {
                offsetX += itemWidth;
            }
        }
    }

    public SodukoItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_soduko_item, this, true);
    }

    public void fillDigitLeft(int[] leftDigits, int selectedDigit) {
        TextView numberView = findViewById(R.id.tv_number);
        numberView.setVisibility(View.GONE);

        View containerView = findViewById(R.id.fl_left_container);
        containerView.setVisibility(View.VISIBLE);

        int[] RESID = new int[]{
                R.id.tv_left_1,
                R.id.tv_left_2,
                R.id.tv_left_3,
                R.id.tv_left_4,
                R.id.tv_left_5,
                R.id.tv_left_6,
                R.id.tv_left_7,
                R.id.tv_left_8,
                R.id.tv_left_9,
        };
        for (int i = 0;i<9;i++) {
            TextView leftView = findViewById(RESID[i]);
            if (leftDigits[i] == 0) {
                leftView.setVisibility(View.GONE);
            } else {
                leftView.setVisibility(View.VISIBLE);
            }
            if (selectedDigit != 0 && selectedDigit == i+1) {
                leftView.setBackgroundColor(getResources().getColor(R.color.main_color));
                leftView.setTextColor(getResources().getColor(android.R.color.white));
            } else {
                leftView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                leftView.setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    public void fillDigit(int digit, Soduko.State state, int selectedDigit) {
        View containerView = findViewById(R.id.fl_left_container);
        containerView.setVisibility(View.GONE);

        TextView numberView = findViewById(R.id.tv_number);
        numberView.setVisibility(View.VISIBLE);
        if (digit == 0) {
            numberView.setText("");
        } else {
            numberView.setText(digit+"");
        }

        if (selectedDigit != 0 && digit == selectedDigit && state != Soduko.State.Wrong) {
            numberView.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            if (state == Soduko.State.Init) {
                numberView.setTextColor(getResources().getColor(android.R.color.black));
            } else if (state == Soduko.State.Right) {
                numberView.setTextColor(getResources().getColor(R.color.main_color));
            } else if (state == Soduko.State.Wrong) {
                numberView.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
        }
    }

}
