package com.luke.skeleton.base.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;

import com.luke.skeleton.BR;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseViewModel extends BaseObservable
        implements ILoadingViewModel {

    protected CompositeDisposable disposables;
    protected Bundle savedInstanceState;

    private boolean loadingIndicatorEnabled;
    private int loadingVisibility = View.GONE;

    private IBaseView view;

    @Override
    public void init() {}


    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        if (disposables != null) {
            disposables.dispose();
            disposables = null;
        }
    }

    @Override
    public void onRestart() {}

    @Override
    public void onSaveInstanceState(Bundle outState) {}

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
    }

    @Override
    public void onPermissionsGranted(int requestCode, String[] permissions, int[] grantResults) {}

    @Bindable
    @Override
    public boolean isLoadingIndicatorEnabled() {
        return loadingIndicatorEnabled;
    }

    @Bindable
    @Override
    public int getLoadingVisibility(){
        return loadingVisibility;
    }

    public void showLoading() {
        loadingIndicatorEnabled = true;
        loadingVisibility = View.VISIBLE;
        notifyPropertyChanged(BR.loadingVisibility);
        notifyPropertyChanged(BR.loadingIndicatorEnabled);
    }

    public void hideLoading() {
        loadingIndicatorEnabled = false;
        loadingVisibility = View.GONE;
        notifyPropertyChanged(BR.loadingVisibility);
        notifyPropertyChanged(BR.loadingIndicatorEnabled);
    }

    public void addDisposable(Disposable d){
        if (disposables == null) {
            disposables = new CompositeDisposable();
        }
        disposables.add(d);
    }

}

