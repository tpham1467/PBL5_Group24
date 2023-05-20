package com.midterm.restaurant_app.model;

public class Request {
    private String Content;
    private String NameTable;
    private String Time;
    private String idRequest;
    private boolean resolve;

    public Request(String content, String nameTable, String time, String idRequest, boolean resolve) {
        Content = content;
        NameTable = nameTable;
        Time = time;
        this.idRequest = idRequest;
        this.resolve = resolve;
    }

    public Request() {
    }

    public boolean isResolve() {
        return resolve;
    }

    public void setResolve(boolean resolve) {
        this.resolve = resolve;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getNameTable() {
        return NameTable;
    }

    public void setNameTable(String nameTable) {
        NameTable = nameTable;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(String idRequest) {
        this.idRequest = idRequest;
    }
}
