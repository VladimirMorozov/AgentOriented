package me.vmorozov.traffic.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulation {
	
	private static List<SimulatedObject> simulatedObjects = new ArrayList<SimulatedObject>();
	private static int time;
	
	private static Map<String, Intersection> intersections = new HashMap<String, Intersection>();
	
	public static int getTime() {
		return time;
	}
	
	public static void simulate(List<SimulatedObject> simulated) {
		for (SimulatedObject simObject : simulated) {
			simulatedObjects.add(simObject);
			simObject.setActive(true);
		}
		
		Thread simulatorThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (Simulation.class) {
					while(true) {
						tick();
						try {
							Simulation.class.wait(1000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} 
				}
			}
		});
		simulatorThread.start();
		
		
	}
	
	
	
	public static void addSimulatedObject(SimulatedObject simulatedObject, boolean activate) {
		if (activate) {
			simulatedObject.setActive(true);
		}
		simulatedObjects.add(simulatedObject);
	}
	
	public static SimpleIntersection createTestSimulation() {
		SimpleIntersection main = SimpleIntersection.createSimple("main intersection");
		SimpleIntersection second = SimpleIntersection.createSimpleWithConnection(main, "second intersection");
		SimpleIntersection third = SimpleIntersection.createSimpleWithConnection(second, "third intersection");
		intersections.put("main intersection", main);
		intersections.put("second intersection", second);
		intersections.put("third intersection", third);
		
		third.getRightOutgoing().connectByDistance(main.getLeftIncoming());
		main.getLeftOutgoing().connectByDistance(third.getRightIncoming());
		
		main.getBottomOutgoing().connectByDistance(second.getBottomIncoming());
		second.getBottomOutgoing().connectByDistance(main.getBottomIncoming());
		
		second.getTopOutgoing().connectByDistance(third.getTopIncoming());
		third.getTopOutgoing().connectByDistance(second.getTopIncoming());
		
		main.getTopOutgoing().connectByDistance(third.getBottomIncoming());
		third.getBottomOutgoing().connectByDistance(main.getTopIncoming());
		
		return main;
	}
	
	public static Intersection getIntersectionByWayId(int wayId) {
		for (Intersection intersection : intersections.values()) {
			if (intersection.hasWay(wayId)) {
				return intersection;
			}
		}
		throw new RuntimeException("way doesnt belong to any of intersections");
		
	}
	
	public static synchronized Intersection getIntersectionByName(String name) {
		return intersections.get(name);
	}
	
	private static void tick() {
		time++;
		System.out.println("simulation tick: " + time);
		for (SimulatedObject simObject : simulatedObjects) {
			simObject.simulationTick();
		}
	}

}
