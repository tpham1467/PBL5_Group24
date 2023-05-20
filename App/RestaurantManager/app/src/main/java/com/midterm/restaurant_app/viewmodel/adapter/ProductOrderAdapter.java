package com.midterm.restaurant_app.viewmodel.adapter;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.restaurant_app.MainActivity;
import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.databinding.DialogRemoveFoodBinding;
import com.midterm.restaurant_app.databinding.ItemDetailsOrderBinding;
import com.midterm.restaurant_app.model.Account;
import com.midterm.restaurant_app.model.DetailOrder;
import com.midterm.restaurant_app.model.Order;
import com.midterm.restaurant_app.model.Product;
import com.midterm.restaurant_app.viewmodel.modelView.DetailOrderViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ViewHolder> {

    private Context context;
    private List<DetailOrder> detailOrderItems;
    private ItemDetailsOrderBinding bindingDetailsOrder;
    private HashMap<String,Product> productHashMap;
    private CheckBox checkBox;
    private int status;
    private Order order;

    public ProductOrderAdapter(Context context,HashMap<String,Product> productHashMap, int status,Order order) {
        this.context = context;
        this.productHashMap = productHashMap;
        this.status = status;
        this.order = order;
    }

    public void setData(List<DetailOrder> items){
        this.detailOrderItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        bindingDetailsOrder = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_details_order,
                parent,
                false
        );
        return new ViewHolder(bindingDetailsOrder);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetailOrder detailOrder = detailOrderItems.get(position);
        holder.bindingDetailsOrder.setDetailOrder(detailOrder);

        MainActivity mainActivity = new MainActivity();
        Account account = mainActivity.accountSignIn;

        checkBox = holder.bindingDetailsOrder.checkboxComfirm;
        if(detailOrder.getStatusDetailOrder().equals("Done")){
            checkBox.setChecked(true);
        }
        else checkBox.setChecked(false);

        if(status == 1){
            bindingDetailsOrder.llTableitem.removeView(bindingDetailsOrder.constrainAction);
        }

        if(account.getIdRole() == 0){
            holder.bindingDetailsOrder.constrainAction.removeView(checkBox);
        }
        else {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    updateStatus(detailOrder.getIdDetailOrder(),isChecked);
                }
            });
        }

        setImageAndgetTotalBill(detailOrder);

        holder.bindingDetailsOrder.imRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFeedbackDialog(Gravity.CENTER,detailOrder);
            }
        });

    }

    private void setImageAndgetTotalBill(DetailOrder detailOrder){
        for (Map.Entry<String, Product> entry : productHashMap.entrySet()) {
            if(detailOrder.getIdProduct().equals(entry.getKey())){
                bindingDetailsOrder.tvNameProduct.setText(entry.getValue().getNameProduct());
                bindingDetailsOrder.tvCostFood.setText(entry.getValue().getPricesProduct().toString());
                if(entry.getValue().getUrlProduct()!= ""){
                    Glide.with(context)
                            .load(entry.getValue().getUrlProduct())
                            .centerCrop()
                            .placeholder(R.drawable.initialimage)
                            .into(bindingDetailsOrder.cirvProduct);
                }
            }
        }
    }
    private void openFeedbackDialog(int gravity, DetailOrder detailOrder){
        final Dialog dialog = new Dialog(bindingDetailsOrder.imRemove.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_remove_food);
        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        TextView nameProduct = dialog.findViewById(R.id.tv_name_remove);
        CircleImageView circleImageProduct = dialog.findViewById(R.id.my_image_remove);

        for (Map.Entry<String, Product> entry : productHashMap.entrySet()) {
            if(detailOrder.getIdProduct().equals(entry.getKey())){
                nameProduct.setText(entry.getValue().getNameProduct());
                if(entry.getValue().getUrlProduct()!= ""){
                    Glide.with(context)
                            .load(entry.getValue().getUrlProduct())
                            .centerCrop()
                            .placeholder(R.drawable.initialimage)
                            .into(circleImageProduct);
                }
            }
        }

        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windownAttribute = window.getAttributes();
        windownAttribute.gravity = gravity;
        window.setAttributes(windownAttribute);
        Spinner spReason = dialog.findViewById(R.id.sp_reason);

        String[] reasons = {"Đã hết nguyên liệu","Có vấn đề trong lúc nấu","Khác"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(reasons));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context.getApplicationContext(),R.layout.style_spinner,arrayList);
        spReason.setAdapter(arrayAdapter);

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        DetailOrderViewModel detailOrderViewModel = new DetailOrderViewModel();

        Button btnDelete = dialog.findViewById(R.id.btn_del);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailOrderViewModel.delete(detailOrder.getIdDetailOrder());
                dialog.dismiss();
                for (Map.Entry<String, Product> entry : productHashMap.entrySet()) {
                    if(detailOrder.getIdProduct().equals(entry.getKey())){
                        Double totalBill = order.getTotalBill() - detailOrder.getQuantity()* entry.getValue().getPricesProduct();
                        FirebaseDatabase.getInstance().getReference("Order")
                                .child(detailOrder.getIdOrder())
                                .child("totalBill")
                                .setValue(totalBill);
                        notifyDataSetChanged();
                    }
                }
            }
        });

        if(Gravity.BOTTOM == gravity){
            dialog.setCancelable(true);
        }
        else {
            dialog.setCancelable(false);
        }

        dialog.show();
    }

//    private void updateTotal(){
//
//    }


    private void updateStatus(String idDetailOrder, boolean isChecked){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DetailOrder").child(idDetailOrder);
        if(isChecked){
            databaseReference.child("statusDetailOrder").setValue("Done");
        }
        else{
            databaseReference.child("statusDetailOrder").setValue("Not Done");
        }
    }

    @Override
    public int getItemCount() {
        if(detailOrderItems != null){
            return detailOrderItems.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ItemDetailsOrderBinding bindingDetailsOrder;

        public ViewHolder(@NonNull ItemDetailsOrderBinding binding) {
            super(binding.getRoot());
            this.bindingDetailsOrder = binding;
        }

    }
}
