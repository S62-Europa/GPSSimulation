package Dtos;

import java.util.Date;

public class TransLocationDto {
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
}
