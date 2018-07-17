package com.luke.skeleton.base.storage;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.orhanobut.hawk.Hawk;

import io.reactivex.Observable;

public class ObservableLocalStorage<T> {

    private String key;

    private final BehaviorRelay<T> subject;

    public ObservableLocalStorage(String key) {
        this.key = key;
        T data = getFromStorage();

        if(data != null) {
            subject = BehaviorRelay.createDefault(data);
        } else {
            subject = BehaviorRelay.create();
        }
    }

    public T getDataSync() {
        T data = getFromStorage();
        return data;
    }

    public Observable<T> getData() {
        return subject;
    }

    public void setData(T data) {
        saveToStorage(data);
        if(data != null) {
            subject.accept(data);
        }
    }

    public void clear() {
        setData(null);
    }

    private T getFromStorage() {
        return Hawk.get(key);
    }

    private void saveToStorage(T data) {
        Hawk.put(key, data);
    }

}
