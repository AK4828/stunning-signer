package com.meruvian.droidsigner.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.meruvian.droidsigner.DroidSignerApplication;
import com.meruvian.droidsigner.entity.Authentication;
import com.meruvian.droidsigner.R;
import com.meruvian.droidsigner.job.LoginJob;
import com.meruvian.droidsigner.service.LoginService;
import com.meruvian.droidsigner.utils.AuthenticationUtils;
import com.path.android.jobqueue.JobManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

/**
 * Created by root on 8/12/15.
 */
public class LoginActivity extends AppCompatActivity  {
    @Bind(R.id.input_email) EditText inputEmail;
    @Bind(R.id.input_password) EditText inputPassword;
    @Bind(R.id.btn_login) Button btnLogin;
    @Bind(R.id.progress) View progressBar;

    private JobManager jobManager;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        DroidSignerApplication application = DroidSignerApplication.getInstance();
        jobManager = application.getJobManager();
        EventBus.getDefault().register(this);

        if (AuthenticationUtils.getCurrentAuthentication() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

            // TODO: Refresh token
            // jobManager.addJobInBackground(LoginJob.refresh());
        }
    }

    @OnClick(R.id.btn_login) void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                jobManager.addJobInBackground(LoginJob.login(inputEmail.getText().toString(), inputPassword.getText().toString()));
            default:
                break;
        }
    }

    public void onEventMainThread(LoginJob.LoginEvent event) {
        int status = event.getStatus();

        if (status == LoginJob.LoginEvent.LOGIN_ERROR) {
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            inputEmail.setEnabled(true);
            inputPassword.setEnabled(true);
        }

        if (status == LoginJob.LoginEvent.LOGIN_SUCCESS){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        if (status == LoginJob.LoginEvent.LOGIN_STARTED){
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            inputEmail.setEnabled(false);
            inputPassword.setEnabled(false);
        }
    }
}
