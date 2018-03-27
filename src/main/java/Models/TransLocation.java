package Models;

import java.util.List;

public class TransLocation {
    //Coordinates (lat/lon) from gpx file.
    private List<Coordinate> coordinates;

    public TransLocation(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }
}
