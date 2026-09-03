package com.tracking.tracking_app.DTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MarkerRequestDTO {
    private final double latitude;
    private final double longitude;
    private final String color;
    private final String title;
    private final String markerDate;

    @JsonCreator
    public MarkerRequestDTO(
            @JsonProperty("marker_date") String markerDate,
            @JsonProperty("latitude") double latitude,
            @JsonProperty("longitude") double longitude,
            @JsonProperty("color") String color,
            @JsonProperty("title") String title) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.color = color;
        this.title = title;
        this.markerDate = markerDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public String getMarkerDate() {
        return markerDate;
    }
}
