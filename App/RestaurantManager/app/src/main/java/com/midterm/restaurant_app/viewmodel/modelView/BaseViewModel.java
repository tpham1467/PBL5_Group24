package com.midterm.restaurant_app.viewmodel.modelView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.midterm.restaurant_app.model.repository.Repository;

import java.util.List;

public class BaseViewModel<T> extends ViewModel {
    protected Repository<T> repository;
    protected MutableLiveData<List<T>> items = new MutableLiveData<>();
    protected MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    protected MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public BaseViewModel(Repository<T> repository) {
        this.repository = repository;
    }

    public LiveData<List<T>> getItems() {
        return items;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void insert(T item) {
        isLoading.setValue(true);
        repository.insert(item);
    }

    public void update(String id, T item) {
        isLoading.setValue(true);
        repository.update(id, item);
    }

    public void delete(String id) {
        isLoading.setValue(true);
        repository.delete(id);
    }

    public LiveData<List<T>> getAll() {
        isLoading.setValue(true);
        return repository.getAll();
    }

    public LiveData<T> getById(String id) {
        isLoading.setValue(true);
        return repository.getById(id);
    }

    public void deleteAll() {
        isLoading.setValue(true);
        repository.deleteAll();
    }

    protected void handleException(Exception e) {
        isLoading.setValue(false);
        errorMessage.setValue(e.getMessage());
    }
}
