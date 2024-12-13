package com.example.trackmygrades.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "gradeTable")
public class Grade {
    @PrimaryKey(autoGenerate = true)
    private int gradeId;
    private int studentId;
    private int assessmentId;
    private double grade;
    private String comment;

    public Grade() {
    }

    public Grade(int assignmentId, double grade, String comment,  int studentId) {
        this.assessmentId = assignmentId;
        this.grade = grade;
        this.comment = comment;
        this.studentId = studentId;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade1 = (Grade) o;
        return gradeId == grade1.gradeId && studentId == grade1.studentId && assessmentId == grade1.assessmentId && Double.compare(grade, grade1.grade) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeId, studentId, assessmentId, grade);
    }

}


