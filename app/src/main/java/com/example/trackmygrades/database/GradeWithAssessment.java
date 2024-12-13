package com.example.trackmygrades.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.trackmygrades.database.entities.Assessment;
import com.example.trackmygrades.database.entities.Grade;

public class GradeWithAssessment {
    @Embedded
    public Grade grade;

    @Relation(
            parentColumn = "assessmentId",
            entityColumn = "assessmentId"
    )
    public Assessment assessment;
}
