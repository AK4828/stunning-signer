package id.rootca.sivion.dsigner.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import id.rootca.sivion.dsigner.R;

/**
 * Created by dianw on 8/26/15.
 */
public class FragmentUtils {
	public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment, boolean addToBackStack) {
		if (!addToBackStack) {
			fragmentManager.popBackStack();
		}

		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.container_body, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
