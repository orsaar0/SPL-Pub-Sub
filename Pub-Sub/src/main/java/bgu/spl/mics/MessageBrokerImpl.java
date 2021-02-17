package bgu.spl.mics;

import bgu.spl.mics.application.messages.TerminateBroadcast;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
    private static ConcurrentHashMap<Event, Future> eventFuture = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Subscriber, BlockingDeque<Message>> subscriberToDo = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Class<?>, ConcurrentLinkedQueue<Subscriber>> messageSubscribeQ = new ConcurrentHashMap<>();

    /**
     * SingleTone
     */
    private static class SingletonHolder {
        private static MessageBroker instance = new MessageBrokerImpl();
    }

    /**
     * Private constructor
     */
    private MessageBrokerImpl() {
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBroker getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
//        add the subscriber to the EventMessage type Q
        messageSubscribeQ.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        messageSubscribeQ.get(type).add(m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
//        add the subscriber to the BroadcastMessage type Q
        messageSubscribeQ.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        messageSubscribeQ.get(type).add(m);
    }

    // TODO check this warning
    @Override
    public <T> void complete(Event<T> e, T result) {
        eventFuture.get(e).resolve(result);
    }

    // TODO check the sync needed here
    @Override
    public void sendBroadcast(Broadcast b) {
        if (!messageSubscribeQ.get(b.getClass()).isEmpty()) {//if there is such broadcast in map
            for (Subscriber s : messageSubscribeQ.get(b.getClass())) {
                
                    if (b.getClass() == TerminateBroadcast.class) {
                        subscriberToDo.get(s).addFirst(b);
                    } else subscriberToDo.get(s).add(b);
                }
            
        }
    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        ConcurrentLinkedQueue<Subscriber> q = messageSubscribeQ.get(e.getClass());
        if (q == null) {
            return null;
        }
        synchronized (q) {
            if (q.isEmpty()) {//no subscribers in q
                return null;
            } else {
                Future<T> future = new Future<>();
                eventFuture.putIfAbsent(e, future);

                Subscriber active = q.peek();
                if (active == null) {
                    return null;
                }
                synchronized (active) {
                    active = q.poll();
                    if (active != null && subscriberToDo.get(active) != null) {
                        subscriberToDo.get(active).add(e);
                        q.add(active);
                    } else return null;
                }
                return future;
            }
        }
    }

    @Override
    public void register(Subscriber m) {
        subscriberToDo.putIfAbsent(m, new LinkedBlockingDeque<>());
    }

    @Override
    public void unregister(Subscriber m) {
        synchronized (m) {//todo change this
            for (Message e : subscriberToDo.get(m)) {
                if (eventFuture.get(e) != null) //if e is tickBroadcast it has no future
                    eventFuture.get(e).resolve(null);
                //     if (m.getClass() == M.class & e.getClass() == MissionReceivedEvent.class)
                //       ((M) m).incTotal();
            }
            subscriberToDo.remove(m);
        }

        for (ConcurrentLinkedQueue<Subscriber> messSubQ : messageSubscribeQ.values()) {
            synchronized (messSubQ) {
                messSubQ.remove(m);
            }
        }
    }

    @Override
    public Message awaitMessage(Subscriber m) throws InterruptedException {
        return subscriberToDo.get(m).take();
    }
}


