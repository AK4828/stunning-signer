package com.meruvian.droidsigner.job;

import com.meruvian.droidsigner.DroidSignerApplication;
import com.meruvian.droidsigner.entity.Document;
import com.meruvian.droidsigner.service.DocumentService;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

/**
 * Created by root on 8/14/15.
 */
public class DocumentDownloadJob extends Job {
    private String id;
    private Document document;


    public DocumentDownloadJob(String id) {
        super(new Params(1).requireNetwork().persist());
        this.id = id;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        RestAdapter restAdapter = DroidSignerApplication.getInstance().getRestAdapter();
        DocumentService documentService = restAdapter.create(DocumentService.class);
        document = documentService.getDocumentById(id);

        EventBus.getDefault().post(new DocumentDownloadEvent(document));
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

    public static class DocumentDownloadEvent {
        private Document document;

        public DocumentDownloadEvent(Document document) {
            this.document = document;
        }

        public Document getDocument() {
            return document;
        }
    }
}
