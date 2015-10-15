package id.rootca.sivion.dsigner.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.pdfview.PDFView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import id.rootca.sivion.dsigner.DroidSignerApplication;
import id.rootca.sivion.dsigner.R;
import id.rootca.sivion.dsigner.entity.Document;
import id.rootca.sivion.dsigner.entity.DocumentDao;
import id.rootca.sivion.dsigner.entity.FileInfo;
import id.rootca.sivion.dsigner.job.DocumentFileDownloadJob;
import id.rootca.sivion.dsigner.job.JobStatus;

/**
 * Created by akm on 12/10/15.
 */
public class DocumentViewFragment extends Fragment {
    @Bind(R.id.pdfview) PDFView pdfView;
    @Bind(R.id.fab_sign) FloatingActionButton fabSign;

    public static DocumentViewFragment newInstance(Long id){
        DocumentViewFragment instance = new DocumentViewFragment();
        instance.setArguments(new Bundle());
        instance.getArguments().putLong("id", id);

        return instance;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_document, container, false);
        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) fabSign.getLayoutParams();
            p.setMargins(0, 0, dpToPx(getActivity(), 8), 0); // get rid of margins since shadow area is now the margin
            fabSign.setLayoutParams(p);
        }

        fabSign.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_paint_brush).colorRes(android.R.color.white));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Long id = getArguments().getLong("id");
        DroidSignerApplication app  = DroidSignerApplication.getInstance();
        DocumentDao documentDao = app.getDaoSession().getDocumentDao();
        Document doc = documentDao.queryBuilder().where(DocumentDao.Properties.FileInfoId.eq(id)).build().unique();

        if (doc != null) {
            FileInfo fileInfo = doc.getFileInfo();
            String path = fileInfo.getPath();
            Log.d("PATH",path);
            pdfView.fromFile(new File(path))
                    .defaultPage(1)
                    .showMinimap(false)
                    .enableSwipe(true)
                    .load();
        }else {
            Toast.makeText(getActivity(), "Failed receive Document", Toast.LENGTH_SHORT).show();
        }
    }

    private static int dpToPx(Context context, float dp) {
        // Reference http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }
}