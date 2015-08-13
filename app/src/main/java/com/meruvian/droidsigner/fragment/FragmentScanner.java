package com.meruvian.droidsigner.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

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
    @Override
    public void handleResult(Result rawResult){
        Bundle bundle = new Bundle();
        bundle.putString("qrcode",rawResult.getText());

        Toast.makeText(getActivity(), "Code = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onPause(){
        super.onPause();
        mScannerView.stopCamera();
    }
}
