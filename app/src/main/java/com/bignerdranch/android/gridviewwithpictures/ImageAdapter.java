
package com.bignerdranch.android.gridviewwithpictures;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            int pict_dim = ((MemorizeActivity) mContext).grid.getColumnWidth();
            imageView.setLayoutParams(new GridView.LayoutParams(pict_dim, pict_dim));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds.get(position));
        return imageView;
    }

    public void LoadArr(List<Integer> list) {
        mThumbIds = new ArrayList<>(list);
    }

    public void LoadElem(int index, int rId) {
        mThumbIds.set(index, rId);
    }

}
