package com.bignerdranch.android.gridviewwithpictures;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private int cell_type;  // тип ячейки (1 - ImageView, 2 - TextView)
    private int cell_w;     // ширина ячейки
    private int cell_h;     // высота ячейки
    private List<Integer> mThumbIds;
    private ArrayList<String> stringsArr;

    public MyAdapter(Context c, int cell_type, int cell_w, int cell_h) {
        mContext = c;
        this.cell_type = cell_type;
        this.cell_w = cell_w;
        this.cell_h = cell_h;
        mThumbIds = new ArrayList<>();
        stringsArr = new ArrayList<>();
    }

    public int getCount()
    {
        return ((cell_type == 1) ? mThumbIds.size() : stringsArr.size());
    }

    public Object getItem(int position)
    {
        return ((cell_type == 1) ? mThumbIds.get(position) : stringsArr.get(position));
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (cell_type == 1) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(cell_w, cell_h));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds.get(position));
            return imageView;
        }
        else { //cell_type == 2
            TextView tv;
            if(convertView == null){
                tv = new TextView(mContext);
                tv.setText(stringsArr.get(position));
                tv.setLayoutParams(new GridView.LayoutParams(cell_w, cell_h));
                tv.setGravity(Gravity.CENTER);
                TextViewCompat.setAutoSizeTextTypeWithDefaults(tv, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
//                tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tv.setTextColor(mContext.getResources().getColor(R.color.dark_green));
            }else{
                tv = (TextView) convertView;
            }
            return tv;
        }
    }

    public void LoadArr2(List<String> list) {
        stringsArr = new ArrayList<>(list);
    }

    public void LoadArr(List<Integer> list) {
        mThumbIds = new ArrayList<>(list);
    }

    public void LoadElem(int index, int rId) {
        mThumbIds.set(index, rId);
    }

}
