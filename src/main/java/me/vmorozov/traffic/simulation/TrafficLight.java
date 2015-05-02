package me.vmorozov.traffic.simulation;

import java.util.ArrayList;
import java.util.List;

public class TrafficLight {
	
	private Signal signal = Signal.RED;
	private List<WayWithTrafficLight> controlledWays = new ArrayList<WayWithTrafficLight>();
	
	public int getCongestion() {
		int result = 0;
		for (WayWithTrafficLight way : controlledWays) {
			result += way.getCongestion();
		}
		return result; 
	}

	public Signal getSignal() {
		return signal;
	}

	public void setSignal(Signal signal) {
		this.signal = signal;
	}

	public void addControlledWay(WayWithTrafficLight way) {
		controlledWays.add(way);
	}

}
