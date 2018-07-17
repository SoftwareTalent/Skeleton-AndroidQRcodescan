package com.luke.skeleton.base.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.luke.skeleton.base.viewmodel.IViewModel;
import com.luke.skeleton.base.viewmodel.IViewModel.IBaseView;

public abstract class BaseViewModelActivity extends BaseActivity implements IBaseView {

    private IViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = createViewModel();

        if(savedInstanceState != null) {
            viewModel.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.onStart();
    }

    @Override
    protected void onStop() {
        viewModel.onStop();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        viewModel.onRestart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        viewModel.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        viewModel.onPermissionsGranted(requestCode, permissions, grantResults);
    }

    @Override
    public void finishScreen() {
        finish();
    }

    protected abstract IViewModel createViewModel();

}
