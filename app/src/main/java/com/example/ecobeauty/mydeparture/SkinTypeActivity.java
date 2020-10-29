package com.example.ecobeauty.mydeparture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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

    public String typeSkin;
    public int  count = 0;
    int cameraRequestCode = 001;

    Classifier classifier;
    Bitmap imageBitmap;
    String identifiedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_type);

        classifier = new Classifier(Utils.assetFilePath(this, Constants.NETWORK_FILE));

        Button capture = findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,cameraRequestCode);
            }
        });

        RadioGroup radioGroup = findViewById(R.id.rGroup);
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
            Intent intent1 = new Intent(SkinTypeActivity.this, Types.class);
            intent1.putExtra(Constants.PRED, identifiedType);
            startActivity(intent1);
        }
    }

    public void changeActivity() {
        Intent intent = new Intent(this, MyDepartureActivity.class);
        Bundle data1 = new Bundle();
        data1.putString(Constants.CHECK_TYPE, typeSkin);
        intent.putExtras(data1);
        startActivity(intent);
    }
}