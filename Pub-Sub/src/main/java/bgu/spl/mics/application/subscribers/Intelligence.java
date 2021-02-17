package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
    private Callback<TickBroadcast> c;
    private List<MissionInfo> missions;
    private int currentTimeTick;
    private Callback<TerminateBroadcast> cTer;
    private CountDownLatch countDownLatch;

    public Intelligence(List<MissionInfo> missions, CountDownLatch countDownLatch) {
        super("Intelligance");
        this.missions = missions;
        this.countDownLatch = countDownLatch;
    }

    @Override
    protected void initialize() {
        c = m -> {
            currentTimeTick = m.getTime();
            for (int i = 0; i < missions.size(); i++) {
                if (missions.get(i).getTimeIssued() <= currentTimeTick) {
                    missions.get(i).setTimeIssued(currentTimeTick);
                    getSimplePublisher().sendEvent(new MissionReceivedEvent(missions.get(i)));
                    missions.remove(missions.get(i));
                }
            }
        };
        cTer = m -> {
            terminate();
        };
        subscribeBroadcast(TickBroadcast.class, c);
        subscribeBroadcast(TerminateBroadcast.class, cTer);
        countDownLatch.countDown();
    }
}