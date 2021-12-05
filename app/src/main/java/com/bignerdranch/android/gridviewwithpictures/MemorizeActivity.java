package com.bignerdranch.android.gridviewwithpictures;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import static com.bignerdranch.android.gridviewwithpictures.MainActivity.*;

public class MemorizeActivity extends AppCompatActivity {
    private int state = 0; //отслеживает состояние активности
    private GridView tbl_gv;
    private int nThings; //число картинок для запоминания
    private int memTime; //время для запоминания (сек)
    private int nRow; //число строк tbl_gv
    private int nCol; //число столбцов tbl_gv


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize);
        Settings settings = new Settings(this);
        //читаем индекс массива числа картинок
        int index = settings.getIndNumThingsArr();
        nThings = getThingNum(index);
        index = settings.getIndMemTimeArr();
        memTime = getMemTime(index);
        nRow = MIN_NUM_THINGS;
        nCol = nThings / nRow;


        tbl_gv = (GridView) findViewById(R.id.tbl_gv);
    }

    @Override
    public void onResume() {
        super.onResume();
        ++state;
        if (state == 1) { //вызвали сразу после onCreate
            // Выводим таблицу GridView с картинками
//            showTable();
        }
    }
}