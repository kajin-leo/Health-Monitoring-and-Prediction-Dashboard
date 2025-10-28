package com.cs79_1.interactive_dashboard.DTO.BatchImport;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ImportProgress {
    private String jobId;
    private int totalNum;
    private int processedNum;
    private int failedNum;
    private boolean completed = false;
    private boolean success = false;
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime completedTime;
    private List<String> errors = new ArrayList<>();
    private int current;
    private String status = "Starting...";

    public ImportProgress(String jobId, int total){
        this.jobId = jobId;
        this.totalNum = total;
    }

    public synchronized void incrementProgress(){
        this.processedNum++;
    }

    public synchronized void incrementFailed(){
        this.failedNum++;
    }

    public synchronized void addError(String error){
        if (errors.size() < 100) {
            errors.add(error);
        }
    }

    public int getPercentage() {
        if (totalNum == 0) return 0;
        return (processedNum + failedNum) * 100 / totalNum;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        if (completed) {
            this.completedTime = LocalDateTime.now();
        }
    }
}