package me.vmorozov.traffic.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleIntersection extends Intersection{
	
	private Waypoint rightOutgoing;
	private Waypoint rightIncoming;
	
	private Waypoint leftOutgoing;
	private Waypoint leftIncoming;
	
	private Waypoint topOutgoing;
	private Waypoint topIncoming;
	
	private Waypoint bottomOutgoing;
	private Waypoint bottomIncoming;
	
	public static SimpleIntersection createSimple(String name) {
		Waypoint left = new Waypoint("left waypoint of " + name);
		Waypoint top = new Waypoint("top waypoint of " + name);
		Waypoint right = new Waypoint("left waypoint of " + name);
		Waypoint bottom = new Waypoint("bottom waypoint of " + name);
		
		Waypoint leftOutgoing = new Waypoint("left outgoing waypoint of " + name);
		Waypoint topOutgoing = new Waypoint("top outgoing waypoint of " + name);
		Waypoint rightOutgoing = new Waypoint("right outgoing waypoint of " + name);
		Waypoint bottomOutgoing = new Waypoint("bottom outgoing waypoint of " + name);
		
		left.connectByTrafficLight(bottomOutgoing, rightOutgoing, topOutgoing);
		top.connectByTrafficLight(leftOutgoing, rightOutgoing, bottomOutgoing);
		right.connectByTrafficLight(leftOutgoing, topOutgoing, bottomOutgoing);
		bottom.connectByTrafficLight(leftOutgoing, topOutgoing, rightOutgoing);
		
		TrafficLight leftLight = new TrafficLight();
		TrafficLight topLight = new TrafficLight();
		TrafficLight rightLight = new TrafficLight();
		TrafficLight bottomLight = new TrafficLight();
		
		left.setCommonTrafficLight(leftLight);
		top.setCommonTrafficLight(topLight);
		right.setCommonTrafficLight(rightLight);
		bottom.setCommonTrafficLight(bottomLight);
		
		SignalPhase leftRightPhase = new SignalPhase(30, 0, Signal.RED, Arrays.asList(leftLight, rightLight), "left-right phase of " + name);
		SignalPhase topBottomPhase = new SignalPhase(20, 0, Signal.RED, Arrays.asList(topLight, bottomLight), "top-bottom phase of " + name);
		leftRightPhase.setNextPhase(topBottomPhase);
		topBottomPhase.setNextPhase(leftRightPhase);
		List<SignalPhase> signalCycle = new ArrayList<SignalPhase>();
		signalCycle.add(leftRightPhase);
		signalCycle.add(topBottomPhase);
		
		leftRightPhase.setActive(true);
		
		int greenLength = leftRightPhase.getGreenIntervalLength() + topBottomPhase.getGreenIntervalLength();
		
		
		return new SimpleIntersection(rightOutgoing, right, 
				leftOutgoing, left, 
				topOutgoing, top, 
				bottomOutgoing, bottom, signalCycle);
	}
	
	public static SimpleIntersection createSimpleWithConnection(SimpleIntersection connectTo, String name) {
		SimpleIntersection intersection = createSimple(name);
		intersection.getLeftOutgoing().connectByDistance(connectTo.rightIncoming);
		connectTo.getRightOutgoing().connectByDistance(intersection.getLeftIncoming());
		return intersection;
	}
	
	

	public SimpleIntersection(Waypoint rightOutgoing, Waypoint rightIncoming,
			Waypoint leftOutgoing, Waypoint leftIncoming, Waypoint topOutgoing,
			Waypoint topIncoming, Waypoint bottomOutgoing,
			Waypoint bottomIncoming, List<SignalPhase> signalCycle) {
		super();
		this.rightOutgoing = rightOutgoing;
		this.rightIncoming = rightIncoming;
		this.leftOutgoing = leftOutgoing;
		this.leftIncoming = leftIncoming;
		this.topOutgoing = topOutgoing;
		this.topIncoming = topIncoming;
		this.bottomOutgoing = bottomOutgoing;
		this.bottomIncoming = bottomIncoming;
		this.signalCycle = signalCycle;
	}

	public Waypoint getRightOutgoing() {
		return rightOutgoing;
	}

	public void setRightOutgoing(Waypoint rightOutgoing) {
		this.rightOutgoing = rightOutgoing;
	}

	public Waypoint getRightIncoming() {
		return rightIncoming;
	}

	public void setRightIncoming(Waypoint rightIncoming) {
		this.rightIncoming = rightIncoming;
	}

	public Waypoint getLeftOutgoing() {
		return leftOutgoing;
	}

	public void setLeftOutgoing(Waypoint leftOutgoing) {
		this.leftOutgoing = leftOutgoing;
	}

	public Waypoint getLeftIncoming() {
		return leftIncoming;
	}

	public void setLeftIncoming(Waypoint leftIncoming) {
		this.leftIncoming = leftIncoming;
	}

	public Waypoint getTopOutgoing() {
		return topOutgoing;
	}

	public void setTopOutgoing(Waypoint topOutgoing) {
		this.topOutgoing = topOutgoing;
	}

	public Waypoint getTopIncoming() {
		return topIncoming;
	}

	public void setTopIncoming(Waypoint topIncoming) {
		this.topIncoming = topIncoming;
	}

	public Waypoint getBottomOutgoing() {
		return bottomOutgoing;
	}

	public void setBottomOutgoing(Waypoint bottomOutgoing) {
		this.bottomOutgoing = bottomOutgoing;
	}

	public Waypoint getBottomIncoming() {
		return bottomIncoming;
	}

	public void setBottomIncoming(Waypoint bottomIncoming) {
		this.bottomIncoming = bottomIncoming;
	}
	

}
