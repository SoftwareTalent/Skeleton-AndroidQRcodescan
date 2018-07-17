package com.luke.skeleton.base.adapter.holder;

import android.view.View;

public interface ViewHolder<T> {
    int getViewLayoutId();
    void bindView(View v);
    void populateView(T item);
    android.databinding.ViewDataBinding getBinding();
}
