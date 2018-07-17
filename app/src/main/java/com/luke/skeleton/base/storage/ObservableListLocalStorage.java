package com.luke.skeleton.base.storage;

import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class ObservableListLocalStorage<T> extends ObservableLocalStorage<List<T>> {

    public interface IDProvider<T> {
        String getId(T item);
    }

    private IDProvider<T> idProvider;
    private Map<String, BehaviorRelay<T>> itemsSubjectMap;

    public ObservableListLocalStorage(String key, IDProvider<T> idProvider) {
        super(key);
        this.idProvider = idProvider;
        itemsSubjectMap = new HashMap<>();
    }

    @Override
    public List<T> getDataSync() {
        List<T> data = super.getDataSync();
        return data != null ? data : new ArrayList<T>();
    }

    @Override
    public void setData(List<T> data) {
        clear();
        addAll(data);
    }

    public void add(T item) {
        Map<String, T> data = getDataMap();
        data.remove(idProvider.getId(item));
        data.put(idProvider.getId(item), item);

        setData(data);
    }

    public void addAll(Collection<T> items) {
        Map<String, T> data = getDataMap();

        if(items != null) {
            for (T item : items) {
                data.remove(idProvider.getId(item));
                data.put(idProvider.getId(item), item);
            }
        }

        setData(data);
    }

    public Observable<T> getItem(String id) {
        BehaviorRelay<T> subject = itemsSubjectMap.get(id);
        if(subject != null) {
            return subject;
        }

        T item = getDataMap().get(id);
        if(item != null) {
            subject = BehaviorRelay.createDefault(item);
        } else {
            subject = BehaviorRelay.create();
        }

        itemsSubjectMap.put(id, subject);

        return subject;
    }

    public T getItemSync(String id) {
        return getDataMap().get(id);
    }

    public void set(T item) {
        Map<String, T> data = getDataMap();
        data.put(idProvider.getId(item), item);

        setData(data);
        notifyItemChanged(item, false);
    }

    public void remove(T item) {
        Map<String, T> data = getDataMap();
        data.remove(idProvider.getId(item));

        setData(data);
        notifyItemChanged(item, true);
    }

    public void removeAll(Collection<T> items) {
        Map<String, T> data = getDataMap();
        for (T item : items) {
            data.remove(idProvider.getId(item));
            notifyItemChanged(item, true);
        }

        setData(data);
    }

    public void clear() {
        super.setData(new ArrayList<T>());
    }

    private void setData(Map<String, T> map) {
        super.setData(new ArrayList<T>(map.values()));
    }

    private LinkedHashMap<String, T> getDataMap() {
        List<T> items = getDataSync();
        final LinkedHashMap<String, T> map = new LinkedHashMap<>();
        for (T item : items) {
            map.put(idProvider.getId(item), item);
        }

        return map;
    }

    private void notifyItemChanged(T item, boolean remove) {
        if(remove) {
            itemsSubjectMap.remove(idProvider.getId(item));
            return;
        }

        BehaviorRelay<T> subject = itemsSubjectMap.get(idProvider.getId(item));
        if(subject != null) {
            subject.accept(item);
        }
    }

}
