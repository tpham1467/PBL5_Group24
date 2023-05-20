package com.midterm.restaurant_app.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public abstract class Repository<T> {

    protected DatabaseReference databaseReference;

    public Repository() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(getDatabaseReference());
    }

    protected abstract String getDatabaseReference();

    public LiveData<List<T>> getAll() {
        MutableLiveData<List<T>> mutableLiveData = new MutableLiveData<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<T> items = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                     T item = snapshot.getValue(getModelClass());
                    items.add(item);
                }
                mutableLiveData.postValue(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                handleDatabaseError(databaseError);
            }
        });

        return mutableLiveData;
    }

    public LiveData<T> getById(String id) {
        MutableLiveData<T> mutableLiveData = new MutableLiveData<>();

        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                T item = dataSnapshot.getValue(getModelClass());
                mutableLiveData.postValue(item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                handleDatabaseError(databaseError);
            }
        });

        return mutableLiveData;
    }

    public void insert(T item) {
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(item);
    }

    public void update(String id, T item) {
        databaseReference.child(id).setValue(item);
    }

    public void delete(String id) {
        databaseReference.child(id).removeValue();
    }

    public void deleteAll() {
        databaseReference.removeValue();
    }

    protected abstract Class<T> getModelClass();

    protected abstract void handleDatabaseError(DatabaseError databaseError);
}
