package com.midterm.restaurant_app.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.midterm.restaurant_app.MainActivity;
import com.midterm.restaurant_app.databinding.FragmentServeBinding;
import com.midterm.restaurant_app.model.Account;
import com.midterm.restaurant_app.model.Order;
import com.midterm.restaurant_app.model.Table;
import com.midterm.restaurant_app.viewmodel.adapter.OrderAdapter;
import com.midterm.restaurant_app.viewmodel.modelView.OrderViewModel;
import com.midterm.restaurant_app.viewmodel.modelView.TableViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServeFragment extends Fragment {
    private FragmentServeBinding binding;
    private OrderAdapter orderAdapter;
    private OrderViewModel orderViewModel;
    private TableViewModel tableViewModel;
    private HashMap<String, Table> hashMapTable;
    private List<Order> lstOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using data binding
        binding = FragmentServeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerAllTable = binding.rvAlltable;

        hashMapTable = new HashMap<>();


        tableViewModel = new TableViewModel();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerAllTable.setLayoutManager(linearLayoutManager);

        orderViewModel = new OrderViewModel();
        MainActivity mainActivity = new MainActivity();
        Account account = mainActivity.accountSignIn;
        orderViewModel.getAll().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                lstOrders = new ArrayList<>();
                for (Order order : orders){
                    if(order.getStatusOrdered().equals("Serving...")){
                        tableViewModel.getById(order.getIdTable()).observe(getViewLifecycleOwner(), new Observer<Table>() {
                            @Override
                            public void onChanged(Table table) {
                                hashMapTable.put(order.getIdTable(),table);
                                orderAdapter = new OrderAdapter(view.getContext(),hashMapTable);
                                if(account.getIdRole() == 1){
                                    binding.tvTitle.setText("All Order");
                                    setAdapterOrder();
                                    recyclerAllTable.setAdapter(orderAdapter);
                                }
                                else {
                                    binding.tvTitle.setText("Your Order");
                                    if(order.getIdAcc().equals(account.getIdAcc()) && order.getStatusOrdered().equals("Serving...")){
                                        setAdapterOrder();
                                        recyclerAllTable.setAdapter(orderAdapter);
                                    }
                                }
                            }

                            private void setAdapterOrder() {
                                lstOrders.add(order);
                                orderAdapter.setData(lstOrders);
                                orderAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        });
    }
}


