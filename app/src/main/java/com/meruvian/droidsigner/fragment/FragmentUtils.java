package com.meruvian.droidsigner.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.meruvian.droidsigner.R;

/**
 * Created by dianw on 8/26/15.
 */
public class FragmentUtils {
    public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.container_body, fragment, tag).addToBackStack(null).commit();
    }
}
