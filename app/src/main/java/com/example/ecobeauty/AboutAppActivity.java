package com.example.ecobeauty;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;

public class AboutAppActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
    }

    public void onBackOnClick(View view)
    {
        Log.i(getString(R.string.AboutAppLog), getString(R.string.backOnMainActivity) );
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
