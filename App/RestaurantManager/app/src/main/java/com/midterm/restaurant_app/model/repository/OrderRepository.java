package com.midterm.restaurant_app.model.repository;

import com.google.firebase.database.DatabaseError;
import com.midterm.restaurant_app.model.Order;

public class OrderRepository extends Repository<Order>{

    public static OrderRepository instance;

    @Override
    protected String getDatabaseReference() {
        return "Order";
    }

    public static synchronized OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    @Override
    protected Class<Order> getModelClass() {
        return Order.class;
    }

    @Override
    protected void handleDatabaseError(DatabaseError databaseError) {

    }
}
