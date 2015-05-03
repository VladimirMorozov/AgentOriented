package me.vmorozov.traffic.simulation;

import java.util.ArrayList;
import java.util.List;

public class SignalPhase extends SimulatedObject {
	
	private int greenIntervalLength;
	private int yellowIntervalLength;
	private int redIntervalLength;
	
	private int startTime;
	
	private SignalPhaseState state = SignalPhaseState.NORMAL;
	private Signal currentSignal;
	
	private List<TrafficLight> phaseLights = new ArrayList<TrafficLight>();
	
	private SignalPhase nextPhase;
	private SignalPhase prioritisedPhase;
	
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
	
	public boolean controlsWay(int wayId) {
		for (TrafficLight light : phaseLights) {
			if (light.controlsWay(wayId)) {
				return true;
			}
		}
		return false;
	}
	
	public void activateAndTick() {
		setActive(true);
		startTime = Simulation.getTime();
		tick();
	}
	
	public int getFullLength() {
		return greenIntervalLength + yellowIntervalLength + redIntervalLength;
	}
	
	public void switchToNormal() { 
		int timeSinceStart = Simulation.getTime() - startTime;
		if (state == SignalPhaseState.KEEP_GREEN && timeSinceStart > greenIntervalLength) {
			startTime = Simulation.getTime() - greenIntervalLength;
		}
		state = SignalPhaseState.NORMAL;
	}
	
	public void keepGreen() {
		state = SignalPhaseState.KEEP_GREEN;
	}
	
	public void endInFavorOf(SignalPhase phase) {
		prioritisedPhase = phase;
		state = SignalPhaseState.END_NOW;
	}
	
	@Override
	public void logTick() {
		System.out.println("current congestion at " + name + ": " + getCongestion() + ", signal: " + currentSignal.toString() + ", state: " + state.toString());
	};

	@Override
	public void tick() {
		int timeSinceStart = Simulation.getTime() - startTime;
		Signal newIntervalSignal = calculateIntervalSignal();
		
		switch(state) {
		case NORMAL:
			if (timeSinceStart > getFullLength()) {
				nextPhase.activateAndTick();
				this.setActive(false);
			} else {
				if (currentSignal != newIntervalSignal) {
					switchLights(newIntervalSignal);
				}
			}
			break;
			
		case KEEP_GREEN:
			if (currentSignal != Signal.GREEN && newIntervalSignal == Signal.GREEN) {
				switchLights(newIntervalSignal);
				
			} else if (currentSignal != Signal.GREEN && newIntervalSignal != Signal.GREEN) {
				//when we have a yellow or red light on this phase we wait for them
				//to end and then repeat phase
				if (timeSinceStart > getFullLength()) {
					this.activateAndTick();
					this.setActive(false);
				} else {
					if (currentSignal != newIntervalSignal) {
						switchLights(newIntervalSignal);
					}
				}
			}
			break;
			
		case END_NOW:
			if (newIntervalSignal == Signal.GREEN) {
				//will shift start time and lead to yellow on this tick
				int timeShift = greenIntervalLength - timeSinceStart;
				startTime -= timeShift;
			}
			
			
			if (timeSinceStart > getFullLength()) {
				prioritisedPhase.activateAndTick();
				this.setActive(false);
			} else {
				if (currentSignal != newIntervalSignal) {
					switchLights(newIntervalSignal);
				}
			}
			
			break;
		}
		
		
		
	}

	private void switchLights(Signal newIntervalSignal) {
		for (TrafficLight trafficLight : phaseLights) {
			trafficLight.setSignal(newIntervalSignal);
		}
		currentSignal = newIntervalSignal;
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
			return Signal.RED;
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
