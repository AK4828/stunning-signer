package com.meruvian.droidsigner.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.meruvian.droidsigner.R;
import com.meruvian.droidsigner.content.adapter.DocumentAdapter;
import com.meruvian.droidsigner.content.adapter.DocumentDownloadedDatabaseAdapter;
import com.meruvian.droidsigner.fragment.ListSignedDocumentFragment;
import com.meruvian.droidsigner.utils.AuthenticationUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by root on 8/12/15.
 */
public class MainActivity extends AppCompatActivity  {
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.nav_view) NavigationView navigationView;
    private DocumentAdapter documentAdapter;
    private DocumentDownloadedDatabaseAdapter documentDownloadedDatabaseAdapter;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        buildAlertDialog();

        if (AuthenticationUtils.getKeyStore() == null) {
            // TODO: Force user to insert keystore file before start using app
            // alertDialog.show();
        }

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        setupNavigationDrawer();

        if (savedInstanceState == null) {
            FragmentTransaction tr = getFragmentManager().beginTransaction();
            tr.replace(R.id.container_body, ListSignedDocumentFragment.newInstance());
            tr.commit();
        }

        documentDownloadedDatabaseAdapter = new DocumentDownloadedDatabaseAdapter(this);
        documentAdapter = new DocumentAdapter(this, documentDownloadedDatabaseAdapter.findAllDownloadedDocument());
//        documentList.setAdapter(documentAdapter);

//        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        String tel = telephonyManager.getDeviceId();
//        Log.d("IMEI",tel);
    }

    private void buildAlertDialog() {
        alertDialog = new AlertDialog.Builder(this)
            .setTitle("Choose certificate file first!")
            .setPositiveButton("Choose", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                    try {
                        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 5);
                    } catch (android.content.ActivityNotFoundException ex) {
                        // Potentially direct the user to the Market with a Dialog
                        Toast.makeText(MainActivity.this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                    }
                }
            })
            .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.finish();
                }
            })
            .setCancelable(false)
            .create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5) {
            if (data == null) {
                alertDialog.show();
            } else {
                Toast.makeText(this, data.getData().getPath(), Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        if (item.getTitle() == null) {
            return true;
        }

        if (item.getTitle().equals(R.string.logout)) {
            onClickLogout();
        }

        if (item.getTitle().equals(R.string.home)) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            drawerLayout.closeDrawer(GravityCompat.END);
        }

        return true;
    }

    public void setupNavigationDrawer() {
        String[] drawerItems = getResources().getStringArray(R.array.nav_drawer_items);
        String[] drawerIcons = getResources().getStringArray(R.array.nav_drawer_icons);

        int i = 0;
        for (String title : drawerItems) {
            MenuItem item = navigationView.getMenu().add(title);
            item.setIcon(new IconDrawable(this, FontAwesomeIcons.valueOf(drawerIcons[i])));

            i++;
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                return onOptionsItemSelected(menuItem);
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
