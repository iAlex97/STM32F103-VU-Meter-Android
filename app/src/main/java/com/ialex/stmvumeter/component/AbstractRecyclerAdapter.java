package com.ialex.stmvumeter.component;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public abstract class AbstractRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected ArrayList<T> items;
    protected Context context;
    protected LayoutInflater inflater;

    public AbstractRecyclerAdapter(Context context, ArrayList<T> initial) {
        this.context = context;
        this.items = initial;
        this.inflater = LayoutInflater.from(context);
    }

    public AbstractRecyclerAdapter(Context context) {
        this(context, new ArrayList<>());
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public void setItems(ArrayList<T> newData) {
        final DiffUtil.Callback diffCallback = getDiffCallback(newData);

        if (diffCallback == null) {
            this.items.clear();
            this.items.addAll(newData);
            this.notifyDataSetChanged();
        } else {
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

            this.items.clear();
            this.items.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }
    }

    protected abstract DiffUtil.Callback getDiffCallback(ArrayList<T> newData);
}
