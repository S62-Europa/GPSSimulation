package Simulation;

import Models.Car;
import Models.Route;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import Models.SubRoute;
import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import org.json.*;

public class CarSimulator {

    private List<Car> cars;
    private List<Route> routes;

    public CarSimulator() {
        cars = new ArrayList<>();
        routes = new ArrayList<>();
        loadCarsFromJson();
        loadRoutesFromGPX();
        generateTransLocations();
    }

    private void loadCarsFromJson() {
        //For testing purposes im creating a test car hard coded.

        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(new JSONTokener(new FileReader("res/cars/cars.json")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            String id = jsonobject.getString("id");
            String country = jsonobject.getString("country");
            Car newCar = new Car(id, country);
            cars.add(newCar);
            System.out.println(newCar.toString());
        }
    }

    private void loadRoutesFromGPX() {
        String folderPath = "res/routes/";
        File file = new File(folderPath);
        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
        System.out.println(Arrays.toString(directories));

        for (String dir : directories) {

            List<SubRoute> subRoutes = new ArrayList<>();

            //Load subRoutes from files
            File subFile = new File(folderPath + dir);
            String[] subRouteNames = subFile.list((current, name) -> new File(current, name).isFile());
            for (String subFileName : subRouteNames) {
                String countryCode = subFileName.substring(subFileName.indexOf("-") + 1, subFileName.indexOf("-") + 3);
                System.out.println("Country code: " + countryCode);
                SubRoute newSubRoute = new SubRoute(countryCode, folderPath + dir + "/" + subFileName);
                subRoutes.add(newSubRoute);
            }

            Route route = new Route(dir, subRoutes);
            routes.add(route);
        }
    }

    private void generateTransLocations() {
        for (Route r : routes) {
            for (SubRoute sr : r.getSubRoutes()) {
                try {
                    final GPX gpx = GPX.read("");
                    Stream stream = gpx.tracks()
                            .flatMap(Track::segments)
                            .flatMap(TrackSegment::points);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void startSimulation() {
        //Every car has to start.
    }
}
