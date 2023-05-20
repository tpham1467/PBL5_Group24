package com.midterm.restaurant_app.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.restaurant_app.databinding.FragmentAlertRequestBinding;
import com.midterm.restaurant_app.model.Product;
import com.midterm.restaurant_app.model.Request;
import com.midterm.restaurant_app.model.Table;
import com.midterm.restaurant_app.viewmodel.adapter.AlertAdapter;
import com.midterm.restaurant_app.viewmodel.adapter.itemsProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class AlertRequest extends Fragment {
    private List<Request> lstRequest;
    private RecyclerView recyclerRequest;
    private AlertAdapter itemsAdapter;
    private FragmentAlertRequestBinding bindingRequest;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerRequest = bindingRequest.rcvRequest;

        itemsAdapter = new AlertAdapter(view.getContext());


//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), recyclerRequest.HORIZONTAL, false);
//        recyclerRequest.setLayoutManager(linearLayoutManager);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3); // số 2 ở đây là số cột hiển thị
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),recyclerRequest.VERTICAL,false);
        recyclerRequest.setLayoutManager(linearLayoutManager);

        FirebaseDatabase.getInstance().getReference("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstRequest = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Request request = childSnapshot.getValue(Request.class);
                    lstRequest.add(request);
                    itemsAdapter.setData(lstRequest);
                    recyclerRequest.setAdapter(itemsAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        itemsAdapter.setData(products);
//        recyclerRequest.setAdapter(itemsAdapter);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindingRequest = FragmentAlertRequestBinding.inflate(inflater, container, false);
        return bindingRequest.getRoot();
    }
}