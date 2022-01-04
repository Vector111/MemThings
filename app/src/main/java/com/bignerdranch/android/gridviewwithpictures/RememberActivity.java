package com.bignerdranch.android.gridviewwithpictures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.LinearLayout;

import static com.bignerdranch.android.gridviewwithpictures.MyFiles.*;
import static com.bignerdranch.android.gridviewwithpictures.MyConvertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RememberActivity extends AppCompatActivity {
    private static final String EXTRA_ALS =
        "com.bignerdranch.android.gridviewwithpictures.als";
    private static final String EXTRA_ALI =
        "com.bignerdranch.android.gridviewwithpictures.ali";
    private static final String EXTRA_CUSTOM_ARR =
        "com.bignerdranch.android.gridviewwithpictures.customArr";

    public ConstraintLayout mainLayout;
    public GridView seek_grid;

    // alS и alI получены от MemorizeActivity, als отсортирована по возрастанию
    // название фото в als соответствует ресурсному id соответствующего файла фото
    private ArrayList<String> alS;  // Названия фотографий
    private ArrayList<Integer> alI; // Resource IDS файлов фотографий (IDS могут повторяться)
    private HashSet<Integer> customSet; // будет хранить множество ресурных id картинок, которые запоминал юзер

    public static Intent newIntent(Context context, ArrayList<String> alS,
        ArrayList<Integer> alI, ArrayList<Integer> customArr)
    {
        Intent intent = new Intent(context, RememberActivity.class);
        intent.putStringArrayListExtra(EXTRA_ALS, alS);
        intent.putIntegerArrayListExtra(EXTRA_ALI, alI);
        intent.putIntegerArrayListExtra(EXTRA_CUSTOM_ARR, customArr);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember);

        alS = getIntent().getStringArrayListExtra(EXTRA_ALS);
        alI = getIntent().getIntegerArrayListExtra(EXTRA_ALI);
        // customArr - выборка запоминаемых юзером ресурных id картинок
        ArrayList<Integer> customArr = getIntent().getIntegerArrayListExtra(EXTRA_CUSTOM_ARR);

        customSet = (HashSet<Integer>) convertListToSet(customArr);

        mainLayout = (ConstraintLayout) findViewById(R.id.mainLayout1);

        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
//        // Get the string array
//        String[] countries = {"Аферистка", "Аферист", "Афера", "Аяяzz", "Аяю..", "Аяю999",
//            "Аяя99", "Аяя98", "Аяя999", "Аяя97", "Аяя96", "Аяя95","Аяя94", "Аяя93", "Аяя92", "Аяя91",
//            "Аура", "Бяка", "Бука", "Один", "Два", "Три", "Четыре", "Пять",
//            "Шесть", "Семь", "Восемь", "Девять", "Десять"};
        // Create the adapter and set it to the AutoCompleteTextView
        String[] stringArray = Arrays.copyOf(alS.toArray(), alS.toArray().length, String[].class);
        ArrayAdapter<String> adapter =
            new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArray);
        textView.setAdapter(adapter);

    }
}
