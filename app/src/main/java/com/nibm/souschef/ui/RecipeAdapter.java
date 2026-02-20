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

    ArrayList<RecipeData> list;
    OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(int position);
    }

    public RecipeAdapter(ArrayList<RecipeData> list,
                         OnRecipeClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;

        public ViewHolder(View itemView,
                          OnRecipeClickListener listener) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);

            itemView.setOnClickListener(v -> {
                if (listener != null &&
                        getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onRecipeClick(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,
                                 int position) {
        holder.txtTitle.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}