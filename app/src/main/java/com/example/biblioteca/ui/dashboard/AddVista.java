package com.example.biblioteca.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddVista extends ViewModel {
    private final MutableLiveData<String> mText;

    public AddVista() {
        mText = new MutableLiveData<>();
        mText.setValue("Agregar Libros");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
