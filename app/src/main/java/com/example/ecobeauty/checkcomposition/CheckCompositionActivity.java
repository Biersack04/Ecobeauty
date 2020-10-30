package com.example.ecobeauty.checkcomposition;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecobeauty.R;
import com.example.ecobeauty.main.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CheckCompositionActivity extends Activity {

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private EditText editText;
    private Button button;
    private ListView listView;
    private TextView checkCompos, textMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_composition);

        editText = findViewById(R.id.structure);
        button = findViewById(R.id.check);
        listView = findViewById(R.id.listView);
        checkCompos = findViewById(R.id.myDepTitle);
        textMsg = findViewById(R.id.textMsgAboutCheck);

        button.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));
        checkCompos.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoMedium)));
        textMsg.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.robotoRegular)));

        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error(Constants.IOE_ERROR_UPDATE);
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        final ArrayList<HashMap<String, Object>> component = new ArrayList<HashMap<String, Object>>();

        final String[] from = Constants.COMPOSITION;
        final int[] to = { R.id.textView, R.id.textView2, R.id.textView3, R.id.textView4};
        final SimpleAdapter adapter = new SimpleAdapter(this, component, R.layout.adapter_item, from, to);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> client;

                String product = editText.getText().toString();
                product = product.toUpperCase();
                String[] words = product.split(getString(R.string.split));

                for (String comp : words) {
                    Cursor cursor = mDb.rawQuery("SELECT * FROM comp1 WHERE name =?", new String[]{comp});
                    cursor.moveToFirst();

                    if (cursor.getCount() != 0) {
                        while (!cursor.isAfterLast()) {
                            client = new HashMap<String, Object>();
                            client.put(getString(R.string.name), cursor.getString(1));
                            client.put(getString(R.string.action), cursor.getString(2));
                            client.put(getString(R.string.danger), cursor.getString(3));
                            client.put(getString(R.string.describe_danger), cursor.getString(4));

                            component.add(client);
                            cursor.moveToNext();
                        }
                        cursor.close();
                        editText.setText("");

                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.componentsMissing, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
                listView.setAdapter(adapter);
            }
        });
    }
    public void onBackClick(View view) {
        finish();
    }
}
