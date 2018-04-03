package Simulation.Listener;

public class SimulationListenerBuilder {

    private String serverAddress = "localhost";
    private String countryCode;
    private String queue = "SimulationToRegistration";
    private ISimulationListener listener = null;

    private SimulationListenerBuilder(String countryCode) {
        this.countryCode = countryCode;
    }

    public static SimulationListenerBuilder createBuilder(String countryCode) {
        return new SimulationListenerBuilder(countryCode);
    }

    public SimulationListenerBuilder setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
        return this;
    }

    public SimulationListenerBuilder setChannel(String queue) {
        this.queue = queue;
        return this;
    }

    public SimulationListenerBuilder setListener(ISimulationListener listener) {
        this.listener = listener;
        return this;
    }

    public SimulationListener build() {
        System.out.println("Created SimulationListener with following parameters");
        System.out.println("ServerAddress: " + serverAddress);
        System.out.println("CountryCode  : " + countryCode);
        System.out.println("Queue        : " + queue);
        return new SimulationListener(serverAddress, countryCode, queue, listener);
    }
}
