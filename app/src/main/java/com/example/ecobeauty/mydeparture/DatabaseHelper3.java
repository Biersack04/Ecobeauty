package com.example.ecobeauty.mydeparture;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ecobeauty.main.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper3 extends SQLiteOpenHelper {

    private SQLiteDatabase mDatabase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    public DatabaseHelper3(Context context) {
        super(context, Constants.DB_NAME_THREE, null, Constants.DB_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            Constants.DB_PATH_THREE = context.getApplicationInfo().dataDir + Constants.DATABASES;
        else
            Constants.DB_PATH_THREE = Constants.DATA_DATA + context.getPackageName() + Constants.DATABASES;
        this.mContext = context;

        copyDatabase();

        this.getReadableDatabase();
    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(Constants.DB_PATH_THREE + Constants.DB_NAME_THREE);
            if (dbFile.exists())
                dbFile.delete();

            copyDatabase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDatabase() {
        File dbFile = new File(Constants.DB_PATH_THREE + Constants.DB_NAME_THREE);
        return dbFile.exists();
    }

    private void copyDatabase() {
        if (!checkDatabase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error(Constants.IOE_ERROR_COPING);
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(Constants.DB_NAME_THREE);
        OutputStream mOutput = new FileOutputStream(Constants.DB_PATH_THREE + Constants.DB_NAME_THREE);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDatabase() throws SQLException {
        mDatabase = SQLiteDatabase.openDatabase(Constants.DB_PATH_THREE + Constants.DB_NAME_THREE, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDatabase != null;
    }

    @Override
    public synchronized void close() {
        if (mDatabase != null)
            mDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }
}
