package id.rootca.sivion.dsigner.service;

import id.rootca.sivion.dsigner.entity.Document;

import java.io.InputStream;
import java.util.Map;

import id.rootca.sivion.dsigner.entity.SignedDocument;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import retrofit.mime.TypedInput;

/**
 * Created by root on 8/14/15.
 */
public interface DocumentService {
    @POST("/api/documents")
    Document saveDocumentFile(@QueryMap Map<String, String> documents, InputStream stream) throws Exception;

    @POST(("/api/documents/{id}/sign"))
    SignedDocument uploadSignedDocument(@Body SignedDocument signedDocument, @Path("id") String id);


    @PUT("/api/documents/{id}")
    Document updateDocument(@Path("id") String docId,Document document);

    @GET("/api/documents/{id}")
    Document getDocumentById(@Path("id") String id);

    @GET("/api/documents/{id}/file")
    Response getDocumentFileById(@Path("id") String id);
}
