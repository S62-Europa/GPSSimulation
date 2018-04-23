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
                Date previousDateTime = null;
                                
                int indexesTravelled = 0;
                while (!sr.isSubRouteDriven()){
                    Date currentDateTime = new Date();
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
                    messageProducer.sendTransLocation(sr.getCountryCode(), dto);
 
                    System.out.println("Lat: " + coor.getLat() + " - Lon: " + coor.getLon());
                    if(previousCoor != null)
                        System.out.println("Speed is " + getSpeedFromCoords(previousCoor, coor, previousDateTime.getTime(), currentDateTime.getTime()) + " km/h");
                    previousCoor = coor;
                    previousDateTime = currentDateTime;
                    
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
    
    private static double getSpeedFromCoords(Coordinate start, Coordinate end, long startTime, long endTime){
        
  // Convert degrees to radians
  double lat1 = start.getLat() * Math.PI / 180.0;
  double lon1 = start.getLon() * Math.PI / 180.0;
 
  double lat2 = end.getLat() * Math.PI / 180.0;
  double lon2 = end.getLon() * Math.PI / 180.0;
 
  // radius of earth in metres
  double r = 6378100;
 
  // P
  double rho1 = r * Math.cos(lat1);
  double z1 = r * Math.sin(lat1);
  double x1 = rho1 * Math.cos(lon1);
  double y1 = rho1 * Math.sin(lon1);
 
  // Q
  double rho2 = r * Math.cos(lat2);
  double z2 = r * Math.sin(lat2);
  double x2 = rho2 * Math.cos(lon2);
  double y2 = rho2 * Math.sin(lon2);
 
  // Dot product
  double dot = (x1 * x2 + y1 * y2 + z1 * z2);
  double cos_theta = dot / (r * r);
 
  double theta = Math.acos(cos_theta);
 
  // Distance in Metres
  double distInMeters = r * theta;
        double speed_mps = distInMeters / (endTime - startTime);
        double speed_kph = (speed_mps * 3600.0) / 1000.0;
        return speed_kph;
    }
}
 