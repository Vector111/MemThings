package com.bignerdranch.android.gridviewwithpictures;

import androidx.appcompat.app.AppCompatActivity;
import static com.bignerdranch.android.gridviewwithpictures.ImageButtonOperations.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import static com.bignerdranch.android.gridviewwithpictures.MySettings.*;
import android.content.Intent;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements DoForPositive1 {
    public SharedPreferences apppref;
    public static final String APP_PREFERENCES = "apppref";

    private ImageButton decThingsNum_ib;
    private ImageButton incThingsNum_ib;
    private TextView thingsNumVal_tv;
    private static int indNumThingsArr = 0;

    private ImageButton decMemTime_ib;
    private ImageButton incMemTime_ib;
    private TextView memTimeVal_tv;
    private static int indMemTimeArr = 0;

    private Button gotoGame_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gotoGame_btn = (Button) findViewById(R.id.gotoGame_btn);

        gotoGame_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMemorizeActivity();
            }
        });

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

//        finish(); //покидаем Activity
//        System.exit(0);
        //        android.os.Process.killProcess(android.os.Process.myPid());
        this.finishAffinity();
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

    private void goToMemorizeActivity() {
        Intent intent = MemorizeActivity.newIntent(MainActivity.this);
        startActivity(intent);
    }
}
