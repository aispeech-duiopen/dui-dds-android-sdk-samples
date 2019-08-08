package com.aispeech.nativedemo.music;

import android.content.Context;
import android.content.SharedPreferences;

import com.aispeech.nativedemo.DuiApplication;
import com.google.gson.Gson;


/**
 * @author nemo
 */
public class LocalMusicSp {
    private SharedPreferences preferences;
    private SharedPreferences preferencesMusic;

    private static LocalMusicSp sInstance;

    private Gson mGson;


    private LocalMusicSp() {
        preferences = DuiApplication.getContext().getSharedPreferences("style", Context.MODE_PRIVATE);
        preferencesMusic = DuiApplication.getContext().getSharedPreferences("music", Context.MODE_PRIVATE);
        mGson = new Gson();
    }


    public static synchronized LocalMusicSp get() {
        if (sInstance == null) {
            sInstance = new LocalMusicSp();
        }
        return sInstance;
    }

    public void put(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public void favorite(Music value) {
        SharedPreferences.Editor editor = preferencesMusic.edit();
        editor.putString(value.getTitle(), mGson.toJson(value));
        editor.apply();
    }

    public boolean isFavorite(String title) {
        Music m = mGson.fromJson(preferencesMusic.getString(title, ""), Music.class);
        if (m != null) {
            return m.isFavorite();
        }
        return false;
    }

    public void unFavorite(Music title) {
        preferencesMusic.edit().remove(title.getTitle()).apply();
    }
}
