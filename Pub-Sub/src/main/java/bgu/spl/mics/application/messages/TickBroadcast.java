package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast<T> implements Broadcast {
    private int time;
    private int terminate;

    public TickBroadcast (int tick, int terminate){
        time = tick;
        this.terminate = terminate;
    }

    public int getTime(){
        return time;
    }
    public int getTerminate() {
        return terminate;
    }
}
