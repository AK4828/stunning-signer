package com.meruvian.droidsigner;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import retrofit.RestAdapter;

/**
 * Created by root on 8/12/15.
 */
public class DroidSignerApplication extends Application {
    private static DroidSignerApplication instance;
    private RestAdapter restAdapter;

    public DroidSignerApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Iconify.with(new FontAwesomeModule());

        restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://192.168.2.106:8080")
                .build();
    }

    public static DroidSignerApplication getInstance() {
        return instance;
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }
}
