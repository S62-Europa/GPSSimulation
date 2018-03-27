package Models;

import java.util.List;

public class SubRoute {
    private String countryCode; //Country the car is driving in
    private String resourcePath;
    private List<Coordinate> coordinates;

    public SubRoute(String countryCode, String resourcePath) {
        this.countryCode = countryCode;
        this.resourcePath = resourcePath;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public void addCoordinate(Coordinate coor) {
        this.coordinates.add(coor);
    }
}
