package com.midterm.restaurant_app.model.repository;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.midterm.restaurant_app.model.Table;

public class TableRepository extends Repository{

    public static TableRepository instance;

    @Override
    protected String getDatabaseReference() {
        return "Table";
    }
    public static synchronized TableRepository getInstance() {
        if (instance == null) {
            instance = new TableRepository();
        }
        return instance;
    }

    @Override
    protected Class<Table> getModelClass() {
        return Table.class;
    }

    @Override
    protected void handleDatabaseError(DatabaseError databaseError) {

        // Get the error message from the database error
        String errorMessage = databaseError.getMessage();

        // Display a message to the user
//        Toast.makeText(context, "Database error: " + errorMessage, Toast.LENGTH_SHORT).show();

        // Log the error for further analysis
        Log.e("DEBUG", "Database error: " + errorMessage);
    }
}