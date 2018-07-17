package com.luke.skeleton.features.adapter;

import android.databinding.ViewDataBinding;
import android.view.View;
import android.widget.TextView;

import com.luke.skeleton.R;
import com.luke.skeleton.base.adapter.holder.ViewHolder;
import com.luke.skeleton.model.Bill;
import com.luke.skeleton.utils.DateUtils;
import com.luke.skeleton.utils.ResourcesUtils;

public class BillViewHolder implements ViewHolder<Bill> {

    private TextView billTextView;

    @Override
    public int getViewLayoutId() {
        return R.layout.item_bill;
    }

    @Override
    public void bindView(View v) {
        billTextView = (TextView) v;
    }

    @Override
    public void populateView(Bill item) {
        billTextView.setText(ResourcesUtils.getString(R.string.item_bill,
                item.getValue(),
                DateUtils.getDateTimeFormatter().print(item.getDate())));
    }

    @Override
    public ViewDataBinding getBinding() {
        return null;
    }
}
