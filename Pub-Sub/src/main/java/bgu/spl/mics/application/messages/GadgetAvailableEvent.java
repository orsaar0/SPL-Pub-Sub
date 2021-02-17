package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class GadgetAvailableEvent<Boolean> implements Event {
    private String gadget;
    private int currentTimeTick;


    public GadgetAvailableEvent(String gadget){
        this.gadget = gadget;
    }
    public String getGadget() {
        return gadget;
    }
    public void setCurrentTimeTick(int tick){ currentTimeTick = tick;}
    public int getCurrentTimeTick(){return currentTimeTick;}
}
