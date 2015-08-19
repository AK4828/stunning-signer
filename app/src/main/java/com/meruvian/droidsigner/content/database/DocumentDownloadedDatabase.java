package com.meruvian.droidsigner.content.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.meruvian.droidsigner.content.model.DocumentDownloadedDatabaseModel;

/**
 * Created by root on 8/17/15.
 */
public class DocumentDownloadedDatabase extends SQLiteOpenHelper{
    public static final String DATABASE = "document_downloaded";
    private static final int VERSION = 1;

    public static final String DOCUMENT_TABLE = "tbl_downloaded_document";
    public static final String CONTENT_TYPE_TABLE = "tbl_content_type";

    private Context context;

    public DocumentDownloadedDatabase(Context context){
        super(context, DATABASE, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DOCUMENT_TABLE + "("
        + DocumentDownloadedDatabaseModel.dbId + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
        + DocumentDownloadedDatabaseModel.subject + " TEXT,"
        + DocumentDownloadedDatabaseModel.description + " TEXT,"
        + DocumentDownloadedDatabaseModel.STATUS + " INTEGER,"
        + DocumentDownloadedDatabaseModel.CREATE_DATE + " INTEGER)");

        db.execSQL("CREATE TABLE " + CONTENT_TYPE_TABLE + "("
        + DocumentDownloadedDatabaseModel.contentTypeID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
        + DocumentDownloadedDatabaseModel.contentType + "TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
        }
    }
}
