package com.midterm.restaurant_app.model;
public class DetailOrder {
    private String idDetailOrder;
    private String idOrder;
    private String idProduct;
    private int quantity;
    private String statusDetailOrder;

    public DetailOrder() {
    }

    public DetailOrder(String idDetailOrder, String idOrder, String idProduct, int quantity, String statusDetailOrder) {
        this.idDetailOrder = idDetailOrder;
        this.idOrder = idOrder;
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.statusDetailOrder = statusDetailOrder;
    }

    public String getIdDetailOrder() {
        return idDetailOrder;
    }

    public void setIdDetailOrder(String idDetailOrder) {
        this.idDetailOrder = idDetailOrder;
    }
    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getStatusDetailOrder() {
        return statusDetailOrder;
    }

    public void setStatusDetailOrder(String statusDetailOrder) {
        this.statusDetailOrder = statusDetailOrder;
    }
}
