package me.vmorozov.traffic.agents;

import me.vmorozov.traffic.plans.TestPlan;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Plans;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentArgument;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Argument;
import jadex.micro.annotation.Arguments;
import jadex.micro.annotation.Description;

/**
 * Feel free to delete me. just testing that jadex works
 * @author Vova
 *
 */
@Agent
@Description("This is stupid2")
@Plans(@Plan(body=@Body(TestPlan.class)))
@Arguments({
	  @Argument(name="intersectionName", clazz=String.class, defaultvalue="\"nerd\"", description="The keyword to react to."),
	  @Argument(name="reply", clazz=String.class, defaultvalue="\"Watch your language\"", description="The reply message.")
})
public class TestAgentBDI  {
	
	@AgentArgument
	private String intersectionName;
	
	@Belief
	private boolean someBelief = true;
	
	@Agent
	private BDIAgent agentApi;
	
	@AgentBody
	public void body() {
            System.out.println("Hello world!");
		agentApi.adoptPlan(new TestPlan());
		agentApi.waitForDelay(1000).get();
		someBelief = false;
	}
	

}
