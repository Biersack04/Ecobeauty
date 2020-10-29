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

	private Context context;
	private String CREATE_TABLE, query;
	private SQLiteDatabase db;
	private ContentValues contentValues;
	private Cursor cursor;
	private Word wordFound, word;
	private List<Word> wordsList;

	public DBSQLiteHandler(Context context) {
		super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		CREATE_TABLE = "CREATE TABLE " + Constants.TABLE_WORD + "("
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
		db = this.getWritableDatabase();
		
		contentValues = new ContentValues();
		contentValues.put(Constants.KEY_WORD, word.getWord());
		contentValues.put(Constants.KEY_POS, word.getPartOfSpeech());
		
		db.insert(Constants.TABLE_WORD,null,contentValues);
		db.close();		
	}

	public void removeWord(Word word){
		db = this.getWritableDatabase();
		
		db.delete(Constants.TABLE_WORD, Constants.KEY_WORD + " = ?", new String[]{String.valueOf(word.getWord())});
		db.close();
	}

	public Word getWord(Word word){
		db = this.getReadableDatabase();

		cursor = db.query(Constants.TABLE_WORD, new String[]{Constants.KEY_ID,Constants.KEY_WORD,Constants.KEY_POS},Constants.KEY_ID + " = ?",
				new String[]{String.valueOf(word.getId())}, null,null,null,null);
		if(cursor!=null)
			cursor.moveToFirst();

		wordFound = new Word(cursor.getString(1), cursor.getString(2));

		return wordFound;
	}

	public ArrayList<Word> getWords(){
		wordsList = new ArrayList<Word>();
		query = "SELECT * FROM " + Constants.TABLE_WORD;
		
		db = this.getReadableDatabase();
		cursor = db.rawQuery(query, null);
		
		if(cursor.moveToFirst()){
			do{
				word = new Word(cursor.getString(1), cursor.getString(2));
				wordsList.add(word);
			}while(cursor.moveToNext());
		}
		return (ArrayList<Word>) wordsList;
	}
}
