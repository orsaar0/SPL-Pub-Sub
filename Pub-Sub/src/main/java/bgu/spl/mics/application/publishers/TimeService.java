package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
    private int terminate;
    private int current;

    public TimeService(int terminate) {
        super("TimeService");
        this.terminate = terminate;
        current = 0;
    }

    @Override
    protected void initialize() {
    }

    @Override
    public synchronized void run() {
        while (current < terminate) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println("Exception caught in TimeService.run");
            }
            current++;

            if (current == terminate)
                getSimplePublisher().sendBroadcast(new TerminateBroadcast<>());
            else getSimplePublisher().sendBroadcast(new TickBroadcast<>(current, terminate));

        }
    }


}
