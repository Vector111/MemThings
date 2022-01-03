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

import java.util.ArrayList;
import java.util.List;

public class RememberActivity extends AppCompatActivity {
    private static final String EXTRA_ALS =
        "com.bignerdranch.android.gridviewwithpictures.als";
    private static final String EXTRA_ALI =
        "com.bignerdranch.android.gridviewwithpictures.ali";
    private static final String EXTRA_CUSTOM_ARR =
        "com.bignerdranch.android.gridviewwithpictures.customArr";

    public ConstraintLayout mainLayout;
    public GridView seek_grid;

    private ArrayList<String> alS;
    private ArrayList<Integer> alI;
    private ArrayList<Integer> customArr; //выборка запоминаемых юзером ресурных id картинок

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
        customArr = getIntent().getIntegerArrayListExtra(EXTRA_CUSTOM_ARR);

        mainLayout = (ConstraintLayout) findViewById(R.id.mainLayout1);

        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        // Get the string array
        String[] countries = {"Один", "Два", "Три", "Четыре", "Пять",
            "Шесть", "Семь", "Восемь", "Девять", "Десять"};
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
            new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        textView.setAdapter(adapter);

    }
}
