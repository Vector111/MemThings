package com.bignerdranch.android.gridviewwithpictures;

import androidx.appcompat.app.AppCompatActivity;
import static com.bignerdranch.android.gridviewwithpictures.ImageButtonOperations.*;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements DoForPositive1 {
    public SharedPreferences apppref;
    public static final String APP_PREFERENCES = "apppref";
    public static final int MIN_NUM_THINGS = 8;

    private ImageButton decThingsNum_ib;
    private ImageButton incThingsNum_ib;
    private TextView thingsNumVal_tv;
    private static int numThingsArr[] = {MIN_NUM_THINGS, MIN_NUM_THINGS * 2,
            MIN_NUM_THINGS * 3,MIN_NUM_THINGS * 4};
    private static int indNumThingsArr = 0;

    private ImageButton decMemTime_ib;
    private ImageButton incMemTime_ib;
    private TextView memTimeVal_tv;
    private static int memTimeArr[] = {10,20,30,40,50,60,
                                       130,140,150,160,170,180};
    private static int indMemTimeArr = 0;

    public static int getThingNum(int index)
    {
        return numThingsArr[index];
    }

    public static int getMemTime(int index)
    {
        return memTimeArr[index];
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        exitGame_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_exit_dlg();
//                finishAffinity();
            }
        });

 */

    }
    @Override
    public void onResume() {
        super.onResume();

        Settings settings = new Settings(this);
        //читаем индекс массива числа картинок
        indNumThingsArr = settings.getIndNumThingsArr();
        //читаем индекс массива времен запоминания
        indMemTimeArr = settings.getIndMemTimeArr();

        decThingsNum_ib = (ImageButton) findViewById(R.id.decThingsNum_ib);
        incThingsNum_ib = (ImageButton) findViewById(R.id.incThingsNum_ib);
        thingsNumVal_tv = (TextView) findViewById(R.id.thingsNumVal_tv);

        decMemTime_ib = (ImageButton) findViewById(R.id.decMemTime_ib);
        incMemTime_ib = (ImageButton) findViewById(R.id.incMemTime_ib);
        memTimeVal_tv = (TextView) findViewById(R.id.memTimeVal_tv);

        new EnterInt(MainActivity.this, numThingsArr, indNumThingsArr,
                decThingsNum_ib, R.drawable.minus,
                incThingsNum_ib, R.drawable.plus, thingsNumVal_tv);
        new EnterInt(MainActivity.this, memTimeArr, indMemTimeArr,
                decMemTime_ib, R.drawable.minus,
                incMemTime_ib, R.drawable.plus, memTimeVal_tv);
    }

    @Override
    public void onPause() {
        super.onPause();

        Settings settings = new Settings(this);
        //запоминаем индекс массива числа картинок
        indNumThingsArr = EnterInt.getCurrentIndex(thingsNumVal_tv, numThingsArr);
        settings.setIndNumThingsArr(indNumThingsArr);
        //запоминаем индекс массива времен запоминания
        indMemTimeArr = EnterInt.getCurrentIndex(memTimeVal_tv, memTimeArr);
        settings.setIndMemTimeArr(indMemTimeArr);
    }

    //вызывается из диалога DlgWithTwoButtons
    public void fDo () {
            finish(); //покидаем Activity
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            call_exit_dlg();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void call_exit_dlg() {
        DlgWithTwoButtons myDialogFragment = new DlgWithTwoButtons(getString(R.string.exitGameActivity), getString(R.string.areYouSure),
                getString(R.string.yes_option), getString(R.string.no_option), this);
        myDialogFragment.show(getSupportFragmentManager(), "myDialog");
    }
}
