package com.bignerdranch.android.gridviewwithpictures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.TextView;
//import static com.bignerdranch.android.gridviewwithpictures.MainActivity.*;
import static com.bignerdranch.android.gridviewwithpictures.MySettings.*;

public class MemorizeActivity extends AppCompatActivity {
    private int state = 0; //отслеживает состояние активности
    private GridView gridView;
    private int tblLayW;
    private int nThings; //число картинок для запоминания
    private int memTime; //время для запоминания (сек)
    private int nRow; //число строк tblLay
    private int nCol; //число столбцов tblLay



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //для портретного режима
        Settings settings = new Settings(this);
        //читаем индекс массива числа картинок
        int index = settings.getIndNumThingsArr();
        nThings = getThingNum(index);
        index = settings.getIndMemTimeArr();
        memTime = getMemTime(index);
        nRow = MIN_NUM_THINGS;
        nCol = nThings / nRow;

        gridView = (GridView) findViewById(R.id.gridView);

        showTable();
    }

    @Override
    public void onResume() {
        super.onResume();
//        ++state;
//        if (state == 1) { //вызвали сразу после onCreate
//            // Выводим таблицу с картинками
//            showTable();
//        }
    }

    private void showTable() {
        gridView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (left == 0 && top == 0 && right == 0 && bottom == 0)
                    return;
                gridView.removeOnLayoutChangeListener(this);
                tblLayW = gridView.getWidth();
                int cellW = 0;//(tblLayW - (nCol - 1) * gap) / nRow;
            }
        });
    }
}