package com.bignerdranch.android.gridviewwithpictures;
class MySettings {
    public static final int MIN_NUM_THINGS = 8;
    public static int numThingsArr[] = {MIN_NUM_THINGS, MIN_NUM_THINGS * 2,
        MIN_NUM_THINGS * 3, MIN_NUM_THINGS * 4, MIN_NUM_THINGS * 5, MIN_NUM_THINGS * 6};
    public static int memTimeArr[] = {10,20,30,40,50,60,
        130,140,150,160,170,180};
    public static int getThingNum(int index) {
        return numThingsArr[index];
    }
    public static int getMemTime(int index) {
        return memTimeArr[index];
    }
}