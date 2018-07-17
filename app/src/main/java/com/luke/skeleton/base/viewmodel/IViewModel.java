package com.luke.skeleton.base.viewmodel;

import android.databinding.Observable;
import android.os.Bundle;

public interface IViewModel extends Observable {

    void init();
    void onStart();
    void onStop();
    void onRestart();
    void onSaveInstanceState(Bundle outState);
    void onRestoreInstanceState(Bundle savedInstanceState);
    void onPermissionsGranted(int requestCode, String permissions[], int[] grantResults);

    interface IBaseView {
        boolean checkForPermissions(String[] permissions);
        void requestRuntimePermissions(int requestCode, String permissions[]);
        void showMessage(String msg);
        void showMessage(int msgRes);
        void finishScreen();
    }
}
