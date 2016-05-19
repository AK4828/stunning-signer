package id.rootca.sivion.dsigner.service;

import id.rootca.sivion.dsigner.entity.Document;
import retrofit.http.Body;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

/**
 * Created by akm on 19/11/15.
 */
public interface SignedDocumentService {
    @PUT("/api/sdocuments/{id}/signed_document")
    Document updateDocument(@Path("id") String docId, @Body TypedFile file);
}
