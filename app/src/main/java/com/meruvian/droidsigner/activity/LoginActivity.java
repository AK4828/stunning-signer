package com.meruvian.droidsigner.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.meruvian.droidsigner.DroidSignerApplication;
import com.meruvian.droidsigner.R;
import com.meruvian.droidsigner.service.LoginService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.RestAdapter;

/**
 * Created by root on 8/12/15.
 */
public class LoginActivity extends AppCompatActivity  {
    @Bind(R.id.input_email) EditText inputEmail;
    @Bind(R.id.input_password) EditText inputPassword;
    @Bind(R.id.btn_login) Button btnLogin;

    private ProgressDialog progressDialog;

    private RestAdapter restAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        restAdapter = DroidSignerApplication.getInstance().getRestAdapter();
    }

    @OnClick(R.id.btn_login) void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                new Login().execute();
            default:
                break;
        }
    }
    class Login extends AsyncTask<String,String,String>{
        boolean failure = false;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Tunggu Sebentar...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            LoginService loginService = restAdapter.create(LoginService.class);
            String username = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            try {
                Map<String, String> param = new HashMap<>();
                param.put("grant_type", "password");
                param.put("username", username);
                param.put("password", password);
                param.put("client_id", "419c6697-14b7-4853-880e-b68e3731e316");
                param.put("client_secret", "s3cr3t");

                // Authorization header: Basic base64(clientId:clientSecret)
                String authorization = new String(Base64.encode("419c6697-14b7-4853-880e-b68e3731e316:s3cr3t".getBytes(), Base64.DEFAULT));

                Map<String, String> response = loginService.login("Basic " + authorization, param);
                Intent ii = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(ii);

                finish();
                Log.i("Response", response.toString());

            }catch (Exception e){
                
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if (s != null) {
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
            }
        }
    }

}
