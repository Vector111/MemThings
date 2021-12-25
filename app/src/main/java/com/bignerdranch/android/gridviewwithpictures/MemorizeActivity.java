package com.bignerdranch.android.gridviewwithpictures;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
//import static com.bignerdranch.android.gridviewwithpictures.MainActivity.*;
import static com.bignerdranch.android.gridviewwithpictures.MySettings.*;
import static com.bignerdranch.android.gridviewwithpictures.Sounds.*;
import static com.bignerdranch.android.gridviewwithpictures.MyConvertions.*;

import java.util.Arrays;
import java.util.List;

public class MemorizeActivity extends AppCompatActivity implements DoForPositive1 {
//    private int state = 0; //отслеживает состояние активности
    public ConstraintLayout mainLayout;
    public ConstraintLayout upSubLayout;
//    public ConstraintLayout subLayout;
    public GridView grid;

//    private boolean spec_flag = false;
    private PictRes pictRes = PictRes.instance();
    private TextView memorizeActivityAim_tv;
    private int nThings; //число картинок для запоминания
    private int memTime; //время для запоминания (сек)
    private int rowsNum; //число строк grid
    private int columnsNum; //число столбцов grid
    private ImageAdapter adapter;
    private TextView timer_tv;
    private Button next_btn;

    private SolveTimer timer;


    boolean goOutFlag = false;

    List<Integer> customArr;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MemorizeActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //для портретного режима
//        subLayout = (ConstraintLayout)findViewById(R.id.subLayout);
        Settings settings = new Settings(this);
        //читаем индекс массива числа картинок
        int index1 = settings.getIndNumThingsArr();
        nThings = getThingNum(index1);
        rowsNum = getRowsNum(index1);
        columnsNum = nThings / rowsNum;
        int index2 = settings.getIndMemTimeArr();
        memTime = getMemTime(index2);

        mainLayout = (ConstraintLayout) findViewById(R.id.mainLayout);
        upSubLayout = (ConstraintLayout) findViewById(R.id.upSubLayout);
//        subLayout = (ConstraintLayout) findViewById(R.id.subLayout);
        memorizeActivityAim_tv = (TextView) findViewById(R.id.memorizeActivityAim_tv);
        grid = (GridView) findViewById(R.id.grid);
        timer_tv = (TextView) findViewById(R.id.timer_tv);

        next_btn = (Button) findViewById(R.id.next_btn);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.myCancel();
                goToRememberActivity();
            }
        });

        showTable();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        showTable();
//        int kkk = 0;
//    }
    private void goToRememberActivity() {
        Integer[] arr = customArr.toArray(new Integer[0]);

        int[] arr_int = toPrimitive(arr);

        Intent intent = RememberActivity.newIntent(MemorizeActivity.this, arr_int);
        startActivity(intent);
    }
    private void showTable() {
        mainLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {

//              if ((left == 0 && top == 0 && right == 0 && bottom == 0) || spec_flag)
                if (left == 0 && top == 0 && right == 0 && bottom == 0)
                    return;

                mainLayout.removeOnLayoutChangeListener(this);

//                spec_flag = true;
                int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
                int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

                //КОСТЫЛЬ! Не знаю, как программно получить android:layout_marginTop и т.п.
                //10dp ==> px
                int marginInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

                int maxGridHeight = screenHeight - memorizeActivityAim_tv.getHeight() - marginInPx -
                    upSubLayout.getHeight() - marginInPx;

                grid.setNumColumns(columnsNum);

                int gridW = screenWidth - grid.getPaddingLeft() - grid.getPaddingRight();
                int cellW = (gridW - (columnsNum - 1) * grid.getHorizontalSpacing()) / columnsNum;

                int gridH = maxGridHeight - grid.getPaddingTop() - grid.getPaddingBottom();
                int cellH = (gridH - (rowsNum - 1) * grid.getVerticalSpacing()) / rowsNum;

                int cellDim = Math.min(cellW, cellH);
                gridW = cellDim * columnsNum + (columnsNum - 1) * grid.getHorizontalSpacing();
                gridH = cellDim * rowsNum + (rowsNum - 1) * grid.getVerticalSpacing();

                int leftPadding = (screenWidth - gridW) / 2;
                int rightPadding = leftPadding;
                int topPadding = (maxGridHeight - gridH) / 2;
                int bottomPadding = topPadding;
                grid.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
                grid.setLayoutParams(new LinearLayout.LayoutParams
                    (gridW + leftPadding * 2,gridH + topPadding * 2));

                grid.setColumnWidth(cellDim);

                adapter = new ImageAdapter(MemorizeActivity.this);
                customArr = pictRes.getCustomArray(rowsNum * columnsNum);
                adapter.LoadArr(customArr);
                grid.setAdapter(adapter);

                timerStart();
            }
        });
    }

    private void timerStart() {
        timer = new SolveTimer((memTime + 1) * 1000, 1000);
        timer.myStart();
//        new CountDownTimer((memTime + 1) * 1000, 1000) {
//            public void onTick(long millisUntilFinished) {
//                showRemainingTime(millisUntilFinished); //toDo
//                if(goOutFlag)
//                    this.cancel();
//            }
//            public void onFinish() {
//                goToRememberActivity(MemorizeActivity.this, RememberActivity.class);
//            }
//        }.start();
    }

    private class SolveTimer extends CountDownTimer {
        private boolean started;
        private long remainSec;
        public long getRemainSec()
        {
            return remainSec;
        }
        public SolveTimer(long millisInFuture, long interval) {
            super(millisInFuture, interval);
        }

        public void myStart() {
            start();
            started = true;
        }

        public boolean isStarted() {
            return started;
        }

        @Override
        public void onFinish() {
            clearContent();
            startPlaying(MemorizeActivity.this, R.raw.metronom_finish);
//            goToRememberActivity(MemorizeActivity.this, RememberActivity.class);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            remainSec = millisUntilFinished / 1000;
            showRemainingTime(millisUntilFinished); //toDo
            if (remainSec <= 4 && remainSec >= 1)
                startPlaying(MemorizeActivity.this, R.raw.metronom_before_finish);

            if(goOutFlag)
                myCancel();
        }

        public void myCancel() {
            cancel();
            started = false;
        }
    }





    private void showRemainingTime(long ms) {
        long s0 = ms / 1000;
        long h = s0 / 3600;
        long m = s0 % 3600 / 60;
        long s = s0 % 60;
        String time = String.format("%02d:%02d", m, s);
        timer_tv.setText(time);

    }
    public void fDo () {

//        finish(); //покидаем Activity
//        android.os.Process.killProcess(android.os.Process.myPid());
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish(); //покидаем Activity
//        System.exit(0);
        goOutFlag = true;
        timer.myCancel();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
//        this.finishAffinity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            call_exit_dlg();
            return true;
//            if(timer.getRemainSec() > 7) {
//                call_exit_dlg();
//                return true;
//            } else
//                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void call_exit_dlg() {
        DlgWithTwoButtons myDialogFragment = new DlgWithTwoButtons(getString(R.string.exitGameActivity), getString(R.string.areYouSure),
            getString(R.string.yes_option), getString(R.string.no_option), this);
        myDialogFragment.show(getSupportFragmentManager(), "myDialog");
    }

    private void clearContent()
    {
        grid.setVisibility(View.GONE);
        memorizeActivityAim_tv.setVisibility(View.GONE);
        timer_tv.setVisibility(View.GONE);
    }
}