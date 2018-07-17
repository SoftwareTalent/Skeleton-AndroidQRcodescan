package com.luke.skeleton.features;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;

import com.luke.skeleton.R;
import com.luke.skeleton.app.Constants;
import com.luke.skeleton.base.activity.BaseActivity;
import com.orhanobut.hawk.Hawk;

import org.joda.time.DateTime;

public class AcceptWaiverActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Hawk.contains(Constants.KEY_WAIVER_ACCEPTED_DATE)) {
            launchHomeScreen();
            return;
        }
        setContentView(R.layout.activity_accept_waiver);

        findViewById(R.id.btn_accept).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DateTime timeNow = DateTime.now();
                Hawk.put(Constants.KEY_WAIVER_ACCEPTED_DATE, timeNow);
                launchHomeScreen();
            }
        });
    }

    private void launchHomeScreen() {
        HomeActivity.launch(AcceptWaiverActivity.this);
        finish();
    }

}
