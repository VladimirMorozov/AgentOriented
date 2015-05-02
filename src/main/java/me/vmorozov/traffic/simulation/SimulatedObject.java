package me.vmorozov.traffic.simulation;

public abstract class SimulatedObject {
	
	private boolean wasActivatedThisTick = false;
	private boolean active = false;
	public final void simulationTick() {
		if (active && !wasActivatedThisTick) {
			tick();
		}
		wasActivatedThisTick = false;
		logTick();
	}
	
	abstract void tick();
	
	/**
	 * always ticks
	 */
	public void logTick() {}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		if (!this.active && active) {
			wasActivatedThisTick = true;
		}
		
		this.active = active;
	}
	
	

}
