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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.restaurant_app.MainActivity;
import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.databinding.ItemProductsBinding;
import com.midterm.restaurant_app.databinding.LayoutDialogAddFoodForTableBinding;
import com.midterm.restaurant_app.model.Account;
import com.midterm.restaurant_app.model.DetailOrder;
import com.midterm.restaurant_app.model.Order;
import com.midterm.restaurant_app.model.Product;
import com.midterm.restaurant_app.model.Table;
import com.midterm.restaurant_app.view.ServeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class itemsProductAdapter extends RecyclerView.Adapter<itemsProductAdapter.ViewHolder>{

    private Context context;
    private List<Product> productItems;
    private ItemProductsBinding binding;
    private LayoutDialogAddFoodForTableBinding bindingDialog;
    private List<String> lstItemTable;
    private List<Order> lstOrder;
    private String idAccount;
    private String selectedItem;
    private List<Table> lstTable;
    private List<DetailOrder> lstDetailOrder;
    private Table tableSelect;
    private Account account;
    private MainActivity mainActivity;
    private List<Order> listAllOrder;
    private String idTable;
    private String spinnerSlected = "";
    private List<Product> filteredData;

    public itemsProductAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Product> items) {
        this.productItems = items;
        this.filteredData = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_products,
                parent,
                false
        );
        mainActivity = new MainActivity();
        account = mainActivity.accountSignIn;

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productItems.get(position);
        holder.binding.setProduct(productItems.get(position));

        for(int i=0; i<product.getRateProduct();i++){
            ImageView imageViewRate = new ImageView(context);
            imageViewRate.setImageResource(R.drawable.ic_baseline_star_rate_24);
            imageViewRate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            binding.linearRate.addView(imageViewRate);
        }

        if (product.getUrlProduct() != "") {
            Glide.with(this.context)
                    .load(product.getUrlProduct())
                    .centerCrop()
                    .placeholder(R.drawable.initialimage)
                    .into(holder.binding.ivFoodimage);
        }

        holder.binding.cardFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFeedbackDialog(Gravity.CENTER);
            }

            private void openFeedbackDialog(int gravity) {
                final Dialog dialog = new Dialog(context);

                bindingDialog = LayoutDialogAddFoodForTableBinding.inflate(LayoutInflater.from(context));
                bindingDialog.setProduct(product);

                handleCheckOrderExist();
                getAllDetailOrder();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(bindingDialog.getRoot());

                Window window = dialog.getWindow();
                if (window == null) {
                    return;
                }

                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                windowAttributes.gravity = gravity;
                window.setAttributes(windowAttributes);

                if (Gravity.BOTTOM == gravity) {
                    dialog.setCancelable(true);
                } else {
                    dialog.setCancelable(false);
                }

                if (product.getUrlProduct() != "") {
                    Glide.with(dialog.getContext())
                            .load(product.getUrlProduct())
                            .centerCrop()
                            .placeholder(R.drawable.initialimage)
                            .into(bindingDialog.ivImageAddProduct);
                }

                FirebaseDatabase.getInstance().getReference("Table").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        lstItemTable = new ArrayList<>();
                        lstTable = new ArrayList<>();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            Table table = childSnapshot.getValue(Table.class);
                            lstTable.add(table);
                            if (account.getIdRole() == 0) {
                                    if (bindingDialog.btnAdd.getText().equals("ADD TO YOUR ORDER")) {
                                        if (table.getIdTable().equals(idTable)) {
                                            setAdapterSpiner(table);
                                            break;
                                        }
                                    } else {
                                        if (!checkTableIsUse(table))
                                            setAdapterSpiner(table);
                                    }
                            } else setAdapterSpiner(table);

                        }
                    }

                    private void setAdapterSpiner(Table table) {
                        lstItemTable.add(table.getNameTable());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context.getApplicationContext(), R.layout.style_spinner, lstItemTable);
                        adapter.notifyDataSetChanged();
                        bindingDialog.listTable.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                bindingDialog.listTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedItem = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                bindingDialog.imgPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentValue = Integer.parseInt(bindingDialog.txtNumberOfDishes.getText().toString());
                        int newValue = currentValue + 1;
                        bindingDialog.txtNumberOfDishes.setText(Integer.toString(newValue));
                    }
                });


                bindingDialog.imgMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentValue = Integer.parseInt(bindingDialog.txtNumberOfDishes.getText().toString());
                        if (currentValue > 0) {
                            int newValue = currentValue - 1;
                            bindingDialog.txtNumberOfDishes.setText(Integer.toString(newValue));
                        }
                    }
                });

                bindingDialog.btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                bindingDialog.btnAdd.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (bindingDialog.btnAdd.getText().equals("ADD TO NEW ORDER")) {
                            handleAddProductToNewOrder(product);
                            setStatusTable();
                        } else if (bindingDialog.btnAdd.getText().equals("ADD TO NEW ORDER")){
                            handleAddProductToOrder(product);
                        }
                        else {
                            handleAddProductForCustomer(product);
                        }
                    }

                    private void setStatusTable() {
                        FirebaseDatabase.getInstance().getReference("Table").child(tableSelect.getIdTable()).child("statusTB").setValue("1");
                    }
                });
                dialog.show();
            }
        });
    }
    public void setTotalBillOrder(String orderId) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order").child(orderId);
        DatabaseReference detailOrderRef = FirebaseDatabase.getInstance().getReference("DetailOrder");
        detailOrderRef.orderByChild("idOrder").equalTo(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final double[] totalBill = {0};
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String productId = snapshot.child("idProduct").getValue(String.class);
                    int quantity = snapshot.child("quantity").getValue(Integer.class);

                    DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product").child(productId);
                    productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            double price = dataSnapshot.child("pricesProduct").getValue(Double.class);
                                totalBill[0] += (price * quantity);

                            orderRef.child("totalBill").setValue(totalBill[0]);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Error", "setTotalBillOrder: " + databaseError.getMessage());
                        }
                    });
                }
                // Set total bill to 0 if there are no products in the order
                if (totalBill[0] == 0) {
                    orderRef.child("totalBill").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "setTotalBillOrder: " + databaseError.getMessage());
            }
        });
    }
    private void handleAddProductToOrder(Product product) {
        for (Order order : lstOrder) {
            if (order.getIdAcc().equals(idAccount) && order.getStatusOrdered().equals("Serving...")) {
                DetailOrder detailOrder = new DetailOrder(createNewId("DO"), order.getIdOrder(), product.getIdProduct(), Integer.parseInt(bindingDialog.txtNumberOfDishes.getText().toString().trim()), "Not Done");

                FirebaseDatabase.getInstance().getReference("DetailOrder").child(createNewId("DO")).setValue(detailOrder);
                setTotalBillOrder(order.getIdOrder());
                break;
            }
        }
    }

    private void handleAddProductForCustomer(Product product){
        String idTable = "";
        for (Table table : lstTable){
            if(table.getNameTable().equals(selectedItem)){
                idTable = table.getIdTable();
                break;
            }
        }
        for (Order order : lstOrder) {
            if(order.getIdTable().equals(idTable))
                if (order.getStatusOrdered().equals("Serving...")) {
                    DetailOrder detailOrder = new DetailOrder(createNewId("DO"), order.getIdOrder(), product.getIdProduct(), Integer.parseInt(bindingDialog.txtNumberOfDishes.getText().toString().trim()), "Not Done");

                    FirebaseDatabase.getInstance().getReference("DetailOrder").child(createNewId("DO")).setValue(detailOrder);
                    setTotalBillOrder(order.getIdOrder());
                    break;
                }
        }
    }

    private void handleAddProductToNewOrder(Product product) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(calendar.getTime());
        String idOrder = createNewId("Ord");
        Order order = new Order(idOrder, idAccount, getIdTableByNameTable(selectedItem), "Serving...", product.getPricesProduct() * Integer.parseInt(bindingDialog.txtNumberOfDishes.getText().toString()), dateTime);
        FirebaseDatabase.getInstance().getReference("Order").child(idOrder).setValue(order);


        DetailOrder detailOrder = new DetailOrder(createNewId("DO"), idOrder, product.getIdProduct(), Integer.parseInt(bindingDialog.txtNumberOfDishes.getText().toString().trim()), "Not Done");

        FirebaseDatabase.getInstance().getReference("DetailOrder").child(createNewId("DO")).setValue(detailOrder);
    }

    private String getIdTableByNameTable(String str) {
        for (Table table : lstTable) {
            if (table.getNameTable().equals(str)) {
                tableSelect = table;
                return table.getIdTable();
            }
        }
        return "";
    }

    private String createNewId(String firstId) {
        String id = "";
        int i = 0;
        boolean check = false;
        while (!check) {
            check = true;
            if (i < 9) id = firstId + "0" + (i + 1);
            else if (i >= 10) id = firstId + (i + 1);
            if (firstId.equals("Ord")) {
                for (Order order : listAllOrder) {
                    if (order.getIdOrder().toString().trim().equals(id)) {
                        check = false;
                        break;
                    }
                }
            } else {
                if (firstId.equals("DO")) {
                    for (DetailOrder detailOrder : lstDetailOrder) {
                        if (detailOrder.getIdDetailOrder().toString().trim().equals(id)) {
                            check = false;
                            break;
                        }
                    }
                }
            }
            i++;
        }
        return id;
    }

    private void getAllDetailOrder() {
        lstDetailOrder = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("DetailOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    DetailOrder detailOrder = childSnapshot.getValue(DetailOrder.class);
                    lstDetailOrder.add(detailOrder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean checkTableIsUse(Table table) {
        for (Order order : lstOrder) {
            if (order.getStatusOrdered().equals("Serving...") && order.getIdTable().equals(table.getIdTable())) {
                return true;
            }
        }
        return false;
    }

    private void handleCheckOrderExist() {
        account = mainActivity.accountSignIn;
        idAccount = account.getIdAcc();
        FirebaseDatabase.getInstance().getReference("Order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean CHECK = false;
                lstOrder = new ArrayList<>();
                listAllOrder = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Order order = childSnapshot.getValue(Order.class);
                    listAllOrder.add(order);
                    if (order.getStatusOrdered().equals("Serving...")) {
                        lstOrder.add(order);
                        if (order.getIdAcc().equals(idAccount)) {
                            CHECK = true;
                            idTable = order.getIdTable();
                            break;
                        }
                    }
                }
                if (CHECK && account.getIdRole() == 0) {
                    bindingDialog.btnAdd.setText("ADD TO YOUR ORDER");
                } else if (!CHECK && account.getIdRole() == 0){
                    bindingDialog.btnAdd.setText("ADD TO NEW ORDER");
                }
                else {
                    bindingDialog.btnAdd.setText("ADD TO TABLE");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (productItems != null) {
            return productItems.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemProductsBinding binding;

        public ViewHolder(ItemProductsBinding itembinding) {
            super(itembinding.getRoot());
            this.binding = itembinding;
        }
    }

}