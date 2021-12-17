package com.bignerdranch.android.gridviewwithpictures;

import android.util.Pair;

class MySettings {
//    public final static  int MIN_NUM_THINGS = 8;
    public final static int numThingsArr[] = {8, 16, 24, 32};
    //Массив количеств строк grid для MemorizeActivity
    public final static int rowsNumArr[] = {4, 4, 6, 8};
    public final static int memTimeArr[] = {10,20,30,40,50,60,
        130,140,150,160,170,180};
    public static int getThingNum(int index) {
        return numThingsArr[index];
    }
    public static int getRowsNum(int index) {
        return rowsNumArr[index];
    }
    public static int getMemTime(int index) {
        return memTimeArr[index];
    }
}