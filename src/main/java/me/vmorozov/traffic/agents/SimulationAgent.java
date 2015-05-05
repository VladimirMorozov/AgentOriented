package me.vmorozov.traffic.agents;

import jadex.bridge.ComponentIdentifier;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;

import jadex.bridge.fipa.SFipa;
import jadex.bridge.service.IService;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.bridge.service.types.message.MessageType;
import jadex.commons.future.DefaultResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import jadex.commons.future.ITuple2Future;
import jadex.micro.MicroAgent;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Argument;
import jadex.micro.annotation.Arguments;

import jadex.micro.annotation.Description;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Agent
@Description("This is Simulation")
@Arguments({
    @Argument(name = "num_of_intersections", clazz = int.class, description = "number of Intersections", defaultvalue = "3"),})
public class SimulationAgent extends MicroAgent {

    int numOfIntersections;
    int mesNr = 0;

    @AgentBody
    public IFuture<Void> executeBody() {
        numOfIntersections = ((Number) getArgument("num_of_intersections")).intValue();
        pline("Start");
        myStart(numOfIntersections);
        return new Future<Void>();
        //return IFuture.DONE;
    }

    protected void myStart(final int max) {
        IFuture cms = getCMS();
        pline("step1! max = " + max);

//			System.out.println("Args: "+num+" "+args);
        cms.addResultListener(createResultListener(new DefaultResultListener() {
            public void resultAvailable(Object result) {
                IComponentManagementService cms = ((IComponentManagementService) result);

                for (int num = 0; num < max; num++) {
                    Map arg = new HashMap();
                    arg.put("nextAgent", createPeerName(num + 1, getComponentIdentifier()));
                    cms.createComponent(
                            createPeerName(num, getComponentIdentifier()),
                            IntersectionAgent.class.getName().replaceAll("\\.", "/") + ".class",
                            new CreationInfo(null, arg, null, null, null, null, null, null, null, null, null, getComponentDescription().getResourceIdentifier())
                    );

                }
            }
        }));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SimulationAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        final IComponentIdentifier nextAgent = getTarget("Intersection0");
//
//        for (int mesNr = 0; mesNr < 3; mesNr++) {
//            mySend(nextAgent, mesNr);
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(SimulationAgent.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

        final IComponentStep step = new IComponentStep<Void>() {
            public IFuture<Void> execute(IInternalAccess ia) {
                mySend(nextAgent, mesNr++);

                waitFor(5000, this);

                return IFuture.DONE;
            }
        };
        scheduleStep(step);
    }

    protected void mySend(IComponentIdentifier nextAgent, int n) {

        pline("SMT:" + nextAgent.getLocalName());
        final Map msg = new HashMap();
        msg.put(SFipa.CONTENT, "Sõnum(" + n + ")");
        msg.put(SFipa.PERFORMATIVE, SFipa.QUERY_IF);
        msg.put(SFipa.CONVERSATION_ID, "suiq" + n);
        msg.put(SFipa.RECEIVERS, new IComponentIdentifier[]{nextAgent});

        msg.put(SFipa.SENDER, getComponentIdentifier());
        sendMessage(msg, SFipa.FIPA_MESSAGE_TYPE);

    }

    /**
     * Send same message back to the sender.
     *
     * @param msg The message.
     * @param mt The message type.
     */
    public void messageArrived(Map<String, Object> msg, MessageType mt) {
        pline("got message");
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
        pline("Gtarg");
        return ret.get();
    }

    /**
     * Create a name for a peer with a given number.
     */
    protected String createPeerName(int num, IComponentIdentifier cid) {
//        String name = cid.getLocalName();
//        int index = name.indexOf("Peer_#");
//        if (index != -1) {
//            name = name.substring(0, index);
//        }
//        if (num != 1) {
//            name += "Peer_#" + num;
//       }

        return "Intersection" + num;
    }

    protected IFuture getCMS() {
        IFuture ret = null;	// Uncomment for no caching.
        if (ret == null) {
            ret = getServiceContainer().searchServiceUpwards(IComponentManagementService.class); // Decoupled service proxy
        }
        return ret;
    }

    protected void pline(String line) {
        System.out.println(getComponentIdentifier().getLocalName() + ": " + line);
    }
}
