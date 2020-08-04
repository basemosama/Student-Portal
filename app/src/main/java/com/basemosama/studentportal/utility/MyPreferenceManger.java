package com.basemosama.studentportal.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.basemosama.studentportal.model.login.LoggedInUser;
import com.basemosama.studentportal.model.student.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//Helper Class To handle dealing with Shared Preference and Encrypt User Info
public class MyPreferenceManger {
    private static final String TAG = "MyPreferenceManger";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public MyPreferenceManger(Context context) {

        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    "shared_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
    }

    public User getCurrentUser() {
        String userJson = sharedPreferences.getString(Constants.SAVED_LOGGED_IN_USER_KEY, "");
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        return gson.fromJson(userJson, User.class);
    }

    public void saveCurrentUser(User user) {
        String userJson = gson.toJson(user);
        sharedPreferences.edit()
                .putString(Constants.SAVED_LOGGED_IN_USER_KEY, userJson)
                .apply();
    }


    public void saveStudent(LoggedInUser user) {
        saveCurrentUser(user.getStudent());
        saveApiKey(user.getApi_token());
        saveIsCurrentUserStudent(true);
    }

    public void saveProfessor(LoggedInUser user) {
        saveCurrentUser(user.getProfessor());
        saveApiKey(user.getApi_token());
        saveIsCurrentUserStudent(false);
    }

    public void removeCurrentUser() {
        sharedPreferences.edit()
                .remove(Constants.SAVED_LOGGED_IN_USER_KEY)
                .apply();
    }


    public String getApiKey() {
        return sharedPreferences.getString(Constants.SAVED_LOGGED_IN_USER_API_TOKEN_KEY, "");
    }

    public void saveApiKey(String apiKey) {
        sharedPreferences.edit()
                .putString(Constants.SAVED_LOGGED_IN_USER_API_TOKEN_KEY, apiKey)
                .apply();
    }

    public boolean isCurrentUserStudent() {
        return sharedPreferences.getBoolean(Constants.SAVED_LOGGED_IN_USER_TYPE_KEY, true);
    }

    public void saveIsCurrentUserStudent(boolean value) {
        sharedPreferences.edit()
                .putBoolean(Constants.SAVED_LOGGED_IN_USER_TYPE_KEY, value)
                .apply();
    }


    public void clearData() {
        sharedPreferences.edit()
                .clear()
                .apply();
    }
}
