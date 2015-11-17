package id.rootca.sivion.dsigner.job;

import android.os.Environment;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import de.greenrobot.event.EventBus;
import id.rootca.sivion.dsigner.DroidSignerApplication;
import id.rootca.sivion.dsigner.entity.Document;
import id.rootca.sivion.dsigner.entity.DocumentDao;
import id.rootca.sivion.dsigner.entity.FileInfo;
import id.rootca.sivion.dsigner.entity.FileInfoDao;
import id.rootca.sivion.dsigner.service.DocumentService;
import retrofit.client.Response;

/**
 * Created by akm on 12/10/15.
 */
public class DocumentFileDownloadJob extends Job {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private String id;
    private FileInfo fileInfo;
    private Document document;

    public static DocumentFileDownloadJob newInstance(String id) {
        DocumentFileDownloadJob job = new DocumentFileDownloadJob();
        job.id = id;

        return job;
    }

    public DocumentFileDownloadJob() {
        super(new Params(1).requireNetwork().persist());
    }
    @Override
    public void onAdded() {
        EventBus.getDefault().post(new DocumentFileDownloadEvent(fileInfo, document, JobStatus.ADDED));
    }

    @Override
    public void onRun() throws Throwable {
        DroidSignerApplication app  = DroidSignerApplication.getInstance();
        DocumentDao documentDao = app.getDaoSession().getDocumentDao();
        FileInfoDao fileInfoDao = app.getDaoSession().getFileInfoDao();
        DocumentService documentService = app.getRestAdapter().create(DocumentService.class);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File outputFile = new File(path, UUID.randomUUID().toString());
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        Response document = documentService.getDocumentFileById(id);
        IOUtils.copy(document.getBody().in(), outputStream);
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(document.getBody().in());

        Document doc = documentDao.queryBuilder().where(DocumentDao.Properties.Id.eq(id)).build().unique();

        fileInfo = doc.getFileInfo();
        fileInfo.setPath(outputFile.getAbsolutePath());
        fileInfoDao.update(fileInfo);

        EventBus.getDefault().post(new DocumentFileDownloadEvent(fileInfo, doc, JobStatus.SUCCESS));
    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new DocumentFileDownloadEvent(fileInfo, document, JobStatus.ABORTED));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        EventBus.getDefault().post(new DocumentFileDownloadEvent(fileInfo, document,JobStatus.SYSTEM_ERROR));
        return false;
    }

    public static class DocumentFileDownloadEvent {
        private FileInfo fileInfo;
        private Document document;
        private int status;

        public DocumentFileDownloadEvent(FileInfo fileInfo, Document document, int status) {
            this.fileInfo = fileInfo;
            this.document = document;
            this.status = status;
        }

        public Document getDocument() {
            return document;
        }

        public FileInfo getFileInfo() {
            return fileInfo;
        }

        public int getStatus() {
            return status;
        }
    }
}
