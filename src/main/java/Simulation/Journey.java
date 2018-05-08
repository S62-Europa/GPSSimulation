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
 
                Coordinate previousCoor = null;
                                
                int indexesTravelled = 0;
                while (!sr.isSubRouteDriven()){
                    Date currentDateTime = new Date();
                    Coordinate coor = sr.getNextCoordinateAtIndex(indexesTravelled);
                    indexesTravelled++;
 
                    if (coor == null) break;
                     
                    System.out.println("Lat: " + coor.getLat() + " - Lon: " + coor.getLon());
                    if(previousCoor != null)
                    {
                        double distBetweenPoints = distance(previousCoor.getLat(), previousCoor.getLon(), coor.getLat(), coor.getLon());
                        System.out.println("Distance to previous point is " + distBetweenPoints);
                        double secondsTillNextCoor = distBetweenPoints / this.car.getSpeed() / 3600;
                        System.out.println(secondsTillNextCoor + " seconds until next point is reached!");
                    }
                    previousCoor = coor;
                                        
                    //Deze dto naar RabbitMQ
                    TransLocationDto dto = new TransLocationDto(
                            car.getSerialNumber(),
                            coor.getLat().toString(),
                            coor.getLon().toString(),
                            getDateTimeNowIso8601UTC(),
                            car.getOriginCountry());
                    messageProducer.sendTransLocation(sr.getCountryCode(), dto);
 
                    System.out.println();
                    Thread.sleep(1000);
                }
 
                if (route.isRouteDriven()){
                    System.out.println("Thread sleeping for 15 minutes");
                    TimeUnit.MINUTES.sleep(15);
                    this.route = carSimulator.getNewRoute();
                }
            }
        } catch (Exception e) {
            System.out.println("interrupted");
        }
        System.out.println("Einde thread zou niet mogelijk moeten zijn...");
    }

    private String getDateTimeNowIso8601UTC() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    private SubRoute findSubRouteThatIsNotDrivenYet() {
        List<SubRoute> srs = route.getSubRoutes();
        for (SubRoute sr : srs) {
            if (!sr.isSubRouteDriven()) {
                return sr;
            }
        }
        route.setRouteDriven(true);
        return srs.get(srs.size() - 1);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
      double theta = lon1 - lon2;
      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
      dist = Math.acos(dist);
      dist = rad2deg(dist);
      dist = dist * 60 * 1.1515;
      
      return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
      return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
      return (rad * 180.0 / Math.PI);
    }
}
 