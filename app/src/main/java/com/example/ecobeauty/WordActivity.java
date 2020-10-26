package com.example.ecobeauty;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.ecobeauty.R;

public class WordActivity extends AppCompatActivity implements OnClickListener{

	TextView txtView_Word,txtView_POS;
	SharedPreferences sp;
	Toolbar mToolBar;
	private static final String LOG_TAG = "myLogs" ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word);
		sp = getSharedPreferences("spWords", 0);
		
		mToolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolBar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		txtView_Word = (TextView) findViewById(R.id.txtView_Individual_Word);
		txtView_POS = (TextView) findViewById(R.id.txtView_Individual_POS);
		txtView_Word.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/robotoMedium.ttf"));
		txtView_POS.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/robotoMedium.ttf"));
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		sp.getString("word", "Data N/A");


		sp.getString("pos","Data N/A" );
		
		txtView_Word.setText(sp.getString("word", "Data N/A").toString());
		txtView_POS.setText(sp.getString("pos", "Data N/A").toString());
		Log.i("TAG", " word  = " +  sp.getString("word", "Data N/A").toString());
		Log.i("TAG", " pos  = " + sp.getString("pos", "Data N/A").toString());

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   switch(item.getItemId()){
	   case android.R.id.home:
	        finish();
	        break;
	    }

	    return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		
	}
}
