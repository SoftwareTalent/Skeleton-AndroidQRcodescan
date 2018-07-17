package com.luke.skeleton.base.adapter.holder;

public interface ViewHolderCreator<T> {
    ViewHolder<T> createViewHolder(int viewType);
}
