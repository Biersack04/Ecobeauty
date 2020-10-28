package com.example.ecobeauty.mydeparture;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecobeauty.MainActivity;
import com.example.ecobeauty.R;

import java.util.ArrayList;
import java.util.List;


public class MyDepartureActivity<view> extends Activity implements RecyclerAdapter.ClickListener,View.OnClickListener{

    public TextView typeSkin;
    DatabaseHelper3 databaseHelper;
    SQLiteDatabase db;
    Cursor cursorPeriod;
    int countPeriodProduct =0;
    String skinType;
    SharedPreferences sPref;
    final String SAVED_TEXT = "saved_text";
    private RecyclerView mRecyclerViewM;
    private RecyclerView mRecyclerViewN;
    RecyclerAdapter mRecyclerAdapterM;
    RecyclerAdapter mRecyclerAdapterN;
    private List<Word> wordsListMorning;
    private List<Word> wordsListNight;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_departure);
        typeSkin = findViewById(R.id.textTypeSkin);
        skinType ="";

        Bundle arguments = getIntent().getExtras();
        if(arguments != null) {
            skinType ="";
            type = arguments.getString("checkType");

            if (type != null) {
                switch (type) {
                    case "normal":
                        typeSkin.setText("Нормальный тип");
                        skinType ="Нормальная";
                        break;
                    case "fat":
                        typeSkin.setText("Жирный тип");
                        skinType ="Жирная";
                        break;
                    case "dry":
                        typeSkin.setText("Сухой тип");
                        skinType ="Сухая";
                        break;
                    case "combined":
                        skinType ="Комбинированная";
                        typeSkin.setText("Комбинированный тип");
                        break;
                    default:
                        break;
                }
            }
        }else{
            loadText();
        }
        mRecyclerViewM = (RecyclerView) findViewById(R.id.recyclerViewMainMorning);
        mRecyclerViewN=(RecyclerView) findViewById(R.id.recyclerViewMainNight);
        mSharedPreferences = getSharedPreferences("spWords", 0);
        prepareRecyclerView("Утро");
        prepareWordsList("Утро");
        prepareRecyclerView("Вечер");
        prepareWordsList("Вечер");
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
            mEditor.putString("word", strWord);
            mEditor.putString("pos", strPOS);
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
            mEditor.putString("word", strWord1);
            mEditor.putString("pos", strPOS1);
            mEditor.commit();

           // Intent intent = new Intent(getApplication(),WordActivity.class);
         //   startActivity(intent);

        }
    }


    private void prepareRecyclerView(String Period) {
        switch (Period) {
            case "Утро":

                mRecyclerViewM = (RecyclerView) findViewById(R.id.recyclerViewMainMorning);
                wordsListMorning = new ArrayList<>();
                mRecyclerAdapterM = new RecyclerAdapter(getApplicationContext(), wordsListMorning);
                mRecyclerViewM.setHasFixedSize(true);
                mRecyclerAdapterM.setListener(this);
                mRecyclerViewM.setItemAnimator(new DefaultItemAnimator());
                mRecyclerViewM.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mRecyclerViewM.setAdapter(mRecyclerAdapterM);

                break;

            case "Вечер":

                mRecyclerViewN = (RecyclerView) findViewById(R.id.recyclerViewMainNight);
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

            if ( Period == "Утро")
            {
                wordsListMorning.add(mapperClass);
            }
            else
            {
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

    public void onTypeSkin(View view)
    {
        Intent intent = new Intent(this, SkinTypeActivity.class);
        startActivity(intent);
    }


    public void onBackOnClick (View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, typeSkin.getText().toString());
        ed.commit();
    }

    void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString(SAVED_TEXT, "");
        typeSkin.setText(savedText);
        switch (savedText) {
            case "Нормальный тип":
                skinType ="Нормальная";
                break;
            case "Жирный тип":
                skinType ="Жирная";
                break;
            case "Сухой тип":
                skinType ="Сухая";
                break;
            case "Комбинированный тип":
                skinType ="Комбинированная";
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
    public void onBackPressed() {
        saveText();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }


}
