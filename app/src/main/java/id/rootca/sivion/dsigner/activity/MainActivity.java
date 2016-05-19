package id.rootca.sivion.dsigner.activity;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.security.KeyChain;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.rootca.sivion.dsigner.R;
import id.rootca.sivion.dsigner.entity.KeyStore;
import id.rootca.sivion.dsigner.fragment.DocumentListFragment;
import id.rootca.sivion.dsigner.fragment.FragmentUtils;
import id.rootca.sivion.dsigner.utils.AuthenticationUtils;

/**
 * Created by root on 8/12/15.
 */
public class MainActivity extends AppCompatActivity  {
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.nav_view) NavigationView navigationView;

    private final List<String> allowedExtensions = Arrays.asList(".pfx", ".p12", ".jks");
    private final List<String> keyStoreTypes = Arrays.asList("PKCS12", "PKCS12", "JKS");

    private AlertDialog alertDialog;

    protected static final int CHOOSE_FILE = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        setupAlertDialog();

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        setupNavigationDrawer();

        if (savedInstanceState == null) {
            FragmentUtils.replaceFragment(getFragmentManager(), DocumentListFragment.newInstance(), false);
        }

//        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        String tel = telephonyManager.getDeviceId();
//        Log.d("IMEI",tel);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Force user to insert keystore file before start using app
        KeyStore keyStore = AuthenticationUtils.getKeyStore();
        if (keyStore == null) {
            alertDialog.show();
        } else {
            if (!new File(keyStore.getLocation()).exists()) {
                alertDialog.show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_FILE) {
            Uri uri = data.getData();

//            String pathFromUri = uri.getPath();
//
//            Log.d("CEK", String.valueOf(pathFromUri));

            getRealPathFromUri(this, uri);

            certificateChoosed(data == null ? null : data.getData());

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.actions, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
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

        if (getResources().getString(R.string.logout).equals(item.getTitle())) {
            logout();
        }

        if (getResources().getString(R.string.home).equals(item.getTitle())) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	        FragmentUtils.replaceFragment(getFragmentManager(), DocumentListFragment.newInstance(), false);
            drawerLayout.closeDrawer(GravityCompat.START);
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

    private void setupAlertDialog() {
        DialogInterface.OnClickListener onPositiveButton = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), CHOOSE_FILE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(MainActivity.this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.choose_cert_title)
                .setMessage(R.string.choose_cert_msg)
                .setPositiveButton(R.string.choose, onPositiveButton)
                .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .setCancelable(false)
                .setIcon(new IconDrawable(this, FontAwesomeIcons.fa_file))
                .create();
    }

    private void certificateChoosed(Uri data) {
        if (data == null) {
            alertDialog.show();
        } else {
            String path = data.getPath();
            int ksType = -1;

            if ((ksType = isExtensionValid(path)) < 0) {
                Toast.makeText(this, R.string.invalid_cert, Toast.LENGTH_LONG).show();
                alertDialog.show();


                return;
            }

            String storeType = keyStoreTypes.get(ksType);
            KeyStore keyStore = new KeyStore();
            keyStore.setLocation(path);
            keyStore.setType(storeType);

            AuthenticationUtils.registerKeyStore(keyStore);
        }
    }

    private int isExtensionValid(String fileName) {
        int i = 0;
        for (String extension : allowedExtensions) {
            if (fileName.endsWith(extension)) {
                return i;
            }

            i++;
        }

        return -1;
    }

    public void logout() {
        AuthenticationUtils.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() <= 1) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public String getRealPathFromUri(Context context, Uri contentUri) {

        Cursor c = getContentResolver().query(contentUri, null, null, null, null);
        c.moveToNext();
        String originalPath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
        c.close();
        return originalPath;




//        try {
//            String[] resolveUri = {MediaStore.MediaColumns.DATA};
//            cursor = context.getContentResolver().query(contentUri, resolveUri, null, null, null);
//            Log.d("AAA", String.valueOf(cursor));
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            cursor.moveToFirst();
//
////            String originalPath = cursor.getString(column_index);
////
////            Log.d("PATH", originalPath);
//            return cursor.getString(column_index);
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
     }
}
