package me.vmorozov.traffic.simulation;

public class Car extends SimulatedObject {
	
	private Waypoint location;
	private Way currentDestination;
	
	public Car(Waypoint startingLocation) {
		location = startingLocation;
		currentDestination = selectNewDestination();
		currentDestination.registerCarWaiting(this);
	}

	private Way selectNewDestination() {
		int randomIndex = (int)(Math.random() * (double)location.ways.size());
		return location.ways.get(randomIndex);
	}

	@Override
	public void tick() {
		System.out.println("Car tick");
		if (currentDestination instanceof WayWithTrafficLight) {
			Simulation.getIntersectionByWayId(currentDestination.getId()).allowUrgentPassageOnWay(currentDestination.getId());
		}
		boolean hasMoved = currentDestination.tryMoveThroughBy(this);
		
		if (currentDestination instanceof WayWithTrafficLight) {
			System.out.println("moving through intersection: " + hasMoved);
		} else {
			System.out.println("moving by road");
		}

		if (hasMoved) {
			if (currentDestination instanceof WayWithTrafficLight) {
				Simulation.getIntersectionByWayId(currentDestination.getId()).endUrgentPassage();
			}
			location = currentDestination.to;
			System.out.println("car moved to: " + location.getName());
			if (location.ways.size() == 0) {
				throw new RuntimeException("car cannot move");
			}
			currentDestination = selectNewDestination();
			currentDestination.registerCarWaiting(this);
		} else { 
			System.out.println("car is at:" + location.getName());
		}
	}

	public Waypoint getLocation() {
		return location;
	}

	public void setLocation(Waypoint location) {
		this.location = location;
	}

	public Way getCurrentDestination() {
		return currentDestination;
	}

	public void setCurrentDestination(Way currentDestination) {
		this.currentDestination = currentDestination;
	}
	
	

}
