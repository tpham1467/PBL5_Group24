package com.midterm.restaurant_app.model;

public class Table {
    private String idTable;
    private String nameTable;
    private String statusTB;
    private String status_rq;
    private String time_rq;
    private int capacity;
    private int floor;
    private int is_tranfer_foods;

    public Table() {
    }

    public Table(String idTable, String nameTable, String statusTB, String status_rq, String time_rq, int capacity, int floor, int is_tranfer_foods) {
        this.idTable = idTable;
        this.nameTable = nameTable;
        this.statusTB = statusTB;
        this.status_rq = status_rq;
        this.time_rq = time_rq;
        this.capacity = capacity;
        this.floor = floor;
        this.is_tranfer_foods = is_tranfer_foods;
    }

    public String getIdTable() {
        return idTable;
    }

    public void setIdTable(String idTable) {
        this.idTable = idTable;
    }

    public String getNameTable() {
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public String getStatusTB() {
        return statusTB;
    }

    public void setStatusTB(String statusTB) {
        this.statusTB = statusTB;
    }

    public String getStatus_rq() {
        return status_rq;
    }

    public void setStatus_rq(String status_rq) {
        this.status_rq = status_rq;
    }

    public String getTime_rq() {
        return time_rq;
    }

    public void setTime_rq(String time_rq) {
        this.time_rq = time_rq;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getIs_tranfer_foods() {
        return is_tranfer_foods;
    }

    public void setIs_tranfer_foods(int is_tranfer_foods) {
        this.is_tranfer_foods = is_tranfer_foods;
    }
}
