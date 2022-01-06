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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
//import static com.bignerdranch.android.gridviewwithpictures.MainActivity.*;
import static com.bignerdranch.android.gridviewwithpictures.MySettings.*;
import static com.bignerdranch.android.gridviewwithpictures.Settings.*;
import static com.bignerdranch.android.gridviewwithpictures.MyFiles.*;
import static com.bignerdranch.android.gridviewwithpictures.Sounds.*;
import static com.bignerdranch.android.gridviewwithpictures.MyConvertions.*;
import static com.bignerdranch.android.gridviewwithpictures.MyRandoms.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MemorizeActivity extends AppCompatActivity implements DoForPositive1 {
    public ConstraintLayout mainLayout;
    public ConstraintLayout upSubLayout;
    public GridView grid;
    private PictRes pictRes = PictRes.instance();
    private TextView memorizeActivityAim_tv;
    private int nThings; //число картинок для запоминания
    private int memTime; //время для запоминания (сек)
    private int rowsNum; //число строк grid
    private int columnsNum; //число столбцов grid
    private MyAdapter adapter;
    private TextView timer_tv;
    private Button next_btn;
    private SolveTimer timer;
    boolean goOutFlag = false;
    private ArrayList<Integer> customArr;
    private ArrayList<Pair<String, Integer>> pairsArrSI_IdMayNotUniq; //массив уникальных пар,
            // где pair.first = <название картинки>,pair.second = <идентификатор ресурса картинки>
            // pair.second не обязаны быть уникальными
    private ArrayList<Pair<String, Integer>> pairsArrSI_IdUniq; //массив уникальных пар,
            // где pair.first = <название картинки>,pair.second = <идентификатор ресурса картинки>
            // pair.second обязаны быть уникальными

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MemorizeActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Settings settings = new Settings(this);
        //читаем индекс массива числа картинок
        int index1 = settings.getIndNumThingsArr();
        nThings = getThingNum(index1);
        rowsNum = getRowsNum(index1);
        columnsNum = nThings / rowsNum;
        int index2 = settings.getIndMemTimeArr();
        memTime = getMemTime(index2);

        // Генерируем pairsArrSI
        formPairsArrSI();

        mainLayout = (ConstraintLayout) findViewById(R.id.mainLayout);
        upSubLayout = (ConstraintLayout) findViewById(R.id.upSubLayout);
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

        suspendedShow();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        suspendedShow();
//        int kkk = 0;
//    }
    private void formPairsArrSI()
    {
        pairsArrSI_IdMayNotUniq = form_pairsArrSI_withFlag(false);
        // Сортируем pairsArrSI_IdMayNotUniq по названию фото (по возрастанию)
        Collections.sort(pairsArrSI_IdMayNotUniq, new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(final Pair<String, Integer> o1, final Pair<String, Integer> o2) {
                return (o1.first.compareTo(o2.first));
            }
        });
        pairsArrSI_IdUniq = form_pairsArrSI_withFlag(true);
    }

    private ArrayList<Pair<String, Integer>> form_pairsArrSI_withFlag(boolean str2_uniq)
    {
        // Читаем из файла "cards.txt" список пар строк,
        // где pair.first = <название картинки> и
        // pair.second = <имя файла картинки (без расширения)>
        // если str2_uniq = false, то множество pair.second не обязано быть уникальным)
        ArrayList<Pair<String, String>> selPairsArr = (ArrayList)getPairsList(this, "cards.txt", str2_uniq);
        //Заполняем ret
        ArrayList<Pair<String, Integer>> ret = new ArrayList<>();
        for (int i = 0; i < selPairsArr.size(); ++i){
            int resID = getResources().getIdentifier(selPairsArr.get(i).second, "drawable", getPackageName());
            Pair<String, String> pair1 = selPairsArr.get(i);
            Pair<String, Integer> pair2 = new Pair<>(pair1.first, resID);
            ret.add(pair2);
        }
        return ret;
    }

    private void formCustomArr()
    {
        // Получим выборку для запоминания картинок
        // rowsNum * columnsNum случайных натуральных чисел из диапазона [0 ... (pairsArrSI_IdUniq - 1)]
        HashSet<Integer> set = (HashSet)getRandomUniqSubset(rowsNum * columnsNum, pairsArrSI_IdUniq.size());
        // Адаптируем эту выборку для customArr
        customArr = new ArrayList<>();
        Iterator iter = set.iterator();
        for (int i = 0; i < set.size(); i++) {
            Pair<String, Integer> pair = pairsArrSI_IdUniq.get((int)iter.next());
            customArr.add(pair.second);
        }
    }

    private void goToRememberActivity()
    {
        ArrayList<String> alS = new ArrayList<>();
        ArrayList<Integer> alI = new ArrayList<>();
        for (int i = 0; i < pairsArrSI_IdMayNotUniq.size(); ++i){
            alS.add(pairsArrSI_IdMayNotUniq.get(i).first);
            alI.add(pairsArrSI_IdMayNotUniq.get(i).second);
        }
        Intent intent = RememberActivity.newIntent(MemorizeActivity.this,
            alS, alI, customArr);
        startActivity(intent);
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

                adapter = new MyAdapter(MemorizeActivity.this, cellDim);

                // Генерируем customArr
                formCustomArr();

                adapter.LoadArr(customArr);
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

//                textView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//                    @Override
//                    public void	onItemClick(AdapterView<?> parent, View view, int position, long id)
//                    {
//                        int k = 0;
//                    }
//                });

                timerStart();
            }
        });
    }

    private void timerStart() {
        timer = new SolveTimer((memTime + 1) * 1000, 1000);
        timer.myStart();
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
           /* startPlaying(MemorizeActivity.this, R.raw.metronom_finish);*/
//            goToRememberActivity(MemorizeActivity.this, RememberActivity.class);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            remainSec = millisUntilFinished / 1000;
            showRemainingTime(millisUntilFinished);
            if (remainSec == 7)
                startPlaying(MemorizeActivity.this, R.raw.metronom_before_finish);

            if(goOutFlag)
                myCancel();
        }

        public void myCancel() {
            cancel();
            if (mediaPlayer != null)
                mediaPlayer.stop();
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

    private void clearContent()
    {
        grid.setVisibility(View.GONE);
        memorizeActivityAim_tv.setVisibility(View.GONE);
        timer_tv.setVisibility(View.GONE);
    }
}