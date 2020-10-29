package com.example.ecobeauty.mydeparture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ecobeauty.main.Constants;
import com.example.ecobeauty.mydeparture.Word;

import java.util.ArrayList;
import java.util.List;

public class DBSQLiteHandler extends SQLiteOpenHelper {

	Context context;

	public DBSQLiteHandler(Context context) {
		super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + Constants.TABLE_WORD + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.KEY_WORD + " TEXT,"
                + Constants.KEY_POS + " TEXT" + ")";
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_WORD);
		onCreate(db);
	}

	public void addWord(Word word){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(Constants.KEY_WORD, word.getWord());
		contentValues.put(Constants.KEY_POS, word.getPartOfSpeech());
		
		db.insert(Constants.TABLE_WORD,null,contentValues);
		db.close();		
	}

	public void removeWord(Word word){
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(Constants.TABLE_WORD, Constants.KEY_WORD + " = ?", new String[]{String.valueOf(word.getWord())});
		db.close();
	}

	public Word getWord(Word word){
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(Constants.TABLE_WORD, new String[]{Constants.KEY_ID,Constants.KEY_WORD,Constants.KEY_POS},Constants.KEY_ID + " = ?",
				new String[]{String.valueOf(word.getId())}, null,null,null,null);
		if(cursor!=null)
			cursor.moveToFirst();

		Word wordFound = new Word(cursor.getString(1), cursor.getString(2));

		return wordFound;
	}

	public ArrayList<Word> getWords(){
		List<Word> wordsList = new ArrayList<Word>();
		String query = "SELECT * FROM " + Constants.TABLE_WORD;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		if(cursor.moveToFirst()){
			do{
				Word word = new Word(cursor.getString(1), cursor.getString(2));
				wordsList.add(word);
			}while(cursor.moveToNext());
		}
		return (ArrayList<Word>) wordsList;
	}
}
