package com.midterm.restaurant_app.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.midterm.restaurant_app.MainActivity;
import com.midterm.restaurant_app.databinding.FragmentHomeBinding;
import com.midterm.restaurant_app.model.Product;
import com.midterm.restaurant_app.viewmodel.adapter.itemsProductAdapter;
import com.midterm.restaurant_app.viewmodel.modelView.ProductViewModel;

import java.util.ArrayList;
import java.util.List;
public class HomeFragment extends Fragment {
    private RecyclerView recyclerPopular;
    private RecyclerView recyclerFoods;
    private itemsProductAdapter itemsAdapter;
    private itemsProductAdapter itemsAdapterPopular;
    private FragmentHomeBinding bindingHome;
    List<Product> lstProduct;

    private MainActivity mainActivity;
    private String Gmail;

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

        recyclerPopular = bindingHome.rvPopular;
        recyclerFoods = bindingHome.rvFoods;

        mainActivity = new MainActivity();


        Gmail = mainActivity.GMAIL;

        itemsAdapter = new itemsProductAdapter(view.getContext());
        itemsAdapterPopular = new itemsProductAdapter(view.getContext());

        lstProduct = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),recyclerPopular.HORIZONTAL,false);
        recyclerPopular.setLayoutManager(linearLayoutManager);
        ProductViewModel productViewModel = new ProductViewModel();
        productViewModel.getAll().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                List<Product> lstPro = new ArrayList<>();
                for (Product product : products){
                    if(product.getRateProduct() >=4){
                        lstPro.add(product);
                        itemsAdapterPopular.setData(lstPro);
                        recyclerPopular.setAdapter(itemsAdapterPopular);
                    }
                }
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3); // số 2 ở đây là số cột hiển thị
        recyclerFoods.setLayoutManager(gridLayoutManager);
        productViewModel.getAll().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                itemsAdapter.setData(products);
                recyclerFoods.setAdapter(itemsAdapter);
            }
        });

        bindingHome.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().toLowerCase().trim();
                filterProductListByName(searchText);
            }

            private List<Product> lstFilter;

            private void filterProductListByName(String searchText) {
                productViewModel.getAll().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        lstFilter = new ArrayList<>();
                        for (Product product : products) {
                            if (product.getNameProduct().toLowerCase().contains(searchText)) {
                                if(product.getRateProduct() >=4){
                                    lstFilter.add(product);
                                    itemsAdapterPopular.setData(lstFilter);
                                    recyclerPopular.setAdapter(itemsAdapterPopular);
                                }
                                else{
                                    lstFilter.add(product);
                                    itemsAdapter.setData(lstFilter);
                                    recyclerFoods.setAdapter(itemsAdapter);
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindingHome = FragmentHomeBinding.inflate(inflater, container, false);
        return bindingHome.getRoot();
    }
}