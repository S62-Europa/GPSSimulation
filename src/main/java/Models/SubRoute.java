package Models;

import java.util.ArrayList;
import java.util.List;

public class SubRoute {
    private String countryCode; //Country the car is driving in
    private String resourcePath;
    private List<Coordinate> coordinates;
    private boolean subRouteDriven;

    public SubRoute(String countryCode, String resourcePath) {
        coordinates = new ArrayList<>();
        this.countryCode = countryCode;
        this.resourcePath = resourcePath;
        this.subRouteDriven = false;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public boolean isSubRouteDriven() {
        return subRouteDriven;
    }

    public void setSubRouteDriven(boolean subRouteDriven) {
        this.subRouteDriven = subRouteDriven;
    }

    public void addCoordinate(Coordinate coor) {
        this.coordinates.add(coor);
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Coordinate getNextCoordinateAtIndex(int index){
        if (index >= coordinates.size()){
            this.subRouteDriven = true;
            return null;
        }
        return coordinates.get(index);
    }
}
