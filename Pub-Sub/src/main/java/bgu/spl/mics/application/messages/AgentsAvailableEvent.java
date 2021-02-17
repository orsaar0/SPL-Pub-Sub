package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class AgentsAvailableEvent<Boolean> implements Event {
    private List<String> serials;
    private List<String> agentsNames;
    private int serialNumMoneypenny;

    private int duration;


    public AgentsAvailableEvent(List<String> serials, int duration) {
        this.serials = serials;
        this.duration = duration;
    }

    public List<String> getSerials() {
        return serials;
    }

    public int getDuration() {
        return duration;
    }


    public void setAgentsNames(List<String> names) {
        this.agentsNames = names;
    }

    public List<String> getAgentsNames() {
        return agentsNames;
    }

    public int getSerialNumMoneypenny() {
        return serialNumMoneypenny;
    }

    public void setSerialNumMoneypenny(int serialNumMoneypenny) {
        this.serialNumMoneypenny = serialNumMoneypenny;
    }
}
