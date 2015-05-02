package me.vmorozov.traffic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.vmorozov.traffic.simulation.Car;
import me.vmorozov.traffic.simulation.SimpleIntersection;
import me.vmorozov.traffic.simulation.SimulatedObject;
import me.vmorozov.traffic.simulation.Simulation;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	SimpleIntersection main = Simulation.createTestSimulation();
    	ArrayList<SimulatedObject> test = new ArrayList<SimulatedObject>();
    	test.add(new Car(main.getLeftIncoming()));
        Simulation.simulate(test);
    }
}
