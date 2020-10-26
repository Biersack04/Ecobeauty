package com.example.ecobeauty.aboutapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.ecobeauty.MainActivity;
import com.example.ecobeauty.R;

public class AboutAppActivity extends Activity {
    private Button btnProblem;
    private TextView textMsg;
    private TextView  btnAbout;
    private TextView  textMsg2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        btnProblem = (Button) findViewById(R.id.problem);
        btnProblem.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/robotoMedium.ttf"));

        btnAbout = (TextView ) findViewById(R.id.textAbout);
        btnAbout.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/robotoMedium.ttf"));

        textMsg= (TextView ) findViewById(R.id.textmsg);
        textMsg.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/robotoMedium.ttf"));

        textMsg2= (TextView ) findViewById(R.id.textmsg2);
        textMsg2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/robotoRegular.ttf"));

    }

    public void onBackOnClick(View view)
    {
        Log.i(getString(R.string.AboutAppLog), getString(R.string.onBackMainActivity) );
        Intent intent = new Intent(AboutAppActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onProblemClick(View view)
    {
        Log.i(getString(R.string.AboutAppLog), getString(R.string.problems) );
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.linkForms)));
        startActivity(browserIntent);
    }

}
