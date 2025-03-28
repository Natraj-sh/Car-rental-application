package com.example.carapplication.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class LocationDTO {
    private int locationCode;
    private String locationName;

    public int getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(int locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LocationDTO(int locationCode, String locationName) {
        this.locationCode = locationCode;
        this.locationName = locationName;
    }

    public LocationDTO() {
    }
}
