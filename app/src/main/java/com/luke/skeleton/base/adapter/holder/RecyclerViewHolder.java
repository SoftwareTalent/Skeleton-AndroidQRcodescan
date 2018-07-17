package com.luke.skeleton.base.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    private ViewHolder<T> viewHolder;

    public RecyclerViewHolder(ViewHolder<T> viewHolder, View view) {
        super(view);
        this.viewHolder = viewHolder;
    }

    public ViewHolder<T> getViewHolder() {
        return viewHolder;
    }

}
