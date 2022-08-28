package com.stars.soduko.model;

import android.util.Log;

public class Soduko {

    public enum State {
        None,
        Init,
        Right,
        Wrong
    }

    private final static String tag = Soduko.class.getSimpleName();

    private final int[][] soduko = new int[9][9];
    private final int[][] answer = new int[9][9];
    private final Soduko.State[][] state = new Soduko.State[9][9];

    private final int[][][] possible = new int[9][9][9];

    public boolean init(String puzzle, String answer) {

        if (puzzle.length() != 81 || answer.length() != 81) {
            return false;
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.soduko[i][j] = puzzle.charAt(i * 9 + j) - '0';
                this.answer[i][j] = answer.charAt(i * 9 + j) - '0';

                if (soduko[i][j] != 0) {
                    state[i][j] = State.Init;
                } else {
                    state[i][j] = State.None;
                }
            }
        }

        return isSodukoValid();
    }

    public int getDigit(int i, int j) {
        return soduko[i][j];
    }

    public int[] getPossibleDigits(int i, int j) {
        return possible[i][j];
    }

    public Soduko.State getState(int i, int j) {
        return state[i][j];
    }

    public boolean canSetDigit(int i, int j) {
        return state[i][j] != Soduko.State.Init && state[i][j] != Soduko.State.Right;
    }

    public void cleanDigit(int i, int j) {
        soduko[i][j] = 0;
    }

    /**
     * 写入数字，只有在正确的情况下，才修改possible
     */
    public void setDigit(int i, int j, int digit) {
        soduko[i][j] = digit;
        if (soduko[i][j] == answer[i][j]) {
            state[i][j] = Soduko.State.Right;
        } else {
            state[i][j] = Soduko.State.Wrong;
        }

        if (state[i][j] == Soduko.State.Right) {
            //行、列、自身清空
            for (int k = 0; k < 9; k++) {
                possible[k][j][digit - 1] = 0;
                possible[i][k][digit - 1] = 0;
                possible[i][j][k] = 0;
            }
            //九宫内减少
            for (int a = (i / 3) * 3; a < (i / 3) * 3 + 3; a++) {
                for (int b = (j / 3) * 3; b < (j / 3) * 3 + 3; b++) {
                    possible[a][b][digit - 1] = 0;
                }
            }
        }
    }

    public void setPossible(int i, int j, int digit) {
        possible[i][j][digit - 1] = 1 - possible[i][j][digit - 1];
    }

    /**
     * 作弊模式，自动填充所有的PossibleDigits
     */
    public void calcAllPossibleDigits() {
        //重置possible
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    possible[i][j][k] = 1;
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (soduko[i][j] != 0) {

                    //该格子的备选全部清楚
                    for (int k = 0; k < 9; k++) {
                        possible[i][j][k] = 0;
                    }

                    //该行都减少
                    for (int k = 0; k < 9; k++) {
                        possible[i][k][soduko[i][j] - 1] = 0;
                    }

                    //该列减少
                    for (int k = 0; k < 9; k++) {
                        possible[k][j][soduko[i][j] - 1] = 0;
                    }

                    //九宫减少
                    for (int a = (i / 3) * 3; a < (i / 3) * 3 + 3; a++) {
                        for (int b = (j / 3) * 3; b < (j / 3) * 3 + 3; b++) {
                            possible[a][b][soduko[i][j] - 1] = 0;
                        }
                    }
                }
            }
        }
    }

    public int getDigitCount(int digit) {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (soduko[i][j] == digit) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isSodukoValid() {
        //每一行数字不重复
        for (int i = 0; i < 9; i++) {
            int[] count = new int[9];
            for (int j = 0; j < 9; j++) {
                if (soduko[i][j] == 0) {
                    continue;
                }

                if (count[soduko[i][j] - 1] != 0) {
                    return false;
                } else {
                    count[soduko[i][j] - 1]++;
                }
            }
        }

        //每一列数字不重复
        for (int j = 0; j < 9; j++) {
            int[] count = new int[9];
            for (int i = 0; i < 9; i++) {
                if (soduko[i][j] == 0) {
                    continue;
                }

                if (count[soduko[i][j] - 1] != 0) {
                    return false;
                } else {
                    count[soduko[i][j] - 1]++;
                }
            }
        }

        //九宫
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int[] count = new int[9];
                for (int ii = i * 3; ii < i * 3 + 3; ii++) {
                    for (int jj = j * 3; jj < j * 3 + 3; jj++) {
                        if (soduko[ii][jj] == 0) {
                            continue;
                        }

                        if (count[soduko[ii][jj] - 1] != 0) {
                            return false;
                        } else {
                            count[soduko[ii][jj] - 1]++;
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean isSuccess() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (soduko[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // 以下是开发模式
    public boolean auto() {
        if (rule1() || rule2() || optimizePossibleRule1()) {
            return true;
        } else {
            return false;
        }
    }

    //备选里只剩下1个
    private boolean rule1() {
//        Log.i(tag, "开始用规则1解数独");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (soduko[i][j] != 0) {
                    continue;
                }
                int digit = 0;
                for (int k = 0; k < 9; k++) {
                    if (possible[i][j][k] == 1) {
                        if (digit != 0) {
                            digit = 0;
                            //说明这是第二个，放弃
                            break;
                        } else {
                            digit = k + 1;
                        }
                    }
                }

                if (digit != 0) {
                    //找到一个
//                    Log.i(tag, "规则1找到答案(" + i + "," + j + ") = " + digit);
                    setDigit(i, j, digit);
                    return true;
                }
            }
        }
//        Log.i(tag, "规则1无法找到答案");
        return false;
    }

    //横、竖、九宫之内只有1个备选
    private boolean rule2() {

//        Log.i(tag, "开始用规则2解数独");
        //横
        for (int i = 0; i < 9; i++) {
            int[] count = new int[9];
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    if (possible[i][j][k] != 0) {
                        count[k]++;
                    }
                }
            }
            for (int countIndex = 0; countIndex < 9; countIndex++) {
                if (count[countIndex] == 1) {
                    //说明找到了答案，下面开始找位置

                    for (int j = 0; j < 9; j++) {
                        if (possible[i][j][countIndex] != 0) {
//                            Log.i(tag, "规则2找到答案(" + i + "," + j + ") = " + (countIndex + 1));
                            setDigit(i, j, countIndex + 1);
                            return true;
                        }
                    }
                }
            }
        }

        //竖
        for (int j = 0; j < 9; j++) {
            int[] count = new int[9];
            for (int i = 0; i < 9; i++) {
                for (int k = 0; k < 9; k++) {
                    if (possible[i][j][k] != 0) {
                        count[k]++;
                    }
                }
            }
            for (int countIndex = 0; countIndex < 9; countIndex++) {
                if (count[countIndex] == 1) {
                    //说明找到了答案，下面开始找位置

                    for (int i = 0; i < 9; i++) {
                        if (possible[i][j][countIndex] != 0) {
//                            Log.i(tag, "规则2找到答案(" + i + "," + j + ") = " + (countIndex + 1));
                            setDigit(i, j, countIndex + 1);
                            return true;
                        }
                    }
                }
            }
        }

        //九宫
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int[] count = new int[9];
                for (int ii = i * 3; ii < i * 3 + 3; ii++) {
                    for (int jj = j * 3; jj < j * 3 + 3; jj++) {
                        for (int k = 0; k < 9; k++) {
                            if (possible[ii][jj][k] != 0) {
                                count[k]++;
                            }
                        }
                    }
                }

                for (int countIndex = 0; countIndex < 9; countIndex++) {
                    if (count[countIndex] == 1) {
                        //说明找到了答案，下面开始找位置
                        for (int ii = i * 3; ii < i * 3 + 3; ii++) {
                            for (int jj = j * 3; jj < j * 3 + 3; jj++) {
                                if (possible[ii][jj][countIndex] != 0) {
//                                    Log.i(tag, "规则2找到答案(" + ii + "," + jj + ") = " + (countIndex + 1));
                                    setDigit(ii, jj, countIndex + 1);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

//        Log.i(tag, "规则2无法找到答案");
        return false;
    }

    //宫区块排除法
    //如果某一个格子里的备选数字k，都在某一行，那么这一整行都不去除k
    //如果某一个格子里的备选数字k，都在某一列，那么这一整列都不去除k
    private boolean optimizePossibleRule1() {
        //计算是否在某一行
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 1; k <= 9; k++) {
                    boolean rowFlag = true;
                    boolean columnFlag = true;
                    int row = -1;
                    int column = -1;
                    for (int ii = i * 3; ii < i * 3 + 3; ii++) {
                        for (int jj = j * 3; jj < j * 3 + 3; jj++) {
                            if (soduko[ii][jj] == 0 && possible[ii][jj][k - 1] == 1) {
                                if (row == -1) {
                                    row = ii;
                                } else if (row != ii) {
                                    rowFlag = false;
                                }

                                if (column == -1) {
                                    column = jj;
                                } else if (column != jj) {
                                    columnFlag = false;
                                }
                            }
                            if (!rowFlag && !columnFlag) {
                                break;
                            }
                        }

                        if (!rowFlag && !columnFlag) {
                            break;
                        }
                    }

                    boolean hasRowChanged = false;
                    if (rowFlag && row != -1) {
                        //找到某一行
                        for (int temp = 0; temp < 9; temp++) {
                            if (temp < j * 3 || temp >= j * 3 + 3) {
                                if (possible[row][temp][k - 1] == 1) {
                                    hasRowChanged = true;
                                }
                                possible[row][temp][k - 1] = 0;
                            }
                        }
                        if (hasRowChanged) {
                            Log.i("asherchen", "optimizePossibleRule1 ：row = " + row + " , digit = " + k);
                        }
                    }

                    boolean hasColumnChanged = false;
                    if (columnFlag && column != -1) {
                        //找到某一行
                        for (int temp = 0; temp < 9; temp++) {
                            if (temp < i * 3 || temp >= i * 3 + 3) {
                                if (possible[temp][column][k - 1] == 1) {
                                    hasColumnChanged = true;
                                }
                                possible[temp][column][k - 1] = 0;
                            }
                        }
                        if (hasColumnChanged) {
                            Log.i("asherchen", "optimizePossibleRule1 ：column = " + column + " , digit = " + k);
                        }
                    }

                    if (hasColumnChanged || hasRowChanged) {
                        return true;
                    }

                }

            }
        }
        return false;
    }

    //隐性数对/数组
}
