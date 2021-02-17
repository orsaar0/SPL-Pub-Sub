package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.concurrent.CountDownLatch;


/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

    private int serialNum;
    private int currentTimeTick;
    private Callback<MissionReceivedEvent> cM;
    private Callback<TickBroadcast> cT;
    private Callback<TerminateBroadcast> cTer;
    private CountDownLatch countDownLatch;

    public M(int serialNum, CountDownLatch countDownLatch) {
        super("M");
        this.serialNum = serialNum;
        this.countDownLatch = countDownLatch;
    }

    @Override
    protected void initialize() {
        cM = m -> {

            Diary.getInstance().incrementTotal();
            SendReleaseEvent<Boolean> sre = new SendReleaseEvent<>(m.getAgentsSerials(), false, m.getDuration());
            AgentsAvailableEvent<?> aag = new AgentsAvailableEvent<>(m.getAgentsSerials(), m.getDuration());
            Future<Boolean> agentsFuture = getSimplePublisher().sendEvent(aag);

            if (agentsFuture == null || agentsFuture.get() == null) terminate();
            else {

                if (!agentsFuture.get()) {
                    Future<Boolean> sendReleaseFuture = getSimplePublisher().sendEvent(sre);
                    if (sendReleaseFuture == null) terminate();
                    complete(m, "aborted");
                } else {
                    GadgetAvailableEvent<?> gaa = new GadgetAvailableEvent<>(m.getGadget());
                    Future<Boolean> gadgetFuture = getSimplePublisher().sendEvent(gaa);

                    if (gadgetFuture == null || gadgetFuture.get() == null) {
                        terminate();

                    } else {
                        if (!gadgetFuture.get()) {
                            Future<Boolean> sendReleaseFuture = getSimplePublisher().sendEvent(sre);
                            if (sendReleaseFuture == null) terminate();

                            complete(m, "aborted");
                        } else {
                            if (currentTimeTick > m.getExpiryTime()) {
                                Future<Boolean> sendReleaseFuture = getSimplePublisher().sendEvent(sre);
                                if (sendReleaseFuture == null) terminate();
                                complete(m, "aborted");
                            } else {
                                sre.send = true;
                                Future<Boolean> sendReleaseFuture = getSimplePublisher().sendEvent(sre);
                                if (sendReleaseFuture == null) terminate();
                                complete(m, "completed");
                                Report report = new Report(m.getMissionInfo(), aag.getAgentsNames(), aag.getSerialNumMoneypenny(), serialNum, gaa.getCurrentTimeTick(), currentTimeTick);
                                Diary.getInstance().addReport(report);
                            }
                        }
                    }
                }
            }
//            } catch (Exception e) {
//                System.out.println("Exception caught in MRE CallBack");
//            }
        };
        cT = m -> {
            currentTimeTick = m.getTime();
        };
        cTer = m -> {
            terminate();
        };

        subscribeEvent(MissionReceivedEvent.class, cM);
        subscribeBroadcast(TerminateBroadcast.class, cTer);
        subscribeBroadcast(TickBroadcast.class, cT);
        countDownLatch.countDown();

    }
    public void incTotal()
    {
        Diary.getInstance().incrementTotal();

    }
}
