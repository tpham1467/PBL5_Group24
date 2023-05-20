package com.midterm.restaurant_app.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.restaurant_app.MainActivity;
import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.databinding.FragmentHisOrderBinding;
import com.midterm.restaurant_app.model.Account;
import com.midterm.restaurant_app.model.DetailOrder;
import com.midterm.restaurant_app.model.Order;
import com.midterm.restaurant_app.viewmodel.adapter.HisOrderAdapter;
import com.midterm.restaurant_app.viewmodel.modelView.AccountViewModel;
import com.midterm.restaurant_app.viewmodel.modelView.DetailOrderViewModel;
import com.midterm.restaurant_app.viewmodel.modelView.OrderViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HisOrderFragment extends Fragment {
    private RecyclerView recyclerHisOrder;
    private HisOrderAdapter hisOrderAdapter;
    private OrderViewModel orderViewModel;
    private AccountViewModel accountViewModel;
    private HashMap<String, Account> hashMapAccount;
    private List<Order> lstOrders;
    private FragmentHisOrderBinding binding;
    private DetailOrderViewModel detailOrderViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHisOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerHisOrder = view.findViewById(R.id.rv_allHisOrderedTable);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), recyclerHisOrder.VERTICAL, false);
        recyclerHisOrder.setLayoutManager(linearLayoutManager);

        orderViewModel = new OrderViewModel();
        accountViewModel = new AccountViewModel();
        MainActivity mainActivity = new MainActivity();
        Account account = mainActivity.accountSignIn;

        hashMapAccount = new HashMap<>();

        orderViewModel.getAll().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                lstOrders = new ArrayList<>();
                for (Order order : orders) {
                    if (order.getStatusOrdered().equals("Complete")) {
                        accountViewModel.getById(order.getIdAcc()).observe(getViewLifecycleOwner(), new Observer<Account>() {
                            @Override
                            public void onChanged(Account t_account) {
                                hashMapAccount.put(order.getIdAcc(), t_account);
                                hisOrderAdapter = new HisOrderAdapter(view.getContext(), hashMapAccount);
                                if (account.getIdRole() == 1) {
                                    binding.tvTitle.setText("His Ordered");
                                    setAdapterDb();
                                    recyclerHisOrder.setAdapter(hisOrderAdapter);
                                } else {
                                    binding.tvTitle.setText("Your Ordered");
                                    if (order.getIdAcc().equals(account.getIdAcc()) && order.getStatusOrdered().equals("Complete")) {
                                        setAdapterDb();
                                        recyclerHisOrder.setAdapter(hisOrderAdapter);
                                    }
                                }

                            }

                            private void setAdapterDb() {
                                lstOrders.add(order);
                                hisOrderAdapter.setData(lstOrders);
                                hisOrderAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        });

        List<DetailOrder> lstDetailOrder = new ArrayList<>();

        detailOrderViewModel = new DetailOrderViewModel();
        detailOrderViewModel.getAll().observe(getViewLifecycleOwner(), new Observer<List<DetailOrder>>() {
            @Override
            public void onChanged(List<DetailOrder> detailOrders) {
                for (DetailOrder detailOrder : detailOrders){
                    lstDetailOrder.add(detailOrder);
                }
            }
        });

        binding.imvDelAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn có chắc chắn muốn xoá dữ liệu này không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Order order : lstOrders) {
                            for (DetailOrder detailOrder : lstDetailOrder){
                                if(detailOrder.getIdOrder().equals(order.getIdOrder())){
                                    if (account.getIdRole() == 1) {
                                        detailOrderViewModel.delete(detailOrder.getIdDetailOrder());
                                        orderViewModel.delete(order.getIdOrder());
                                    } else {
                                        if(order.getIdAcc().equals(account.getIdAcc())){
                                            detailOrderViewModel.delete(detailOrder.getIdDetailOrder());
                                            orderViewModel.delete(order.getIdOrder());
                                        }
                                    }
                                }
                            }
                        }
                        hisOrderAdapter.setData(lstOrders);
                        hisOrderAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Không thực hiện hành động xoá dữ liệu
                    }
                });
                builder.show();
            }
        });
    }
}