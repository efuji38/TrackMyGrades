package com.example.trackmygrades.viewHolders;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trackmygrades.R;
import com.example.trackmygrades.database.entities.Assessment;

public class ViewAssessmentViewHolder extends RecyclerView.ViewHolder {
    private final TextView assessmentTitleTextView;
    private final TextView assessmentDueDateTextView;

    private ViewAssessmentViewHolder(View itemView) {
        super(itemView);
        assessmentTitleTextView = itemView.findViewById(R.id.assessmentTitleTextView);
        assessmentDueDateTextView = itemView.findViewById(R.id.assessmentDueDateTextView);
    }

    public void bind(Assessment assessment) {
        assessmentTitleTextView.setText(assessment.getTitle());
        assessmentDueDateTextView.setText(assessment.getDueDate().toString());
    }

    public static ViewAssessmentViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_assessment_recycler, parent, false);
        return new ViewAssessmentViewHolder(view);
    }
}
