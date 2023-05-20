package com.midterm.restaurant_app.viewmodel.modelView;

import com.midterm.restaurant_app.model.Table;
import com.midterm.restaurant_app.model.repository.TableRepository;

public class TableViewModel extends BaseViewModel<Table>{
    public TableViewModel(){
        super(TableRepository.getInstance());
    }
}
