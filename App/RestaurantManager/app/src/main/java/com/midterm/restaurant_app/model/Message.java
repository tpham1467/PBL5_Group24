package com.midterm.restaurant_app.model;

public class Message {
    private String idMess;
    private String idAcc;
    private String content;
    private String dtimeMess;

    public Message(String idMess, String idAcc, String content, String dtimeMess) {
        this.idMess = idMess;
        this.idAcc = idAcc;
        this.content = content;
        this.dtimeMess = dtimeMess;
    }

    public Message() {
    }

    public String getIdMess() {
        return idMess;
    }

    public void setIdMess(String idMess) {
        this.idMess = idMess;
    }

    public String getIdAcc() {
        return idAcc;
    }

    public void setIdAcc(String idAcc) {
        this.idAcc = idAcc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDtimeMess() {
        return dtimeMess;
    }

    public void setDtimeMess(String dtimeMess) {
        this.dtimeMess = dtimeMess;
    }
}
