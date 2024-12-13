package com.example.trackmygrades.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trackmygrades.R;
import com.example.trackmygrades.database.GradeWithAssessment;
import com.example.trackmygrades.database.TrackMyGradesDatabase;
import com.example.trackmygrades.database.entities.Assessment;
import com.example.trackmygrades.database.entities.Grade;

public class ViewGradesViewHolder extends RecyclerView.ViewHolder {

    private final TextView assessmentTitleTextView;
    private final TextView gradeTextView;
    private final TextView commentTextView;
    private TrackMyGradesDatabase db;


    private ViewGradesViewHolder(View itemView) {
        super(itemView);
        assessmentTitleTextView = itemView.findViewById(R.id.assessmentTitleTextView);
        gradeTextView = itemView.findViewById(R.id.gradeTextView);
        commentTextView = itemView.findViewById(R.id.commentTextView);
    }

    public void bind(GradeWithAssessment gradeWithAssessment) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // Query the assessment title based on the assessmentId
//                Assessment assessment = db.assessmentDAO().getAssessmentById(String.valueOf(grade.getAssignmentId()));
//                String assessmentTitle = (assessment != null) ? assessment.getTitle() : "Unknown Title";
//
//                // Set the assessment title on the main thread
//                assessmentTitleTextView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        assessmentTitleTextView.setText(assessmentTitle);
//                        gradeTextView.setText(String.valueOf(grade.getGrade()));  // Assuming grade is a number
//                        commentTextView.setText(grade.getComment());
//                    }
//                });
//            }
//        }).start();
        String assessmentTitle = (gradeWithAssessment != null) ? gradeWithAssessment.assessment.getTitle() : "Unknown Title";
        assessmentTitleTextView.setText(assessmentTitle);
        gradeTextView.setText(String.valueOf(gradeWithAssessment.grade.getGrade()));  // Assuming grade is a number
        commentTextView.setText(gradeWithAssessment.grade.getComment());
    }

    public static ViewGradesViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_grades_recycler, parent, false);
        return new ViewGradesViewHolder(view);
    }
}
