/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.vmorozov.traffic.agents;

import jadex.bridge.ComponentIdentifier;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.fipa.SFipa;
import jadex.bridge.service.IService;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.types.message.MessageType;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import jadex.micro.MicroAgent;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Argument;
import jadex.micro.annotation.Arguments;
import jadex.micro.annotation.Description;
import jadex.micro.annotation.Implementation;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import java.util.HashMap;
import java.util.Map;

@Agent
@Description("This is agent")
@Arguments({
    @Argument(name = "nextAgent", clazz = IntersectionAgent.class, description = "Next intersection"),})

@ProvidedServices(
        @ProvidedService(type = IntersectionService.class,
                implementation = @Implementation(expression = "$component")))
@Service
public class IntersectionAgent extends MicroAgent implements IntersectionService {

    @Agent
    IComponentIdentifier nextAgent;

    /**
     * Called when the agent is started.
     *
     * @return
     */
    @AgentBody
    public IFuture<Void> executeBody() {
       pline("Hello");

        String nextAgentName = (String) getArgument("nextAgent");

        nextAgent = getTarget(nextAgentName);
        pline("next name: '" + nextAgent.getName() + "'");

        return new Future<Void>();
    }

    /**
     * Send same message back to the sender.
     *
     * @param msg The message.
     * @param mt The message type.
     */
    public void messageArrived(Map<String, Object> msg, MessageType mt) {
        Map<String, Object> reply = new HashMap<String, Object>(msg);

        //IComponentIdentifier sender = (IComponentIdentifier) msg.get(SFipa.SENDER);
        //  if (mt.equals(SFipa.FIPA_MESSAGE_TYPE)) {
        String s = (String) reply.get(SFipa.CONTENT);
        pline(" MSG:" + s);
        reply.put(SFipa.CONTENT, s + "+");
        // }

        reply.put(SFipa.SENDER, getComponentIdentifier());

        reply.put(SFipa.RECEIVERS, new IComponentIdentifier[]{nextAgent});

        sendMessage(reply, mt);
    }

    /**
     * Get the component identifier for sending.
     */
    protected IComponentIdentifier getTarget(String name) {
        final IComponentIdentifier def = new ComponentIdentifier(name, getComponentIdentifier().getRoot());

        final Future<IComponentIdentifier> ret = new Future<IComponentIdentifier>();

        getServiceContainer().searchService(IntersectionAgent.class, RequiredServiceInfo.SCOPE_LOCAL)
                .addResultListener(new IResultListener<IntersectionAgent>() {
                    public void resultAvailable(IntersectionAgent result) {
                        ret.setResult(((IService) result).getServiceIdentifier().getProviderId());
                    }

                    public void exceptionOccurred(Exception exception) {
                        ret.setResult(def);
                    }
                });
        return ret.get();
    }

    protected void pline(String line) {
        System.out.println(getComponentIdentifier().getLocalName() + ": "+line);
    }
}
