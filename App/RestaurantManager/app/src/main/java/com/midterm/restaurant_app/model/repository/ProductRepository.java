package com.midterm.restaurant_app.model.repository;

import com.google.firebase.database.DatabaseError;
import com.midterm.restaurant_app.model.Product;

public class ProductRepository extends Repository<Product> {

    public static ProductRepository instance;

    public static synchronized ProductRepository getInstance(){
        if(instance == null){
            instance = new ProductRepository();
        }

        return  instance;
    }

    @Override
    protected String getDatabaseReference() {
        return "Product";
    }

    @Override
    protected Class<Product> getModelClass() {
        return Product.class;
    }

    @Override
    protected void handleDatabaseError(DatabaseError databaseError) {

    }
}
