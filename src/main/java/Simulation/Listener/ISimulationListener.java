package Simulation.Listener;

import Dtos.TransLocationDto;

public interface ISimulationListener {

    void onReceiveFromSimulation(TransLocationDto transLocationDto);

}
