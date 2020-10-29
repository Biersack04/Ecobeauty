package com.example.ecobeauty.mydeparture;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecobeauty.R;
import com.example.ecobeauty.main.Constants;

public class Types extends AppCompatActivity {
    private String typeSkin, prediction;
    private TextView text;
    private Intent intentToMyDepartureActivity;
    private Bundle activityData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);
        text = findViewById(R.id.textViewResult);
        prediction  = getIntent().getStringExtra(Constants.PRED);

        if (prediction.equals(getString(R.string.normal))){
            text.setText(R.string.normalDescribe);
        }
        else if (prediction.equals(getString(R.string.fat))){
            text.setText(R.string.fatDescribe);
        }
        else if (prediction.equals(getString(R.string.dry))){
            text.setText(R.string.dryDescribe);
        }
        else if (prediction.equals(getString(R.string.combined))){
            text.setText(R.string.combineDescribe);
        }

        switch (prediction) {
            case Constants.NORMAL:
                typeSkin = getString(R.string.normal);
                break;
            case Constants.FAT:
                typeSkin = getString(R.string.fat);
                break;
            case Constants.DRY:
                typeSkin = getString(R.string.dry);
                break;
            case Constants.COMBINED:
                typeSkin = getString(R.string.combined);
                break;
            default:
                break;
        }
    }

    public void onBackSkin (View view) {
        intentToMyDepartureActivity = new Intent(this, MyDepartureActivity.class);
        activityData = new Bundle();
        activityData.putString(Constants.CHECK_TYPE, typeSkin);
        intentToMyDepartureActivity.putExtras(activityData);
        startActivity(intentToMyDepartureActivity);
    }
}