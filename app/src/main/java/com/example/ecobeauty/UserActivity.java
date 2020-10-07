package com.example.ecobeauty;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;

public class UserActivity extends Activity {

    String[] category = {"Глаза", "Лицо", "Волосы", "Губы", "Руки", "Брови"};
    String item;
    EditText nameBox;
    EditText dateBox;
    Button delButton;
    Button saveButton;
    DatabaseHelper2 sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId;
    DatabaseReference myRef;
    FirebaseDatabase database;
    String uid;
    Calendar dateAndTime=Calendar.getInstance();
    String idFireBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        nameBox = (EditText) findViewById(R.id.name);
        dateBox = (EditText) findViewById(R.id.date);
        setInitialDateTime();
        delButton = (Button) findViewById(R.id.deleteButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        Spinner spinner = (Spinner) findViewById(R.id.category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        sqlHelper = new DatabaseHelper2(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            userId = extras.getLong("id");
            idFireBase = extras.getString("name");
        }

        if (userId > 0) {

            userCursor = db.rawQuery("select * from " + DatabaseHelper2.TABLE + " where " +
                    DatabaseHelper2.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            nameBox.setText(userCursor.getString(1));
            dateBox.setText(DateUtils.formatDateTime(this,
                    userCursor.getLong(3),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
            userCursor.close();

        } else {

            delButton.setVisibility(View.GONE);
        }

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

    }

    public void setDate(View v) {

        Log.i("UserActivityLog", " Выбор Даты ");
        new DatePickerDialog(this, sd,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialDateTime() {

        Log.i("UserActivityLog", " Установка начального времени и даты ");
        dateBox.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener sd=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    public void save(View view){

        Log.i("UserActivityLog", "Сохранение продукта");
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper2.COLUMN_NAME, nameBox.getText().toString());
        cv.put(DatabaseHelper2.COLUMN_CATEGORY, item);
        cv.put(DatabaseHelper2.COLUMN_DATE, dateAndTime.getTimeInMillis());

        if (userId > 0) {

            db.update(DatabaseHelper2.TABLE, cv, DatabaseHelper2.COLUMN_ID + "=" + userId,
                    null);
        } else {
            db.insert(DatabaseHelper2.TABLE, null, cv);
        }

        goHome();
    }

    public void delete(View view){

        Log.i("UserActivityLog", " Удаление продукта ");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        database = FirebaseDatabase.getInstance("https://ecobeauty2-2c5ec.firebaseio.com/");
        myRef = database.getReference(uid);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef
                .child(uid)
                .child("UserCosmetics")
                .orderByChild("name")
                .equalTo(idFireBase);
        query.getRef().removeValue();

        db.delete(DatabaseHelper2.TABLE, "_id = ?", new String[]{String.valueOf(userId)});

        goHome();
    }
    private void goHome(){
        db.close();
        Log.i("UserActivityLog", " Переход к Главной Активности ");
        Intent intent = new Intent(this, MyCosmeticsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}

