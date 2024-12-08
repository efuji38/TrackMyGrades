package com.example.trackmygrades.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gradeTable")
public class Grade {
    @PrimaryKey(autoGenerate = true)
    private int gradeId;
    private int studentId;
    private int assignmentId;
    private double grade;
    private String comment;

    public Grade(int assignmentId, String comment, double grade, int gradeId, int studentId) {
        this.assignmentId = assignmentId;
        this.comment = comment;
        this.grade = grade;
        this.gradeId = gradeId;
        this.studentId = studentId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
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
}


