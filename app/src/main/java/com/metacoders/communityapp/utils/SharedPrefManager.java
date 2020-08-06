package com.metacoders.communityapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.metacoders.communityapp.models.UserModel;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "newsrmesharedprefs";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_EMAIL = "useremail";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_USER_ROLE = "userrole";
    private static final String KEY_USER_TOKEN = "usertoken";
    private static final String KEY_USER_TYPE = "usertype";

    public SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }

        return mInstance;
    }

    public boolean userLogin(String id, String name, String email, String token, String role, String userType) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_TOKEN, token);
        editor.putString(KEY_USER_ROLE, role);
        editor.putString(KEY_USER_TYPE, userType);

        editor.apply();

        return true;
    }

    public String  getUserToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(KEY_USER_TOKEN, null) ;

        return   token  ;
    }

    public UserModel getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return new UserModel(
                sharedPreferences.getString(KEY_USER_ID, null),
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(KEY_USER_EMAIL, null),
                sharedPreferences.getString(KEY_USER_TOKEN, null),
                sharedPreferences.getString(KEY_USER_ROLE, null),
                sharedPreferences.getString(KEY_USER_TYPE, null)
        );
    }

    /*public boolean isLogged() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USER_NAME, null) != null) {
            return true;
        }

        return false;
    }*/

    public boolean logout() {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        return true;
    }


}