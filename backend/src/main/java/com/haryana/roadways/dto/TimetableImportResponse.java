
package com.haryana.roadways.dto;

import java.util.ArrayList;
import java.util.List;

public class TimetableImportResponse {

    private int totalRecords;
    private int importedRecords;
    private int skippedRecords;
    private int failedRecords;

    private List<String> messages = new ArrayList<>();
    private List<String> errors = new ArrayList<>();

    public TimetableImportResponse() {
    }

    public TimetableImportResponse(
            int totalRecords,
            int importedRecords,
            int skippedRecords,
            int failedRecords,
            List<String> messages,
            List<String> errors) {

        this.totalRecords = totalRecords;
        this.importedRecords = importedRecords;
        this.skippedRecords = skippedRecords;
        this.failedRecords = failedRecords;
        this.messages = messages;
        this.errors = errors;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getImportedRecords() {
        return importedRecords;
    }

    public void setImportedRecords(int importedRecords) {
        this.importedRecords = importedRecords;
    }

    public int getSkippedRecords() {
        return skippedRecords;
    }

    public void setSkippedRecords(int skippedRecords) {
        this.skippedRecords = skippedRecords;
    }

    public int getFailedRecords() {
        return failedRecords;
    }

    public void setFailedRecords(int failedRecords) {
        this.failedRecords = failedRecords;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
