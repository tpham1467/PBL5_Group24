package com.midterm.restaurant_app.viewmodel.modelView;
import com.midterm.restaurant_app.model.Order;
import com.midterm.restaurant_app.model.repository.OrderRepository;

public class OrderViewModel extends BaseViewModel<Order>{

    public OrderViewModel(){
        super(OrderRepository.getInstance());
    }
}
