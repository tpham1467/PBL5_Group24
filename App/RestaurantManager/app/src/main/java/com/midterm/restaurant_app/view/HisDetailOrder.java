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

import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.databinding.FragmentDetailHisOrderBinding;
import com.midterm.restaurant_app.model.DetailOrder;
import com.midterm.restaurant_app.model.Order;
import com.midterm.restaurant_app.model.Product;
import com.midterm.restaurant_app.viewmodel.adapter.ProductOrderAdapter;
import com.midterm.restaurant_app.viewmodel.modelView.DetailOrderViewModel;
import com.midterm.restaurant_app.viewmodel.modelView.OrderViewModel;
import com.midterm.restaurant_app.viewmodel.modelView.ProductViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HisDetailOrder extends Fragment {

    private RecyclerView recyclerListFoods;
    private ProductOrderAdapter productOrderAdapter;
    private FragmentDetailHisOrderBinding binding;
    private DetailOrderViewModel detailOrderViewModel;
    private List<DetailOrder> lstDetailOrder;
    private HashMap<String, Product> hashMapProduct;
    private ProductViewModel productViewModel;
    List<String> getData;
    private String idOrder;
    private int status;
    private String nameUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        getData = new ArrayList<>();
        nameUser = "";
        if (bundle != null) {
            getData = Arrays.asList(bundle.getString("idOrder").toString().split(" "));
            for (int i = 0; i< getData.size();i++){
                if(i == 0){
                    idOrder = getData.get(i);
                }
                else if(i == 1){
                    status = Integer.parseInt(getData.get(i));
                }
                else {
                    nameUser += getData.get(i)+ " ";
                }
            }
        }
        nameUser = nameUser.trim();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailHisOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerListFoods = view.findViewById(R.id.rv_his_detail_ordered);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),recyclerListFoods.VERTICAL,false);
        recyclerListFoods.setLayoutManager(linearLayoutManager);

        detailOrderViewModel = new DetailOrderViewModel();
        productViewModel = new ProductViewModel();
        detailOrderViewModel.getAll().observe(getViewLifecycleOwner(), new Observer<List<DetailOrder>>() {
            @Override
            public void onChanged(List<DetailOrder> detailOrders) {
                lstDetailOrder =new ArrayList<>();
                hashMapProduct = new HashMap<>();
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
                                        productOrderAdapter = new ProductOrderAdapter(view.getContext(),hashMapProduct,status,order);
                                        productOrderAdapter.setData(lstDetailOrder);
                                        recyclerListFoods.setAdapter(productOrderAdapter);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        binding.tvNameOrdered.setText(nameUser);
    }
}
