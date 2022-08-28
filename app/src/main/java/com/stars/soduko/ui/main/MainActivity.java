package com.stars.soduko.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;

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

    private SodukoPuzzleViewModel mPuzzleViewModel;
    private SodukoViewModel mViewModel;

    private int mCurrentPuzzle;

    private void initViewModels() {
        mPuzzleViewModel = getApplicationScopeViewModel(SodukoPuzzleViewModel.class);
        mViewModel = getActivityScopeViewModel(SodukoViewModel.class);
    }

    private void initData() {
        mCurrentPuzzle = SPUtils.getInstance().getInt("current_puzzle",0);
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        mSodukoView = findViewById(R.id.view_soduko);

        // 答案组合
        float itemWidth = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(32)) / 9.0f;
        for (int i = 0; i < ANSWER_DIGIT_IDS.length; i++) {
            final int digit = i + 1;
            TextView digitView = findViewById(ANSWER_DIGIT_IDS[i]);
            digitView.getLayoutParams().width = (int) itemWidth;
            digitView.getLayoutParams().height = (int) itemWidth;
            digitView.setText(digit+"");
        }

        // 可能组合
        for (int i = 0; i < POSSIBLE_DIGIT_IDS.length; i++) {
            final int digit = i + 1;
            TextView digitView = findViewById(POSSIBLE_DIGIT_IDS[i]);
            digitView.getLayoutParams().width = (int) itemWidth;
            digitView.getLayoutParams().height = (int) itemWidth;
            digitView.setText(digit+"");
        }
    }

    private void initActions() {
        findViewById(R.id.tv_level).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LevelActivity.class);
                startActivityForResult(intent, LEVEL_REQUEST_CODE);
            }
        });

        findViewById(R.id.btn_test_entry).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SodukoTestActivity.class);
            startActivity(intent);
        });

        LinearLayout devContainer = findViewById(R.id.ll_dev_container);
        findViewById(R.id.tv_dev).setOnClickListener(view -> devContainer.setVisibility(View.VISIBLE));

        findViewById(R.id.btn_rule_base).setOnClickListener(view -> {
            mViewModel.onAutoSetClicked();
        });

        findViewById(R.id.btn_fill_possible).setOnClickListener(view -> {
            mViewModel.onFillPossibleClick();
        });


        findViewById(R.id.action_clean).setOnClickListener(view -> {
            int[] selectedIJ = mSodukoView.getSelectedIJ();
            mViewModel.onCleanClicked(selectedIJ[0], selectedIJ[1]);
        });

        for (int i = 0; i < ANSWER_DIGIT_IDS.length; i++) {
            final int digit = i + 1;
            TextView digitView = findViewById(ANSWER_DIGIT_IDS[i]);
            digitView.setOnClickListener(view -> {
                VibrateUtils.vibrate(10);
                int[] selectedIJ = mSodukoView.getSelectedIJ();
                mViewModel.onDigitClicked(selectedIJ[0], selectedIJ[1], digit);
            });
        }

        // 可能组合
        for (int i = 0; i < POSSIBLE_DIGIT_IDS.length; i++) {
            final int digit = i + 1;
            TextView digitView = findViewById(POSSIBLE_DIGIT_IDS[i]);
            digitView.setOnClickListener(view -> {
                VibrateUtils.vibrate(10);
                int[] selectedIJ = mSodukoView.getSelectedIJ();
                mViewModel.onPossibleClicked(selectedIJ[0], selectedIJ[1], digit);
            });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModels();
        initData();
        initView();
        initActions();

        SodukoPuzzle sodukoPuzzle = mPuzzleViewModel.getSodukoPuzzle(mCurrentPuzzle);
        mViewModel.setSodukoPuzzle(sodukoPuzzle);
        mViewModel.getCurrentSoduko().observe(this, new Observer<Soduko>() {
            @Override
            public void onChanged(Soduko soduko) {
                mSodukoView.refresh(soduko);
                refreshUI(soduko);
            }
        });
        mViewModel.setOnSodukoChangedListener(new SodukoViewModel.OnSodukoChangedListener() {
            @Override
            public void onSodukoChanged(Soduko soduko) {
                mSodukoView.refresh(soduko);
                refreshUI(soduko);
            }
        });
    }

    private void refreshUI(Soduko soduko) {
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
                            mViewModel.setSodukoPuzzle(sodukoPuzzle);
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
                            mViewModel.setSodukoPuzzle(sodukoPuzzle);
                        }
                    })
                    .setNegativeButton("重新开始", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SodukoPuzzle sodukoPuzzle = mPuzzleViewModel.getSodukoPuzzle(mCurrentPuzzle);
                            mViewModel.setSodukoPuzzle(sodukoPuzzle);
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
                    mViewModel.setSodukoPuzzle(sodukoPuzzle);
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
