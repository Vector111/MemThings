package com.bignerdranch.android.gridviewwithpictures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class RememberActivity extends AppCompatActivity {
    private static final String EXTRA_MEMORIZED_IDS =
        "com.bignerdranch.android.gridviewwithpictures.memorized_ids";

    private int [] memorizedIdsArr;

    public static Intent newIntent(Context context, int[] arr) {
        Intent intent = new Intent(context, RememberActivity.class);
        intent.putExtra(EXTRA_MEMORIZED_IDS, arr);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember);
        memorizedIdsArr = getIntent().getIntArrayExtra(EXTRA_MEMORIZED_IDS);


        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        int[] chainIds = { R.id.button, R.id.button2 }; // the ids you set on your views above
        float[] weights = { 1, 5 };
        set.createHorizontalChain(ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
            chainIds, weights, ConstraintSet.CHAIN_SPREAD);

        set.applyTo(layout);
    }
}