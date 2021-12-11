
package com.bignerdranch.android.gridviewwithpictures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.Toast;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static java.lang.Math.*;

//import java.io.IOException;
//import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;



public class GameActivity extends AppCompatActivity implements DoForPositive1{
    private PictRes pictRes = PictRes.instance();
    private Random rand = new Random(new Date().getTime());

//    private boolean allowGridClicks = false;

    private ImageAdapter adapter1, adapter2;
    private TextView mSelectText;

    public LinearLayout mainLayout0, mainLayout;
    private int mLinearLayoutW, mLinearLayoutH; //ширина и высота mainLayout (когда они определились)
    TextView mThumbnail;
    public GridView gridview1, gridview2;

    private Button mReportButton, mButton3, mButton4, mButton5, mButton6, mButton7, mButton8;

    private Button[] mGameButtons;// = new Button[] {mButton3, mButton4, mButton5, mButton6, mButton7, mButton8};

    private TextView textViewTime;

    private int colN = 3;
    public int colW;
    public int leftPad1, leftPad2;
    public int rightPad1, rightPad2;
    public int topPad1, topPad2;
    public int bottomPad1, bottomPad2;

    private int randCellIndOfLeftGrid; //рандомный номер картинки левого grid
    private int randCellIndOfRightGrid; //рандомный номер картинки правого grid

    private SolveTimer solveTimer;

    //количество типов игр: 3*3, 4*4, 5*5, 6*6, 7*7, 8*8
    private int nGameTypes = 6;
    //кол-во попыток в каждой игре для фиксирования результата и записи в файл
    private int nAttempts = 3;
    //лучшие результаты (усредн) игрока (в секундах) по всем типам игр:
    // 3*3, 4*4, 5*5, 6*6, 7*7, 8*8
    private ArrayList<Integer> mBestResults;
    //текущие результаты игрока по всем nAttempts попыткам (в секундах)
    // для каждого типа игр: 3*3, 4*4, 5*5, 6*6, 7*7, 8*8
    private ArrayList<ArrayList<Integer>> mCurrResults;
    //текущие усредненные результаты игрока (в секундах)
    // для каждого типа игр:pictRes 3*3, 4*4, 5*5, 6*6, 7*7, 8*8
    private ArrayList<Integer> mCurrAvgResults;
    //текущее количество попыток по каждой игре
    // после завершения последней удачной попытки (или при прерывании текущего сеанса) это количество сбрасывается в ноль
    private ArrayList<Integer> nAttemptsForGames;

    private String  mStatisticFn = "statistic4";
    // Параметры SolveTimer
    private long bigMillis = 36000000; // = 10 часам;максим. число мс,
                                       // которое user явно не будет тратить на решение
    private long limitSec = 3599; // максим. возможное время обдумывания попытки игры (равно: (1 час - 1 сек))

    private long intervalMs = 1000;    //tick of SolveTimer (мс)
    private long timeElapsedMs;          //израсходовано мс с начала решения задачи

    private static String gameTitles[] = {"3*3", "4*4","5*5","6*6","7*7","8*8"};

    private int mCurrGame = -1; //(0)3*3, (1)4*4, (2)5*5, (3)6*6, (4)7*7, (5)8*8

    private int mNewGame = -1; //(0)3*3, (1)4*4, (2)5*5, (3)6*6, (4)7*7, (5)8*8

    private int dlg2Kind;

    public int getCurrGame() {
        return mCurrGame;
    }

    public Button getGameButton(int ind) {
        return mGameButtons[ind];
    }
    public String getGameTitle(int ind) {
        return gameTitles[ind];
    }

    private void initHelpingObjects() {
        mCurrResults = new ArrayList<>();
        nAttemptsForGames = new ArrayList<>();
        for (int i = 0; i < nGameTypes; i++) {
            ArrayList<Integer> attempt = new ArrayList<>();
            for (int j = 0; j < nAttempts; j++) {
                attempt.add(-1);
            }
            mCurrResults.add(attempt);
            nAttemptsForGames.add(0);
        }
    }

    private void updateCurrResult(int numGame, int attempt) {
        //attempt отсчитывается от 1
        mCurrResults.get(numGame).set(attempt - 1, msToSec(timeElapsedMs));
    }

    private void updateResults(int numGame, int currAvg) {
        if (currAvg < mBestResults.get(numGame) || mBestResults.get(numGame) == -1)
            mBestResults.set(numGame, currAvg);

        mCurrAvgResults.set(numGame, currAvg);
    }

    private void loadStatistic() {
        mBestResults = new ArrayList<>();
        mCurrAvgResults = new ArrayList<>();
        for(int i = 0; i < nGameTypes; i++) {
            mBestResults.add(-1);
            mCurrAvgResults.add(-1);
        }

        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(openFileInput(mStatisticFn)));
            for(int i = 0; i < nGameTypes; i++) {
                mBestResults.set(i, dis.readInt());
                mCurrAvgResults.set(i, dis.readInt());
            }
            dis.close();
        } catch (FileNotFoundException e) {
            return;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveStatistic() {
        try {
            DataOutputStream dis = new DataOutputStream(new BufferedOutputStream(openFileOutput(mStatisticFn, MODE_PRIVATE)));
            for(int i = 0; i < nGameTypes; i++) {
                dis.writeInt(mBestResults.get(i));
                dis.writeInt(mCurrAvgResults.get(i));
            }
            dis.close();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void beforeRepaintGrids(int colN) {
        if (solveTimer.isStarted()) {//игра mCurrGame уже ранее стартовала
            mNewGame = colN - 3;
            String title;
            if (mNewGame == mCurrGame) {
                title = getString(R.string.restart_game, gameTitles[mCurrGame]);
            } else {
                title = getString(R.string.break_curr_start_new_game, gameTitles[mCurrGame], gameTitles[mNewGame]);
            }
            dlg2Kind = 1;
            DlgWithTwoButtons myDialogFragment = new DlgWithTwoButtons(title, "",
                    getString(R.string.ok_option), getString(R.string.cancel_option), this);
            myDialogFragment.show(getSupportFragmentManager(), "myDialog");
        } else {
            repaintGrids(colN);
        }
    }

    public void repaintGrids(int colN) {
        int numGame = colN - 3;
        mGameButtons[numGame].setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_green)));//выделенный цвет
        if (mCurrGame > -1 && mCurrGame != numGame) {
            mGameButtons[mCurrGame].setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_500))); //дефолтный цвет
        }
        mCurrGame = numGame;
        gridview1.setNumColumns(colN);
        gridview2.setNumColumns(colN);
        leftPad1 =   gridview1.getPaddingLeft();
        leftPad2 =   gridview2.getPaddingLeft();
        rightPad1 =  gridview1.getPaddingRight();
        rightPad2 =  gridview2.getPaddingRight();
        topPad1 =    gridview1.getPaddingTop();
        topPad2 =    gridview2.getPaddingTop();
        bottomPad1 = gridview1.getPaddingBottom();
        bottomPad2 = gridview2.getPaddingBottom();
        int gridW = (mLinearLayoutW - (leftPad1 + rightPad1 + leftPad2 + rightPad2)) / 2;
        int gridH = mLinearLayoutH - (topPad1 + bottomPad1);
        gridW = min(gridW, gridH);
        colW = (gridW - (colN - 1) * gridview1.getHorizontalSpacing()) / colN;
        //подкорректируем gridw из-за ошибок округления предыдущей строки
        gridW = colW * colN + (colN - 1) * gridview1.getHorizontalSpacing();

        //подкорректируем позцию второго gridview, чтобы он отстоял от правого края ровно на android:paddingRight
        int delta = mLinearLayoutW - gridW * 2 - (leftPad1 + rightPad1 + leftPad2 + rightPad2);
        int layoutW1 = gridW + leftPad1 + rightPad1 + delta;
        int layoutW2 = mLinearLayoutW - layoutW1;
        gridview1.setLayoutParams(new LinearLayout.LayoutParams(layoutW1, MATCH_PARENT));
        gridview2.setLayoutParams(new LinearLayout.LayoutParams(layoutW2, MATCH_PARENT));

        gridview1.setColumnWidth(colW);
        gridview2.setColumnWidth(colW);

        adapter1 = new ImageAdapter(GameActivity.this);
        adapter2 = new ImageAdapter(GameActivity.this);

        Custom2 custom2 = pictRes.getCustomArrays(colN * colN);
        // вычисляем рандомный номер картинки левого grid
        randCellIndOfLeftGrid = rand.nextInt(colN * colN);
        // вычисляем рандомный номер картинки правого grid
        randCellIndOfRightGrid = rand.nextInt(colN * colN);
        // обеспечиваем вставку картинки c номером randCellIndOfLeftGrid левого grid
        // на вычисленную позицию randCellIndOfRightGrid в правый grid
        custom2.second.set(randCellIndOfRightGrid, custom2.first.get(randCellIndOfLeftGrid));
        adapter1.LoadArr(custom2.first);
        adapter2.LoadArr(custom2.second);

        gridview1.setOnItemClickListener(new GridViewOnItemClickListener());
        gridview2.setOnItemClickListener(new GridViewOnItemClickListener());

        timerStart();

        gridview1.setAdapter(adapter1);
        gridview2.setAdapter(adapter2);
    }

    private void timerStart() {
        showElapsedTime(0);
        solveTimer.myStart();;
    }

    private void report() {
        LayoutInflater inflater = getLayoutInflater();
        View dlgView = inflater.inflate(R.layout.statistic_dlg, null);
        TextView textViewGames = (TextView) dlgView.findViewById(R.id.textViewGames);
        TextView textViewRecords = (TextView) dlgView.findViewById(R.id.textViewRecords);
        TextView textViewLastResults = (TextView) dlgView.findViewById(R.id.textViewLastResults);

        StringBuilder builder1 = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();
        StringBuilder builder3 = new StringBuilder();

        builder1.append(getString(R.string.game_title));
        builder2.append(getString(R.string.records));
        builder3.append(getString(R.string.last_results));

        for (int i = 0; i < nGameTypes; i++) {
            builder1.append("[").append(gameTitles[i]).append("]\n");
            String record = (mBestResults.get(i) == -1) ? getString(R.string.no_results) : (Integer.toString(mBestResults.get(i)) + "\n");
            builder2.append(record);
            record = (mCurrAvgResults.get(i) == -1) ? getString(R.string.no_results) : (Integer.toString(mCurrAvgResults.get(i)) + "\n");
            builder3.append(record);
        }
        textViewGames.append(builder1.toString());
        textViewRecords.append(builder2.toString());
        textViewLastResults.append(builder3.toString());

        DlgStatistic myDialogFragment = new DlgStatistic("OK", dlgView);
        myDialogFragment.show(getSupportFragmentManager(), "myDialog");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //для альбомного режима
        setContentView(R.layout.activity_game);

        mainLayout0 = (LinearLayout)findViewById(R.id.mainLayout0);
        mainLayout = (LinearLayout)findViewById(R.id.mainLayout);

        mThumbnail = (TextView)findViewById(R.id.thumbnail);

        gridview1 = (GridView) findViewById(R.id.gridView1);
        gridview2 = (GridView) findViewById(R.id.gridView2);

        mReportButton = (Button) findViewById(R.id.report_button);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideThumbCallFunc(1, 0);
            }
        });

        mButton3 = (Button) findViewById(R.id.button3);
        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideThumbCallFunc(2, 3);
            }
        });
        mButton4 = (Button) findViewById(R.id.button4);
        mButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideThumbCallFunc(2, 4);
            }
        });
        mButton5 = (Button) findViewById(R.id.button5);
        mButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideThumbCallFunc(2, 5);
            }
        });
        mButton6 = (Button) findViewById(R.id.button6);
        mButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideThumbCallFunc(2, 6);
            }
        });
        mButton7 = (Button) findViewById(R.id.button7);
        mButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideThumbCallFunc(2, 7);
            }
        });
        mButton8 = (Button) findViewById(R.id.button8);
        mButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideThumbCallFunc(2, 8);
            }
        });

        mGameButtons = new Button[] {mButton3, mButton4, mButton5, mButton6, mButton7, mButton8};

        textViewTime = (TextView) findViewById(R.id.textViewTime);

        solveTimer = new SolveTimer(bigMillis, intervalMs);

        loadStatistic();

        initHelpingObjects();
    }

    private void hideThumbCallFunc(int what, int colN) {
        //what - какую функцию вызвать после удаления mThumbnail:
        //what =  1: вызываем report()
        //what == 2: вызываем repaintGrids(colN)
        if (mThumbnail.getVisibility() == View.VISIBLE) {
            mThumbnail.setVisibility(View.GONE);
            mainLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (left == 0 && top == 0 && right == 0 && bottom == 0)
                        return;
                    mLinearLayoutW = mainLayout.getWidth();
                    mLinearLayoutH = mainLayout.getHeight();
                    mainLayout.removeOnLayoutChangeListener(this);
                    if (what == 1) report(); else beforeRepaintGrids(colN);
                }
            });
        } else {
            if (what == 1) report(); else beforeRepaintGrids(colN);
        }
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        saveStatistic();
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        loadStatistic();
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        loadStatistic();
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        saveStatistic();
    }

    private class SolveTimer extends CountDownTimer {
        private boolean started;
        public SolveTimer(long millisInFuture, long interval) {
            super(millisInFuture, interval);
        }
        public boolean isStarted() {
            return started;
        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timeElapsedMs = bigMillis - millisUntilFinished;
            showElapsedTime(timeElapsedMs);
        }

        public void myStart() {
            start();
            started = true;
        }

        public void myCancel() {
            cancel();
            started = false;
        }
    }
    private static int msToSec(long ms) {
        return (int)(ms / 1000);
    }

    private void showElapsedTime(long ms) {
        long s0 = ms / 1000;
        long h = s0 / 3600;
        long m = s0 % 3600 / 60;
        long s = s0 % 60;
        String time = String.format("%02d:%02d", m, s);
        textViewTime.setText(time);
        if (s0 == limitSec) {
            solveTimer.myCancel();
            String notifyStr = getString(R.string.timerFinished, gameTitles[mCurrGame]);
            nAttemptsForGames.set(mCurrGame, 0); //число попыток игры обнуляется
            startPlaying(R.raw.incorrect_attempt);
            DlgWithOneButton myDialogFragment = new DlgWithOneButton("", notifyStr, "OK");
            myDialogFragment.show(getSupportFragmentManager(), "myDialog");
        }
    }

    public void attemptFinishActions(int numGame, int  mode) {
        //останавливаем таймер
        solveTimer.myCancel();
        //не реагируем на нажатия ячеек обоих gridview
        gridview1.setOnItemClickListener(null);
        gridview2.setOnItemClickListener(null);

        if (mode == 2) {
            nAttemptsForGames.set(numGame, 0); //число попыток игры обнуляем
        }
    }

    private class GridViewOnItemClickListener implements GridView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            //выясним количество строк (или столбцов) gridview1 и gridview2
            int colN = gridview1.getNumColumns();

            //смотрим номер предыдущей попытки
            int numGame = colN - 3; //номер игры считается с нуля.
            attemptFinishActions(numGame, 1);

            String gameTitle = gameTitles[numGame];
            int currAttemptForGame = nAttemptsForGames.get(numGame);

            String notifyStr;
            int soundRid;
            int avg = 0;
            if (((parent == gridview1) && (position == randCellIndOfLeftGrid)) ||
                    ((parent == gridview2) && (position == randCellIndOfRightGrid))) { //угадал
                currAttemptForGame++; //инкрементируем номер попытки
                nAttemptsForGames.set(numGame, currAttemptForGame);
                if (currAttemptForGame < nAttempts) {
                    soundRid = R.raw.correct_attempt;
                    updateCurrResult(numGame, currAttemptForGame);
                    notifyStr = getString(R.string.attempt_correct, currAttemptForGame, gameTitle);
                } else { //currAttemptForGame == nAttempts
                    updateCurrResult(numGame, currAttemptForGame);
                    //вычислим среднее значение (в сек.) среди nAttempts попыток в конкретной игре
                    avg = 0;
                    for (int i = 0; i < nAttempts; i++) {
                        avg += mCurrResults.get(numGame).get(i);
                    }
                    avg = (int)Math.round(1. * avg / nAttempts);

                    notifyStr = getString(R.string.all_attempts_correct, gameTitle, nAttempts, avg);

                    nAttemptsForGames.set(numGame, 0); //число попыток игры обнуляем
                    updateResults(numGame, avg);
                    soundRid = R.raw.correct_game;
                }

                if (parent == gridview1) {
                    mixGridCellWithDrawable(gridview1, position, (ImageView)v, R.drawable.correct);
                    mixGridCellWithDrawable(gridview2, randCellIndOfRightGrid, (ImageView)gridview2.getChildAt(randCellIndOfRightGrid), R.drawable.correct);
                } else {
                    mixGridCellWithDrawable(gridview2, position, (ImageView)v, R.drawable.correct);
                    mixGridCellWithDrawable(gridview1, randCellIndOfLeftGrid, (ImageView)gridview1.getChildAt(randCellIndOfLeftGrid), R.drawable.correct);
                }

                //добавляем в текст кнопки текущей игры номер удачной попытки (если номер попытки < nAttempts),
                //либо выводим обычный текст кнопки
                String gameButtonText = (currAttemptForGame < nAttempts) ?
                        gameTitles[numGame] + " (" + currAttemptForGame + ")" : gameTitles[numGame];
                getGameButton(numGame).setText(gameButtonText);
            } else { //НЕ угадал
                notifyStr = getString(R.string.attempt_incorrect, gameTitle);
                nAttemptsForGames.set(numGame, 0); //число попыток игры обнуляется

                soundRid = R.raw.incorrect_attempt;

                if (parent == gridview1)
                    mixGridCellWithDrawable(gridview1, position, (ImageView)v, R.drawable.incorrect);
                else
                    mixGridCellWithDrawable(gridview2, position, (ImageView)v, R.drawable.incorrect);

                getGameButton(numGame).setText(gameTitles[numGame]);
            }

            startPlaying(soundRid);

            DlgWithOneButton myDialogFragment = new DlgWithOneButton("", notifyStr, "OK");
            myDialogFragment.show(getSupportFragmentManager(), "myDialog");

//            Toast toast = Toast.makeText(GameActivity.this, toastStr, Toast.LENGTH_LONG);
//            toast.show();
        }
    }

    private void mixGridCellWithDrawable(GridView grid, int pos, ImageView imageView, int rDrawableId) {
        int rIdCorrect = (int)grid.getAdapter().getItem(pos);
        Resources r = getResources();
        Drawable[] layers = new Drawable[2];
        layers[0] = r.getDrawable(rIdCorrect);
        layers[1] = r.getDrawable(rDrawableId);
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        imageView.setImageDrawable(layerDrawable);
    }
    private void startPlaying(int rid) {
        MediaPlayer mediaPlayer = MediaPlayer.create(GameActivity.this, rid);
        mediaPlayer.start();
    }

    public void fDo () {
    //вызывается из диалога DlgWithTwoButtons
        if (dlg2Kind == 1) {
            attemptFinishActions(mCurrGame, 2);
            getGameButton(mCurrGame).setText(getGameTitle(mCurrGame));
            repaintGrids(mNewGame + 3);
        }
        else {//dlg2Kind == 2
            solveTimer.myCancel();
            finish(); //покидаем Activity
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            dlg2Kind = 2;
            DlgWithTwoButtons myDialogFragment = new DlgWithTwoButtons("", getString(R.string.exitGameActivity),
                    getString(R.string.yes_option), getString(R.string.no_option), this);
            myDialogFragment.show(getSupportFragmentManager(), "myDialog");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
