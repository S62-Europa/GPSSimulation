package eu.s62.gpssimulator;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

    public static void main(String args[]) {
        System.out.println("Starting simulation...");
        try {
            final GPX gpx = GPX.read("res/nederland.gpx");
            gpx.tracks()
                    .flatMap(Track::segments)
                    .flatMap(TrackSegment::points)
                    .forEach(coords->printCoord(coords));
            
        } catch (IOException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void printCoord(WayPoint coord) {
        System.out.println(coord.getLatitude() + " - " + coord.getLongitude());
    }
}
