package com.midterm.restaurant_app.model;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.List;

public class Conversation {
    private String idConvs;
    private String urlAvatar;
    private List<String> idAcc;
    private List<String> idMess;

    public Conversation(String idConvs, String urlAvatar, List<String> idAcc, List<String> idMess) {
        this.idConvs = idConvs;
        this.urlAvatar = urlAvatar;
        this.idAcc = idAcc;
        this.idMess = idMess;
    }

    public Conversation() {
    }

    public String getIdConvs() {
        return idConvs;
    }

    public void setIdConvs(String idConvs) {
        this.idConvs = idConvs;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public List<String> getIdAcc() {
        return idAcc;
    }

    public void setIdAcc(List<String> idAcc) {
        this.idAcc = idAcc;
    }

    public List<String> getIdMess() {
        return idMess;
    }

    public void setIdMess(List<String> idMess) {
        this.idMess = idMess;
    }
}
