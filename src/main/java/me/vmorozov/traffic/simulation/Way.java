package me.vmorozov.traffic.simulation;


public abstract class Way {
	
	private static int nextId = 0;
	private int id;
	
	public Waypoint to;
	

	public Way(Waypoint to) {
		super();
		this.to = to;
		synchronized (Way.class) {
			id = nextId;
			nextId++;
		}
	}
	
	public abstract void registerCarWaiting(Car car);

	public abstract boolean tryMoveThroughBy(Car car);

	public int getId() {
		return id;
	}

}
