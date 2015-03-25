package me.vmorozov.traffic.agents;

import me.vmorozov.traffic.plans.TestPlan;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Plans;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Description;

/**
 * Feel free to delete me. just testing that jadex works
 * @author Vova
 *
 */
@Agent
@Description("This is stupid")
@Plans(@Plan(body=@Body(TestPlan.class)))
public class TestAgentBDI  {
	
	@Belief
	private boolean someBelief = true;
	
	@Agent
	private BDIAgent agentApi;
	
	@AgentBody
	public void body() {
		agentApi.adoptPlan(new TestPlan());
		agentApi.waitForDelay(1000).get();
		someBelief = false;
	}
	

}
