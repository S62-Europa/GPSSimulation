package Simulation;

import Dtos.TransLocationDto;
import Models.Car;
import Models.Coordinate;
import Models.Route;
import Models.SubRoute;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Journey extends Thread {
    private CarSimulator carSimulator;
    private MessageProducer messageProducer;
    private Car car;
    private Route route;

    public Journey(CarSimulator carSimulator, MessageProducer messageProducer, Car car, Route route) {
        this.carSimulator = carSimulator;
        this.messageProducer = messageProducer;
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
                    messageProducer.sendTransLocation(dto);

                    System.out.println("Lat: " + coor.getLat() + " - Lon: " + coor.getLon());

                    Thread.sleep(1000);
                }

                if (route.isRouteDriven()){
                    System.out.println("Thread sleeping for 15 minutes");
                    TimeUnit.MINUTES.sleep(15);
                    this.route = carSimulator.getNewRoute();
                }
            }
        } catch(Exception e) {
            System.out.println("interrupted");
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
