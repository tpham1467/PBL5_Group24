package com.midterm.restaurant_app.model;

public class Product {
    private String urlProduct;
    private String idProduct;
    private String nameProduct;
    private double pricesProduct;
    private String detailProduct;
    private int rateProduct;

    public Product() {
    }

    public Product(String urlProduct, String idProduct, String nameProduct, double pricesProduct, String detailProduct, int rateProduct) {
        this.urlProduct = urlProduct;
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.pricesProduct = pricesProduct;
        this.detailProduct = detailProduct;
        this.rateProduct = rateProduct;
    }

    public String getUrlProduct() {
        return urlProduct;
    }

    public void setUrlProduct(String urlProduct) {
        this.urlProduct = urlProduct;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public Double getPricesProduct() {
        return pricesProduct;
    }

    public void setPricesProduct(double pricesProduct) {
        this.pricesProduct = pricesProduct;
    }
    public String getDetailProduct() {
        return detailProduct;
    }

    public void setDetailProduct(String detailProduct) {
        this.detailProduct = detailProduct;
    }

    public int getRateProduct() {
        return rateProduct;
    }

    public void setRateProduct(int rateProduct) {
        this.rateProduct = rateProduct;
    }
}
