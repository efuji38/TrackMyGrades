package com.example.trackmygrades.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = "assessmentTable")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int assignmentId;
    private String title;
    private LocalDateTime dueDate;
    private int teacherId;

    public Assessment(int assignmentId, LocalDateTime dueDate, int teacherId, String title) {
        this.assignmentId = assignmentId;
        this.dueDate = dueDate;
        this.teacherId = teacherId;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assessment that = (Assessment) o;
        return assignmentId == that.assignmentId && teacherId == that.teacherId && Objects.equals(title, that.title) && Objects.equals(dueDate, that.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignmentId, title, dueDate, teacherId);
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
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
}
