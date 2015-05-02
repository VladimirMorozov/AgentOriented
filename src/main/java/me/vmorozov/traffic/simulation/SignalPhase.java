package me.vmorozov.traffic.simulation;

import java.util.ArrayList;
import java.util.List;

public class SignalPhase extends SimulatedObject {
	
	private int greenIntervalLength;
	private int yellowIntervalLength;
	private int redIntervalLength;
	
	private int startTime;
	
	private Signal currentSignal;
	
	private List<TrafficLight> phaseLights = new ArrayList<TrafficLight>();
	
	private SignalPhase nextPhase;
	
	private String name;

	public SignalPhase(int greenIntervalLength, int startTime,
			Signal currentSignal, List<TrafficLight> phaseLights, String name) {
		
		this.greenIntervalLength = greenIntervalLength;
		this.startTime = startTime;
		this.currentSignal = currentSignal;
		this.phaseLights = phaseLights;
		this.setName(name);
		
		yellowIntervalLength = 3;
		redIntervalLength = 2;
		
		Simulation.addSimulatedObject(this, false);
	}
	
	public void activateAndTick() {
		setActive(true);
		startTime = Simulation.getTime();
		tick();
	}
	
	public int getFullLength() {
		return greenIntervalLength + yellowIntervalLength + redIntervalLength;
	}
	
	@Override
	public void logTick() {
		System.out.println("current congestion at " + name + ": " + getCongestion() + ", signal: " + currentSignal.toString());
	};

	@Override
	public void tick() {
		int timeSinceStart = Simulation.getTime() - startTime;
		if (timeSinceStart > getFullLength()) {
			nextPhase.activateAndTick();
			this.setActive(false);
		} else {
			Signal newIntervalSignal = calculateIntervalSignal();
			if (currentSignal != newIntervalSignal) {
				for (TrafficLight trafficLight : phaseLights) {
					trafficLight.setSignal(newIntervalSignal);
				}
			}
			currentSignal = newIntervalSignal;
		}
		
	}
	
	public int getCongestion() {
		int result = 0;
		for (TrafficLight light : phaseLights) {
			result += light.getCongestion();
		}
		return result;
	}
	
	private Signal calculateIntervalSignal() {
		int timeSinceStart = Simulation.getTime() - startTime;
		if (timeSinceStart <= greenIntervalLength) {
			return Signal.GREEN;
		} else if (timeSinceStart <= greenIntervalLength + yellowIntervalLength) {
			return Signal.YELLOW;
		} else if (timeSinceStart <= greenIntervalLength + yellowIntervalLength + redIntervalLength) {
			return Signal.RED;
		} else {
			throw new RuntimeException("traffic light phase should have ended");
		}
	}

	public SignalPhase getNextPhase() {
		return nextPhase;
	}

	public void setNextPhase(SignalPhase nextPhase) {
		this.nextPhase = nextPhase;
	}

	public int getGreenIntervalLength() {
		return greenIntervalLength;
	}

	public void setGreenIntervalLength(int greenIntervalLength) {
		this.greenIntervalLength = greenIntervalLength;
	}

	public int getYellowIntervalLength() {
		return yellowIntervalLength;
	}

	public void setYellowIntervalLength(int yellowIntervalLength) {
		this.yellowIntervalLength = yellowIntervalLength;
	}

	public int getRedIntervalLength() {
		return redIntervalLength;
	}

	public void setRedIntervalLength(int redIntervalLength) {
		this.redIntervalLength = redIntervalLength;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
