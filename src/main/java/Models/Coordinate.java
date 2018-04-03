package Models;

public class Coordinate {
    private Double lat;
    private Double lon;

    public Coordinate(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }
}
