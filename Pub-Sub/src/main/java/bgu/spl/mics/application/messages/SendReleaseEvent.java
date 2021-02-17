package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class SendReleaseEvent<Boolean> implements Event {
    public boolean send;
    public List<String> serials;
    public int duration;

    public SendReleaseEvent(List<String> serials, boolean send, int duration){
        this.serials = serials;
        this.send=send;
        this.duration=duration;
    }

}
