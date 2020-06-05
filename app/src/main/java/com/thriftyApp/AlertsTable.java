package com.thriftyApp;

public class AlertsTable {

    int aid, uid;
    String message, alert_at;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getalert_at() {
        return alert_at;
    }

    public void setalert_at(String alert_at) {
        this.alert_at = alert_at;
    }


}
