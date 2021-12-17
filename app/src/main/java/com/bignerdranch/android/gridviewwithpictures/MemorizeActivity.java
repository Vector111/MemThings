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

import java.util.List;

public class MemorizeActivity extends AppCompatActivity {
    public boolean spec_flag = false;

    public static final int MAX_COLUMNS_NUM = 6;
    private PictRes pictRes = PictRes.instance();
//    private int state = 0; //отслеживает состояние активности
    public ConstraintLayout mainLayout;
//    public ConstraintLayout subLayout;
    TextView memorizeActivityTitle_tv;
    TextView timer_tv;
    public GridView grid;
    private int nThings; //число картинок для запоминания
    private int memTime; //время для запоминания (сек)
    private int rowsNum; //число строк grid
    private int columnsNum; //число столбцов grid
    private ImageAdapter adapter;




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
//        subLayout = (ConstraintLayout) findViewById(R.id.subLayout);
        memorizeActivityTitle_tv = (TextView) findViewById(R.id.memorizeActivityTitle_tv);
        grid = (GridView) findViewById(R.id.grid);
        timer_tv = (TextView) findViewById(R.id.timer_tv);

        showTable();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        showTable();
//        int kkk = 0;
//    }

    private void showTable() {
        mainLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if ((left == 0 && top == 0 && right == 0 && bottom == 0) || spec_flag)
                    return;

                spec_flag = true;
                int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
                int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

                //КОСТЫЛЬ! Не знаю, как программно получить android:layout_marginTop и т.п.
                //10dp ==> px
                int marginInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

                int maxGridHeight = screenHeight - memorizeActivityTitle_tv.getHeight() - marginInPx -
                    timer_tv.getHeight() - marginInPx;

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
                List<Integer> customArr = pictRes.getCustomArray
                        (rowsNum * columnsNum);
                adapter.LoadArr(customArr);
                grid.setAdapter(adapter);
            }
        });
    }
}