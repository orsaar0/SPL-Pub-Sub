package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.SendReleaseEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.concurrent.CountDownLatch;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {

    private int serialNum;
    private int currentTime;
    private Callback<AgentsAvailableEvent> c;
    private Callback<SendReleaseEvent> cSR;
    private Callback<TickBroadcast> cT;
    private Callback<TerminateBroadcast> cTer;
    private CountDownLatch countDownLatch;

    public Moneypenny(int serialNum,CountDownLatch countDownLatch) {
        super("Moneypenny");
        this.serialNum = serialNum;
        this.countDownLatch=countDownLatch;
    }

    @Override
    protected void initialize() {
        Squad squad = Squad.getInstance();

        cSR = m -> {
            if (m.send) {
                complete(m, true);
                squad.sendAgents(m.serials, m.duration);
            } else {
                complete(m, true);
                squad.releaseAgents(m.serials);
            }
        };

        c = m -> {
            if (squad.getAgents(m.getSerials())) {
                complete(m, true);
                m.setAgentsNames(squad.getAgentsNames(m.getSerials())); //setAgentsNames in the AAE
                m.setSerialNumMoneypenny(serialNum);
            } else {
                complete(m, false);
            }
        };
        cT = m -> currentTime = m.getTime();
        cTer = m -> terminate();

        if (serialNum == 1) {
            subscribeEvent(AgentsAvailableEvent.class, c);
            subscribeBroadcast(TickBroadcast.class, cT);
            subscribeBroadcast(TerminateBroadcast.class,cTer);
        } else {
            subscribeBroadcast(TickBroadcast.class, cT);
            subscribeEvent(SendReleaseEvent.class, cSR);
            subscribeBroadcast(TerminateBroadcast.class,cTer);
        }
        countDownLatch.countDown();
    }

}
