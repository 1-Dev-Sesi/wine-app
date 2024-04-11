package com.devsesi.wineapp.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Arrays;
import java.util.List;

public class SharedPreferencesUtils {

    public static String getSavedUsername(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }

    public static String getSavedPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        return sharedPreferences.getString("password", "");
    }

    public static List<String> getUserRoles(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        String userRolesString = sharedPreferences.getString("user_roles", "");

        return Arrays.asList(userRolesString.split(","));
    }

    public static void clearSavedCredentials(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void saveCredentials(Context context, String username, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    public static void saveUserRoles(Context context, List<String> userRoles) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder rolesStringBuilder = new StringBuilder();
        for (String role : userRoles) {
            rolesStringBuilder.append(role).append(",");
        }

        String rolesString = rolesStringBuilder.deleteCharAt(rolesStringBuilder.length() - 1).toString();
        editor.putString("user_roles", rolesString);
        editor.apply();
    }
}
