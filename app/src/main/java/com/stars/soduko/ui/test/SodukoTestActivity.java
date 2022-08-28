package com.stars.soduko.ui.test;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.VibrateUtils;
import com.stars.base.ui.StarActivity;
import com.stars.soduko.R;
import com.stars.soduko.model.Soduko;
import com.stars.soduko.model.SodukoPuzzle;
import com.stars.soduko.ui.main.SodukoView;
import com.stars.soduko.ui.main.SodukoViewModel;

public class SodukoTestActivity extends StarActivity {


    private String puzzle = "800060020490000000000000090073000000020170000000320000050080070907006000000507800";
    private String answer = "831964527495712683762853194573648912628179345149325768356281479987436251214597836";

    private SodukoView mSodukoView;
    private Soduko mSoduko;

    private SodukoViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soduko_test);

        mViewModel = getActivityScopeViewModel(SodukoViewModel.class);

        mSodukoView = findViewById(R.id.view_soduko);

        SodukoPuzzle sodukoPuzzle = new SodukoPuzzle(puzzle, answer);
        refreshPuzzle(sodukoPuzzle);

        // 答案组合
        float itemWidth = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(32)) / 9.0f;
        for (int i = 0; i < ANSWER_DIGIT_IDS.length; i++) {
            final int digit = i + 1;
            TextView digitView = findViewById(ANSWER_DIGIT_IDS[i]);
            digitView.getLayoutParams().width = (int) itemWidth;
            digitView.getLayoutParams().height = (int) itemWidth;
            digitView.setText(digit+"");
            digitView.setOnClickListener(view -> {
                VibrateUtils.vibrate(10);
                mViewModel.onDigitClicked(digit);
                refresh(mSoduko);
            });
        }

        // 可能组合
        for (int i = 0; i < POSSIBLE_DIGIT_IDS.length; i++) {
            final int number = i + 1;
            TextView digitView = findViewById(POSSIBLE_DIGIT_IDS[i]);
            digitView.getLayoutParams().width = (int) itemWidth;
            digitView.getLayoutParams().height = (int) itemWidth;
            digitView.setText(number+"");
            digitView.setOnClickListener(view -> {
                VibrateUtils.vibrate(10);
                mViewModel.onPossibleClicked(number);
            });
        }

        findViewById(R.id.btn_rule_base).setOnClickListener(view -> {
            mViewModel.onAutoSetClicked();
            refresh(mSoduko);
        });

        findViewById(R.id.btn_fill_possible).setOnClickListener(view -> {
            mSoduko.calcAllPossibleDigits();
            mSodukoView.refresh(mSoduko);
            refresh(mSoduko);
        });
    }

    private void refreshPuzzle(SodukoPuzzle sodukoPuzzle) {
        mSoduko = new Soduko();
        mSoduko.init(sodukoPuzzle.puzzle, sodukoPuzzle.answer);
        mViewModel.init(mSoduko, mSodukoView);
        refresh(mSoduko);
    }

    private void refresh(Soduko soduko) {
        for (int i = 0; i < 9; i++) {
            if (soduko.getDigitCount(i+1) == 9) {
                findViewById(ANSWER_DIGIT_IDS[i]).setVisibility(View.INVISIBLE);
                findViewById(POSSIBLE_DIGIT_IDS[i]).setVisibility(View.INVISIBLE);
            } else {
                findViewById(ANSWER_DIGIT_IDS[i]).setVisibility(View.VISIBLE);
                findViewById(POSSIBLE_DIGIT_IDS[i]).setVisibility(View.VISIBLE);
            }
        }
    }


    private final static int[] ANSWER_DIGIT_IDS = new int[]{
            R.id.answer_digit_1,
            R.id.answer_digit_2,
            R.id.answer_digit_3,
            R.id.answer_digit_4,
            R.id.answer_digit_5,
            R.id.answer_digit_6,
            R.id.answer_digit_7,
            R.id.answer_digit_8,
            R.id.answer_digit_9,
    };

    private final static int[] POSSIBLE_DIGIT_IDS = new int[]{
            R.id.possible_digit_1,
            R.id.possible_digit_2,
            R.id.possible_digit_3,
            R.id.possible_digit_4,
            R.id.possible_digit_5,
            R.id.possible_digit_6,
            R.id.possible_digit_7,
            R.id.possible_digit_8,
            R.id.possible_digit_9,
    };

}
