package com.tracking.tracking_app.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "coordinates")
public class Marker {
    @Id
    @Indexed
    private String markerID;
    private String markerDate;
    private double latitude;
    private double longitude;
    private String title;
    private String type;
    @Transient
    private String color;
    private String userId;

    public String getMarkerID() {
        return markerID;
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

    public void setColor(String color) {
        this.color = color;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMarkerDate() {
        return markerDate;
    }

    public void setMarkerDate(String markerDate) {
        this.markerDate = markerDate;
    }

    public void setMarkerID(String markerID) {
        this.markerID = markerID;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
