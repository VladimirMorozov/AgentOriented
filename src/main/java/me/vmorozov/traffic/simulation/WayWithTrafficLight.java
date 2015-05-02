package me.vmorozov.traffic.simulation;

import java.util.LinkedList;
import java.util.Queue;

public class WayWithTrafficLight extends Way {

	public WayWithTrafficLight(Waypoint to) {
		super(to);
	}

	private TrafficLight trafficLight;

	private Queue<Car> trafficLightQueue = new LinkedList<Car>();

	private boolean isFirstInQueue(Car car) {
		return trafficLightQueue.peek().equals(car);
	}
	
	public int getCongestion() {
		return trafficLightQueue.size();
	}

	@Override
	public void registerCarWaiting(Car car) {
		trafficLightQueue.add(car);
	}

	@Override
	public boolean tryMoveThroughBy(Car car) {
		if (trafficLight.getSignal() == Signal.GREEN && isFirstInQueue(car)) {
			trafficLightQueue.poll();
			return true;
		}
		return false;
	}

	public TrafficLight getTrafficLight() {
		return trafficLight;
	}

	public void setTrafficLight(TrafficLight trafficLight) {
		this.trafficLight = trafficLight;
	}

}
