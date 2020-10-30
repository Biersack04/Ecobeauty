package com.example.ecobeauty.mydeparture;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecobeauty.R;
import com.example.ecobeauty.main.Constants;
import com.example.ecobeauty.main.MainActivity;

import java.util.ArrayList;
import java.util.List;


public class MyDepartureActivity<view> extends Activity implements RecyclerAdapter.ClickListener,View.OnClickListener{

    public TextView typeSkin, titleName, nameDay, typeSkinMsg, morning, evening;
    private RecyclerView mRecyclerViewM, mRecyclerViewN;
    private List<Word> wordsListMorning, wordsListNight;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    int countPeriodProduct = 0;

    DatabaseHelper3 databaseHelper;
    SQLiteDatabase db;
    Cursor cursorPeriod;
    String skinType, type;
    SharedPreferences sPref;
    RecyclerAdapter mRecyclerAdapterM, mRecyclerAdapterN;
    Button myDeparture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_departure);

        typeSkin = findViewById(R.id.textTypeSkin);
        myDeparture = findViewById(R.id.buttonDeparture);
        titleName = findViewById(R.id.myDepTitle);
        nameDay = findViewById(R.id.numDay);
        typeSkinMsg = findViewById(R.id.typeSkin);
        morning = findViewById(R.id.morningMsg);
        evening = findViewById(R.id.eveningMsg);


        typeSkin.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoRegular)));
        myDeparture.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));
        titleName.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));
        nameDay.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoRegular)));
        typeSkinMsg.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoRegular)));
        morning.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));
        evening.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));

        skinType ="";

        Bundle arguments = getIntent().getExtras();
        if(arguments != null) {
            skinType ="";
            type = arguments.getString(Constants.CHECK_TYPE);

            if (type != null) {
                switch (type) {
                    case Constants.NORMAL:
                        typeSkin.setText(R.string.normalType);
                        skinType = getString(R.string.normalText);
                        break;
                    case Constants.FAT:
                        typeSkin.setText(R.string.fatType);
                        skinType = getString(R.string.fatText);
                        break;
                    case Constants.DRY:
                        typeSkin.setText(R.string.dryType);
                        skinType = getString(R.string.dryText);
                        break;
                    case Constants.COMBINED:
                        skinType = getString(R.string.combinedText);
                        typeSkin.setText(R.string.combinedType);
                        break;
                    default:
                        break;
                }
            }
        }else{
            loadText();
        }
        mRecyclerViewM = findViewById(R.id.recyclerViewMainMorning);
        mRecyclerViewN= findViewById(R.id.recyclerViewMainNight);
        mSharedPreferences = getSharedPreferences(Constants.SP_WORDS, 0);
        prepareRecyclerView(Constants.MORNING);
        prepareWordsList(Constants.MORNING);
        prepareRecyclerView(Constants.EVENING);
        prepareWordsList(Constants.EVENING);
        databaseHelper =new DatabaseHelper3(this);
       }

    @Override
    public void itemClicked(View view, int position) {
        View a =mRecyclerViewM.findContainingItemView(view);

        if (a!=null)
        {
            Word mapperObject = wordsListMorning.get(position);
            String strWord = mapperObject.getWord();
            String strPOS = mapperObject.getPartOfSpeech();
            mEditor = mSharedPreferences.edit();
            mEditor.putString(Constants.WORD, strWord);
            mEditor.putString(Constants.POS, strPOS);
            mEditor.commit();

         //   Intent intent = new Intent(getApplication(), WordActivity.class);
            //startActivity(intent);
        }
        else
        {
            Word mapperObject1 = wordsListNight.get(position);
            String strWord1 = mapperObject1.getWord();
            String strPOS1 = mapperObject1.getPartOfSpeech();
            mEditor = mSharedPreferences.edit();
            mEditor.putString(Constants.WORD, strWord1);
            mEditor.putString(Constants.POS, strPOS1);
            mEditor.commit();
           // Intent intent = new Intent(getApplication(),WordActivity.class);
         //   startActivity(intent);

        }
    }

    private void prepareRecyclerView(String Period) {
        switch (Period) {
            case Constants.MORNING:
                mRecyclerViewM = findViewById(R.id.recyclerViewMainMorning);
                wordsListMorning = new ArrayList<>();
                mRecyclerAdapterM = new RecyclerAdapter(getApplicationContext(), wordsListMorning);
                mRecyclerViewM.setHasFixedSize(true);
                mRecyclerAdapterM.setListener(this);
                mRecyclerViewM.setItemAnimator(new DefaultItemAnimator());
                mRecyclerViewM.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mRecyclerViewM.setAdapter(mRecyclerAdapterM);
                break;

            case Constants.EVENING:
                mRecyclerViewN = findViewById(R.id.recyclerViewMainNight);
                wordsListNight = new ArrayList<>();
                mRecyclerAdapterN = new RecyclerAdapter(getApplicationContext(), wordsListNight);
                mRecyclerViewN.setHasFixedSize(true);
                mRecyclerAdapterN.setListener(this);
                mRecyclerViewN.setItemAnimator(new DefaultItemAnimator());
                mRecyclerViewN.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mRecyclerViewN.setAdapter(mRecyclerAdapterN);
                break;
        }
    }

    private void prepareWordsList(String Period) {
        databaseHelper = new DatabaseHelper3(this);
        db = databaseHelper.getReadableDatabase();

        cursorPeriod = db.rawQuery("" +
                "select c.name, c.description " +
                "from type t " +
                "join main m on t._id=m.type_of_skin " +
                "join cosm c on m.id_cosm=c._id " +
                "where m.type_of_skin = (select _id from type where type = ?) and m.when_= ? ", new String[]{skinType, Period});
        countPeriodProduct = cursorPeriod.getCount();
        cursorPeriod.moveToFirst();

        for (int i = 0; i < countPeriodProduct; i++) {
            Word mapperClass = new Word(cursorPeriod.getString(0), cursorPeriod.getString(1));

            if ( Period == Constants.MORNING) {
                wordsListMorning.add(mapperClass);
            }
            else {
                wordsListNight.add(mapperClass);
            }
            cursorPeriod.moveToNext();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mRecyclerAdapterM.setListener(this);
        mRecyclerViewM.setAdapter(mRecyclerAdapterM);
        mRecyclerAdapterN.setListener(this);
        mRecyclerViewN.setAdapter(mRecyclerAdapterN);
        }

    public void onTypeSkin(View view) {
        Intent intent = new Intent(this, SkinTypeActivity.class);
        startActivity(intent);
    }

    public void onBackOnClick (View view) {
        saveText();
        Intent intent = new Intent(MyDepartureActivity.this, MainActivity.class);
        startActivity(intent);
    }

    void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Constants.SAVED_TEXT, typeSkin.getText().toString());
        ed.commit();
    }

    void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString(Constants.SAVED_TEXT, "");
        typeSkin.setText(savedText);
        switch (savedText) {
            case Constants.NORMAL_TYPE:
                skinType =getString(R.string.normalText);
                break;
            case Constants.FAT_TYPE:
                skinType =getString(R.string.fatText);
                break;
            case Constants.DRY_TYPE:
                skinType =getString(R.string.dryText);
                break;
            case Constants.COMBINED_TYPE:
                skinType =getString(R.string.combinedText);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveText();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveText();
    }

    @Override
    public void finish() {
        super.finish();
        saveText();
    }

    @Override
    public void onBackPressed() {
        saveText();
        Intent intent = new Intent(MyDepartureActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) { }
}
