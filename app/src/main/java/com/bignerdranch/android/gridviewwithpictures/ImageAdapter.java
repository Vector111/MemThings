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

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Integer> mThumbIds;
//    private int count;

//    public static float dpToPx(final Context context, final float dp) {
//        return dp * context.getResources().getDisplayMetrics().density;
//    }


    public ImageAdapter(Context c) {
        mContext = c;
        mThumbIds = new ArrayList<>();
//        count = mThumbIds.size();
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return mThumbIds.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        int pict_dim = ((MemorizeActivity) mContext).grid.getColumnWidth();
        if (position > 0) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(pict_dim, pict_dim));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds.get(position));
            return imageView;
        }
        else {
            TextView tv;
            if(convertView == null){
                tv = new TextView(mContext);
                tv.setText("Ё");
//                tv.setWidth(pict_dim);
//                tv.setHeight(pict_dim);
//                int pxVal = (int) TypedValue.applyDimension(
//                    TypedValue.COMPLEX_UNIT_DIP, pict_dim, mContext.getResources().getDisplayMetrics());
                tv.setLayoutParams(new GridView.LayoutParams(pict_dim, pict_dim));
//                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, pict_dim/2);
                tv.setGravity(Gravity.CENTER);
                TextViewCompat.setAutoSizeTextTypeWithDefaults(tv, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
//                TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(tv, pict_dim-2, pict_dim, 1, TypedValue.COMPLEX_UNIT_PX);
                tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tv.setTextColor(mContext.getResources().getColor(R.color.dark_green));
//                tv.setBackground(mContext.getResources().getDrawable(R.drawable.white));
                //                tv.setPadding(0, 0, 0, pict_dim/4);
            }else{
                tv = (TextView) convertView;
            }
            return tv;
        }
    }

    public void LoadArr(List<Integer> list) {
        mThumbIds = new ArrayList<>(list);
    }

    public void LoadElem(int index, int rId) {
        mThumbIds.set(index, rId);
    }

}
