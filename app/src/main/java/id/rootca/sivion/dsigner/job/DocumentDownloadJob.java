package id.rootca.sivion.dsigner.job;

import android.os.Environment;

import id.rootca.sivion.dsigner.DroidSignerApplication;
import id.rootca.sivion.dsigner.entity.Document;
import id.rootca.sivion.dsigner.entity.DocumentDao;
import id.rootca.sivion.dsigner.entity.FileInfo;
import id.rootca.sivion.dsigner.entity.FileInfoDao;
import id.rootca.sivion.dsigner.entity.user.User;
import id.rootca.sivion.dsigner.service.DocumentService;
import id.rootca.sivion.dsigner.utils.AuthenticationUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by root on 8/14/15.
 */
public class DocumentDownloadJob extends Job {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private String id;
    private Document document;

    public static DocumentDownloadJob newInstance(String id) {
        DocumentDownloadJob job = new DocumentDownloadJob();
        job.id = id;

        return job;
    }

    public DocumentDownloadJob() {
        super(new Params(1).requireNetwork().persist());
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new DocumentDownloadEvent(document, JobStatus.ADDED));
    }

    @Override
    public void onRun() throws Throwable {
        DroidSignerApplication app = DroidSignerApplication.getInstance();
        DocumentService documentService = app.getRestAdapter().create(DocumentService.class);
        DocumentDao documentDao = app.getDaoSession().getDocumentDao();
        FileInfoDao fileInfoDao = app.getDaoSession().getFileInfoDao();
        File outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // Scanned document already on database
        document = documentDao.queryBuilder().where(DocumentDao.Properties.Id.eq(id)).build().unique();
        if (document != null) {
            EventBus.getDefault().post(new DocumentDownloadEvent(document, JobStatus.SUCCESS));

            return;
        }

        document = documentService.getDocumentById(id);
        FileInfo fileInfo = document.getDetachedFileInfo();
        fileInfo.setPath("");
        long fileInfoId = fileInfoDao.insert(fileInfo);

        User user = AuthenticationUtils.getCurrentAuthentication().getUser();
        document.setDbCreateBy(user.getId());
        document.setDbCreateDate(new Date());
        document.setDbActiveFlag(1);
        document.setFileInfoId(fileInfoId);

        documentDao.insert(document);

        EventBus.getDefault().post(new DocumentDownloadEvent(document, JobStatus.SUCCESS));
    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new DocumentDownloadEvent(document, JobStatus.ABORTED));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        EventBus.getDefault().post(new DocumentDownloadEvent(document, JobStatus.SYSTEM_ERROR));

        return false;
    }

    public static class DocumentDownloadEvent {
        private Document document;
        private int status;

        public DocumentDownloadEvent(Document document, int status) {
            this.document = document;
            this.status = status;
        }

        public Document getDocument() {
            return document;
        }

        public int getStatus() {
            return status;
        }
    }
}
