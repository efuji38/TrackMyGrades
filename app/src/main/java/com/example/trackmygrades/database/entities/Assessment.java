package com.example.trackmygrades.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = "assessmentTable")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int assessmentId;
    private String title;
    private LocalDateTime dueDate;
    @ColumnInfo(name = "teacherId")
    private int teacherId;

    public Assessment(String title, LocalDateTime dueDate, int teacherId) {
        this.title = title;
        this.dueDate = dueDate;
        this.teacherId = teacherId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assessment that = (Assessment) o;
        return assessmentId == that.assessmentId && teacherId == that.teacherId && Objects.equals(title, that.title) && Objects.equals(dueDate, that.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assessmentId, title, dueDate, teacherId);
    }

    public int getAssessmentId() {
        return assessmentId;
    }


    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
