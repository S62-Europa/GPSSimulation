package Simulation;

import Models.Car;
import Models.Coordinate;
import Models.Route;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import Models.SubRoute;
import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import org.json.*;

public class CarSimulator {

    private List<Car> cars;
    private List<Route> routes;
    private List<Thread> journeys;
    private Random rndm;
    private MessageProducer messageProducer;

    public CarSimulator() {
        cars = new ArrayList<>();
        routes = new ArrayList<>();
        journeys = new ArrayList<>();
        rndm = new Random();
        messageProducer = new MessageProducer();
        loadCarsFromJson();
        loadRoutesFromGPX();
        generateCoords();
        createJourneys();
    }

    private void loadCarsFromJson() {
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

    private void generateCoords() {
        for (Route r : routes) {
            for (SubRoute sr : r.getSubRoutes()) {
                try {
                    final GPX gpx = GPX.read(sr.getResourcePath());
                        gpx.tracks()
                            .flatMap(Track::segments)
                            .flatMap(TrackSegment::points)
                            .forEach(coord->saveCoords(sr, coord));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void saveCoords(SubRoute sr, WayPoint coord) {
        sr.addCoordinate(new Coordinate(coord.getLatitude().doubleValue(), coord.getLongitude().doubleValue()));
    }

    private void createJourneys() {

        for (Car c : cars){
            int rndmRouteIndex = rndm.nextInt(routes.size());
            Journey journey = new Journey(this, messageProducer, c, routes.get(rndmRouteIndex));
            journeys.add(journey);
        }
    }

    public Route getNewRoute() {
        int rndmRouteIndex = rndm.nextInt(routes.size());
        Route route = routes.get(rndmRouteIndex);
        route.setRouteDriven(false);
        route.setAllSubRoutesToFalse();
        return route;
    }

    public void startSimulation() throws InterruptedException {
        for (Thread t : journeys){
            t.start();
            Thread.sleep((rndm.nextInt(10 - 1 + 1) + 1) * 1000);
        }
    }
}
