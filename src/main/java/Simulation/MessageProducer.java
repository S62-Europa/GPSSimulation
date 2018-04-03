package Simulation;

import Dtos.TransLocationDto;
import com.google.gson.Gson;

import java.nio.charset.Charset;

public class MessageProducer {

    public static Charset UTF8 = Charset.forName("UTF-8");

    private Gateway simulationToRegistration;

    public MessageProducer(){
        startSimulationToRegistration();
    }

    public void sendTransLocation(TransLocationDto dto) throws Exception {
        simulationToRegistration.channel.basicPublish("", "SimulationToRegistration", null, convertPayLoadToBytes(dto));
        System.out.println("Simulation has send the payload to the queue at SimulationToRegistration.");
    }

    private byte[] convertPayLoadToBytes(Object payload) {
        return new Gson().toJson(payload).getBytes(UTF8);
    }

    private void startSimulationToRegistration(){
        try {
            simulationToRegistration = new Gateway();
            simulationToRegistration.channel.queueDeclare("SimulationToRegistration", false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
