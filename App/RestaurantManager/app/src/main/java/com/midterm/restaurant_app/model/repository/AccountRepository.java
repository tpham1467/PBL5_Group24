package com.midterm.restaurant_app.model.repository;

import com.google.firebase.database.DatabaseError;
import com.midterm.restaurant_app.model.Account;

public class AccountRepository extends Repository<Account> {

    public static AccountRepository instance;

    public static synchronized AccountRepository getInstance(){
        if(instance == null){
            instance = new AccountRepository();
        }

        return instance;
    }

    @Override
    protected String getDatabaseReference() {
        return "Account";
    }

    @Override
    protected Class<Account> getModelClass() {
        return Account.class;
    }

    @Override
    protected void handleDatabaseError(DatabaseError databaseError) {
    }
}
