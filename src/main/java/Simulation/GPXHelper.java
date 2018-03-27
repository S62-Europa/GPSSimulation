package Simulation;

import Models.Car;
import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GPXHelper {
    public static void readGPX(String path){
        try {
            final GPX gpx = GPX.read(path);
            gpx.tracks()
                    .flatMap(Track::segments)
                    .flatMap(TrackSegment::points)
                    .forEach(GPXHelper::generateCoords);
        } catch (IOException ex) {
            Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void generateCoords(WayPoint coord) {

        System.out.println(coord.getLatitude() + " - " + coord.getLongitude());
    }
}
