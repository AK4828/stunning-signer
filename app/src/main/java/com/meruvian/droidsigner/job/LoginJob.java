package com.meruvian.droidsigner.job;

import android.app.ProgressDialog;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;

import com.meruvian.droidsigner.DroidSignerApplication;
import com.meruvian.droidsigner.DroidSignerConstants;
import com.meruvian.droidsigner.activity.LoginActivity;
import com.meruvian.droidsigner.entity.Authentication;
import com.meruvian.droidsigner.entity.user.User;
import com.meruvian.droidsigner.service.LoginService;
import com.meruvian.droidsigner.service.UserService;
import com.meruvian.droidsigner.utils.AuthenticationUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

/**
 * Created by root on 8/14/15.
 */
public class LoginJob extends Job {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private String username;
    private String password;
    private boolean refreshToken = false;

    public LoginJob() {
        super(new Params(1).requireNetwork().persist());
    }

    public static LoginJob login(String username, String password) {
        LoginJob loginJob = new LoginJob();
        loginJob.username = username;
        loginJob.password = password;
        loginJob.refreshToken = false;

        return loginJob;
    }

    public static LoginJob refresh() {
        LoginJob loginJob = new LoginJob();
        loginJob.refreshToken = true;

        return loginJob;
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

        Authentication authentication = AuthenticationUtils.getCurrentAuthentication();

        Map<String, String> param = new HashMap<>();

        if (refreshToken) {
            param.put("grant_type", "refresh_token");
            param.put("refresh_token", authentication.getRefreshToken());
        } else {
            param.put("grant_type", "password");
            param.put("username", username);
            param.put("password", password);
        }

        param.put("client_id", DroidSignerConstants.APP_ID);
        param.put("client_secret", DroidSignerConstants.APP_SECRET);

        Log.d("", "Sending login request");
        Log.d("", param.toString());

        // Authorization header: Basic base64(clientId:clientSecret)
        String authorization = DroidSignerConstants.APP_AUTHORIZATION;
        authentication = loginService.login("Basic " + authorization, param);

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
        log.error(throwable.getMessage(), throwable);

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
