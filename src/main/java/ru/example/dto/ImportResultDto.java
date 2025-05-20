package ru.example.dto;

import java.util.ArrayList;
import java.util.List;

public class ImportResultDto {
    private int totalImported;
    private int successCount;
    private int errorCount;
    private List<String> errors = new ArrayList<>();

    public int getTotalImported() {
        return totalImported;
    }

    public void setTotalImported(int totalImported) {
        this.totalImported = totalImported;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void addError(String error) {
        this.errors.add(error);
        this.errorCount++;
    }

    public void incrementSuccess() {
        this.successCount++;
    }
}
