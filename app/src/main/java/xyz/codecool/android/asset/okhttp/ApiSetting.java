package xyz.codecool.android.asset.okhttp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ApiSetting {
    private String KEY_USER_LOGIN = "user_login";
    private String KEY_USER_NAME = "user_name";
    private String KEY_USER_ID = "user_id";

    private String KEY_ASSET_CODE = "asset_code";
    private String KEY_IP_SERVER = "ipserver";
    private String KEY_STATUS_LOGIN = "status_login";

    private SharedPreferences mPreferences;

    public ApiSetting(Context context) {
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setStatusLogin(boolean status) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(KEY_STATUS_LOGIN, status);
        editor.apply();
    }

    public boolean getStatusLogin() {
        return mPreferences.getBoolean(KEY_STATUS_LOGIN, false);
    }

    //---------------------START BASE_URL-------------//
    public String getBASE_URL(String route) {
        return mPreferences.getString(KEY_IP_SERVER, "127.0.0.1")+route;
    }

    public void setBaseUrlPublic(String url) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(KEY_IP_SERVER, url);
        editor.apply();
    }

    public void setName(String name) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    public String getName() {
        return mPreferences.getString(KEY_USER_NAME, "");
    }

    public void setId(int id) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(KEY_USER_ID, id);
        editor.apply();
    }

    public int getId() {
        return mPreferences.getInt(KEY_USER_ID, 1);
    }

    public void setLogin(boolean status) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(KEY_USER_LOGIN, status);
        editor.apply();
    }

    public boolean getLogin() {
        return mPreferences.getBoolean(KEY_USER_LOGIN, false);
    }

}
