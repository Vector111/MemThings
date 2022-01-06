package com.bignerdranch.android.gridviewwithpictures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import static com.bignerdranch.android.gridviewwithpictures.MyFiles.*;

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

class Sounds {
    static MediaPlayer mediaPlayer;
    public static void startPlaying(Context context, int rid) {
        mediaPlayer = MediaPlayer.create(context, rid);
        mediaPlayer.start();
    }
}

class MyConvertions {
    public static final int[] EMPTY_INT_ARRAY = new int[0];

    public static int[] toPrimitive(Integer[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return EMPTY_INT_ARRAY;
        }
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; ++i) {
            result[i] = array[i].intValue();
        }
        return result;
    }

    public static Set<Integer> convertListToSet(List<Integer> list) {
        HashSet<Integer> ret = new HashSet<>();
        for (int i = 0; i < list.size(); ++i) {
            ret.add(list.get(i));
        }
        return ret;
    }

    public static HashMap<String, Integer> genStringIntegerMap(ArrayList<String> alS, ArrayList<Integer> alI){
        HashMap<String, Integer> ret = new HashMap<>();
        for (int i = 0; i < alS.size(); ++i)
            ret.put(alS.get(i), alI.get(i));

        return ret;
    }

}

class MyRandoms {

    /*
        Функция возвращает из диапазона целых чисел [0...n-1]
        случайное подмножество уникальных чисел в количестве m (m <= n)
    */
    public static Set<Integer> getRandomUniqSubset(int m, int n)
    {
        Set<Integer> set = new LinkedHashSet<>();
        if (n == m) {
            for (int i = 0; i < m; ++i)
                set.add(i);

        } else { // m < n
            Random rand = new Random(new Date().getTime());
            while (set.size() < m)
                set.add(rand.nextInt(n));

        }
        return set;
    }
}

class MyFiles {
    /*
        Функция читает текстовый файл с именем fn,
        каждая строка которого представляет собой пару String,
        разделенных ";".
        Функция возвращает список пар таких строк.
        Функция игнорирует пустые строки файла,
        а также оставляет только уникальные первые элементы строк,
        и уникальные вторые (если str2_uniq = true)
    */
    public static List<Pair<String, String>> getPairsList(Context context, String fn, boolean str2_uniq)
    {
        AssetManager am = context.getAssets();
        String line = null;
        InputStream is = null;
        List<Pair<String, String>> ret = new ArrayList<>();
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        try {
            is = am.open(fn);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String[] tmp = line.split(";");
//                String[] tmp = line.split("\\s+");
                if (tmp.length != 2)
                    continue;
                String s1 = tmp[0];
                String s2 = tmp[1];
                s1 = s1.trim();
                s2 = s2.trim();
                if ((!set1.contains(s1) && !str2_uniq) || (!set1.contains(s1) && str2_uniq) && !set2.contains(s2)) {
                    ret.add(new Pair<>(s1, s2));
                }
                set1.add(s1);
                if(str2_uniq)
                    set2.add(s2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}

class MyKeyBoard {
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

class MyToasts {
    public static void myToastShow(Activity activity, String text, int gravity, int duration) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout,
            (ViewGroup) activity.findViewById(R.id.toast_layout_root));
        TextView custom_toast_text = (TextView) layout.findViewById(R.id.custom_toast_text);
        custom_toast_text.setText(text);
        Toast toast = new Toast(activity.getApplicationContext());
        toast.setGravity(gravity, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }
}

class MyGrids {
    public static void mixGridCellWithDrawable(Context context, GridView grid, int pos, ImageView imageView, int rDrawableId) {
        int rId = (int) grid.getAdapter().getItem(pos);
        Resources r = context.getResources();
        Drawable[] layers = new Drawable[2];
        layers[0] = r.getDrawable(rId);
        layers[1] = r.getDrawable(rDrawableId);
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        imageView.setImageDrawable(layerDrawable);
    }
}
