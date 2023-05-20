package com.midterm.restaurant_app.viewmodel.modelView;

import com.midterm.restaurant_app.model.Account;
import com.midterm.restaurant_app.model.repository.AccountRepository;
import com.midterm.restaurant_app.model.repository.Repository;

public class AccountViewModel extends BaseViewModel<Account> {
    public AccountViewModel() {
        super(AccountRepository.getInstance());
    }
}
