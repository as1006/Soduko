package com.stars.soduko.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.stars.soduko.model.Soduko;
import com.stars.soduko.model.SodukoPuzzle;

/**
 * 主页的ViewModel
 */
public class SodukoViewModel extends ViewModel {

    private final MutableLiveData<Soduko> mSodukoLiveData = new MutableLiveData<>();

    public LiveData<Soduko> getCurrentSoduko() {
        return mSodukoLiveData;
    }

    public void setSodukoPuzzle(SodukoPuzzle sodukoPuzzle) {
        Soduko soduko = new Soduko();
        soduko.init(sodukoPuzzle.puzzle, sodukoPuzzle.answer);
        mSodukoLiveData.postValue(soduko);
    }

    interface OnSodukoChangedListener {
        void onSodukoChanged(Soduko soduko);
    }

    private OnSodukoChangedListener mOnSodukoChangedListener;
    public void setOnSodukoChangedListener(OnSodukoChangedListener listener) {
        mOnSodukoChangedListener = listener;
    }


    //以下是页面逻辑处理部分
    public void onDigitClicked(int i, int j, int digit) {
        Soduko soduko = mSodukoLiveData.getValue();
        if (i != -1 && j != -1 && soduko != null) {
            if (soduko.canSetDigit(i, j)) {
                soduko.setDigit(i, j, digit);
                mOnSodukoChangedListener.onSodukoChanged(soduko);
            }
        }
    }

    public void onPossibleClicked(int i, int j, int digit) {
        Soduko soduko = mSodukoLiveData.getValue();
        if (i != -1 && j != -1 && soduko != null) {
            if (soduko.canSetDigit(i, j)) {
                soduko.setPossible(i, j, digit);
                mOnSodukoChangedListener.onSodukoChanged(soduko);
            }
        }
    }

    public void onFillPossibleClick() {
        Soduko soduko = mSodukoLiveData.getValue();
        if (soduko != null) {
            soduko.calcAllPossibleDigits();
            mOnSodukoChangedListener.onSodukoChanged(soduko);
        }

    }

    public void onAutoSetClicked() {
        Soduko soduko = mSodukoLiveData.getValue();
        if (soduko != null && soduko.auto()) {
            mOnSodukoChangedListener.onSodukoChanged(soduko);
        }
    }

    public void onCleanClicked(int i, int j) {
        Soduko soduko = mSodukoLiveData.getValue();
        if (i != -1 && j != -1 && soduko != null) {
            if (soduko.canSetDigit(i, j)) {
                soduko.cleanDigit(i, j);
                mOnSodukoChangedListener.onSodukoChanged(soduko);
            }
        }
    }
}
