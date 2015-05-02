package me.vmorozov.traffic.simulation;

import java.util.Arrays;
import java.util.List;

import org.bouncycastle.crypto.RuntimeCryptoException;

public class Intersection {
	
	private List<SignalPhase> signalCycle;
	protected String name;
	
	public void allowUrgentPassageOnWay(Way way) {
		//throw new RuntimeException("Unimplemented");
	}
	
	public void switchToAuto() {
		//throw new RuntimeException("Unimplemented");
	}
	
	public void synchronize(int greenwaveInfo) {
		//throw new RuntimeException("Unimplemented");
	}
	
	
	@Override
	public String toString() {
		return "[Intersection: " + name + "]";
	}


}
