package com.bignerdranch.android.gridviewwithpictures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.bignerdranch.android.gridviewwithpictures.MyConvertions.*;
import static com.bignerdranch.android.gridviewwithpictures.MyGrids.*;
import static com.bignerdranch.android.gridviewwithpictures.MyToasts.*;
import static com.bignerdranch.android.gridviewwithpictures.MyKeyBoard.*;
import static com.bignerdranch.android.gridviewwithpictures.MySettings.getRowsNum;
import static com.bignerdranch.android.gridviewwithpictures.MySettings.getThingNum;
import static com.bignerdranch.android.gridviewwithpictures.Sounds.startPlaying;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class RememberActivity extends AppCompatActivity implements DoForPositive1 {
    private static final String EXTRA_ALS =
        "com.bignerdranch.android.gridviewwithpictures.als";
    private static final String EXTRA_ALI =
        "com.bignerdranch.android.gridviewwithpictures.ali";
    private static final String EXTRA_CUSTOM_ARR =
        "com.bignerdranch.android.gridviewwithpictures.customArr";

    private static final String TAG = "MyTag";


    public ConstraintLayout mainLayout;
    public ConstraintLayout upSubLayout;
    private AutoCompleteTextView autoCompleteTextView;
    private TextView speek_tv;
    private TextView warningTextView;
    public GridView grid;
    private TextView rememberActivityAim_tv;
    private int nThings; //число картинок для запоминания
    private int rowsNum; //число строк grid
    private int columnsNum; //число столбцов grid
    private int cellDim; //ширина стороны квадрата ячейки
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
    private HashMap<String, Integer> map; //словарь для поиска ресурсных id по названиям фото
    private ArrayList<Integer> customArr;// выборка запоминаемых юзером ресурных id картинок
    private HashSet<Integer> customSet; // будет хранить множество ресурных id картинок, которые запоминал юзер

    private int dlg2Kind;
    // число нажатий кнопки ready_btn : если readyBtnPressTimes >= 1 ==>
    // текст кнопки будет переключаться между "Источник" и "Результат"
    private int readyBtnPressTimes = 0;
    private ArrayList<ImageView> imageViewArrayList;
    private ImageButton voiceInput_ib;
    private static int voiceInputIbState = 1;

    private SpeechRecognizer sr;
    private boolean bBreakSpeechListening;

    private AudioManager mAudioManager;
    private int mStreamVolume = 0;
    private Handler mHandler = new Handler();

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

        //Создаем словарь для поиска ресурсных id по названиям фото
        map = genStringIntegerMap(alS, alI);


        customArr = getIntent().getIntegerArrayListExtra(EXTRA_CUSTOM_ARR);
        customSet = (HashSet<Integer>) convertListToSet(customArr);

        Settings settings = new Settings(this);
        //читаем индекс массива числа картинок
        int index1 = settings.getIndNumThingsArr();
        nThings = getThingNum(index1);
        rowsNum = getRowsNum(index1);
        columnsNum = nThings / rowsNum;

        mainLayout = (ConstraintLayout) findViewById(R.id.mainLayout1);
        upSubLayout = (ConstraintLayout) findViewById(R.id.upSubLayout1);
        warningTextView = (TextView) findViewById(R.id.warningTextView);
        rememberActivityAim_tv = (TextView) findViewById(R.id.rememberActivityAim_tv);
        grid = (GridView) findViewById(R.id.seek_grid);
        timer_tv = (TextView) findViewById(R.id.timer_tv1);

        bBreakSpeechListening = false;

        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new listener());

        ready_btn = (Button) findViewById(R.id.ready_btn);
        ready_btn.setText(R.string.ready);
        ready_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg2Kind = 1;
                if(readyBtnPressTimes == 0) {
                    DlgWithTwoButtons myDialogFragment = new DlgWithTwoButtons("Уверены, что все вспомнили?", "",
                        getString(R.string.yes_option), getString(R.string.cancel_option), RememberActivity.this);
                    myDialogFragment.show(getSupportFragmentManager(), "myDialog");
                }
                else
                    fDo();

            }
        });

        speek_tv = (TextView) findViewById(R.id.speek_tv);
        voiceInput_ib = (ImageButton) findViewById(R.id.voiceInput_ib);
        voiceInput_ib.setImageResource(R.drawable.microphone_normal);
        voiceInput_ib.setBackgroundResource(R.drawable.bg_microphone_normal);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mStreamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // getting system volume into var for later un-muting

        if(voiceInput_ib.isEnabled()){
            voiceInputIbToPressedState();
            voiceInputIbState *= (-1);

            startListening();
        }

        voiceInput_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(voiceInputIbState == 1) {
                    voiceInputIbToPressedState();
                    bBreakSpeechListening = false;
                    startListening();
                }
                else {
                    voiceInputIbToNormalState();
                    bBreakSpeechListening = true;
                    sr.stopListening();
                    startAudioSound();
                }

                voiceInputIbState *= (-1);
            }
        });

        suspendedShow();
    }

    private void startListening()
    {
        mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0); // setting system volume to zero, muting

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"com.bignerdranch.android.gridviewwithpictures");

        String languagePref = "ru_RU";
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languagePref);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languagePref);
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, languagePref);

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,3);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,500);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,500);
        sr.startListening(intent);
        Log.i("111111","11111111");
    }

    private void voiceInputIbToPressedState()
    {
        voiceInput_ib.setImageResource(R.drawable.pause);
        voiceInput_ib.setBackgroundResource(R.drawable.bg_pause);
        speek_tv.setText(R.string.speak_pls);
        speek_tv.setBackgroundResource(R.drawable.golden_rod_shape);
    }

    private void voiceInputIbToNormalState()
    {
        voiceInput_ib.setImageResource(R.drawable.microphone_normal);
        voiceInput_ib.setBackgroundResource(R.drawable.bg_microphone_normal);
        speek_tv.setText("");
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

                cellDim = Math.min(cellW, cellH);
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

                adapter = new MyAdapter(RememberActivity.this, cellDim, 1);

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

                autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
                String[] stringArray = Arrays.copyOf(alS.toArray(), alS.toArray().length, String[].class);
                ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(RememberActivity.this, android.R.layout.simple_list_item_1, stringArray);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void	onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        hideKeyboard(RememberActivity.this);

                        attemptToInsertPhoto(autoCompleteTextView.getText().toString());

                        autoCompleteTextView.setText("");
                    }
                });

                grid.setOnItemClickListener(new GridViewOnItemClickListener());

                timerStart();
            }
        });
    }

    private void attemptToInsertPhoto(String photoName)
    {
        //Пытаемся вставить фото в соответствии с его названием
        photoName = photoName.toLowerCase();
        if(!map.containsKey(photoName))
            return;
        int rid = map.get(photoName); //id фото в соответствии с названием
        //Определяем позицию для вставки (попутно проверяя, не вставлен ли в grid уже такой же rid)
        int insertPos = -2; //позиция для вставки
        for (int i = 0; i < grid.getAdapter().getCount(); ++i){
            int rid1 = (int)((MyAdapter)grid.getAdapter()).getItem(i);
            if(rid1 == rid){
                insertPos = -1;
                break;
            }
            if (rid1 == R.drawable.question_mark){
                insertPos = i;
                break;
            }
        }
        if(insertPos < 0) { //проблемы
            startPlaying(RememberActivity.this, R.raw.user_error);
            String toastStr = (insertPos == -1) ? "Такое фото вы уже вставили!" :
                "Таблица уже заполнена! Либо Удалите неверный вариант, либо нажите кнопку [Готово]";
            myToastShow(RememberActivity.this, toastStr, Gravity.CENTER, Toast.LENGTH_SHORT );
        }
        else //нашлась доступная свободная ячейка
            setViewToGridCell(grid, insertPos, rid);
    }

    class listener implements RecognitionListener
    {
        public void onReadyForSpeech(Bundle params)
        {
            Log.d(TAG, "onReadyForSpeech");
            if(!bBreakSpeechListening) {
                speek_tv.setText(R.string.speak_pls);
            }
//            startAudioSound();
        }

        public void onBeginningOfSpeech()
        {
            Log.d(TAG, "onBeginningOfSpeech");
        }

        public void onRmsChanged(float rmsdB)
        {
            Log.d(TAG, "onRmsChanged");
        }

        public void onBufferReceived(byte[] buffer)
        {
            Log.d(TAG, "onBufferReceived");
        }

        public void onEndOfSpeech()
        {
            Log.d(TAG, "onEndofSpeech");
            if(!bBreakSpeechListening){
                speek_tv.setText("");
            }
        }

        public void onError(int error)
        {
            Log.d(TAG,  "error " +  error);

            if(!bBreakSpeechListening){
                String errS;
                if(error == 2) {
                    errS = "Подключите Интернет!";
                    voiceInputIbToNormalState();
                    voiceInputIbState *= (-1);
                    bBreakSpeechListening = true;
                    sr.stopListening();
                    speek_tv.setText(errS);
//                    startAudioSound();
                    return;
                }
                else if(error == 7) {
                    errS = "Говорите четче!";
                }
                else {
                    errS = "Проблемы речевого ввода";
                }
                speek_tv.setText(errS);
    //            Toast.makeText(RememberActivity.this, errS, Toast.LENGTH_SHORT).show();//toDo
                startListening();
            }
//            startAudioSound();
        }

        public void onResults(Bundle results)
        {
            Log.d(TAG, "onResults " + results);

            if(!bBreakSpeechListening) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for (int i = 0; i < data.size(); ++i) {
                    Log.d(TAG, "result " + data.get(i));
                    String strResult = data.get(i);
                    strResult = strResult.trim();
                    String[] words = strResult.split("\\s+");

                    //Сначала проверим совпадение по отдельным словам
                    for (int j = 0; j < words.length; ++j) {
                        String word = words[j];
                        attemptToInsertPhoto(word);
                    }
                    //Теперь проверим совпадение имени фото по двум подряд идущим через пробел словам
                    for (int j = 0; j < (words.length - 1); ++j) {
                        String photoName = words[j] + " " + words[j + 1];
                        attemptToInsertPhoto(photoName);
                    }
                }
                startListening();
            }
//            startAudioSound();
        }

        public void onPartialResults(Bundle partialResults)
        {
            Log.d(TAG, "onPartialResults");
        }

        public void onEvent(int eventType, Bundle params)
        {
            Log.d(TAG, "onEvent " + eventType);
        }
    }

    private void startAudioSound() {
        mHandler.postDelayed(() -> {
            mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, mStreamVolume, 0); // again setting the system volume back to the original, un-mutting
        }, 300);
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
        String time = String.format("%02d:%02d:%02d", h, m, s);
        timer_tv.setText(time);
    }

    private class GridViewOnItemClickListener implements GridView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
        {
            int pressRid = (int)((MyAdapter)parent.getAdapter()).getItem(position);
            if(pressRid != R.drawable.question_mark)
                setViewToGridCell(grid, position, R.drawable.question_mark);
        }
    }

    private void setViewToGridCell(GridView grid, int position, int rid)
    {
        ImageView imageView = (ImageView) grid.getChildAt(position);
        imageView.setImageResource(rid);
        ((MyAdapter)grid.getAdapter()).setItem(position, rid);
    }

    //Копируем ячейки (ImageView) grid в список
    private ArrayList<ImageView> copyGridImageViewCellsToList(GridView grid)
    {
        ArrayList<ImageView> ret = new ArrayList<>();
        for (int i = 0; i < grid.getAdapter().getCount(); ++i) {
            ImageView imageView = (ImageView) (ImageView)grid.getChildAt(i);
            ret.add(imageView);
        }
        return ret;
    }

    public void fDo () {
        if(dlg2Kind == 1) {
            ++readyBtnPressTimes;
            if(readyBtnPressTimes == 1) {
                voiceInput_ib.setVisibility(View.GONE);
                speek_tv.setVisibility(View.GONE);
                bBreakSpeechListening = true;
                sr.stopListening();
                startAudioSound();

                ready_btn.setText(R.string.source);
                goOutFlag = true;
                timer.myCancel();
                autoCompleteTextView.setVisibility(View.GONE);
//                ready_btn.setVisibility(View.GONE);
                warningTextView.setVisibility(View.GONE);
                rememberActivityAim_tv.setVisibility(View.GONE);
                grid.setOnItemClickListener(null);
                //Проверяем правильность вспомненных фото
                boolean correct = true;
                boolean filledAllCells = true;
                for (int i = 0; i < grid.getAdapter().getCount(); ++i) {
                    int rid = (int) ((MyAdapter) grid.getAdapter()).getItem(i);
                    if (rid == R.drawable.question_mark) { //одна из ячеек не заполнена
                        filledAllCells = false;
                    }
                    if (!customSet.contains(rid) && rid != R.drawable.question_mark) { //одна из неверных картинок
                        //Помечаем неверное фото крестиком
                        ImageView imageView = (ImageView) grid.getChildAt(i);
                        mixGridCellWithDrawable(this, grid, i, imageView, R.drawable.incorrect);
                        correct = false;
                    }
                }

                //Копируем ячейки (ImageView) grid в список
                imageViewArrayList = copyGridImageViewCellsToList(grid);

                String resultTitle;
                String resultMessage;
                if (correct && filledAllCells) {//задание выполнено успешно
                    startPlaying(RememberActivity.this, R.raw.success);
                    resultTitle = "Поздравляем! Задание выполнено успешно!";
                    resultMessage = "Время выполнения = " + timer_tv.getText();
                } else {//задание не выполнено
                    startPlaying(RememberActivity.this, R.raw.unsuccess);
                    resultTitle = "Задание НЕ выполнено!";
                    if (!correct && !filledAllCells) {//не все фото правильно отгаданы и есть незаполненные ячейки
                        resultMessage = "Не все фото правильные и есть незаполненные ячейки.";
                    } else if (correct) {//вставленные фото правильные, но есть незаполненные ячейки
                        resultMessage = "Не все фото отгаданы.";
                    } else {//не все фото правильно отгаданы
                        resultMessage = "Некоторые фото неверные.";
                    }
                }
                DlgWithOneButton myDialogFragment = new DlgWithOneButton(resultTitle, resultMessage, "OK");
                myDialogFragment.show(getSupportFragmentManager(), "myDialog");
            }
            else { //readyBtnPressTimes > 1
                if (readyBtnPressTimes % 2 == 0) {
                    ready_btn.setText(R.string.solution);
                    adapter = new MyAdapter(RememberActivity.this, cellDim, 1);
                    adapter.LoadArr(customArr);
                } else {
                    ready_btn.setText(R.string.source);
                    adapter = new MyAdapter(RememberActivity.this, cellDim, 2);
                    adapter.LoadImageViewArr(imageViewArrayList);
                }
                grid.setAdapter(adapter);
            }
        }
        else { // dlg2Kind == 2
            goOutFlag = true;
            timer.myCancel();

            bBreakSpeechListening = true;
            sr.stopListening();
            startAudioSound();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
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
        dlg2Kind = 2;
        DlgWithTwoButtons myDialogFragment = new DlgWithTwoButtons(getString(R.string.exitGameActivity), getString(R.string.areYouSure),
            getString(R.string.yes_option), getString(R.string.no_option), this);
        myDialogFragment.show(getSupportFragmentManager(), "myDialog");
    }
}