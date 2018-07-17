package com.luke.skeleton.features.viewmodel;

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
    }

}
