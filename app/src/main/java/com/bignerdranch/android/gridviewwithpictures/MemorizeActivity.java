package com.bignerdranch.android.gridviewwithpictures;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
//import static com.bignerdranch.android.gridviewwithpictures.MainActivity.*;
import static com.bignerdranch.android.gridviewwithpictures.MySettings.*;

import java.util.List;

public class MemorizeActivity extends AppCompatActivity {
    public static final int MAX_COLUMNS_NUM = 6;
    private PictRes pictRes = PictRes.instance();
    private int state = 0; //отслеживает состояние активности
    public ConstraintLayout mainLayout;
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
        mainLayout = (ConstraintLayout)findViewById(R.id.mainLayout);
        Settings settings = new Settings(this);
        //читаем индекс массива числа картинок
        int index = settings.getIndNumThingsArr();
        nThings = getThingNum(index);
        index = settings.getIndMemTimeArr();
        memTime = getMemTime(index);
        rowsNum = MIN_NUM_THINGS;
        columnsNum = nThings / rowsNum;

        grid = (GridView) findViewById(R.id.grid);

//        showTable();
        int kkk = 0;
        Button mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTable();
            }
        });
        int nnn = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        showTable();
        int kkk = 0;
    }

    private void showTable() {
///*
        mainLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (left == 0 && top == 0 && right == 0 && bottom == 0)
                    return;

                int mLayoutW = mainLayout.getWidth();
                int mLayoutH = mainLayout.getHeight();
                mainLayout.removeOnLayoutChangeListener(this);

                grid.setNumColumns(columnsNum);

                int gridW = mLayoutW - grid.getPaddingLeft() - grid.getPaddingRight();
                cellW = (gridW - (columnsNum - 1) * grid.getHorizontalSpacing()) / MAX_COLUMNS_NUM;
                //подкорректируем gridw из-за ошибок округления предыдущей строки
                gridW = cellW * columnsNum + (columnsNum - 1) * grid.getHorizontalSpacing();
                int gridH = cellW * MIN_NUM_THINGS + (MIN_NUM_THINGS - 1) * grid.getVerticalSpacing();
                int leftPadding = (mLayoutW - gridW) / 2;
                int rightPadding = leftPadding;
                int topPadding = (mLayoutH - gridH) / 2;
                int bottomPadding = topPadding;
                grid.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
                grid.setLayoutParams(new LinearLayout.LayoutParams
                    (gridW + leftPadding * 2,gridH + topPadding * 2));

                grid.setColumnWidth(cellW);

                adapter = new ImageAdapter(MemorizeActivity.this);
                List<Integer> customArr = pictRes.getCustomArray
                        (rowsNum * columnsNum);
                adapter.LoadArr(customArr);
                grid.setAdapter(adapter);
                int kkk = 0;
            }
        });
//*/
/*
        int mLayoutW = mainLayout.getWidth();

        grid.setNumColumns(columnsNum);

        int gridW = mLayoutW - (grid.getPaddingLeft() + grid.getPaddingRight());
        cellW = (gridW - (columnsNum - 1) * grid.getHorizontalSpacing()) / MAX_COLUMNS_NUM;
        //подкорректируем gridw из-за ошибок округления предыдущей строки
        gridW = cellW * columnsNum + (columnsNum - 1) * grid.getHorizontalSpacing();

        grid.setLayoutParams(new LinearLayout.LayoutParams
                (gridW + grid.getPaddingLeft() + grid.getPaddingRight(), MATCH_PARENT));

        grid.setColumnWidth(cellW);

        adapter = new ImageAdapter(MemorizeActivity.this);
        List<Integer> customArr = pictRes.getCustomArray
                (rowsNum * columnsNum);
        adapter.LoadArr(customArr);
        grid.setAdapter(adapter);
*/
    }
}