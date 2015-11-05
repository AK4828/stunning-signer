package id.rootca.sivion.dsigner.job;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.UUID;

import de.greenrobot.event.EventBus;
import id.or.rootca.sivion.toolkit.cms.CmsSigner;
import id.or.rootca.sivion.toolkit.commons.KeyPairUtils;
import id.or.rootca.sivion.toolkit.commons.KeyStoreUtils;
import id.rootca.sivion.dsigner.DroidSignerApplication;
import id.rootca.sivion.dsigner.entity.DaoSession;
import id.rootca.sivion.dsigner.entity.KeyStore;
import id.rootca.sivion.dsigner.entity.SignedDocument;
import id.rootca.sivion.dsigner.entity.SignedDocumentDao;
import id.rootca.sivion.dsigner.utils.AuthenticationUtils;

/**
 * Created by akm on 15/10/15.
 */
public class DocumentSignJob extends Job {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private String password;
    private String filePath;

    public static DocumentSignJob newInstance(String password,String filePath) {
        DocumentSignJob job = new DocumentSignJob();

        job.password = password;
        job.filePath = filePath;
        return job;
    }

    public DocumentSignJob(){
        super(new Params(1).requireNetwork().persist());

    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new DocumentSignEvent(password, filePath, JobStatus.ADDED));
    }

    @Override
    public void onRun() throws Throwable {
        KeyStore keyStore = AuthenticationUtils.getKeyStore();
        try {
            FileInputStream p12File = new FileInputStream(new File(keyStore.getLocation()));
            char[] certPassword = password.toCharArray();
            java.security.KeyStore ks = KeyStoreUtils.getKeyStore(p12File, certPassword, keyStore.getType());

            KeyPair keyPair = KeyPairUtils.getKeyPair(ks, certPassword);
            X509Certificate cert = KeyStoreUtils.getCertificate(ks);
            Assert.assertNotNull(keyPair);
            Assert.assertNotNull(cert);

            File inputFile = new File(filePath);
            File outputPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File outputFile = new File(outputPath, UUID.randomUUID().toString() + ".p7b");
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            FileReader reader = new FileReader(outputFile);

            IOUtils.copy(reader, outputStream);

            CmsSigner signer = new CmsSigner(ks,certPassword);
            signer.sign(inputFile, outputFile);


            Collection<? extends Certificate> certs = signer.getCertificates(outputFile);

            DaoSession daoSession = DroidSignerApplication.getInstance().getDaoSession();
            SignedDocumentDao dao = daoSession.getSignedDocumentDao();

            SignedDocument signedDocument = new SignedDocument();
            signedDocument.setSignatureBlob(FileUtils.readFileToByteArray(outputFile));

            dao.insert(signedDocument);

            EventBus.getDefault().post(new DocumentSignEvent(password, filePath, JobStatus.SUCCESS));

            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(reader);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        EventBus.getDefault().post(new DocumentSignEvent(password,filePath , JobStatus.SYSTEM_ERROR));
        return false;
    }

    public static class DocumentSignEvent {
        private String password;
        private String filePath;

        private int status;

        public DocumentSignEvent(String password,String filePath, int status) {
            this.filePath = filePath;
            this.password = password;
            this.status = status;
        }

        public String getFilePath() {
            return filePath;
        }

        public String getPassword() {
            return password;
        }

        public int getStatus() {
            return status;
        }
    }
}