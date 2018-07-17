package com.luke.skeleton.base.adapter;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luke.skeleton.base.adapter.holder.RecyclerViewHolder;
import com.luke.skeleton.base.adapter.holder.ViewHolder;
import com.luke.skeleton.base.adapter.holder.ViewHolderCreator;

import java.util.List;

public class GenericRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder<T>> {

    public interface OnItemClickListener {
        void onItemClick(RecyclerViewHolder holder);
    }

    protected LayoutInflater inflater;
    private ViewHolderCreator<T> viewHolderCreator;
    private OnItemClickListener onItemClickListener;
    private List<T> items;
    private Context context;

    public GenericRecyclerAdapter(Context context, ViewHolderCreator<T> viewHolderCreator,
                                  List<T> items, OnItemClickListener onItemClickListener) {
        this.items = items;
        this.viewHolderCreator = viewHolderCreator;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return context;
    }

    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public List<T> getItems() {
        return items;
    }

    public void removeItem(T item) {
        int pos = getPosition(item);

        items.remove(pos);
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public int getPosition(T item) {
        return items.indexOf(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (items != null) ? items.size() : 0;
    }

    @Override
    public RecyclerViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder<T> holder = viewHolderCreator.createViewHolder(viewType);
        View view = inflater.inflate(holder.getViewLayoutId(), parent, false);
        holder.bindView(view);

        final RecyclerViewHolder<T> recyclerViewHolder =  new RecyclerViewHolder<>(holder, view);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(recyclerViewHolder);
                }
            }
        });

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder<T> holder, int position) {
        populateView(holder, position);
        if(holder.getViewHolder() instanceof BaseObservable) {
            ((BaseObservable)holder.getViewHolder()).notifyChange();
        }
        if(holder.getViewHolder().getBinding() != null) {
            holder.getViewHolder().getBinding().executePendingBindings();
        }
    }

    /**
     * Fills view with data. Standard behavior can be override by inheritors
     * @param holder
     * @param position
     */
    protected void populateView(RecyclerViewHolder<T> holder, int position) {
        holder.getViewHolder().populateView(getItem(position));
    }

}
