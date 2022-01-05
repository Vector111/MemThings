package com.bignerdranch.android.gridviewwithpictures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.bignerdranch.android.gridviewwithpictures.MyFiles.*;
import static com.bignerdranch.android.gridviewwithpictures.MyConvertions.*;
import static com.bignerdranch.android.gridviewwithpictures.MyKeyBoard.*;
import static com.bignerdranch.android.gridviewwithpictures.MySettings.getRowsNum;
import static com.bignerdranch.android.gridviewwithpictures.MySettings.getThingNum;
import static com.bignerdranch.android.gridviewwithpictures.Sounds.mediaPlayer;
import static com.bignerdranch.android.gridviewwithpictures.Sounds.startPlaying;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RememberActivity extends AppCompatActivity implements DoForPositive1 {
    private static final String EXTRA_ALS =
        "com.bignerdranch.android.gridviewwithpictures.als";
    private static final String EXTRA_ALI =
        "com.bignerdranch.android.gridviewwithpictures.ali";
    private static final String EXTRA_CUSTOM_ARR =
        "com.bignerdranch.android.gridviewwithpictures.customArr";

    public ConstraintLayout mainLayout;
    public ConstraintLayout upSubLayout;
    public GridView grid;
    private TextView rememberActivityAim_tv;
    private int nThings; //число картинок для запоминания
    private int rowsNum; //число строк grid
    private int columnsNum; //число столбцов grid
    private MyAdapter adapter;
    private TextView timer_tv;
    private Button ready_btn;

    // Параметры RememberTimer
    private static final long bigMillis = 36000000; // = 10 часам - максим. число мс,
                                                    // которое user явно не будет тратить на решение
    private static final long intervalMs = 1000;    //один tick of RememberTimer (мс)
    private long timeElapsedMs;                     //израсходовано мс с начала решения задачи
    private RememberTimer timer;
    boolean goOutFlag = false;

    // alS и alI получены от MemorizeActivity, als отсортирована по возрастанию
    // название фото в als соответствует ресурсному id соответствующего файла фото
    private ArrayList<String> alS;  // Названия фотографий
    private ArrayList<Integer> alI; // Resource IDS файлов фотографий (IDS могут повторяться)
    private HashSet<Integer> customSet; // будет хранить множество ресурных id картинок, которые запоминал юзер

    private int dlg2Kind;

    public static Intent newIntent(Context context, ArrayList<String> alS,
        ArrayList<Integer> alI, ArrayList<Integer> customArr)
    {
        Intent intent = new Intent(context, RememberActivity.class);
        intent.putStringArrayListExtra(EXTRA_ALS, alS);
        intent.putIntegerArrayListExtra(EXTRA_ALI, alI);
        intent.putIntegerArrayListExtra(EXTRA_CUSTOM_ARR, customArr);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        alS = getIntent().getStringArrayListExtra(EXTRA_ALS);
        alI = getIntent().getIntegerArrayListExtra(EXTRA_ALI);
        // customArr - выборка запоминаемых юзером ресурных id картинок
        ArrayList<Integer> customArr = getIntent().getIntegerArrayListExtra(EXTRA_CUSTOM_ARR);
        customSet = (HashSet<Integer>) convertListToSet(customArr);

        Settings settings = new Settings(this);
        //читаем индекс массива числа картинок
        int index1 = settings.getIndNumThingsArr();
        nThings = getThingNum(index1);
        rowsNum = getRowsNum(index1);
        columnsNum = nThings / rowsNum;

        mainLayout = (ConstraintLayout) findViewById(R.id.mainLayout1);
        upSubLayout = (ConstraintLayout) findViewById(R.id.upSubLayout1);
        rememberActivityAim_tv = (TextView) findViewById(R.id.rememberActivityAim_tv);
        grid = (GridView) findViewById(R.id.seek_grid);
        timer_tv = (TextView) findViewById(R.id.timer_tv1);

        ready_btn = (Button) findViewById(R.id.ready_btn);
        ready_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.myCancel();
            }
        });

        suspendedShow();
    }

    private void suspendedShow() {
        mainLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (left == 0 && top == 0 && right == 0 && bottom == 0)
                    return;

                mainLayout.removeOnLayoutChangeListener(this);

                int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
                int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

                //КОСТЫЛЬ! Не знаю, как программно получить android:layout_marginTop и т.п.
                //10dp ==> px
                int marginInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

                int maxGridHeight = screenHeight - rememberActivityAim_tv.getHeight() - marginInPx -
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

                adapter = new MyAdapter(RememberActivity.this, cellDim);

                //ToDo: подготовить иконки со знаком вопроса "?"
                ArrayList<Integer> adapterArray = new ArrayList<>();
                for (int i = 0; i < nThings; ++i){
                    adapterArray.add(R.drawable.question_mark);
                }
                adapter.LoadArr(adapterArray);
                grid.setAdapter(adapter);

//                textView.addTextChangedListener (new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    }
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        String s1 = s.toString();
//                        int kkk = 0;
//                    }
//                });

                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
                String[] stringArray = Arrays.copyOf(alS.toArray(), alS.toArray().length, String[].class);
                ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(RememberActivity.this, android.R.layout.simple_list_item_1, stringArray);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void	onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        hideKeyboard(RememberActivity.this);
                        autoCompleteTextView.setText("");
                    }
                });

                timerStart();
            }
        });
    }

    private void timerStart() {
        timer = new RememberTimer(bigMillis, intervalMs);
        timer.myStart();
    }

    private class RememberTimer extends CountDownTimer {
        private boolean started;

        public RememberTimer(long millisInFuture, long interval) {
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
//            clearContent();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timeElapsedMs = bigMillis - millisUntilFinished;
            showElapsedTime(timeElapsedMs);

            if(goOutFlag)
                myCancel();
        }

        public void myCancel() {
            cancel();
//            if (mediaPlayer != null)
//                mediaPlayer.stop();
            started = false;
        }
    }

    private void showElapsedTime(long ms) {
        long s0 = ms / 1000;
        long h = s0 / 3600;
        long m = s0 % 3600 / 60;
        long s = s0 % 60;
        String time = String.format("%03d:%02d", m, s);
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
//        finish();
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

//    private void clearContent()
//    {
//        grid.setVisibility(View.GONE);
//        rememberActivityAim_tv.setVisibility(View.GONE);
//        timer_tv.setVisibility(View.GONE);
//    }

}