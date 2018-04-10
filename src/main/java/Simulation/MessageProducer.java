package Simulation;

import Dtos.TransLocationDto;
import org.json.JSONObject;

import java.io.*;

public class MessageProducer {
    private Gateway SimulationToItaly;
    private Gateway SimulationToGermany;
    private Gateway SimulationToTheNetherlands;
    private Gateway SimulationToBelgium;
    private Gateway SimulationToFinland;

    public MessageProducer(){
        startSimulationToBelgium();
        startSimulationToFinland();
        startSimulationToGermany();
        startSimulationToItaly();
        startSimulationToTheNetherlands();
    }

    public void sendTransLocation(String countryCode, TransLocationDto dto) throws Exception {
        JSONObject jsonObj = new JSONObject(dto);

        switch (countryCode){
            case "IT":
                SimulationToItaly.channel.basicPublish("", "SimulationToItaly", null, convertPayLoadToBytes(jsonObj.toString()));
                break;
            case "DE":
                SimulationToItaly.channel.basicPublish("", "SimulationToGermany", null, convertPayLoadToBytes(jsonObj.toString()));
                break;
            case "NL":
                SimulationToItaly.channel.basicPublish("", "SimulationToTheNetherlands", null, convertPayLoadToBytes(jsonObj.toString()));
                break;
            case "BE":
                SimulationToItaly.channel.basicPublish("", "SimulationToBelgium", null, convertPayLoadToBytes(jsonObj.toString()));
                break;
            case "FI":
                SimulationToItaly.channel.basicPublish("", "SimulationToFinland", null, convertPayLoadToBytes(jsonObj.toString()));
                break;
            default:
                throw new Exception();
        }
        System.out.println("Simulation has send the payload to the queue.");
    }

    private byte[] convertPayLoadToBytes(String payload) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream writter = new ObjectOutputStream(baos);
        writter.writeObject(payload);
        return baos.toByteArray();
    }

    private void startSimulationToItaly(){
        try {
            SimulationToItaly = new Gateway();
            SimulationToItaly.channel.queueDeclare("SimulationToItaly", false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSimulationToGermany(){
        try {
            SimulationToGermany = new Gateway();
            SimulationToGermany.channel.queueDeclare("SimulationToGermany", false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSimulationToTheNetherlands(){
        try {
            SimulationToTheNetherlands = new Gateway();
            SimulationToTheNetherlands.channel.queueDeclare("SimulationToTheNetherlands", false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSimulationToBelgium(){
        try {
            SimulationToBelgium = new Gateway();
            SimulationToBelgium.channel.queueDeclare("SimulationToBelgium", false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSimulationToFinland(){
        try {
            SimulationToFinland = new Gateway();
            SimulationToFinland.channel.queueDeclare("SimulationToFinland", false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
