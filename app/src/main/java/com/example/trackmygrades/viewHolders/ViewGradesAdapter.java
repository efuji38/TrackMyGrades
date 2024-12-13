package com.example.trackmygrades.viewHolders;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.trackmygrades.database.GradeWithAssessment;
import com.example.trackmygrades.database.entities.Assessment;
import com.example.trackmygrades.database.entities.Grade;

public class ViewGradesAdapter extends ListAdapter<GradeWithAssessment, ViewGradesViewHolder> {
    public ViewGradesAdapter(@NonNull DiffUtil.ItemCallback<GradeWithAssessment> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewGradesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewGradesViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewGradesViewHolder holder, int position) {
        GradeWithAssessment current = getItem(position);  // Get the current Assessment item
        holder.bind(current);
    }

    public static class GradeDiffCallback extends DiffUtil.ItemCallback<Grade> {
        @Override
        public boolean areItemsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
            return oldItem == newItem;  // Compare based on assessmentId
        }

        @Override
        public boolean areContentsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
            return oldItem.equals(newItem);  // Check for equality based on content
        }
    }
}
