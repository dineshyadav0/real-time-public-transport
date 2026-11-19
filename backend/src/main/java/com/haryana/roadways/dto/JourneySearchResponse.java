package com.haryana.roadways.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class JourneySearchResponse {

    private Long scheduleId;
    private String routeNumber;
    private String routeHeading;
    private String depotName;

    private String source;
    private String destination;
    private String via;
    private String serviceType;
    private String operatingDays;

    private LocalDate travelDate;

    private LocalTime boardingTime;
    private LocalTime arrivalTime;

    private String sourceDocument;
    private Integer sourcePage;

    private boolean verified;

    public JourneySearchResponse() {
    }

    public JourneySearchResponse(
            Long scheduleId,
            String routeNumber,
            String routeHeading,
            String depotName,
            String source,
            String destination,
            String via,
            String serviceType,
            String operatingDays,
            LocalDate travelDate,
            LocalTime boardingTime,
            LocalTime arrivalTime,
            String sourceDocument,
            Integer sourcePage,
            boolean verified) {

        this.scheduleId = scheduleId;
        this.routeNumber = routeNumber;
        this.routeHeading = routeHeading;
        this.depotName = depotName;
        this.source = source;
        this.destination = destination;
        this.via = via;
        this.serviceType = serviceType;
        this.operatingDays = operatingDays;
        this.travelDate = travelDate;
        this.boardingTime = boardingTime;
        this.arrivalTime = arrivalTime;
        this.sourceDocument = sourceDocument;
        this.sourcePage = sourcePage;
        this.verified = verified;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }

    public String getRouteHeading() {
        return routeHeading;
    }

    public void setRouteHeading(String routeHeading) {
        this.routeHeading = routeHeading;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getOperatingDays() {
        return operatingDays;
    }

    public void setOperatingDays(String operatingDays) {
        this.operatingDays = operatingDays;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }

    public LocalTime getBoardingTime() {
        return boardingTime;
    }

    public void setBoardingTime(LocalTime boardingTime) {
        this.boardingTime = boardingTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getSourceDocument() {
        return sourceDocument;
    }

    public void setSourceDocument(String sourceDocument) {
        this.sourceDocument = sourceDocument;
    }

    public Integer getSourcePage() {
        return sourcePage;
    }

    public void setSourcePage(Integer sourcePage) {
        this.sourcePage = sourcePage;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}