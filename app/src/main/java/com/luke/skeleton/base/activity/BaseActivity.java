package com.luke.skeleton.base.activity;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (!handleCustomBackPressedAction()) {
            if(!isFinishing())
                super.onBackPressed();
        }
    }

    /**
     * This is equivalent to calling {@link #showFragment(int, Fragment, boolean)}
     * with false set for the addToBackStack parameter.
     *
     * @param contentFrame - container res id to show fragment in
     * @param fragment     - fragment to show
     */
    public void showFragment(int contentFrame, Fragment fragment) {
        showFragment(contentFrame, fragment, false);
    }

    /**
     * Shows fragment in specific contentFrame
     *
     * @param contentFrame   - container res id to show fragment in
     * @param fragment       - fragment to show
     * @param addToBackStack - whether this fragment should be added to back stack or not
     */
    public void showFragment(int contentFrame, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getCanonicalName());
        }
        transaction.replace(contentFrame, fragment);
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * Pops back stack of fragments to navigate back to required fragment
     * @param popFragmentClass - fragment navigate to
     * @param inclusive - should we delete or keep that fragment
     */
    public void popBackStack(Class popFragmentClass, boolean inclusive) {
        getSupportFragmentManager().popBackStack(popFragmentClass != null
                ? popFragmentClass.getCanonicalName() : null,
                inclusive ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0);
    }

    public boolean checkForPermissions(String[] permissions) {
        for(String permission : permissions) {
            boolean isGranted =  checkForPermission(permission);
            if(!isGranted) {
                return false;
            }
        }

        return true;
    }

    public boolean checkForPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestRuntimePermissions(int requestCode, String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void showMessage(int msgRes) {
        Toast.makeText(this, msgRes, Toast.LENGTH_LONG).show();
    }

    /**
     * Method to give child a chance to intercept back btn pressed event
     * to implement it's own logic
     * @return whether back btn pressed event is consumed or not
     */
    protected boolean handleCustomBackPressedAction() {
        return false;
    }

}
