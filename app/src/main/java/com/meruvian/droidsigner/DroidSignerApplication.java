package com.meruvian.droidsigner;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.meruvian.droidsigner.entity.Authentication;
import com.meruvian.droidsigner.activity.LoginActivity;
import com.meruvian.droidsigner.entity.DaoMaster;
import com.meruvian.droidsigner.entity.DaoSession;
import com.meruvian.droidsigner.utils.AuthenticationUtils;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

/**
 * Created by root on 8/12/15.
 */
public class DroidSignerApplication extends Application {
    private static DroidSignerApplication instance;
    private RestAdapter restAdapter;
    private JobManager jobManager;
    private ObjectMapper objectMapper;
    private DaoSession daoSession;

    public DroidSignerApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Iconify.with(new FontAwesomeModule());
        configureJobManager();
        configureRestAdaper();
        configureDatabase();
    }

    private void configureRestAdaper() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                Authentication auth = AuthenticationUtils.getCurrentAuthentication();

                if (auth != null) {
                    request.addHeader("Authorization", "Bearer " + auth.getAccesToken());
                }
            }
        };

        restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://crs.meruvian.org")
                .setRequestInterceptor(requestInterceptor)
                .setConverter(new JacksonConverter(objectMapper))
                .build();
    }

    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();
        jobManager = new JobManager(this, configuration);
    }

    private void configureDatabase() {
        DaoMaster.OpenHelper helper = new DaoMaster.OpenHelper(this, DroidSignerConstants.DATABASE_NAME, null) {
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };

        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    public static DroidSignerApplication getInstance() {
        return instance;
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
