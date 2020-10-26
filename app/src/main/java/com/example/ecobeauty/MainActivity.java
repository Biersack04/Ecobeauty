package com.example.ecobeauty;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecobeauty.aboutapp.AboutAppActivity;
import com.example.ecobeauty.checkcomposition.CheckCompositionActivity;
import com.example.ecobeauty.mycosmetics.MyCosmeticsActivity;
import com.example.ecobeauty.mydeparture.MyDepartureActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityLog" ;
    private Button btnAbout, myDep, myCosm, checkComp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView firstEditText = (TextView)findViewById(R.id.textView1);
        firstEditText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/maintext.ttf"));

        btnAbout = (Button) findViewById(R.id.aboutApp);
        btnAbout.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/robotoMedium.ttf"));

        myDep = (Button) findViewById(R.id.myDeparture);
        myDep.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/robotoMedium.ttf"));

        myCosm = (Button) findViewById(R.id.myMakeup);
        myCosm.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/robotoMedium.ttf"));

        checkComp = (Button) findViewById(R.id.checkComposition);
        checkComp.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/robotoMedium.ttf"));


    }

    @Override
    public void onBackPressed() {

        Log.i("MainActivityLog", "Пользователь хочет выйти из программы" );
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStackImmediate();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Вы действительно хотите покинуть программу? ")
                    .setCancelable(false)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(0);
                                }
                            }).setNegativeButton("Нет", null).show();
        }
    }

    public void onMyDeparture(View view)
    {
        Log.i("MainActivityLog", "Переход на активность 'Мой Уход'" );
        Intent intent = new Intent(MainActivity.this, MyDepartureActivity.class);
        startActivity(intent);
    }

    public void onCosmeticsClick(View view)
    {
        Log.i("MainActivityLog", "Переход на активность 'Моя косметичка'" );
        Intent intent = new Intent(MainActivity.this, MyCosmeticsActivity.class);
        startActivity(intent);
    }

    public void onCheckClick(View view)
    {
        Log.i("MainActivityLog", "Переход на активность 'Проверить Состав'" );
        Intent intent = new Intent(MainActivity.this, CheckCompositionActivity.class);
        startActivity(intent);
    }

    public void onMyButtonClick(View view)
    {
        Log.i("MainActivityLog", "Переход на активность 'О Приложении'" );
        Intent intent = new Intent(MainActivity.this, AboutAppActivity.class);
        startActivity(intent);
    }

}
