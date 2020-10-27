package com.example.ecobeauty.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecobeauty.R;
import com.example.ecobeauty.aboutapp.AboutAppActivity;
import com.example.ecobeauty.checkcomposition.CheckCompositionActivity;
import com.example.ecobeauty.mycosmetics.MyCosmeticsActivity;
import com.example.ecobeauty.mydeparture.MyDepartureActivity;

public class MainActivity extends AppCompatActivity {
    
    private Button btnAbout, myDep, myCosm, checkComp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView firstEditText = (TextView)findViewById(R.id.textView1);
        firstEditText.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.maintext)));
        btnAbout = (Button) findViewById(R.id.aboutApp);
        btnAbout.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));

        myDep = (Button) findViewById(R.id.myDeparture);
        myDep.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));

        myCosm = (Button) findViewById(R.id.myMakeup);
        myCosm.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));

        checkComp = (Button) findViewById(R.id.checkComposition);
        checkComp.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));


    }

    @Override
    public void onBackPressed() {

        Log.i(Constants.TAG_MAIN, getString(R.string.exitTheProgram) );
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStackImmediate();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.reallyQuitTheProgram)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveTaskToBack(true);
                                    Process.killProcess(Process.myPid());
                                    System.exit(0);
                                }
                            }).setNegativeButton(R.string.no, null).show();
        }
    }

    public void onMyDeparture(View view)
    {
        Log.i(Constants.TAG_MAIN, getString(R.string.goToMyDeparture) );
        Intent intent = new Intent(MainActivity.this, MyDepartureActivity.class);
        startActivity(intent);
    }

    public void onCosmeticsClick(View view)
    {
        Log.i(Constants.TAG_MAIN, getString(R.string.goToMyCosmetics) );
        Intent intent = new Intent(MainActivity.this, MyCosmeticsActivity.class);
        startActivity(intent);
    }

    public void onCheckClick(View view)
    {
        Log.i(Constants.TAG_MAIN, getString(R.string.goToCheckComposition) );
        Intent intent = new Intent(MainActivity.this, CheckCompositionActivity.class);
        startActivity(intent);
    }

    public void onMyButtonClick(View view)
    {
        Log.i(Constants.TAG_MAIN, getString(R.string.goToAboutApp) );
        Intent intent = new Intent(MainActivity.this, AboutAppActivity.class);
        startActivity(intent);
    }

}
