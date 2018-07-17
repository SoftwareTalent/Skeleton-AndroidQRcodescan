package com.luke.skeleton.features.viewmodel;

import android.databinding.Bindable;

import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.luke.skeleton.BR;
import com.luke.skeleton.base.viewmodel.BaseViewModel;
import com.luke.skeleton.model.Bill;
import com.luke.skeleton.storage.BillDataProvider;

import java.util.List;

import io.reactivex.functions.Consumer;

public class PaymentViewModel extends BaseViewModel {

    public interface IView extends IBaseView {
        void updateBillHistory(List<Bill> items);
    }

    private IView view;
    private BillDataProvider billDataProvider;

    private PaymentMethodType paymentMethodType;

    public PaymentViewModel(IView view, BillDataProvider billDataProvider) {
        this.view = view;
        this.billDataProvider = billDataProvider;
    }

    @Override
    public void onStart() {
        super.onStart();
        addDisposable(billDataProvider.getBills().subscribe(new Consumer<List<Bill>>() {

            @Override
            public void accept(List<Bill> bills) throws Exception {
                view.updateBillHistory(bills);
            }
        }));
        addDisposable(billDataProvider.getPaymentMethod().subscribe(new Consumer<PaymentMethodType>() {

            @Override
            public void accept(PaymentMethodType type) throws Exception {
                paymentMethodType = type;
                notifyPropertyChanged(BR.paymentMethod);
            }
        }));
    }

    @Bindable
    public String getPaymentMethod() {
        return paymentMethodType != null ? paymentMethodType.getCanonicalName() : "";
    }

    public void onPaymentMethodChanged(PaymentMethodType paymentMethod) {
        billDataProvider.setPaymentMethod(paymentMethod);
    }

}
