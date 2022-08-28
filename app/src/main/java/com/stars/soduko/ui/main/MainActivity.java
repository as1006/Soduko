package com.stars.soduko.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.VibrateUtils;
import com.stars.base.ui.StarActivity;
import com.stars.soduko.R;
import com.stars.soduko.model.Soduko;
import com.stars.soduko.model.SodukoPuzzle;
import com.stars.soduko.ui.level.LevelActivity;
import com.stars.soduko.ui.test.SodukoTestActivity;
import com.stars.soduko.viewmodel.SodukoPuzzleViewModel;

/**
 * 主页，包括答题等部分
 */
public class MainActivity extends StarActivity {

    private final static int LEVEL_REQUEST_CODE = 100001;

    private SodukoView mSodukoView;
    private Soduko mSoduko;

    private SodukoPuzzleViewModel mPuzzleViewModel;
    private SodukoViewModel mViewModel;

    private int mCurrentPuzzle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPuzzleViewModel = getApplicationScopeViewModel(SodukoPuzzleViewModel.class);
        mViewModel = getActivityScopeViewModel(SodukoViewModel.class);

        mSodukoView = findViewById(R.id.view_soduko);

        mCurrentPuzzle = SPUtils.getInstance().getInt("current_puzzle",0);
        SodukoPuzzle sodukoPuzzle = mPuzzleViewModel.getSodukoPuzzle(mCurrentPuzzle);
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

        TextView levelEntry = findViewById(R.id.tv_level);
        levelEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LevelActivity.class);
                startActivityForResult(intent, LEVEL_REQUEST_CODE);
            }
        });

        TextView devMode = findViewById(R.id.tv_dev);
        LinearLayout devContainer = findViewById(R.id.ll_dev_container);
        devMode.setOnClickListener(view -> devContainer.setVisibility(View.VISIBLE));

        findViewById(R.id.btn_rule_base).setOnClickListener(view -> {
            mViewModel.onAutoSetClicked();
            refresh(mSoduko);
        });

        findViewById(R.id.btn_fill_possible).setOnClickListener(view -> {
            mSoduko.calcAllPossibleDigits();
            mSodukoView.refresh(mSoduko);
            refresh(mSoduko);
        });

        findViewById(R.id.btn_test_entry).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SodukoTestActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.action_clean).setOnClickListener(view -> {
            mViewModel.onCleanClicked();
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

        if (soduko.isSuccess()) {
            sodukoDone();
        }
    }

    private void sodukoDone() {

        SPUtils.getInstance().put("puzzle_"+mCurrentPuzzle+"_done", true, true);

        if (mCurrentPuzzle == mPuzzleViewModel.getSize() - 1) {
            new AlertDialog.Builder(this)
                    .setMessage("恭喜你，完成全部关卡")
                    .setCancelable(false)
                    .setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SodukoPuzzle sodukoPuzzle = mPuzzleViewModel.getSodukoPuzzle(mCurrentPuzzle);
                            refreshPuzzle(sodukoPuzzle);
                        }
                    }).show();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("恭喜你，完成本关卡")
                    .setCancelable(false)
                    .setPositiveButton("下一关", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mCurrentPuzzle++;
                            SPUtils.getInstance().put("current_puzzle", mCurrentPuzzle, true);
                            SodukoPuzzle sodukoPuzzle = mPuzzleViewModel.getSodukoPuzzle(mCurrentPuzzle);
                            refreshPuzzle(sodukoPuzzle);
                        }
                    })
                    .setNegativeButton("重新开始", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SodukoPuzzle sodukoPuzzle = mPuzzleViewModel.getSodukoPuzzle(mCurrentPuzzle);
                            refreshPuzzle(sodukoPuzzle);
                        }
                    }).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LEVEL_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                int puzzle = data.getIntExtra("Puzzle", -1);
                if (puzzle != -1) {
                    mCurrentPuzzle = puzzle;
                    SodukoPuzzle sodukoPuzzle = mPuzzleViewModel.getSodukoPuzzle(mCurrentPuzzle);
                    refreshPuzzle(sodukoPuzzle);
                }
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
