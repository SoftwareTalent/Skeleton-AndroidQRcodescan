package com.luke.skeleton.features;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.luke.skeleton.R;
import com.luke.skeleton.app.Constants;
import com.luke.skeleton.base.activity.BaseActivity;
import com.luke.skeleton.utils.DateUtils;
import com.orhanobut.hawk.Hawk;

import org.joda.time.DateTime;

public class WaiverActivity extends BaseActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, WaiverActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiver);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DateTime acceptedDate = Hawk.get(Constants.KEY_WAIVER_ACCEPTED_DATE);
        TextView acceptedTextView = findViewById(R.id.txt_accepted);
        acceptedTextView.setText(getString(R.string.accepted_waiver_label,
                DateUtils.getShortDateTimeFormatter().print(acceptedDate)));
    }
}
