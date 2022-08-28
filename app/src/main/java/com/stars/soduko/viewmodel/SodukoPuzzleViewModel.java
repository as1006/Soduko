package com.stars.soduko.viewmodel;

import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.Utils;
import com.stars.soduko.model.SodukoPuzzle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 这里管理所有的数独关卡
 */
public class SodukoPuzzleViewModel extends ViewModel {

    private List<SodukoPuzzle> mSodukoPuzzles = new ArrayList<>();


    public void init() {
        try {
            InputStream is = Utils.getApp().getAssets().open("soduko.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String line = br.readLine();
                if (ObjectUtils.isEmpty(line)) {
                    break;
                }
                String[] splits = line.split(",");
                mSodukoPuzzles.add(new SodukoPuzzle(splits[0],splits[1]));
            }
        } catch (Exception e) {

        }
    }

    public SodukoPuzzle getSodukoPuzzle(int index) {
        return mSodukoPuzzles.get(index);
    }

    public int getSize() {
        return mSodukoPuzzles.size();
    }

    public List<SodukoPuzzle> getAllPuzzles() {
        return mSodukoPuzzles;
    }

}
