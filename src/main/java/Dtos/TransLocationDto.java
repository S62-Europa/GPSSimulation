package Dtos;

import java.io.Serializable;

public class TransLocationDto implements Serializable {
    private String serialNumber;
    private String lat;
    private String lon;
    private String timestamp;
    private String countryCode; //Country the car is driving in

    public TransLocationDto(String serialNumber, String lat, String lon, String timestamp, String countryCode) {
        this.serialNumber = serialNumber;
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp;
        this.countryCode = countryCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
