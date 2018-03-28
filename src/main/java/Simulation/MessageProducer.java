package Simulation;

import Dtos.TransLocationDto;

import java.io.*;

public class MessageProducer {
    private Gateway simulationToRegistration;

    public MessageProducer(){
        startSimulationToRegistration();
    }

    public void sendTransLocation(TransLocationDto dto) throws Exception {
        simulationToRegistration.channel.basicPublish("", "SimulationToRegistration", null, convertPayLoadToBytes(dto));
        System.out.println("Simulation has send the payload to the queue at SimulationToRegistration.");
    }

    private byte[] convertPayLoadToBytes(Object payload) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream writter = new ObjectOutputStream(baos);
        writter.writeObject(payload);
        return baos.toByteArray();
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
