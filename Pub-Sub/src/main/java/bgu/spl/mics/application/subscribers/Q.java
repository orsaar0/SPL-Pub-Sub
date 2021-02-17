package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.concurrent.CountDownLatch;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

    private Callback<GadgetAvailableEvent> cG;
    private Callback<TickBroadcast> cT;
    private Callback<TerminateBroadcast> cTer;
    int currentTime;
    private CountDownLatch countDownLatch;

    public Q(CountDownLatch countDownLatch) {
        super("Q");
        this.countDownLatch = countDownLatch;
    }

    @Override
    protected void initialize() {
        cG = m -> {
            if (Inventory.getInstance().getItem(m.getGadget())) {
                complete(m, true);
            } else {
                complete(m, false);
            }
            m.setCurrentTimeTick(currentTime);
        };
        cT = m -> {
            currentTime = m.getTime();
        };

        cTer = m ->
                terminate();


        subscribeEvent(GadgetAvailableEvent.class, cG);
        subscribeBroadcast(TickBroadcast.class, cT);
        subscribeBroadcast(TerminateBroadcast.class, cTer);

        countDownLatch.countDown();

    }


}
