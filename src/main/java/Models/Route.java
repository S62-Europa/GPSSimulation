package Models;

import java.util.List;

public class Route {
    /*
    Routes have subroutes in different countries.
    All the routes combined form 1 route.
    all subroutes are saved as gpx files.

    Examples how Route files are saved:
    - Route 1: contains a route in Italy and one in Germany.
    - Route 2: conatins a route in the Netherlands.
    - Route 3: contains a route in Italy, Germany, The Netherlands and Sweden.
    etc.
    We could all make 10 routes through our own contry and 10 multinational routes with google maps.
    Then we have more then enough routes to run the simulation.

    With subroutes stopping and starting at the borders we always know in what country we are.
     */

    private String routeNumber;
    private List<SubRoute> subRoutes; //All the GPX files that make 1 route
    private boolean routeDriven;

    public Route(String routeNumber, List<SubRoute> subRoutes) {
        this.routeNumber = routeNumber;
        this.subRoutes = subRoutes;
        this.routeDriven = false;
    }

    public boolean isRouteDriven() {
        return routeDriven;
    }

    public void setRouteDriven(boolean routeDriven) {
        this.routeDriven = routeDriven;
    }

    public void setAllSubRoutesToFalse(){
        for (SubRoute sr : subRoutes){
            sr.setSubRouteDriven(false);
        }
    }

    public List<SubRoute> getSubRoutes() {
        return subRoutes;
    }
}
