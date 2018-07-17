package com.luke.skeleton.features;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.luke.skeleton.R;
import com.luke.skeleton.base.activity.BaseViewModelActivity;
import com.luke.skeleton.base.adapter.GenericRecyclerAdapter;
import com.luke.skeleton.base.adapter.holder.ViewHolder;
import com.luke.skeleton.base.adapter.holder.ViewHolderCreator;
import com.luke.skeleton.base.viewmodel.IViewModel;
import com.luke.skeleton.features.adapter.BillViewHolder;
import com.luke.skeleton.features.viewmodel.PaymentViewModel;
import com.luke.skeleton.features.viewmodel.PaymentViewModel.IView;
import com.luke.skeleton.model.Bill;
import com.luke.skeleton.storage.BillDataProvider;

import java.util.List;

public class PaymentActivity extends BaseViewModelActivity implements IView {

    public static void launch(Context context) {
        Intent intent = new Intent(context, PaymentActivity.class);
        context.startActivity(intent);
    }

    private PaymentViewModel viewModel;

    private GenericRecyclerAdapter<Bill> billsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        billsAdapter = new GenericRecyclerAdapter<>(this, new ViewHolderCreator<Bill>() {

            @Override
            public ViewHolder<Bill> createViewHolder(int viewType) {
                return new BillViewHolder();
            }
        }, null, null);

        RecyclerView billingHistoryRecyclerView = findViewById(R.id.recycler_billing_history);
        billingHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        billingHistoryRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        billingHistoryRecyclerView.setAdapter(billsAdapter);
    }

    @Override
    protected IViewModel createViewModel() {
        viewModel = new PaymentViewModel(this, BillDataProvider.getInstance());
        return viewModel;
    }

    @Override
    public void updateBillHistory(List<Bill> items) {
        billsAdapter.setItems(items);
    }

}
