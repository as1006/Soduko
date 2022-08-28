package com.stars.soduko.utils;

public class SodukoUtils {
    public interface OnSodukoTraverseListener {
        public void onPosition(int i, int j);
    }

    public static void traverse(OnSodukoTraverseListener listener) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                listener.onPosition(i, j);
            }
        }
    }
}
