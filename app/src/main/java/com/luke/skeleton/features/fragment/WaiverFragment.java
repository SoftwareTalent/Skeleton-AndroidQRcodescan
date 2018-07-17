package com.luke.skeleton.features.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luke.skeleton.R;
import com.luke.skeleton.utils.StringUtils;

public class WaiverFragment extends Fragment {

    private TextView waiverTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_waiver, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();

        waiverTextView = v.findViewById(R.id.txt_waiver);
        waiverTextView.setText(StringUtils.readRawTextFile(getContext(), R.raw.waiver));
    }

}
