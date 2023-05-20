package com.midterm.restaurant_app.viewmodel.modelView;

import com.midterm.restaurant_app.model.DetailOrder;
import com.midterm.restaurant_app.model.repository.DetailOrderRepository;
import com.midterm.restaurant_app.model.repository.Repository;

public class DetailOrderViewModel extends BaseViewModel<DetailOrder> {
    public DetailOrderViewModel() {
        super(DetailOrderRepository.getInstance());
    }
}
