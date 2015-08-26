package com.meruvian.droidsigner.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meruvian.droidsigner.DroidSignerApplication;
import com.meruvian.droidsigner.R;
import com.meruvian.droidsigner.content.adapter.DocumentAdapter;
import com.meruvian.droidsigner.content.adapter.DocumentDetailsAdapter;
import com.meruvian.droidsigner.content.adapter.DocumentDownloadedDatabaseAdapter;
import com.meruvian.droidsigner.entity.Document;
import com.meruvian.droidsigner.job.DocumentDownloadJob;
import com.path.android.jobqueue.JobManager;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 * Created by root on 8/14/15.
 */
public class DocumentDownloadedFragment extends Fragment {
    @Bind(R.id.doc_subject) TextView docSubject;
    @Bind(R.id.doc_description) TextView docDescription;
    @Bind(R.id.doc_props) RecyclerView docProps;

    private DocumentAdapter documentAdapter;
    private DocumentDownloadedDatabaseAdapter documentDownloadedDatabaseAdapter;
    private DocumentDetailsAdapter docAdapter;

    public static DocumentDownloadedFragment newInstance(String id) {
        DocumentDownloadedFragment instance = new DocumentDownloadedFragment();
        instance.setArguments(new Bundle());
        instance.getArguments().putString("id", id);

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_document_download, container, false);
        ButterKnife.bind(this, view);

        docProps.setHasFixedSize(true);
        docProps.setLayoutManager(new LinearLayoutManager(getActivity()));
        docProps.setAdapter(docAdapter = new DocumentDetailsAdapter(getActivity()));

        JobManager jobManager = DroidSignerApplication.getInstance().getJobManager();
        jobManager.addJobInBackground(DocumentDownloadJob.newInstance(getArguments().getString("id")));

        EventBus.getDefault().register(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        documentDownloadedDatabaseAdapter = new DocumentDownloadedDatabaseAdapter(getActivity());
        documentAdapter = new DocumentAdapter(getActivity(), documentDownloadedDatabaseAdapter.findAllDownloadedDocument());
    }

    public void onEventMainThread(DocumentDownloadJob.DocumentDownloadEvent event) {
        Document document = event.getDocument();
        docSubject.setText(document.getSubject());
        docDescription.setText(document.getDescription());
        docAdapter.update(document);

        document.setStatus(1);
        document.setCreateDate(new Date().getTime());

        documentDownloadedDatabaseAdapter.save(document);
        documentAdapter.clear();
        documentAdapter.addDocuments(documentDownloadedDatabaseAdapter.findAllDownloadedDocument());
    }
}
