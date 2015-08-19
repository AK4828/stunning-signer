package com.meruvian.droidsigner.content.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.meruvian.droidsigner.content.DocumentDownloadedContentProvider;
import com.meruvian.droidsigner.content.model.DocumentDownloadedDatabaseModel;
import com.meruvian.droidsigner.entity.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 8/17/15.
 */
public class DocumentDownloadedDatabaseAdapter {
    private Uri dbDocumentDownloaded = Uri.parse(DocumentDownloadedContentProvider.CONTENT_PATH + DocumentDownloadedContentProvider.TABLES[0]);

    private Context context;

    public DocumentDownloadedDatabaseAdapter(Context context){
        this.context = context;
    }


    public void save(Document document){
        ContentValues values = new ContentValues();

        if (document.getDbId()==-1){
            values.put(DocumentDownloadedDatabaseModel.subject, document.getSubject());
            values.put(DocumentDownloadedDatabaseModel.description, document.getDescription());
            values.put(DocumentDownloadedDatabaseModel.STATUS, 1);
            values.put(DocumentDownloadedDatabaseModel.CREATE_DATE, document.getCreateDate());

            context.getContentResolver().insert(dbDocumentDownloaded, values);
        } else {
            values.put(DocumentDownloadedDatabaseModel.subject, document.getSubject());
            values.put(DocumentDownloadedDatabaseModel.description, document.getDescription());
            values.put(DocumentDownloadedDatabaseModel.STATUS, 1);
            values.put(DocumentDownloadedDatabaseModel.CREATE_DATE, document.getCreateDate());

            context.getContentResolver().update(dbDocumentDownloaded, values, DocumentDownloadedDatabaseModel.dbId+ " = ?", new
                    String[] { document.getId() + "" });
        }
    }

    public List<Document>findDocumentBySubject(String subject){
        String query = DocumentDownloadedDatabaseModel.subject + "like ? AND" + DocumentDownloadedDatabaseModel.STATUS +" = ?";
        String[] parameter = { "%" + subject + "%","1" };

        Cursor cursor = context.getContentResolver().query(dbDocumentDownloaded, null, query, parameter,
                DocumentDownloadedDatabaseModel.CREATE_DATE);

        List<Document> documents = new ArrayList<Document>();

        if (cursor != null){
            while (cursor.moveToNext()){
                Document document = new Document();
                document.setDbId(cursor.getInt(cursor.getColumnIndex(DocumentDownloadedDatabaseModel.dbId)));
                document.setSubject(cursor.getString(cursor.getColumnIndex(DocumentDownloadedDatabaseModel.subject)));
                document.setDescription(cursor.getString(cursor.getColumnIndex(DocumentDownloadedDatabaseModel.description)));

                documents.add(document);
            }
        }
        cursor.close();

        return documents;
    }

    public  List<Document>findAllDownloadedDocument(){
        String query = DocumentDownloadedDatabaseModel.STATUS + "= ?";
        String[] parameter = {"1"};
        Cursor cursor = context.getContentResolver().query(dbDocumentDownloaded, null, query, parameter, DocumentDownloadedDatabaseModel.CREATE_DATE);

        List<Document> documents = new ArrayList<Document>();

        if (cursor != null){
            if (cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    Document document = new Document();
                    document.setDbId(cursor.getInt(cursor.getColumnIndex(DocumentDownloadedDatabaseModel.dbId)));
                    document.setSubject(cursor.getString(cursor.getColumnIndex(DocumentDownloadedDatabaseModel.subject)));
                    document.setDescription(cursor.getString(cursor.getColumnIndex(DocumentDownloadedDatabaseModel.description)));
                    document.setStatus(cursor.getInt(cursor.getColumnIndex(DocumentDownloadedDatabaseModel.STATUS)));
                    document.setCreateDate(cursor.getLong(cursor.getColumnIndex(DocumentDownloadedDatabaseModel.CREATE_DATE)));

                    documents.add(document);
                }
            }
        }
        cursor.close();

        return documents;
    }

    public void delete(Document document) {
        ContentValues values = new ContentValues();
        values.put(DocumentDownloadedDatabaseModel.STATUS, 0);
        context.getContentResolver().update(dbDocumentDownloaded, values, DocumentDownloadedDatabaseModel.dbId + " = ? ", new
                String[]{document.getId() + ""});
    }

}
