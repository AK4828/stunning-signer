package com.meruvian.droidsigner.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meruvian.droidsigner.DroidSignerApplication;
import com.meruvian.droidsigner.R;
import com.meruvian.droidsigner.entity.Document;
import com.meruvian.droidsigner.job.DocumentDownloadJob;
import com.path.android.jobqueue.JobManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 * Created by root on 8/14/15.
 */
public class FragmentDocumentDownloaded extends Fragment {
    @Bind(R.id.text_subject)
    TextView txtSubject;
    @Bind(R.id.text_description)
    TextView txtDescription;
    @Bind(R.id.text_content_type)
    TextView txtContentType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_document_download, container, false);
        ButterKnife.bind(this, view);

        JobManager jobManager = DroidSignerApplication.getInstance().getJobManager();
        jobManager.addJobInBackground(new DocumentDownloadJob(getArguments().getString("id")));

        EventBus.getDefault().register(this);

        return  view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onEventMainThread(DocumentDownloadJob.DocumentDownloadEvent event) {
        Document document = event.getDocument();
        txtSubject.setText(document.getSubject());
        txtDescription.setText(document.getDescription());
        txtContentType.setText(document.getFileInfo().getContentType());
    }
}
