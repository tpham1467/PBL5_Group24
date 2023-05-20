package com.midterm.restaurant_app.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.midterm.restaurant_app.MainActivity;
import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.databinding.FragmentDetailServeBinding;
import com.midterm.restaurant_app.model.Account;
import com.midterm.restaurant_app.model.DetailOrder;
import com.midterm.restaurant_app.model.Order;
import com.midterm.restaurant_app.model.Product;
import com.midterm.restaurant_app.model.Table;
import com.midterm.restaurant_app.viewmodel.adapter.ProductOrderAdapter;
import com.midterm.restaurant_app.viewmodel.modelView.DetailOrderViewModel;
import com.midterm.restaurant_app.viewmodel.modelView.OrderViewModel;
import com.midterm.restaurant_app.viewmodel.modelView.ProductViewModel;
import com.midterm.restaurant_app.viewmodel.modelView.TableViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailsOrderFragment extends Fragment {

    private RecyclerView recyclerListFoods;
    private static ProductOrderAdapter productsOrAdapter;
    private DetailOrderViewModel detailOrderViewModel;
    private List<DetailOrder> lstDetailOrder;
    private HashMap<String, Product> hashMapProduct;
    private ProductViewModel productViewModel;
    private TableViewModel tableViewModel;

    private FragmentDetailServeBinding bindingDetailServe;
    private String idOrder;
    private int status;
    private String idTable;
    private Table table;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            idOrder = bundle.getString("idOrder").toString().split(" ")[0];
            status = Integer.parseInt(bundle.getString("idOrder").toString().split(" ")[1]);
            idTable = bundle.getString("idOrder").toString().split(" ")[2];
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bindingDetailServe = FragmentDetailServeBinding.inflate(inflater, container, false);
        return bindingDetailServe.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerListFoods = view.findViewById(R.id.rv_detailtable);
        productViewModel = new ProductViewModel();
        hashMapProduct = new HashMap<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),recyclerListFoods.VERTICAL,false);
        recyclerListFoods.setLayoutManager(linearLayoutManager);

        detailOrderViewModel = new DetailOrderViewModel();
        detailOrderViewModel.getAll().observe(getViewLifecycleOwner(), new Observer<List<DetailOrder>>() {
            @Override
            public void onChanged(List<DetailOrder> detailOrders) {
                lstDetailOrder =new ArrayList<>();
                for (DetailOrder detailOrder : detailOrders){
                    productViewModel.getById(detailOrder.getIdProduct()).observe(getViewLifecycleOwner(), new Observer<Product>() {
                        @Override
                        public void onChanged(Product product) {
                            if(idOrder.equals(detailOrder.getIdOrder())){
                                hashMapProduct.put(detailOrder.getIdProduct(),product);
                                lstDetailOrder.add(detailOrder);
                                OrderViewModel orderViewModel = new OrderViewModel();
                                orderViewModel.getById(idOrder).observe(getViewLifecycleOwner(), new Observer<Order>() {
                                    @Override
                                    public void onChanged(Order order) {
                                        productsOrAdapter = new ProductOrderAdapter(view.getContext(),hashMapProduct,status,order);
                                        productsOrAdapter.setData(lstDetailOrder);
                                        recyclerListFoods.setAdapter(productsOrAdapter);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        MainActivity mainActivity = new MainActivity();
        Account account = mainActivity.accountSignIn;
        ConstraintLayout constraintLayout = bindingDetailServe.constraintDetailOrder;
        if(account.getIdRole()==0){
            constraintLayout.removeView(bindingDetailServe.btnTranfer);
        }

        tableViewModel = new TableViewModel();
        tableViewModel.getById(idTable).observe(getViewLifecycleOwner(), new Observer<Table>() {
            @Override
            public void onChanged(Table table) {
                if(table.getIs_tranfer_foods()==0)
                {
                    bindingDetailServe.btnTranfer.setText("YÊU CẦU VẬN CHUYỂN");
                }
                else if(table.getIs_tranfer_foods()==1)
                {
                    bindingDetailServe.btnTranfer.setText("VẬN CHUYỂN");
                }
                else if(table.getIs_tranfer_foods()==2)
                {
                    bindingDetailServe.btnTranfer.setText("ĐANG VẬN CHUYỂN");
                }
            }
        });


        bindingDetailServe.btnTranfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(bindingDetailServe.btnTranfer.getText().equals("YÊU CẦU VẬN CHUYỂN")){
                        bindingDetailServe.btnTranfer.setText("VẬN CHUYỂN");
                        FirebaseDatabase.getInstance().getReference("Table").child(idTable).child("is_tranfer_foods").setValue(1);
                    }
                    else if(bindingDetailServe.btnTranfer.getText().equals("VẬN CHUYỂN")){
                        bindingDetailServe.btnTranfer.setText("ĐANG VẬN CHUYỂN");
                        FirebaseDatabase.getInstance().getReference("Table").child(idTable).child("is_tranfer_foods").setValue(2);
                    }

            }
        });
    }
}