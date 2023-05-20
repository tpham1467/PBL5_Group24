package com.midterm.restaurant_app.viewmodel.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.ViewTreeLifecycleOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.restaurant_app.MainActivity;
import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.databinding.ItemOrderBinding;
import com.midterm.restaurant_app.model.Account;
import com.midterm.restaurant_app.model.Order;
import com.midterm.restaurant_app.model.Table;
import com.midterm.restaurant_app.viewmodel.modelView.OrderViewModel;
import com.midterm.restaurant_app.viewmodel.modelView.ProductViewModel;
import com.midterm.restaurant_app.viewmodel.modelView.TableViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> orderList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private ItemOrderBinding bindingOrder;
    private HashMap<String, Table> tableHashMap;
    private CheckBox checkBoxAction;

    public OrderAdapter(Context context,HashMap<String, Table> tableHashMap) {
        layoutInflater = LayoutInflater.from(context);
        this.tableHashMap =tableHashMap;
    }

    public void setData(List<Order> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        bindingOrder = ItemOrderBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(bindingOrder);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bindingOrder.setOrder(orderList.get(position));

        MainActivity mainActivity = new MainActivity();
        Account account = mainActivity.accountSignIn;

        checkBoxAction = holder.bindingOrder.checkboxAction;

        if(order.getStatusOrdered().equals("Complete")){
            checkBoxAction.setChecked(true);
        }
        else checkBoxAction.setChecked(false);

        if(account.getIdRole() == 0){
            holder.bindingOrder.llTableitem.removeView(checkBoxAction);
        }
        else {
            checkBoxAction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    updateStatus(order,isChecked);
                }
            });
        }


        for (Map.Entry<String, Table> entry : tableHashMap.entrySet()) {
            if(order.getIdTable().equals(entry.getKey())){
                holder.bindingOrder.tvNametable.setText(entry.getValue().getNameTable().toString());
            }
        }

        holder.bindingOrder.llTableitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("idOrder", order.getIdOrder().toString() + " 0 " + order.getIdTable());
                Navigation.findNavController(view).navigate(R.id.action_serveFragment_to_detailsOrderFragment, bundle);
            }
        });

    }

    private void updateStatus(Order order, boolean isChecked){

        Table table = getTableById(order);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Order").child(order.getIdOrder());
        if(isChecked){
            databaseReference.child("statusOrdered").setValue("Complete");
            setStatusTable(table,"0");
        }
        else{
            databaseReference.child("statusOrdered").setValue("Serving...");
            setStatusTable(table,"1");
        }

    }

    private void setStatusTable(Table table,String status){
        FirebaseDatabase.getInstance()
                .getReference("Table")
                .child(table.getIdTable())
                .child("statusTB")
                .setValue(status);

    }

    private Table getTableById(Order order){
        for (Map.Entry<String, Table> entry : tableHashMap.entrySet()) {
            String s = order.getIdTable();
            if(order.getIdTable().equals(entry.getKey())){
                return entry.getValue();
            }
        }
        return null;
    }




    @Override

    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderBinding bindingOrder;
        public ViewHolder(@NonNull ItemOrderBinding binding) {
            super(binding.getRoot());
            this.bindingOrder = binding;
        }
    }


}
