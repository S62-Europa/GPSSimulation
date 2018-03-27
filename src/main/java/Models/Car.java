package Models;

import Dtos.TransLocationDto;

import java.util.Timer;

public class Car {
    private String serialNumber;
    private Timer timer;
    private String originCountry;

    public Car(String serialNumber, String originCountry) {
        this.timer = new Timer();
        this.originCountry = originCountry;
        this.serialNumber = serialNumber;
    }

    public TransLocationDto generateTransLocation(){
        //This method uses libraries to calculate the lat and lon from the route.
        //The timestamp comes from the timer in this object.
        //A subroute has a countrycode, a subroute always is in only 1 country.
        //A multinational route needs multiple subroutes to be international.
        //The serial number comes from the serialnumber propertie of this class.
        return null;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    public String toString() {
        return this.serialNumber + " " + this.originCountry;
    }
}