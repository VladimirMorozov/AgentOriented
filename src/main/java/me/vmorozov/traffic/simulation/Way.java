package me.vmorozov.traffic.simulation;


public abstract class Way {
	
	public Waypoint to;
	

	public Way(Waypoint to) {
		super();
		this.to = to;
	}
	
	public abstract void registerCarWaiting(Car car);

	public abstract boolean tryMoveThroughBy(Car car);
	

}
