package com.meruvian.droidsigner.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meruvian.droidsigner.DroidSignerApplication;
import com.meruvian.droidsigner.entity.Authentication;
import com.meruvian.droidsigner.entity.KeyStore;

import java.io.IOException;

/**
 * Created by root on 8/14/15.
 */
public class AuthenticationUtils {
    private static final String AUTHENTICATION = "AUTHENTICATION";
    private static final String KEY_STORE = "KEY_STORE";

    public static void registerAuthentication(Authentication authentication) {
       registerPreference(AUTHENTICATION, authentication);
    }

    public static Authentication getCurrentAuthentication(){
        return getObjectFromPreference(AUTHENTICATION, Authentication.class);
    }

    public static void registerKeyStore(KeyStore keyStore) {
        registerPreference(KEY_STORE, keyStore);
    }

    public static KeyStore getKeyStore() {
        return getObjectFromPreference(KEY_STORE, KeyStore.class);
    }

    private static void registerPreference(String key, Object o) {
        ObjectMapper mapper = DroidSignerApplication.getInstance().getObjectMapper();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DroidSignerApplication.getInstance());
        SharedPreferences.Editor editor = preferences.edit();

        try {
            editor.putString(key, mapper.writeValueAsString(o));
        } catch (JsonProcessingException e) {
            Log.e(AuthenticationUtils.class.getSimpleName(), e.getMessage(), e);
        }

        editor.apply();
    }

    private static <T> T getObjectFromPreference(String key, Class<T> clazz) {
        ObjectMapper mapper = DroidSignerApplication.getInstance().getObjectMapper();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DroidSignerApplication.getInstance());
        String jsonAuth = preferences.getString(key, "");

        if (!jsonAuth.equals("")) {
            try {
                return mapper.readValue(jsonAuth, clazz);
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
        editor.remove(KEY_STORE);
        editor.apply();
    }
}
