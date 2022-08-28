package com.stars.soduko.ui.main;

import androidx.lifecycle.ViewModel;

import com.stars.soduko.model.Soduko;

public class SodukoViewModel extends ViewModel {

    private Soduko mSoduko;
    private SodukoView mSodukoView;

    public void init(Soduko soduko, SodukoView sodukoView) {
        mSoduko = soduko;
        mSodukoView = sodukoView;

        mSodukoView.refresh(mSoduko);
    }

    public void onDigitClicked(int digit) {
        int[] selectedIJ = mSodukoView.getSelectedIJ();
        int i = selectedIJ[0];
        int j = selectedIJ[1];
        if (i != -1 && j != -1) {
            if (mSoduko.canSetDigit(i, j)) {
                mSoduko.setDigit(i, j, digit);
                mSodukoView.refresh(mSoduko);
            }
        }
    }

    public void onPossibleClicked(int digit) {
        int[] selectedIJ = mSodukoView.getSelectedIJ();
        int i = selectedIJ[0];
        int j = selectedIJ[1];
        if (i != -1 && j != -1) {
            if (mSoduko.canSetDigit(i, j)) {
                mSoduko.setPossible(i, j, digit);
                mSodukoView.refresh(mSoduko);
            }
        }
    }

    public void onAutoSetClicked() {
        if (mSoduko.auto()) {
            mSodukoView.refresh(mSoduko);
        }
    }

    public void onCleanClicked() {
        int[] selectedIJ = mSodukoView.getSelectedIJ();
        int i = selectedIJ[0];
        int j = selectedIJ[1];
        if (i != -1 && j != -1) {
            if (mSoduko.canSetDigit(i, j)) {
                mSoduko.cleanDigit(i, j);
                mSodukoView.refresh(mSoduko);
            }
        }
    }
}
