package id.rootca.sivion.dsigner.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import id.rootca.sivion.dsigner.DroidSignerApplication;
import id.rootca.sivion.dsigner.R;
import id.rootca.sivion.dsigner.adapter.DocumentListAdapter;
import id.rootca.sivion.dsigner.entity.Authentication;
import id.rootca.sivion.dsigner.entity.DaoSession;
import id.rootca.sivion.dsigner.entity.Document;
import id.rootca.sivion.dsigner.entity.DocumentDao;
import id.rootca.sivion.dsigner.job.DocumentSignJob;
import id.rootca.sivion.dsigner.job.JobStatus;
import id.rootca.sivion.dsigner.utils.AuthenticationUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DocumentListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DocumentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentListFragment extends Fragment {
    @Bind(R.id.fab) FloatingActionButton btnScan;
    @Bind(R.id.doc_list) RecyclerView docList;
    private DocumentListAdapter docListAdapter;

    public static DocumentListFragment newInstance() {
        DocumentListFragment fragment = new DocumentListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public DocumentListFragment() {
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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) btnScan.getLayoutParams();
            p.setMargins(0, 0, dpToPx(getActivity(), 8), 0); // get rid of margins since shadow area is now the margin
            btnScan.setLayoutParams(p);
        }

        btnScan.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_qrcode).colorRes(android.R.color.white));
        docList.setHasFixedSize(true);
        docList.setLayoutManager(new LinearLayoutManager(getActivity()));
        docList.setAdapter(docListAdapter = new DocumentListAdapter(getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadDocuments();
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        Authentication authentication = AuthenticationUtils.getCurrentAuthentication();
        Log.d("cek expire", String.valueOf(authentication.getExpiresIn()));
        FragmentUtils.replaceFragment(getFragmentManager(), ScannerFragment.newInstance(), true);
    }

    private void loadDocuments() {
        DaoSession daoSession = DroidSignerApplication.getInstance().getDaoSession();
        final DocumentDao docDao = daoSession.getDocumentDao();
        new AsyncTask<Void, Void, List<Document>>() {
            @Override
            protected List<Document> doInBackground(Void... params) {
                return docDao.queryBuilder()
                        .orderDesc(DocumentDao.Properties.DbCreateDate)
                        .limit(50)
                        .build().forCurrentThread()
                        .list();
            }

            @Override
            protected void onPostExecute(List<Document> documents) {
                docListAdapter.addDocuments(documents);
            }
        }.execute();
    }

    private static int dpToPx(Context context, float dp) {
        // Reference http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }

}
