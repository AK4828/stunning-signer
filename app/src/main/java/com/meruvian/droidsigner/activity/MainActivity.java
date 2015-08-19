package com.meruvian.droidsigner.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.meruvian.droidsigner.R;
import com.meruvian.droidsigner.content.adapter.DocumentAdapter;
import com.meruvian.droidsigner.content.adapter.DocumentDownloadedDatabaseAdapter;
import com.meruvian.droidsigner.fragment.FragmentScanner;
import com.meruvian.droidsigner.utils.AuthenticationUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by root on 8/12/15.
 */
public class MainActivity extends AppCompatActivity  {
    private Toolbar mToolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private DocumentAdapter documentAdapter;
    private DocumentDownloadedDatabaseAdapter documentDownloadedDatabaseAdapter;
    @Bind(R.id.documentList) ListView documentList;

    public void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.content, fragment, tag).addToBackStack(null).commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        setupNavigationDrawer();
        FloatingActionButton FAB;
        FAB = (FloatingActionButton)findViewById(R.id.fab);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new FragmentScanner(), "scanner");
            }
        });

        documentDownloadedDatabaseAdapter = new DocumentDownloadedDatabaseAdapter(this);
        documentAdapter = new DocumentAdapter(this, documentDownloadedDatabaseAdapter.findAllDownloadedDocument());
        documentList.setAdapter(documentAdapter);

//        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        String tel = telephonyManager.getDeviceId();
//        Log.d("IMEI",tel);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.actions, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                documentAdapter.clear();
                documentAdapter.addDocuments(documentDownloadedDatabaseAdapter.findDocumentBySubject(query));

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return  true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
//        if(item.getItemId() == R.id.action_scan){
//
//        }
        if (item.getItemId() == android.R.id.home ){
            drawerLayout.openDrawer(GravityCompat.START);

        }
        if (item.getItemId() == R.id.logout){
            onClickLogout();
        }
        if (item.getItemId() == R.id.home ){
            startActivity(new Intent(this, MainActivity.class));
        }
        return true;
    }
    public void setupNavigationDrawer(){
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()){
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                switch (menuItem.getItemId()){
                    case R.id.logout:
                        onClickLogout();
                        break;
                    default:

                }
                return true;
            }

        });

        drawerLayout.closeDrawers();
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() <= 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public void onClickLogout(){
        AuthenticationUtils.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
