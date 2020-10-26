package com.example.ecobeauty.mydeparture;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecobeauty.R;
import com.example.ecobeauty.mydeparture.MyDepartureActivity;

public class Types extends AppCompatActivity {
    public String typeSkin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);
        TextView text = findViewById(R.id.textViewResult);
        String prediction  = getIntent().getStringExtra("pred");


        if (prediction.equals("normal")){
            text.setText("У вас нормальный тип кожи. он является самым редким типом кожи. " +
                    "Такой тип в меру чувствителен. Кожа матовая, упругая, поры почти не видны");

        }
        else if (prediction.equals("fat")){
            text.setText("У вас жирный тип кожи. Этому типу кожи присущи жирный блеск, " +
                    "пористость, частые высыпания, такие как акне или угри");

        }

        else if (prediction.equals("dry")){
            text.setText("У вас сухой тип кожи. Для данного типа характерны частое шелушение, " +
                    "покраснения. Такая кожа часто тонкая, нежная и сильно подвержена влиянию внешних факторов");

        }
        else if (prediction.equals("combined")){
            text.setText("У вас комбинированный тип кожи. Данный тип кожи является самым распространенным. " +
                    "Особенность состоит в том, что различные участки кожи имеют разным тип, например: " +
                    "щеки имеют сухой тип, а Т-зона - жирный. Для такого типа кожи свойственны аллергичность " +
                    "и частые высыпания и раздражения.");
        }

        switch (prediction) {
            case "normal":
                typeSkin = "normal";
                break;
            case "fat":
                typeSkin = "fat";
                break;
            case "dry":
                typeSkin = "dry";
                break;
            case "combined":
                typeSkin = "combined";
                break;
            default:
                break;
        }

    }


    public void onBackSkin (View view) {
        Intent intent = new Intent(this, MyDepartureActivity.class);
        Bundle data1 = new Bundle();
        data1.putString("checkType", typeSkin);
        intent.putExtras(data1);
        startActivity(intent);
    }

}