package com.example.ecobeauty.mycosmetics;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.example.ecobeauty.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import java.util.Calendar;

public class UserActivity extends Activity {

    EditText nameBox, dateBox;
    Button delButton, saveButton;
    DatabaseHelper2 sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId;
    DatabaseReference myRef;
    FirebaseDatabase database;
    Calendar dateAndTime;
    String uid, idFireBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        nameBox = (EditText) findViewById(R.id.name);
        dateBox = (EditText) findViewById(R.id.date);

        delButton = (Button) findViewById(R.id.deleteButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        sqlHelper = new DatabaseHelper2(this);
        db = sqlHelper.getWritableDatabase();

        dateAndTime = Calendar.getInstance();

        setInitialDateTime();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            userId = extras.getLong("id");
            idFireBase = extras.getString("name");
        }

        if (userId > 0) {

            userCursor = db.rawQuery("select * from " + DatabaseHelper2.TABLE + " where " + DatabaseHelper2.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            nameBox.setText(userCursor.getString(1));
            dateBox.setText(DateUtils.formatDateTime(this, userCursor.getLong(2), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
            userCursor.close();

        } else {

            delButton.setVisibility(View.GONE);
        }


    }

    public void setDate(View v) {

        new DatePickerDialog(this, sd,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialDateTime() {

        dateBox.setText(DateUtils.formatDateTime(this, dateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    DatePickerDialog.OnDateSetListener sd=new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    public void save(View view){

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper2.COLUMN_NAME, nameBox.getText().toString());
        cv.put(DatabaseHelper2.COLUMN_DATE, dateAndTime.getTimeInMillis());

        if (userId > 0) {

            db.update(DatabaseHelper2.TABLE, cv, DatabaseHelper2.COLUMN_ID + "=" + userId, null);

        } else {

            db.insert(DatabaseHelper2.TABLE, null, cv);
        }

        goHome();
    }

    public void delete(View view){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        database = FirebaseDatabase.getInstance(getString(R.string.dataBase));
        myRef = database.getReference(uid);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef
                .child(uid)
                .child(getString(R.string.userCosmetics))
                .orderByChild("name")
                .equalTo(idFireBase);
        query.getRef().removeValue();

        db.delete(DatabaseHelper2.TABLE, "_id = ?", new String[]{String.valueOf(userId)});

        goHome();
    }

    private void goHome(){

        db.close();
        Intent intent = new Intent(this, MyCosmeticsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}

