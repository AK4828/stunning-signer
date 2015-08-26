package com.meruvian.droidsigner.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.meruvian.droidsigner.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListSignedDocumentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListSignedDocumentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListSignedDocumentFragment extends Fragment {
    @Bind(R.id.fab) FloatingActionButton btnScan;

    public static ListSignedDocumentFragment newInstance() {
        ListSignedDocumentFragment fragment = new ListSignedDocumentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ListSignedDocumentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_signed_document, container, false);
        ButterKnife.bind(this, view);

        btnScan.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_qrcode).colorRes(android.R.color.white));

        return view;
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        FragmentUtils.replaceFragment(getFragmentManager(), ScannerFragment.newInstance(), null);
    }
}
