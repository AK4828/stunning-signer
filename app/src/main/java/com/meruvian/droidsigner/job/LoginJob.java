package com.meruvian.droidsigner.job;

import android.app.ProgressDialog;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;

import com.meruvian.droidsigner.DroidSignerApplication;
import com.meruvian.droidsigner.activity.LoginActivity;
import com.meruvian.droidsigner.entity.Authentication;
import com.meruvian.droidsigner.entity.user.User;
import com.meruvian.droidsigner.service.LoginService;
import com.meruvian.droidsigner.service.UserService;
import com.meruvian.droidsigner.utils.AuthenticationUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

/**
 * Created by root on 8/14/15.
 */
public class LoginJob extends Job {
    private String username;
    private String password;

    public LoginJob(String username, String password) {
        super(new Params(1).requireNetwork().persist());

        this.username = username;
        this.password = password;
    }

    @Override
    public void onAdded() {

        EventBus.getDefault().post(new LoginEvent(LoginEvent.LOGIN_STARTED, null));

    }

    @Override
    public void onRun() throws Throwable {
        RestAdapter restAdapter = DroidSignerApplication.getInstance().getRestAdapter();
        LoginService loginService = restAdapter.create(LoginService.class);
        UserService userService = restAdapter.create(UserService.class);

        Map<String, String> param = new HashMap<>();
        param.put("grant_type", "password");
        param.put("username", username);
        param.put("password", password);
        param.put("client_id", "419c6697-14b7-4853-880e-b68e3731e316");
        param.put("client_secret", "s3cr3t");

        // Authorization header: Basic base64(clientId:clientSecret)
        String authorization = new String(Base64.encode("419c6697-14b7-4853-880e-b68e3731e316:s3cr3t".getBytes(), Base64.DEFAULT));

        Authentication authentication = loginService.login("Basic " + authorization, param);

        // Login success
        if (authentication != null) {
            AuthenticationUtils.registerAuthentication(authentication);
            User user = userService.getCurrentUser();
            authentication.setUser(user);
            AuthenticationUtils.registerAuthentication(authentication);

            EventBus.getDefault().post(new LoginEvent(LoginEvent.LOGIN_SUCCESS, authentication));
        } else {
            EventBus.getDefault().post(new LoginEvent(LoginEvent.LOGIN_FAILED, null));
        }

    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new LoginEvent(LoginEvent.LOGIN_ERROR, null));
    }


    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        Log.e("Auth", throwable.getMessage(), throwable);

        return false;
    }

    public static class LoginEvent {
        public static final int LOGIN_SUCCESS = 0;
        public static final int LOGIN_FAILED = 1;
        public static final int LOGIN_ERROR = 2;
        public static final int LOGIN_STARTED = 3;

        private final Authentication authentication;
        private final int status;

        public LoginEvent(int status, Authentication authentication) {
            this.authentication = authentication;
            this.status = status;
        }

        public Authentication getAuthentication() {
            return authentication;
        }

        public int getStatus() {
            return status;
        }
    }

}
