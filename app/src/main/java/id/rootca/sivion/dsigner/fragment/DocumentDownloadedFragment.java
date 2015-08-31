package id.rootca.sivion.dsigner.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.path.android.jobqueue.JobManager;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import id.or.rootca.sivion.toolkit.commons.KeyStoreUtils;
import id.rootca.sivion.dsigner.DroidSignerApplication;
import id.rootca.sivion.dsigner.R;
import id.rootca.sivion.dsigner.adapter.DocumentDetailAdapter;
import id.rootca.sivion.dsigner.entity.Document;
import id.rootca.sivion.dsigner.entity.KeyStore;
import id.rootca.sivion.dsigner.job.DocumentDownloadJob;
import id.rootca.sivion.dsigner.job.JobStatus;
import id.rootca.sivion.dsigner.utils.AuthenticationUtils;

/**
 * Created by root on 8/14/15.
 */
public class DocumentDownloadedFragment extends Fragment {
    @Bind(R.id.doc_subject) TextView docSubject;
    @Bind(R.id.doc_description) TextView docDescription;
    @Bind(R.id.doc_props) RecyclerView docProps;
    @Bind(R.id.doc_sign) Button docSign;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Document document;
    private AlertDialog alertDialog;
    private DocumentDetailAdapter docAdapter;

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
        docProps.setAdapter(docAdapter = new DocumentDetailAdapter(getActivity()));

        JobManager jobManager = DroidSignerApplication.getInstance().getJobManager();
        jobManager.addJobInBackground(DocumentDownloadJob.newInstance(getArguments().getString("id")));

        setupAlertDialog();

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

    @OnClick(R.id.doc_sign)
    public void signBtnClicked() {
        alertDialog.show();
    }

    public void onEventMainThread(DocumentDownloadJob.DocumentDownloadEvent event) {
        if (event.getStatus() == JobStatus.SUCCESS) {
            document = event.getDocument();
            docSubject.setText(document.getSubject());
            docDescription.setText(document.getDescription());
            docAdapter.update(document);

            docSign.setEnabled(true);
        }
    }

    private void signData(Object data, String password) {
        KeyStore keyStore = AuthenticationUtils.getKeyStore();

        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(keyStore.getLocation()));
            java.security.KeyStore ks = KeyStoreUtils.getKeyStore(input, password.toCharArray(), keyStore.getType());
            IOUtils.closeQuietly(input);

            File outputFile = new File(getActivity().getFilesDir(), UUID.randomUUID().toString());

//            XmlObjectSigner xmlObjectSigner = new XmlObjectSigner(ks, password.toCharArray());
//            xmlObjectSigner.sign(data, outputFile);

            StringWriter writer = new StringWriter();
            FileReader reader = new FileReader(outputFile);

            IOUtils.copy(reader, writer);

            Toast.makeText(getActivity(), writer.toString(), Toast.LENGTH_LONG).show();

            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(writer);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Failed signing document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            log.error(e.getMessage(), e);
        }
    }

    private void setupAlertDialog() {
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        DialogInterface.OnClickListener onPositiveButton = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signData(document, input.getText().toString());
            }
        };

        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.password)
                .setPositiveButton(R.string.sign, onPositiveButton)
                .setCancelable(false)
                .setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_key))
                .setView(input)
                .create();
    }
}
