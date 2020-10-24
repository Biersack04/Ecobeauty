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

public class SkinTypeActivity extends Activity {

    public static final String NETWORK_FILE= "model_densenet121.pt";
    int cameraRequestCode = 001;
    Classifier classifier;
    public String typeSkin;
    public int  count=0;
    Bitmap imageBitmap;
    String identifiedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_type);

        classifier = new Classifier(Utils.assetFilePath(this,NETWORK_FILE));

        Button capture = findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,cameraRequestCode);
            }
        });

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rGroup);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        Toast.makeText(getApplicationContext(), "Ничего не выбрано",
                                Toast.LENGTH_SHORT).show();
                        count+=1;
                        break;
                    case R.id.radioButtonOne:
                        typeSkin = "normal";
                        count+=1;
                        break;
                    case R.id.radioButtonTwo:
                        typeSkin = "fat";
                        count+=1;
                        break;
                    case R.id.radioButtonThird:
                        typeSkin = "dry";
                        count+=1;
                        break;
                    case R.id.radioButtonFour:
                        typeSkin = "combined";
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
            imageBitmap = (Bitmap) data.getExtras().get("data");
            identifiedType = classifier.predict(imageBitmap);
            Intent intent1 = new Intent(SkinTypeActivity.this, Types.class);
            intent1.putExtra("pred", identifiedType);
            startActivity(intent1);

        }

    }
    public void changeActivity()
    {
        Log.i("SkinTypeActivityLog", " Переход на активность 'Изменить тип кожи' ");
        Intent intent = new Intent(this, MyDepartureActivity.class);
        Bundle data1 = new Bundle();
        data1.putString("checkType", typeSkin);
        intent.putExtras(data1);
        startActivity(intent);


    }

}