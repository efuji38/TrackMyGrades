package com.example.trackmygrades.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.trackmygrades.database.entities.Assessment;

public class ViewAssessmentAdapter extends ListAdapter<Assessment, ViewAssessmentViewHolder> {
    public ViewAssessmentAdapter(@NonNull DiffUtil.ItemCallback<Assessment> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewAssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewAssessmentViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAssessmentViewHolder holder, int position) {
        Assessment current = getItem(position);
        holder.bind(current);
    }

    public static class AssessmentDiffCallback extends DiffUtil.ItemCallback<Assessment> {
        @Override
        public boolean areItemsTheSame(@NonNull Assessment oldItem, @NonNull Assessment newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Assessment oldItem, @NonNull Assessment newItem) {
            return oldItem.equals(newItem);
        }
    }

}


