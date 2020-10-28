package com.example.ecobeauty.mydeparture;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecobeauty.R;
import com.example.ecobeauty.main.Constants;

public class Types extends AppCompatActivity {
    public String typeSkin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);
        TextView text = findViewById(R.id.textViewResult);
        String prediction  = getIntent().getStringExtra(Constants.PRED);

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
        Intent intent = new Intent(this, MyDepartureActivity.class);
        Bundle data1 = new Bundle();
        data1.putString(Constants.CHECK_TYPE, typeSkin);
        intent.putExtras(data1);
        startActivity(intent);
    }
}