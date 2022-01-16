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
    private int cell_w;     // ширина ячейки
    private List<Integer> mThumbIds;
    private List<ImageView> mImageViewList;
    private int adapterType;

    public MyAdapter(Context c, int cell_w, int adapterType) {
        mContext = c;
        this.cell_w = cell_w;
        mThumbIds = new ArrayList<>();
        mImageViewList = new ArrayList<>();
        this.adapterType = adapterType;
    }

    public int getCount() { return (adapterType == 1) ? mThumbIds.size() : mImageViewList.size(); }

    public Object getItem(int position) { return (adapterType == 1) ? mThumbIds.get(position) :
        mImageViewList.get(position); }

    public void setItem(int position, int rid) { mThumbIds.set(position, rid); }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(cell_w, cell_w));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        if(adapterType == 1)
            imageView.setImageResource(mThumbIds.get(position));
        else {//adapterType == 2
            imageView = mImageViewList.get(position);
        }

        return imageView;
    }

    public void LoadImageViewArr(List<ImageView> list) {
        mImageViewList = new ArrayList<>(list);
    }

    public void LoadArr(List<Integer> list) {
        mThumbIds = new ArrayList<>(list);
    }

    public void LoadElem(int index, int rId) {
        mThumbIds.set(index, rId);
    }

}
