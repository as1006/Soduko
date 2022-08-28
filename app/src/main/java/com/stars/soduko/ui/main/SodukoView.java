package com.stars.soduko.ui.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.stars.soduko.R;
import com.stars.soduko.model.Soduko;

public class SodukoView extends FrameLayout {

    public interface OnSodukoItemClickListener {
        void onItemClick(int i, int j);
    }

    public SodukoView(@NonNull Context context) {
        super(context);
        initView(context);
        setWillNotDraw(false);
    }

    public SodukoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        setWillNotDraw(false);
    }

    public SodukoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        setWillNotDraw(false);
    }

    private SodukoItemView[][] mSodukoItemViews = new SodukoItemView[9][9];
    private static int[] ROW_IDS = new int[]{
            R.id.layout_soduko_row_1,
            R.id.layout_soduko_row_2,
            R.id.layout_soduko_row_3,
            R.id.layout_soduko_row_4,
            R.id.layout_soduko_row_5,
            R.id.layout_soduko_row_6,
            R.id.layout_soduko_row_7,
            R.id.layout_soduko_row_8,
            R.id.layout_soduko_row_9
    };

    private static int[] DIGIT_IDS = new int[] {
            R.id.digit_1,
            R.id.digit_2,
            R.id.digit_3,
            R.id.digit_4,
            R.id.digit_5,
            R.id.digit_6,
            R.id.digit_7,
            R.id.digit_8,
            R.id.digit_9
    };
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_soduko, this);
        for (int i = 0; i< ROW_IDS.length; i++) {
            LinearLayout rowView = findViewById(ROW_IDS[i]);
            for (int j = 0; j< DIGIT_IDS.length; j++){
                mSodukoItemViews[i][j] = rowView.findViewById(DIGIT_IDS[j]);
            }
        }
    }


    /**
     * 保持宽高比1：1
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float itemWidth = width/9.0f;
        for (int i = 0;i<9;i++) {
            for (int j = 0;j<9;j++) {
                SodukoItemView itemView = mSodukoItemViews[i][j];
                itemView.getLayoutParams().width = (int) itemWidth;
                itemView.getLayoutParams().height = (int) itemWidth;
            }
        }
    }

    private Paint mLinePaint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mLinePaint.setColor(getContext().getResources().getColor(R.color.soduko_border));

        //边框
        mLinePaint.setStrokeWidth(SizeUtils.dp2px(2));
        canvas.drawLine(0,0,getWidth(),0, mLinePaint);
        canvas.drawLine(getWidth(),0,getWidth(),getHeight(), mLinePaint);
        canvas.drawLine(0,0,0,getHeight(), mLinePaint);
        canvas.drawLine(0,getHeight(),getWidth(),getHeight(), mLinePaint);

        //骨架
        mLinePaint.setStrokeWidth(SizeUtils.dp2px(1));
        canvas.drawLine(0,getHeight()/3.0f,getWidth(),getHeight()/3.0f, mLinePaint);
        canvas.drawLine(0,getHeight()*2/3.0f,getWidth(),getHeight()*2/3.0f, mLinePaint);
        canvas.drawLine(getWidth()/3.0f,0,getWidth()/3.0f,getHeight(), mLinePaint);
        canvas.drawLine(getWidth()*2/3.0f,0,getWidth()*2/3.0f,getHeight(), mLinePaint);

        //细网格
        mLinePaint.setStrokeWidth(SizeUtils.dp2px(0.5f));
        canvas.drawLine(0,getHeight()/9.0f,getWidth(),getHeight()/9.0f, mLinePaint);
        canvas.drawLine(0,getHeight()*2/9.0f,getWidth(),getHeight()*2/9.0f, mLinePaint);
        canvas.drawLine(0,getHeight()*4/9.0f,getWidth(),getHeight()*4/9.0f, mLinePaint);
        canvas.drawLine(0,getHeight()*5/9.0f,getWidth(),getHeight()*5/9.0f, mLinePaint);
        canvas.drawLine(0,getHeight()*7/9.0f,getWidth(),getHeight()*7/9.0f, mLinePaint);
        canvas.drawLine(0,getHeight()*8/9.0f,getWidth(),getHeight()*8/9.0f, mLinePaint);

        canvas.drawLine(getWidth()/9.0f,0,getWidth()/9.0f, getHeight(),mLinePaint);
        canvas.drawLine(getWidth()*2/9.0f,0,getWidth()*2/9.0f, getHeight(),mLinePaint);
        canvas.drawLine(getWidth()*4/9.0f,0,getWidth()*4/9.0f, getHeight(),mLinePaint);
        canvas.drawLine(getWidth()*5/9.0f,0,getWidth()*5/9.0f, getHeight(),mLinePaint);
        canvas.drawLine(getWidth()*7/9.0f,0,getWidth()*7/9.0f, getHeight(),mLinePaint);
        canvas.drawLine(getWidth()*8/9.0f,0,getWidth()*8/9.0f, getHeight(),mLinePaint);
    }


    private int mSelectedI = -1;
    private int mSelectedJ = -1;

    public int[] getSelectedIJ() {
        return new int[]{mSelectedI, mSelectedJ};
    }


    public void refresh(Soduko soduko) {

        //选中的数字
        int selectedDigit = 0;
        if (mSelectedI != -1 && mSelectedJ != -1) {
            selectedDigit = soduko.getDigit(mSelectedI, mSelectedJ);
        }

        for (int i = 0;i<9;i++) {
            for (int j = 0;j<9;j++) {

                if (mSelectedI != -1 && mSelectedJ != -1) {
                    if (i == mSelectedI || j == mSelectedJ || ( i>= (mSelectedI/3)*3 && i <(mSelectedI/3)*3+3 && j>=(mSelectedJ/3)*3 && j<(mSelectedJ/3)*3+3)) {
                        mSodukoItemViews[i][j].setBackgroundColor(getResources().getColor(R.color.bkg_selected));
                    } else {
                        mSodukoItemViews[i][j].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }

                    if (selectedDigit != 0) {
                        if (soduko.getDigit(i,j) == selectedDigit) {
                            mSodukoItemViews[i][j].setBackgroundColor(getResources().getColor(R.color.main_color));
                        }
                    }

                    if (i == mSelectedI && j == mSelectedJ) {
                        mSodukoItemViews[i][j].setBackgroundColor(getResources().getColor(R.color.main_color));
                    }
                } else {
                    mSodukoItemViews[i][j].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }

                mSodukoItemViews[i][j].fillDigit(soduko.getDigit(i,j), soduko.getState(i, j), selectedDigit);
                if (soduko.getDigit(i,j) == 0) {
                    mSodukoItemViews[i][j].fillDigitLeft(soduko.getPossibleDigits(i,j), selectedDigit);
                }

                //设置点击事件
                int finalI = i;
                int finalJ = j;
                mSodukoItemViews[i][j].setOnClickListener(view -> {
                    if (mSelectedI == finalI && mSelectedJ == finalJ) {
                        mSelectedI = -1;
                        mSelectedJ = -1;
                    } else {
                        mSelectedI = finalI;
                        mSelectedJ = finalJ;
                    }
                    refresh(soduko);
                });
            }
        }
    }

}
