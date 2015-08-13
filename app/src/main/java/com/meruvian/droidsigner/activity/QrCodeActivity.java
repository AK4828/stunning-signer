package com.meruvian.droidsigner.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.meruvian.droidsigner.R;
import com.meruvian.droidsigner.fragment.FragmentScanner;

/**
 * Created by root on 8/12/15.
 */
public class QrCodeActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.actions, menu);
        return  true;

    }
    public void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.content, fragment, tag).addToBackStack(null).commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
//        if(item.getItemId() == R.id.action_scan){
//            replaceFragment(new FragmentScanner(), "scanner");
//        }
        return true;
    }

}
