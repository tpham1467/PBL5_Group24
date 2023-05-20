package com.midterm.restaurant_app.model.repository;

import com.google.firebase.database.DatabaseError;
import com.midterm.restaurant_app.model.DetailOrder;

public class DetailOrderRepository extends Repository<DetailOrder> {

    public static DetailOrderRepository instance;

    @Override
    protected String getDatabaseReference() {
        return "DetailOrder";
    }

    public static synchronized DetailOrderRepository getInstance(){
        if(instance == null){
            instance = new DetailOrderRepository();
        }
        return instance;
    }

    @Override
    protected Class<DetailOrder> getModelClass() {
        return DetailOrder.class;
    }

    @Override
    protected void handleDatabaseError(DatabaseError databaseError) {

    }
}
