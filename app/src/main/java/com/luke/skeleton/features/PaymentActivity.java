package com.luke.skeleton.features;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.luke.skeleton.R;
import com.luke.skeleton.app.Constants;
import com.luke.skeleton.base.activity.BaseViewModelActivity;
import com.luke.skeleton.base.adapter.GenericRecyclerAdapter;
import com.luke.skeleton.base.adapter.holder.ViewHolder;
import com.luke.skeleton.base.adapter.holder.ViewHolderCreator;
import com.luke.skeleton.base.viewmodel.IViewModel;
import com.luke.skeleton.databinding.ActivityPaymentBinding;
import com.luke.skeleton.features.adapter.BillViewHolder;
import com.luke.skeleton.features.viewmodel.PaymentViewModel;
import com.luke.skeleton.features.viewmodel.PaymentViewModel.IView;
import com.luke.skeleton.model.Bill;
import com.luke.skeleton.storage.BillDataProvider;

import java.util.List;

public class PaymentActivity extends BaseViewModelActivity implements IView {

    private static final int CHANGE_PAYMENT_REQUEST_CODE = 101;

    public static void launch(Context context) {
        Intent intent = new Intent(context, PaymentActivity.class);
        context.startActivity(intent);
    }

    private PaymentViewModel viewModel;
    private ActivityPaymentBinding binding;

    private GenericRecyclerAdapter<Bill> billsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        binding.setViewModel(viewModel);
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

        findViewById(R.id.btn_change_payment).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DropInRequest dropInRequest = new DropInRequest()
                        .clientToken(Constants.TEMP_BRAINTREE_TOKEN);
                startActivityForResult(dropInRequest.getIntent(PaymentActivity.this), 101);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_PAYMENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                viewModel.onPaymentMethodChanged(result.getPaymentMethodType());
                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }
    }
}
