package com.meruvian.droidsigner.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meruvian.droidsigner.DroidSignerApplication;
import com.meruvian.droidsigner.entity.Authentication;

import java.io.IOException;

/**
 * Created by root on 8/14/15.
 */
public class AuthenticationUtils {
    private static final String AUTHENTICATION = "AUTHENTICATION";

    public static void registerAuthentication(Authentication authentication) {
        ObjectMapper mapper = DroidSignerApplication.getInstance().getObjectMapper();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DroidSignerApplication.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        try {
            editor.putString(AUTHENTICATION, mapper.writeValueAsString(authentication));
        } catch (JsonProcessingException e) {
            Log.e(AuthenticationUtils.class.getSimpleName(), e.getMessage(), e);
        }
        editor.apply();
    }

    public static Authentication getCurrentAuthentication(){
        ObjectMapper mapper = DroidSignerApplication.getInstance().getObjectMapper();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DroidSignerApplication.getInstance());
        String jsonAuth = preferences.getString(AUTHENTICATION, "");

        if (!jsonAuth.equals("")) {
            try {
                return mapper.readValue(jsonAuth, Authentication.class);
            } catch (IOException e) {
                Log.e(AuthenticationUtils.class.getSimpleName(), e.getMessage(), e);
            }
        }

        return null;
    }

    public static void logout() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DroidSignerApplication.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(AUTHENTICATION);
        editor.apply();
    }
}
