package com.midterm.restaurant_app.model;

public class Order {
    private String idOrder;
    private String idAcc;
    private String idTable;
    private String statusOrdered;
    private double totalBill;
    private String dtimeOrder;

    public Order() {}
    public Order(String idOrder, String idAcc, String idTable, String statusOrdered, double totalBill, String dtimeOrder) {
        this.idOrder = idOrder;
        this.idAcc = idAcc;
        this.idTable = idTable;
        this.statusOrdered = statusOrdered;
        this.totalBill = totalBill;
        this.dtimeOrder = dtimeOrder;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdAcc() {
        return idAcc;
    }

    public void setIdAcc(String idAcc) {
        this.idAcc = idAcc;
    }

    public String getIdTable() {
        return idTable;
    }

    public void setIdTable(String idTable) {
        this.idTable = idTable;
    }

    public String getStatusOrdered() {
        return statusOrdered;
    }

    public void setStatusOrdered(String statusOrdered) {
        this.statusOrdered = statusOrdered;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }

    public String getDtimeOrder() {
        return dtimeOrder;
    }

    public void setDtimeOrder(String dtimeOrder) {
        this.dtimeOrder = dtimeOrder;
    }
}
