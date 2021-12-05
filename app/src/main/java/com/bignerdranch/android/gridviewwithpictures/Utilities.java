package com.bignerdranch.android.gridviewwithpictures;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

class ImageButtonOperations {
    public static void setImageButtonEnabled(Context ctxt, boolean enabled, ImageButton item,
                                             int iconResId) {
        item.setEnabled(enabled);
        Drawable originalIcon = ctxt.getResources().getDrawable(iconResId);
        Drawable icon = enabled ? originalIcon : convertDrawableToGrayScale(originalIcon);
        item.setImageDrawable(icon);
    }

    public static Drawable convertDrawableToGrayScale(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Drawable res = drawable.mutate();
        res.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        return res;
    }
}

class EnterInt {
    private TextView val_tv;
    private int arr[];

    public EnterInt(Context ctxt, int arr[], int index,
                    ImageButton dec_ib, int dec_ib_icone_res_id,
                    ImageButton inc_ib, int inc_ib_icone_res_id, TextView val_tv){
        this.val_tv = val_tv;
        this.arr = arr;

        dec_ib.setEnabled(index != 0);
        inc_ib.setEnabled(index != (arr.length-1));
//      setImageButtonEnabled(ctxt, index != 0, dec_ib, dec_ib_icone_res_id);
//      setImageButtonEnabled(ctxt, index != (arr.length-1), inc_ib, inc_ib_icone_res_id);

        val_tv.setText(new Integer(arr[index]).toString());

        dec_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = getCurrentIndex(val_tv,arr);
                index = ((index-1) < 0) ? 0 : index-1;
                val_tv.setText(new Integer(arr[index]).toString());
                dec_ib.setEnabled(index != 0);
                inc_ib.setEnabled(index != (arr.length-1));
//                setImageButtonEnabled(ctxt, index != 0, dec_ib, dec_ib_icone_res_id);
//                setImageButtonEnabled(ctxt, index != (arr.length-1), inc_ib, inc_ib_icone_res_id);
            }
        });

        inc_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = getCurrentIndex(val_tv,arr);
                index = (index > (arr.length-1)) ? arr.length-1 : index+1;
                val_tv.setText(new Integer(arr[index]).toString());
                dec_ib.setEnabled(index != 0);
                inc_ib.setEnabled(index != (arr.length-1));
//                setImageButtonEnabled(ctxt, index != 0, dec_ib, dec_ib_icone_res_id);
//                setImageButtonEnabled(ctxt, index != (arr.length-1), inc_ib, inc_ib_icone_res_id);
            }
        });
    }

    static int getCurrentIndex(TextView val_tv, int arr[] ) {
        int val = Integer.valueOf(val_tv.getText().toString());
        for (int i=0; i < arr.length; i++ ){
            if(arr[i] == val)
                return i;
        }
        return 0;
    }

}

class Settings {
    private static SharedPreferences apppref;
    public static final String APP_PREFERENCES = "apppref";
    Settings(Context context) {
        apppref = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    //запоминаем индекс массива числа картинок
    public void setIndNumThingsArr(int val) {
        SharedPreferences.Editor editor = apppref.edit();
        editor.putInt("indNumThingsArr", val);
        editor.apply();
    }

    //читаем индекс массива числа картинок
    public int getIndNumThingsArr() {
        return apppref.getInt("indNumThingsArr", 0);
    }

    //запоминаем индекс массива времен запоминания
    public void setIndMemTimeArr(int val) {
        SharedPreferences.Editor editor = apppref.edit();
        //запоминаем индекс массива числа картинок
        editor.putInt("indMemTimeArr", val);
        editor.apply();
    }

    //читаем индекс массива времен запоминания
    public int getIndMemTimeArr() {
        return apppref.getInt("indMemTimeArr", 0);
    }
}