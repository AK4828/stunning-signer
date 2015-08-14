package com.meruvian.droidsigner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by root on 28/10/14.
 */
public class DefaultPersistence implements Serializable {

    public enum SYNC_STATUS {
        READY, DONE
    }

    private String id;
    @JsonIgnore
    private LogInformation logInformation = new LogInformation();
    @JsonIgnore
    private String siteId;
    @JsonIgnore
    private String refId;
    @JsonIgnore
    private SYNC_STATUS syncStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LogInformation getLogInformation() {
        return logInformation;
    }

    public void setLogInformation(LogInformation logInformation) {
        this.logInformation = logInformation;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public SYNC_STATUS getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(SYNC_STATUS syncStatus) {
        this.syncStatus = syncStatus;
    }
}
