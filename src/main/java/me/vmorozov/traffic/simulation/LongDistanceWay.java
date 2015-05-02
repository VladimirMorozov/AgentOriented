package me.vmorozov.traffic.simulation;

import java.util.HashMap;
import java.util.Map;

public class LongDistanceWay extends Way {

	private int timeToTraverse = 10;
	private Map<Car, Integer> carsMovingThroughStartTime = new HashMap<Car, Integer>();
	
	public LongDistanceWay(Waypoint to) {
		super(to);
	}



	@Override
	public void registerCarWaiting(Car car) {
		carsMovingThroughStartTime.put(car, Simulation.getTime());
		
	}

	@Override
	public boolean tryMoveThroughBy(Car car) {
		if (carsMovingThroughStartTime.get(car) + timeToTraverse < Simulation.getTime()) {
			return true;
		}
		return false;
	}

}
