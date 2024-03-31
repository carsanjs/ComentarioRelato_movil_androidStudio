package com.example.comentariorelato_movil.ui.camara;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CamaraViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CamaraViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
