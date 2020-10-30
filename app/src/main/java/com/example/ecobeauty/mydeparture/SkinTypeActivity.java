package com.example.ecobeauty.mydeparture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ecobeauty.R;
import com.example.ecobeauty.main.Constants;

public class SkinTypeActivity extends Activity {

    private String typeSkin;
    private int  count = 0;
    private int cameraRequestCode = 001;

    private Classifier classifier;
    private Bitmap imageBitmap;
    private String identifiedType;
    private Button capture;
    private Intent cameraIntent, intentToType, intentToMyDepartureActivity;
    private RadioGroup radioGroup;
    private Bundle activityData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_type);

        classifier = new Classifier(Utils.assetFilePath(this, Constants.NETWORK_FILE));

        capture = findViewById(R.id.capture);
        capture.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));

        capture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,cameraRequestCode);
            }
        });

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        Toast.makeText(getApplicationContext(), R.string.nothingSelected,
                                Toast.LENGTH_SHORT).show();
                        count+=1;
                        break;
                    case R.id.radioButtonOne:
                        typeSkin = getString(R.string.normal);
                        count+=1;
                        break;
                    case R.id.radioButtonTwo:
                        typeSkin = getString(R.string.fat);
                        count+=1;
                        break;
                    case R.id.radioButtonThird:
                        typeSkin = getString(R.string.dry);
                        count+=1;
                        break;
                    case R.id.radioButtonFour:
                        typeSkin = getString(R.string.combined);
                        count+=1;
                        break;
                    default:
                        break;
                }
                if (count !=0)
                {
                    changeActivity();
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == cameraRequestCode && resultCode == RESULT_OK) {
            imageBitmap = (Bitmap) data.getExtras().get(Constants.DATA);
            identifiedType = classifier.predict(imageBitmap);
            intentToType = new Intent(SkinTypeActivity.this, Types.class);
            intentToType.putExtra(Constants.PRED, identifiedType);
            startActivity(intentToType);
        }
    }

    public void changeActivity() {
        intentToMyDepartureActivity = new Intent(this, MyDepartureActivity.class);
        activityData = new Bundle();
        activityData.putString(Constants.CHECK_TYPE, typeSkin);
        intentToMyDepartureActivity.putExtras(activityData);
        startActivity(intentToMyDepartureActivity);
    }
}