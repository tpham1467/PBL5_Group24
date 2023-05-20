package com.midterm.restaurant_app.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.restaurant_app.model.Account;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    private static CustomerRepository instance;
    private final DatabaseReference databaseReference;
    private final List<Account> customerItems = new ArrayList<>();
    private final MutableLiveData<List<Account>> mutableLiveData = new MutableLiveData<>();

    private CustomerRepository(){
        databaseReference = FirebaseDatabase.getInstance().getReference("customer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerItems.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Account customerItem = snapshot.getValue(Account.class);
                    customerItems.add(customerItem);
                }
                mutableLiveData.postValue(customerItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static synchronized CustomerRepository getInstance(){
        if(instance == null){
            instance = new CustomerRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Account>> getMutableLiveData(){
        return mutableLiveData;
    }

    public void addCustomerItem(Account customerItem){
        databaseReference.push().setValue(customerItem);
    }

    public void updateCustomerItem(String key, Account customerItem){
        databaseReference.child(key).setValue(customerItem);
    }

    public void deleteCustomerItem(String key){
        databaseReference.child(key).removeValue();
    }
}
