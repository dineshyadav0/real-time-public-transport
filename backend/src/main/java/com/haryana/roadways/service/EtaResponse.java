package com.haryana.roadways.service;

public class EtaResponse {
    private boolean success;
    private double estimated_eta_minutes;
    private String error;

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public double getEstimated_eta_minutes() { return estimated_eta_minutes; }
    public void setEstimated_eta_minutes(double estimated_eta_minutes) { this.estimated_eta_minutes = estimated_eta_minutes; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}