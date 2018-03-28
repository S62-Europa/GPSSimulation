package Simulation;

public class Start {
    public static void main(String args[]) throws InterruptedException {
        System.out.println("Starting simulation...");
        CarSimulator simulator = new CarSimulator();
        simulator.startSimulation();
    }
}
