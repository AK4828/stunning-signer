package com.meruvian.droidsigner.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.google.zxing.Result;
import com.meruvian.droidsigner.DroidSignerApplication;
import com.meruvian.droidsigner.R;
import com.meruvian.droidsigner.entity.Authentication;
import com.meruvian.droidsigner.job.DocumentDownloadJob;
import com.meruvian.droidsigner.service.DocumentService;
import com.path.android.jobqueue.JobManager;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit.RestAdapter;

/**
 * Created by root on 8/12/15.
 */
public class FragmentScanner extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mScannerView = new ZXingScannerView(getActivity());
        return mScannerView;
    }
    @Override
    public void onResume(){
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    public void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.content, fragment, tag).addToBackStack(null).commit();
    }

    @Override
    public void handleResult(Result rawResult){
        Bundle bundle = new Bundle();
        bundle.putString("qrcode", rawResult.getText());

        String id = rawResult.getText().toString();

        if (id != null) {
            FragmentDocumentDownloaded fragment = new FragmentDocumentDownloaded();
            fragment.setArguments(new Bundle());
            fragment.getArguments().putString("id", id);

            replaceFragment(fragment, "downloaded");
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        mScannerView.stopCamera();
    }
}
