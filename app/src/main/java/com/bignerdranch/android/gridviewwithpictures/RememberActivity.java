package com.bignerdranch.android.gridviewwithpictures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

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

    private ArrayList<Integer> customArr; //выборка запоминаемых юзером ресурных id картинок

    public static Intent newIntent(Context context, ArrayList<String> alS,
        ArrayList<Integer> alI, ArrayList<Integer> customArr)
    {
        Intent intent = new Intent(context, RememberActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(EXTRA_ALS, alS);
        bundle.putIntegerArrayList(EXTRA_ALI, alI);
        bundle.putIntegerArrayList(EXTRA_CUSTOM_ARR, customArr);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember);

        ArrayList<String> alS = getIntent().getStringArrayListExtra(EXTRA_ALS);
        ArrayList<Integer> alI = getIntent().getIntegerArrayListExtra(EXTRA_ALI);
        customArr = getIntent().getIntegerArrayListExtra(EXTRA_CUSTOM_ARR);


        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        int[] chainIds = { R.id.button, R.id.button2 }; // the ids you set on your views above
        float[] weights = { 1, 5 };
        set.createHorizontalChain(ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
            chainIds, weights, ConstraintSet.CHAIN_SPREAD);

        set.applyTo(layout);

        List<Pair<String,String>> list = getPairsList(this, "cards.txt");
        int ppp = 0;

    }
}