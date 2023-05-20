package com.midterm.restaurant_app.model;

public class Account {
    private String idAcc;
    private String nameAcc;
    private String password;
    private String nameUser;
    private String phoneNumber;
    private String email;
    private String address;
    private String urlAvatar;
    private int idRole;

    public Account() {
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public Account(String idAcc, String nameAcc, String password, String nameUser, String phoneNumber, String email, String address, String urlAvatar, int idRole) {
        this.idAcc = idAcc;
        this.nameAcc = nameAcc;
        this.password = password;
        this.nameUser = nameUser;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.urlAvatar = urlAvatar;
        this.idRole = idRole;
    }

    public String getIdAcc() {
        return idAcc;
    }

    public void setIdAcc(String idAcc) {
        this.idAcc = idAcc;
    }

    public String getNameAcc() {
        return nameAcc;
    }

    public void setNameAcc(String nameAcc) {
        this.nameAcc = nameAcc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }
}
