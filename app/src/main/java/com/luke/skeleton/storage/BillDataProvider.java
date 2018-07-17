package com.luke.skeleton.storage;

import com.luke.skeleton.base.storage.ObservableListLocalStorage;
import com.luke.skeleton.base.storage.ObservableListLocalStorage.IDProvider;
import com.luke.skeleton.model.Bill;
import com.luke.skeleton.utils.DateUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class BillDataProvider {

    private static final String KEY_BILLS = "KEY_BILLS";

    private static BillDataProvider instance;

    public static BillDataProvider getInstance() {
        if (instance == null) {
            instance = new BillDataProvider();
        }
        return instance;
    }

    private ObservableListLocalStorage<Bill> billStorage = new ObservableListLocalStorage<>(KEY_BILLS,
            new IDProvider<Bill>() {

                @Override
                public String getId(Bill item) {
                    return DateUtils.getIsoDateTimeFormatter().print(item.getDate());
                }
            });

    private BillDataProvider() {}

    public Observable<List<Bill>> getBills() {
        return billStorage.getData().observeOn(AndroidSchedulers.mainThread());
    }

    public void addBill(Bill bill) {
        billStorage.add(bill);
    }

}
