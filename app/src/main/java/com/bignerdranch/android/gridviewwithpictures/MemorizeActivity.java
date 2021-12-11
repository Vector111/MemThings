package com.bignerdranch.android.gridviewwithpictures;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

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
import android.widget.LinearLayout;
import android.widget.TextView;
//import static com.bignerdranch.android.gridviewwithpictures.MainActivity.*;
import static com.bignerdranch.android.gridviewwithpictures.MySettings.*;

import java.util.List;

public class MemorizeActivity extends AppCompatActivity {
    private PictRes pictRes = PictRes.instance();
    private int state = 0; //отслеживает состояние активности
    public GridView grid;
    private int nThings; //число картинок для запоминания
    private int memTime; //время для запоминания (сек)
    private int rowsNum; //число строк grid
    private int columnsNum; //число столбцов grid
    public int cellW;
    private ImageAdapter adapter;



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
        rowsNum = MIN_NUM_THINGS;
        columnsNum = nThings / rowsNum;

        grid = (GridView) findViewById(R.id.grid);

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
        grid.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (left == 0 && top == 0 && right == 0 && bottom == 0)
                    return;

                grid.removeOnLayoutChangeListener(this);

                int gridW = grid.getWidth();

                grid.setNumColumns(columnsNum);

                gridW -= (grid.getPaddingLeft() + grid.getPaddingRight());
                cellW = (gridW - (columnsNum - 1) * grid.getHorizontalSpacing()) / columnsNum;
                //подкорректируем gridw из-за ошибок округления предыдущей строки
                gridW = cellW * columnsNum + (columnsNum - 1) * grid.getHorizontalSpacing();

                grid.setLayoutParams(new LinearLayout.LayoutParams
                        (gridW + grid.getPaddingLeft() + grid.getPaddingRight(), MATCH_PARENT));

                grid.setColumnWidth(cellW);

                adapter = new ImageAdapter(MemorizeActivity.this);
                List<Integer> customArr = pictRes.getCustomArray
                        (columnsNum * columnsNum);
                adapter.LoadArr(customArr);
            }
        });
    }
}