package com.example.ecobeauty.mydeparture;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {
	
    public static final String PREFS_NAME = "WORD_APP";
    public static final String FAVORITES = "Words_Favorite";
    private static final String LOG_TAG = "myLogs" ;

    public SharedPreference() {
        super();
    }
    
    // This four methods are used for maintaining favorites.
    
    //save to sharedPreference
    public void saveFavorites(Context context, List<Word> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);
        editor.commit();
    }
    
    //add to sharedPreference
    public void addFavorite(Context context, Word word) {
        List<Word> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Word>();
        favorites.add(word);

        saveFavorites(context, favorites);
    }

    //remove from sharedPreference
    public void removeFavorite(Context context, Word word) {
        ArrayList<Word> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(word);
            saveFavorites(context, favorites);
        }
    }

    //get all words from sharedPreference
    public ArrayList<Word> getFavorites(Context context) {
        SharedPreferences settings;
        List<Word> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Word[] favoriteItems = gson.fromJson(jsonFavorites,	Word[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Word>(favorites);
        } else
            return null;

        return (ArrayList<Word>) favorites;
    }
}
