package com.example.ecobeauty.aboutapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.ecobeauty.R;
import com.example.ecobeauty.main.Constants;

public class AboutAppActivity extends Activity {
    private Button btnProblem;
    private TextView textMsg,  btnAbout, textMsg2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        btnProblem = findViewById(R.id.problem);
        btnProblem.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));

        btnAbout = findViewById(R.id.textAbout);
        btnAbout.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));

        textMsg= findViewById(R.id.textmsg);
        textMsg.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));

        textMsg2= findViewById(R.id.textmsg2);
        textMsg2.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoRegular)));
    }

    public void onBackOnClick(View view) {
        Log.i(Constants.TAG_ABOUT_APP, getString(R.string.onBackMainActivity) );
        finish();
    }

    public void onProblemClick(View view) {
        Log.i(Constants.TAG_ABOUT_APP, getString(R.string.problems) );
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.linkForms)));
        startActivity(browserIntent);
    }

}
