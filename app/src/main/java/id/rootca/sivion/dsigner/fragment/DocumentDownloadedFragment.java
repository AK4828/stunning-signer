package id.rootca.sivion.dsigner.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.path.android.jobqueue.JobManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import id.rootca.sivion.dsigner.DroidSignerApplication;
import id.rootca.sivion.dsigner.R;
import id.rootca.sivion.dsigner.adapter.DocumentDetailAdapter;
import id.rootca.sivion.dsigner.entity.Document;
import id.rootca.sivion.dsigner.job.DocumentDownloadJob;
import id.rootca.sivion.dsigner.job.DocumentFileDownloadJob;
import id.rootca.sivion.dsigner.job.JobStatus;

/**
 * Created by root on 8/14/15.
 */
public class DocumentDownloadedFragment extends Fragment {
    @Bind(R.id.doc_subject) TextView docSubject;
    @Bind(R.id.doc_description) TextView docDescription;
    @Bind(R.id.doc_props) RecyclerView docProps;
    @Bind(R.id.doc_view) Button docView;
    @Bind(R.id.downloading_progress) ProgressBar progressBar;

    private Document document;
    private DocumentDetailAdapter docAdapter;
    private JobManager jobManager;
    private View view;

    public static DocumentDownloadedFragment newInstance(String id) {
        DocumentDownloadedFragment instance = new DocumentDownloadedFragment();
        instance.setArguments(new Bundle());
        instance.getArguments().putString("id", id);

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_document_download, container, false);
        ButterKnife.bind(this, view);

        docProps.setHasFixedSize(true);
        docProps.setLayoutManager(new LinearLayoutManager(getActivity()));
        docProps.setAdapter(docAdapter = new DocumentDetailAdapter(getActivity()));

        jobManager = DroidSignerApplication.getInstance().getJobManager();
        jobManager.addJobInBackground(DocumentDownloadJob.newInstance(getArguments().getString("id")));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.doc_view)
    public void viewBtnClicked() {
        jobManager.addJobInBackground(DocumentFileDownloadJob.newInstance(getArguments().getString("id")));
    }

    public void onEventMainThread(DocumentDownloadJob.DocumentDownloadEvent event) {
        if (event.getStatus() == JobStatus.SUCCESS) {
            document = event.getDocument();
            docSubject.setText(document.getSubject());
            docDescription.setText(document.getDescription());
            docAdapter.update(document);

            docView.setEnabled(true);
        }
    }
    public void onEventMainThread(DocumentFileDownloadJob.DocumentFileDownloadEvent event) {
        if (event.getStatus() == JobStatus.SUCCESS) {
            Long id = event.getFileInfo().getDbId();
            String refId = event.getDocument().getId();
            FragmentUtils.replaceFragment(getFragmentManager(), DocumentViewFragment.newInstance(id, refId), true);
        } else if (event.getStatus() == JobStatus.ADDED){
            progressBar.setVisibility(view.VISIBLE);
            docView.setVisibility(view.GONE);
        } else if (event.getStatus() == JobStatus.SYSTEM_ERROR) {
            Toast.makeText(getActivity(),"Failed receiving document",Toast.LENGTH_SHORT);
        }
    }
}
