package Simulation;

import Dtos.TransLocationDto;
import Models.Car;
import Models.Coordinate;
import Models.Route;
import Models.SubRoute;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Journey extends Thread {
    private CarSimulator carSimulator;
    private Car car;
    private Route route;

    public Journey(CarSimulator carSimulator, Car car, Route route) {
        this.carSimulator = carSimulator;
        this.car = car;
        this.route = route;
    }

    public void run(){
        try {
            while (!route.isRouteDriven()) {
                SubRoute sr = findSubRouteThatIsNotDrivenYet();

                int indexesTravelled = 0;
                while (!sr.isSubRouteDriven()){
                    Coordinate coor = sr.getNextCoordinateAtIndex(indexesTravelled);
                    indexesTravelled++;

                    if (coor == null) break;

                    //Deze dto naar RabbitMQ
                    TransLocationDto dto = new TransLocationDto(
                            car.getSerialNumber(),
                            coor.getLat().toString(),
                            coor.getLon().toString(),
                            getDateTimeNowIso8601UTC(),
                            car.getOriginCountry());

                    System.out.println("Lat: " + coor.getLat() + " - Lon: " + coor.getLon());
                    Thread.sleep(1000);
                }

                if (route.isRouteDriven()){
                    this.route = carSimulator.getNewRoute();
                }
            }
        } catch(InterruptedException e) {
            System.out.println("sleep interrupted");
        }
        System.out.println("Einde thread zou niet mogelijk moeten zijn...");
    }

    private String getDateTimeNowIso8601UTC(){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    private SubRoute findSubRouteThatIsNotDrivenYet(){
        List<SubRoute> srs = route.getSubRoutes();
        for (SubRoute sr : srs){
            if (!sr.isSubRouteDriven()){
                return sr;
            }
        }
        route.setRouteDriven(true);
        return srs.get(srs.size() - 1);
    }
}
