package me.vmorozov.traffic.plans;

import java.util.Collections;

import me.vmorozov.traffic.simulation.Intersection;
import me.vmorozov.traffic.simulation.Simulation;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanAborted;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanContextCondition;
import jadex.bdiv3.annotation.PlanFailed;
import jadex.bdiv3.annotation.PlanPassed;
import jadex.bdiv3.runtime.IPlan;
import jadex.bridge.sensor.unit.TimeUnit;

/**
 * Feel free to delete me.
 * @author Vova
 *
 */
@Plan
public class TestPlan {
	
	@PlanAPI
	private IPlan planApi;
	
	@PlanContextCondition(beliefs="someBelief")
	public boolean checkCondition(Boolean someBelief) {
		return someBelief;
	}
	
	@PlanBody
	public void hello() {
		System.out.println("hi one");
		Simulation.createTestSimulation();
		Simulation.simulate(Collections.EMPTY_LIST);
		Intersection intersection = Simulation.getIntersectionByName("main intersection");
		intersection.switchToAuto();
		System.out.println("switch done");
		planApi.waitFor(10000).get();
		System.out.println("hi two");
	}
	
	@PlanAborted
	public void abort() {
		System.out.println("aborted");
	}
	
	@PlanFailed
	public void fail() {
		System.out.println("failed");
	}
	
	@PlanPassed
	public void success() {
		System.out.println("success");
	}

}
