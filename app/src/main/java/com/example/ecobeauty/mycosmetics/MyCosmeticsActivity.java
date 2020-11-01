package com.example.ecobeauty.mycosmetics;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecobeauty.R;
import com.example.ecobeauty.main.Constants;
import com.example.ecobeauty.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.ecobeauty.main.Constants.BASIC_NAME_USER_PRODUCT;
import static com.example.ecobeauty.mycosmetics.DatabaseHelper2.COLUMN_DATE;
import static com.example.ecobeauty.mycosmetics.DatabaseHelper2.COLUMN_ID;
import static com.example.ecobeauty.mycosmetics.DatabaseHelper2.COLUMN_NAME;



public class MyCosmeticsActivity extends AppCompatActivity {

    ListView userList;
    DatabaseHelper2 databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor, seeDate, check, checkDate;
    ArrayList<String> nameCheckedProduct, nameSeeDateProduct, seeDateProduct;
    List<Map<String, String>> items;
    String[] keys = {Constants.LINE_ONE, Constants.LINE_TWO};
    int[] controlIds = {android.R.id.text1, android.R.id.text2};
    Map<String, String> map;
    int numberCountNameUserProduct, nameIndex, dateIndex, IdIndex;
    String nameUserProduct, stringCountNameUserProduct, uid, currentName, currentDate, today, name, DatenewFormat;
    DatabaseReference myRef;
    Date date;
    FirebaseUser user;
    FirebaseDatabase database;
    Long Date, idСheck, millis;
    TextView myCosmetics, cosmText, wishText, textEmptyList;
    ImageView emptyList;
    Toast toast;
    ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cosmetics);

        myCosmetics = (TextView) findViewById(R.id.textAbout);
        myCosmetics.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));
        cosmText = (TextView) findViewById(R.id.сosmtext);
        wishText = (TextView) findViewById(R.id.wishtext);
        emptyList = (ImageView) findViewById(R.id.emptyList);
        textEmptyList= (TextView) findViewById(R.id.textEmptyList);

        myCosmetics.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));
        cosmText.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoRegular)));
        wishText.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoRegular)));
        textEmptyList.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));

        userList = (ListView) findViewById(R.id.list);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = items.get(position).get(Constants.LINE_ONE);
                long ci = check(name);

                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra(Constants.KEY_ID, ci);

                intent.putExtra(Constants.NAME, name);
                startActivity(intent);

            }
        });


        databaseHelper = new DatabaseHelper2(getApplicationContext());
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        database = FirebaseDatabase.getInstance(getString(R.string.dataBase));
        myRef = database.getReference(uid);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        db = databaseHelper.getReadableDatabase();

        date = new Date();
        millis = date.getTime();

        checkDate = db.rawQuery("select name,date from " + DatabaseHelper2.TABLE + " where " +
                DatabaseHelper2.COLUMN_DATE + "<?", new String[]{String.valueOf(millis)});

        while (checkDate.moveToNext()) {

            currentName = checkDate.getString(0);
            currentDate = DateUtils.formatDateTime(this,
                    checkDate.getLong(1), DateUtils.FORMAT_SHOW_DATE);
            today = DateUtils.formatDateTime(this,
                    millis, DateUtils.FORMAT_SHOW_DATE);
            toast = Toast.makeText(getApplicationContext(), getString(R.string.u) + currentName + getString(R.string.expiryDateExpired) + currentDate + "\n" + getString(R.string.now) + today + "\n" + getString(R.string.sendForRecycling),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            if (v != null) v.setGravity(Gravity.CENTER);
            toast.show();

        }

        checkDate.close();

        userCursor = db.rawQuery("select * from " + DatabaseHelper2.TABLE + " order by " + DatabaseHelper2.COLUMN_NAME, null, null);

        nameCheckedProduct = new ArrayList<String>();

        if (userCursor.getCount() >= 1) {

            emptyList.setVisibility(View.GONE);
            textEmptyList.setVisibility(View.GONE);
            userList.setVisibility(View.VISIBLE);

            numberCountNameUserProduct = 0;
            items = new ArrayList<>();
            seeDate = db.rawQuery("select * from " + DatabaseHelper2.TABLE + " order by " + DatabaseHelper2.COLUMN_NAME, null, null);
            seeDateProduct = new ArrayList<String>();
            nameSeeDateProduct = new ArrayList<String>();

            for (int i = 0; i < seeDate.getCount(); i++) {

                while (seeDate.moveToNext()) {

                    numberCountNameUserProduct++;
                    stringCountNameUserProduct = Integer.toString(numberCountNameUserProduct);
                    nameUserProduct = BASIC_NAME_USER_PRODUCT + stringCountNameUserProduct;

                    map = new HashMap<>();

                    nameIndex = seeDate.getColumnIndex(COLUMN_NAME);
                    name = seeDate.getString(nameIndex);
                    nameSeeDateProduct.add(name);
                    map.put(Constants.LINE_ONE, name);
                    dateIndex = seeDate.getColumnIndex(COLUMN_DATE);
                    Date = seeDate.getLong(dateIndex);

                    if (Date == 0)
                    {
                        seeDateProduct.add(" ");
                        DatenewFormat = " ";
                    }
                    else {
                        DatenewFormat = DateUtils.formatDateTime(this, Date, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                        seeDateProduct.add(DatenewFormat);
                    }

                    map.put(Constants.LINE_TWO, DatenewFormat);
                    items.add(map);

                    myRef.child(Constants.USER_COSMETICS).child(nameUserProduct).setValue(new UserCosmetics(name, Date));
                }

                numberCountNameUserProduct++;
                stringCountNameUserProduct = Integer.toString(numberCountNameUserProduct);
                nameUserProduct = BASIC_NAME_USER_PRODUCT + stringCountNameUserProduct;
            }

            numberCountNameUserProduct = 0;

        } else if (userCursor.getCount() == 0) {

            items = new ArrayList<>();
            map = new HashMap<>();
            userList.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
            textEmptyList.setVisibility(View.VISIBLE);
        }

        adapter = new SimpleAdapter(this, items, android.R.layout.simple_list_item_2, keys, controlIds);
        userList.setAdapter(adapter);
    }


    public Long check(String name) {

        idСheck = Long.valueOf(0);

        db = databaseHelper.getReadableDatabase();
        check = db.rawQuery("select * from " + DatabaseHelper2.TABLE + " where " +
                COLUMN_NAME + " = ?", new String[]{name});
        check.moveToFirst();

        IdIndex = check.getColumnIndex(COLUMN_ID); // Выводит
        idСheck = check.getLong(IdIndex);

        return idСheck;
    }


    public void add(View view) {

        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }


    public void goWish(View view) {

        Intent intent = new Intent(this, WishList.class);
        startActivity(intent);
    }

    public void goHome(View view) {
        Intent intent = new Intent(MyCosmeticsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
