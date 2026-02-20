package com.nibm.souschef.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nibm.souschef.R;
import com.nibm.souschef.model.RecipeData;

import java.util.ArrayList;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onClick(int position);
    }

    ArrayList<RecipeData> list;
    OnItemClickListener listener;

    public RecipeAdapter(ArrayList<RecipeData> list,
                         OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtTitle;

        public ViewHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.txtTitle);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(
                        parent.getContext())
                .inflate(R.layout.item_recipe,
                        parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            ViewHolder holder,
            int position) {

        holder.txtTitle.setText(
                list.get(position).title);

        holder.itemView.setOnClickListener(
                v -> listener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}