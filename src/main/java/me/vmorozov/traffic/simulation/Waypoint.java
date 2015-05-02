package me.vmorozov.traffic.simulation;

import java.util.ArrayList;
import java.util.List;

public class Waypoint {
	
	private String name;
	
	public Waypoint(String name) {
		this.name = name;
	}
	
	public List<Way> ways = new ArrayList<Way>();
	
	public void connectByTrafficLight(Waypoint to) {
		WayWithTrafficLight way = new WayWithTrafficLight(to);
		ways.add(way);
	}
	
	public void connectByTrafficLight(Waypoint... to) {
		for (Waypoint waypoint : to) {
			WayWithTrafficLight way = new WayWithTrafficLight(waypoint);
			ways.add(way);
		}
	}
	
	public void connectByDistance(Waypoint to) {
		LongDistanceWay way = new LongDistanceWay(to);
		ways.add(way);
	}
	
	public void interconnectByDistance(Waypoint with) {
		LongDistanceWay way = new LongDistanceWay(with);
		ways.add(way);
		with.connectByDistance(this);
	}
	
	public void setCommonTrafficLight(TrafficLight trafficLight) {
		for (Way way : ways) {
			WayWithTrafficLight wayWithTrafficLight = (WayWithTrafficLight)way;
			wayWithTrafficLight.setTrafficLight(trafficLight);
			trafficLight.addControlledWay(wayWithTrafficLight);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
