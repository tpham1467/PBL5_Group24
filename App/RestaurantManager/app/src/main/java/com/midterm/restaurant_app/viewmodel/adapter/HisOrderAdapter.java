package com.midterm.restaurant_app.viewmodel.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.databinding.ItemHisOrderBinding;
import com.midterm.restaurant_app.model.Account;
import com.midterm.restaurant_app.model.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HisOrderAdapter extends RecyclerView.Adapter<HisOrderAdapter.ViewHolder>{
    private List<Order> orderList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private ItemHisOrderBinding bindingHisOrder;
    private HashMap<String, Account> hashMapAccount;

    public HisOrderAdapter(Context context, HashMap<String, Account> hashMapAccount) {
        layoutInflater = LayoutInflater.from(context);
        this.hashMapAccount =hashMapAccount;
    }

    public void setData(List<Order> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        bindingHisOrder = ItemHisOrderBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(bindingHisOrder);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bindingHisOrder.setOrder(order);

        String nameCustomer = "";
        for (Map.Entry<String, Account> entry : hashMapAccount.entrySet()) {
            if(order.getIdAcc().equals(entry.getKey())){
                nameCustomer = entry.getValue().getNameUser().toString().trim();
                holder.bindingHisOrder.tvNamecus.setText(entry.getValue().getNameUser().toString().trim());
            }
        }

        String finalNameCustomer = nameCustomer;
        holder.bindingHisOrder.llTableitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("idOrder", order.getIdOrder().toString() + " 1 " + finalNameCustomer);
                Navigation.findNavController(view).navigate(R.id.action_hisOrderFragment_to_detailHisOrderFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemHisOrderBinding bindingHisOrder;

        public ViewHolder(@NonNull ItemHisOrderBinding binding) {
            super(binding.getRoot());
            this.bindingHisOrder = binding;
        }
    }
}
