package com.midterm.restaurant_app.model;

public class Upload {
    private String nameImage;
    private String UrlImage;

    public Upload() {
    }

    public Upload(String nameImage, String urlImage) {
        this.nameImage = nameImage;
        UrlImage = urlImage;
    }

    public String getNameImage() {
        return nameImage;
    }

    public void setNameImage(String nameImage) {
        this.nameImage = nameImage;
    }

    public String getUrlImage() {
        return UrlImage;
    }

    public void setUrlImage(String urlImage) {
        UrlImage = urlImage;
    }
}
