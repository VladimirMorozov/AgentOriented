package me.vmorozov.traffic.simulation;

import java.util.List;

public class Intersection {
	
	protected List<SignalPhase> signalCycle;
	protected String name;
	
	public boolean hasWay(int wayId) {
		for (SignalPhase signalPhase : signalCycle) {
			if (signalPhase.controlsWay(wayId)) {
				return true;
			}
		}
		return false;
	}
	
	public void allowUrgentPassageOnWay(int wayId) {

		SignalPhase wayControllingPhase = null;
		for (SignalPhase signalPhase : signalCycle) {
			if (signalPhase.controlsWay(wayId)) {
				wayControllingPhase = signalPhase;
				wayControllingPhase.keepGreen();
			}
		}
		
		if (wayControllingPhase == null) {
			throw new RuntimeException("passage requested on unknown way");
		}
		
		for (SignalPhase signalPhase : signalCycle) {
			if (signalPhase != wayControllingPhase) {
				signalPhase.endInFavorOf(wayControllingPhase);
			}
		}

	}
	
	public void endUrgentPassage() {
		for (SignalPhase signalPhase : signalCycle) {
			signalPhase.switchToNormal();
		}
	}
	
	public void switchToAuto() {
		//throw new RuntimeException("Unimplemented");
	}
	
	public void synchronize(int greenwaveInfo) {
		//throw new RuntimeException("Unimplemented");
	}
	
	
	
	public List<SignalPhase> getSignalCycle() {
		return signalCycle;
	}

	public void setSignalCycle(List<SignalPhase> signalCycle) {
		this.signalCycle = signalCycle;
	}

	@Override
	public String toString() {
		return "[Intersection: " + name + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
