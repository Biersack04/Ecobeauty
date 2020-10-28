package com.example.ecobeauty.mydeparture;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ecobeauty.main.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {

    public SharedPreference() {
        super();
    }
    
    public void saveFavorites(Context context, List<Word> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(Constants.FAVORITES, jsonFavorites);
        editor.commit();
    }

    public void addFavorite(Context context, Word word) {
        List<Word> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Word>();
        favorites.add(word);

        saveFavorites(context, favorites);
    }

     public void removeFavorite(Context context, Word word) {
        ArrayList<Word> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(word);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Word> getFavorites(Context context) {
        SharedPreferences settings;
        List<Word> favorites;

        settings = context.getSharedPreferences(Constants.PREFS_NAME,Context.MODE_PRIVATE);

        if (settings.contains(Constants.FAVORITES)) {
            String jsonFavorites = settings.getString(Constants.FAVORITES, null);
            Gson gson = new Gson();
            Word[] favoriteItems = gson.fromJson(jsonFavorites,	Word[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Word>(favorites);
        } else
            return null;

        return (ArrayList<Word>) favorites;
    }
}
