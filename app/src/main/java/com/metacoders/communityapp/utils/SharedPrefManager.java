package com.metacoders.communityapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.metacoders.communityapp.models.newModels.SettingsModel;
import com.metacoders.communityapp.models.newModels.UserModel;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME_LOGIN = "newsrmeLogInprefs";
    private static final String SHARED_PREF_NAME = "newsrmesharedprefs";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_MODEL = "model";
    private static final String KEY_USER_EMAIL = "useremail";
    private static final String IS_USER_LOGGED_IN = "useremail";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_APP_SETTINGS = "app_settings";
    private static final String KEY_USER_ROLE = "userrole";
    private static final String KEY_USER_TOKEN = "usertoken";
    private static final String KEY_USER_TYPE = "usertype";
    private static final String KEY_USER_LANG_ID = "lang_id";
    private static final String KEY_USER_LANG_NAME = "lang_name";
    private static final String SHARED_PREF_NAME_SETTING = "settings";
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    public SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }

        return mInstance;
    }

    public String getUserToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_LOGIN, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(KEY_USER_TOKEN, "000");
        return token;
    }

    public void saveUserModel(UserModel model) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonString = gson.toJson(model);
        editor.putString(KEY_USER_MODEL, jsonString);
        editor.apply();

    }


    public UserModel getUserModel() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_LOGIN, Context.MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(KEY_USER_MODEL, null);
        Gson gson = new Gson();
        UserModel model = gson.fromJson(jsonString, UserModel.class);
        return model;
    }



    public void logout() {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_EMAIL, "null");
        editor.putString(IS_USER_LOGGED_IN, "no");

       // editor.clear();
        editor.apply();

//        SharedPreferences sharedPreferences1 = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
//
//        editor1.clear();
//        editor1.apply();


    }

    public boolean isUserLoggedIn() {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_LOGIN, Context.MODE_PRIVATE);
        //    SharedPreferences.Editor editor = sharedPreferences.edit();

        String value = sharedPreferences.getString(IS_USER_LOGGED_IN, "no");
        if (value.equals("yes")) {
            return true;

        } else return false;

    }

    public void saveUser_ID_access_token(String userID , String accessToken ) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ID, userID);
        editor.putString(IS_USER_LOGGED_IN, "yes");
        editor.putString(KEY_USER_TOKEN , accessToken) ;
        editor.apply();


    }

    public  String getUser_ID(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_LOGIN, Context.MODE_PRIVATE);
        String user = sharedPreferences.getString(KEY_USER_ID, "0");
        return  user ;
    }


    public void saveLangPref(String id, String name) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_LANG_ID, id);
        editor.putString(KEY_USER_LANG_NAME, name);
        editor.apply();
    }

    public String[] getLangPref() {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_SETTING, Context.MODE_PRIVATE);

        String id = sharedPreferences.getString(KEY_USER_LANG_ID, "0");
        String name = sharedPreferences.getString(KEY_USER_LANG_NAME, "GB");

        /*
         array 0 is id
         array 1 is name
         */
        return new String[]{id, name};
    }

    public void saveAppSettings(SettingsModel model) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonString = gson.toJson(model);
        editor.putString(KEY_APP_SETTINGS, jsonString);
        editor.apply();
    }

    public SettingsModel getAppSettingsModel() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_LOGIN, Context.MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(KEY_APP_SETTINGS, null);
        Gson gson = new Gson();
        SettingsModel model = gson.fromJson(jsonString, SettingsModel.class);
        return model;
    }


}
