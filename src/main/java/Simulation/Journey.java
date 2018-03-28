package Simulation;

import Models.Car;
import Models.Coordinate;
import Models.Route;
import Models.SubRoute;

import java.util.List;

public class Journey extends Thread {
    private Car car;
    private Route route;

    public Journey(Car car, Route route) {
        this.car = car;
        this.route = route;
    }

    public void run(){
        try {
            while (!route.isRouteDriven()) {
                SubRoute sr = findSubRouteThatIsNotDrivenYet();
                if (route.isRouteDriven()) return;

                int indexesTravelled = 0;
                while (!sr.isSubRouteDriven()){
                    Coordinate coor = sr.getNextCoordinateAtIndex(indexesTravelled);
                    indexesTravelled += 50;

                    if (coor == null) break;

                    //naar message queue als TransLocationDto
                    System.out.println("Lat: " + coor.getLat() + " - Lon: " + coor.getLon());
                    Thread.sleep(1000);
                }
            }
            //thread klaar vraag om nieuwe route.
        } catch(InterruptedException e) {
            System.out.println("sleep interrupted");
        }
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
