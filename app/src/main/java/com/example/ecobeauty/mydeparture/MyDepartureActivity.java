package com.example.ecobeauty.mydeparture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecobeauty.R;
import com.example.ecobeauty.main.Constants;
import com.example.ecobeauty.main.MainActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class MyDepartureActivity<view> extends Activity {

    private TextView typeSkin, motivCountText,  titleName, nameDay, typeSkinMsg, morning, evening;;
    private DatabaseHelper3 databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursorPeriod;
    private int countPeriodProduct=0, motivCount;
    private String skinType, savedText, today,today2, dateSH, type;
    private SharedPreferences sPref;
    private RecyclerView mRecyclerViewMorning, mRecyclerViewNight;
    private RecyclerAdapter mRecyclerAdapterMorning, mRecyclerAdapterNight;
    private List<Word> wordsList1,wordsList2;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private DBSQLiteHandler dbHandler;
    private ArrayList<Word> wordsListFavourite;
    private Bundle arguments;
    private Word mapperClass;
    private Intent intent;
    private Calendar calendar, calendar2;
    private SimpleDateFormat simpleDateFormat;
    private Button myDeparture;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_departure);

        typeSkin = findViewById(R.id.textTypeSkin);
        motivCountText = findViewById(R.id.textMotivCount);
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

        arguments = getIntent().getExtras();
        if(arguments != null) {
            skinType="";
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

                dbHandler = new DBSQLiteHandler(getApplicationContext());
                db = dbHandler.getReadableDatabase();
                dbHandler.onUpgrade(db,1,2);

            }
        }else{
            loadText();
        }
        mRecyclerViewMorning = (RecyclerView) findViewById(R.id.recyclerViewMainMorning);
        mRecyclerViewNight=(RecyclerView) findViewById(R.id.recyclerViewMainNight);

        mSharedPreferences = getSharedPreferences(Constants.SP_WORDS, 0);

        prepareRecyclerView(Constants.MORNING);
        prepareWordsList(Constants.MORNING);
        prepareRecyclerView(Constants.EVENING);
        prepareWordsList(Constants.EVENING);

        databaseHelper =new DatabaseHelper3(this);

        sPref = getPreferences(MODE_PRIVATE);
        motivCount = sPref.getInt(Constants.MOTIV_COUNT, 0);
        motivCountText.setText(Integer.toString(motivCount));
        motivation();

    }


    private void prepareRecyclerView(String Period) {
        switch (Period) {
            case Constants.MORNING:
                mRecyclerViewMorning = (RecyclerView) findViewById(R.id.recyclerViewMainMorning);
                wordsList1 = new ArrayList<>();
                mRecyclerAdapterMorning = new RecyclerAdapter(getApplicationContext(), wordsList1);
                mRecyclerViewMorning.setHasFixedSize(true);
                mRecyclerViewMorning.setItemAnimator(new DefaultItemAnimator());
                mRecyclerViewMorning.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mRecyclerViewMorning.setAdapter(mRecyclerAdapterMorning);
                break;
            case Constants.EVENING:
                mRecyclerViewNight = (RecyclerView) findViewById(R.id.recyclerViewMainNight);
                wordsList2 = new ArrayList<>();
                mRecyclerAdapterNight = new RecyclerAdapter(getApplicationContext(), wordsList2);
                mRecyclerViewNight.setHasFixedSize(true);
                mRecyclerViewNight.setItemAnimator(new DefaultItemAnimator());
                mRecyclerViewNight.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mRecyclerViewNight.setAdapter(mRecyclerAdapterNight);
                break;
        }
    }

    private void prepareWordsList(String Period) {
        databaseHelper = new DatabaseHelper3(this);
        db = databaseHelper.getReadableDatabase();
        cursorPeriod = db.rawQuery("" +
                "select name, description " +
                "from main " +
                "where type_of_skin = ? and when_= ? ", new String[]{skinType, Period});
        countPeriodProduct = cursorPeriod.getCount();
        cursorPeriod.moveToFirst();

        for (int i = 0; i < countPeriodProduct; i++) {
            mapperClass = new Word(cursorPeriod.getString(0), cursorPeriod.getString(1));
             if ( Period == Constants.MORNING)
            {
                wordsList1.add(mapperClass);
            }
            else
            {
                wordsList2.add(mapperClass);
            }


            cursorPeriod.moveToNext();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mRecyclerViewMorning.setAdapter(mRecyclerAdapterMorning);
        mRecyclerViewNight.setAdapter(mRecyclerAdapterNight);
    }

    public void onTypeSkin(View view) {
        intent = new Intent(this, SkinTypeActivity.class);
        startActivity(intent);
    }


    public void onBackOnClick (View view) {
        saveText();
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        editor = sPref.edit();
        editor.putString(Constants.SAVED_TEXT, typeSkin.getText().toString());
        simpleDateFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT);
        calendar=Calendar.getInstance();
        today = simpleDateFormat.format(calendar.getTime());
        editor.putString(Constants.SAVED_DATE, today);
        editor.commit();
    }

    void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        savedText = sPref.getString(Constants.SAVED_TEXT, "");
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
        simpleDateFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT);
        calendar=Calendar.getInstance();
        today = simpleDateFormat.format(calendar.getTime());
        dateSH = sPref.getString(Constants.SAVED_DATE, Constants.NULL);
        if (!today.equals(dateSH)) {
            dbHandler = new DBSQLiteHandler(getApplicationContext());
            db = dbHandler.getReadableDatabase();
            dbHandler.onUpgrade(db,1,2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveText();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveText();
    }


    @Override
    public void onBackPressed() {
        saveText();
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public boolean checkAll() {
        dbHandler = new DBSQLiteHandler(getApplicationContext());
        wordsListFavourite = dbHandler.getWords();
        if (wordsListFavourite.size()>=mRecyclerAdapterMorning.getItemCount() && wordsListFavourite.size()!=0)
        {
            return true;
        }
        else return false;
    }


    public void motivation() {
        int count = 0;
        sPref = getPreferences(MODE_PRIVATE);
        dateSH = sPref.getString(Constants.SAVED_DATE_CHECKALL, Constants.NULL);
        simpleDateFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT);
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DATE, 0);
        today = simpleDateFormat.format(calendar.getTime());
        today2 = simpleDateFormat.format(calendar2.getTime());

        if (!dateSH.equals(today2)) {
            if (checkAll() ) {
                motivCount++;
                sPref = getPreferences(MODE_PRIVATE);
                editor = sPref.edit();
                editor.putString(Constants.SAVED_DATE_CHECKALL, today2);
            } else {
                motivCount = 0;
            }
            editor = sPref.edit();
            editor.putInt(Constants.MOTIV_COUNT, motivCount);
            editor.apply();

        }
    }}
