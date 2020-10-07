package com.example.ecobeauty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.example.ecobeauty.DatabaseHelper2.COLUMN_DATE;
import static com.example.ecobeauty.DatabaseHelper2.COLUMN_ID;
import static com.example.ecobeauty.DatabaseHelper2.COLUMN_NAME;

public class MyCosmeticsActivity extends AppCompatActivity {

    ListView userList;
    DatabaseHelper2 databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    Cursor seedate;
    Cursor check;
    ArrayList<String> nameCheckedProduct = new ArrayList<String>();
    ArrayList<String> nameseedateProduct = new ArrayList<String>();
    ArrayList<String> seedateProduct = new ArrayList<String>();
    List<Map<String, String>> items = new ArrayList<>();
    String[] keys = { "line1", "line2" };
    int[] controlIds = { android.R.id.text1,
            android.R.id.text2 };
    Map<String, String> map = new HashMap<>();
    int numberCountNameUserProduct =0;
    String nameUserProduct;
    String stringCountNameUserProduct;
    String basicNameUserProduct ="UserProduct";
    DatabaseReference myRef;
    Date date;
    FirebaseUser user;
    String uid;
    FirebaseDatabase database;
    String currentName;
    String currentDate;
    String today;
    int nameIndex ;
    String name;
    int dateIndex;
    Long Date;
    String DatenewFormat;
    int IDIndex;
    Long idcheck;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cosmetics);

        userList = (ListView)findViewById(R.id.list);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = items.get(position).get("line1");
                long ci= check(name);

                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra("id", ci);


                intent.putExtra("name", name);
                startActivity(intent);

            }
        });


        databaseHelper = new DatabaseHelper2(getApplicationContext());
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        database = FirebaseDatabase.getInstance("https://ecobeauty2-2c5ec.firebaseio.com/");
        myRef = database.getReference(uid);

    }
    @Override
    public void onResume() {
        super.onResume();
        db = databaseHelper.getReadableDatabase();

        date = new Date();
        long millis = date.getTime();

        Cursor CheckDate = db.rawQuery("select name,date from " + DatabaseHelper2.TABLE + " where " +
                DatabaseHelper2.COLUMN_DATE + "<?", new String[]{String.valueOf(millis)});
        while (CheckDate.moveToNext()) {
            currentName = CheckDate.getString(0);
            currentDate = DateUtils.formatDateTime(this,
                    CheckDate.getLong(1), DateUtils.FORMAT_SHOW_DATE);
            today = DateUtils.formatDateTime(this,
                    millis, DateUtils.FORMAT_SHOW_DATE);

            Toast toast = Toast.makeText(getApplicationContext(),
                    "У " + currentName +" истек срок годности: "+currentDate+ "\n" + "Cегодня " + today + "\n"+ "Пора отправить это на переработку!",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            if( v != null) v.setGravity(Gravity.CENTER);
            toast.show();

        }
        CheckDate.close();

        userCursor =  db.rawQuery("select * from "+ DatabaseHelper2.TABLE + " order by "+ DatabaseHelper2.COLUMN_NAME, null, null);

        nameCheckedProduct = new ArrayList<String>();

        if(userCursor.getCount() >= 1) {
            numberCountNameUserProduct =0;
            items = new ArrayList<>();
            seedate =  db.rawQuery("select * from "+ DatabaseHelper2.TABLE + " order by "+ DatabaseHelper2.COLUMN_NAME, null, null);
            seedateProduct = new ArrayList<String>();
            nameseedateProduct = new ArrayList<String>();

            for(int i=0;i<seedate.getCount();i++) {

                while (seedate.moveToNext()) {
                    numberCountNameUserProduct++;
                    stringCountNameUserProduct = Integer.toString(numberCountNameUserProduct);
                    nameUserProduct = basicNameUserProduct + stringCountNameUserProduct;
                    map = new HashMap<>();
                    nameIndex = seedate.getColumnIndex(COLUMN_NAME);
                    name = seedate.getString(nameIndex);
                    nameseedateProduct.add(name);
                    map.put("line1", name);
                    dateIndex = seedate.getColumnIndex(COLUMN_DATE);
                    Date=seedate.getLong(dateIndex);
                    DatenewFormat = DateUtils.formatDateTime(this,
                            Date,
                            DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                    seedateProduct.add(DatenewFormat);
                    map.put("line2", DatenewFormat);
                    items.add(map);
                    myRef.child("UserCosmetics").child(nameUserProduct).setValue(new UserCosmetics(name,Date));
                }
                numberCountNameUserProduct++;
                stringCountNameUserProduct = Integer.toString(numberCountNameUserProduct);
                nameUserProduct = basicNameUserProduct + stringCountNameUserProduct;
            }
            numberCountNameUserProduct =0;

        }
        else if(userCursor.getCount() == 0)
        {
            items = new ArrayList<>();
            map = new HashMap<>();
        }

        ListAdapter adapter = new SimpleAdapter(this, items,
                android.R.layout.simple_list_item_2, keys, controlIds);
        userList.setAdapter(adapter);

    }


    public Long check (String name)
    {
        idcheck= Long.valueOf(0);

        db = databaseHelper.getReadableDatabase();
        check= db.rawQuery("select * from " + DatabaseHelper2.TABLE + " where " +
                COLUMN_NAME + " = ?" , new String[]{name});
        check.moveToFirst();

        IDIndex = check.getColumnIndex(COLUMN_ID); // Выводит
        idcheck = check.getLong(IDIndex);

        return idcheck;
    }


    public void add(View view){

        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }


    public void goWish (View view) {

        Intent intent = new Intent(this, WishList.class);
        startActivity(intent);
    }

    public void goHome(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();

    }
}