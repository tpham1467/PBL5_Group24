package com.midterm.restaurant_app.viewmodel.modelView;

import com.midterm.restaurant_app.model.Product;
import com.midterm.restaurant_app.model.repository.ProductRepository;

public class ProductViewModel extends BaseViewModel<Product>{

    public ProductViewModel(){
        super(ProductRepository.getInstance());
    }
}
