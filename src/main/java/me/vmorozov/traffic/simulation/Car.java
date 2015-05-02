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
		boolean hasMoved = currentDestination.tryMoveThroughBy(this);
		System.out.println("moving: " + hasMoved);
		if (hasMoved) {
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

}
