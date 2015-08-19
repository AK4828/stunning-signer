package com.meruvian.droidsigner.entity;

import java.io.InputStream;

/**
 * Created by root on 8/14/15.
 */
public class FileInfo extends DefaultPersistence {
    private String originalName;
    private String contentType;
    private String path;
    private long size = 0;
    private InputStream dataBlob;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getDataBlob() {
        return dataBlob;
    }

    public void setDataBlob(InputStream dataBlob) {
        this.dataBlob = dataBlob;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
