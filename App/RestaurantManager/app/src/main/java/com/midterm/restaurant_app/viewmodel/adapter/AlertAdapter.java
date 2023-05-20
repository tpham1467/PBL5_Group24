package com.midterm.restaurant_app.viewmodel.adapter;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.databinding.ItemAlertBinding;
import com.midterm.restaurant_app.databinding.ItemHisOrderBinding;
import com.midterm.restaurant_app.model.Request;
import com.midterm.restaurant_app.model.Table;

import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ViewHolder> {

    private Context context;
    List<Request> lstRequest;
    private ItemAlertBinding binding;

    public AlertAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Request> items){
        this.lstRequest = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemAlertBinding.inflate(LayoutInflater.from(context), parent, false);
        return new AlertAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Request request = lstRequest.get(position);
        holder.bindingAlert.tvNameTable.setText(request.getNameTable());
        holder.bindingAlert.tvTime.setText(request.getTime());
        holder.bindingAlert.edtContent.setText(request.getContent());

        CheckBox checkBox = holder.bindingAlert.cbConfirm;

        if (request.isResolve()) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        holder.bindingAlert.cbConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.bindingAlert.cbConfirm.isChecked()) {
                    changeStatusInFirbase(request.getIdRequest(),true);
                } else {
                    changeStatusInFirbase(request.getIdRequest(),false);
                }
            }
            private void changeStatusInFirbase(String idReq,boolean status){
                FirebaseDatabase.getInstance().getReference("Request").child(idReq).child("resolve").setValue(status);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(lstRequest != null){
            return lstRequest.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final ItemAlertBinding bindingAlert;

        public ViewHolder(@NonNull ItemAlertBinding binding) {
            super(binding.getRoot());
            this.bindingAlert = binding;
        }
    }

}
