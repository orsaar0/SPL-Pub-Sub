package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

public class MissionReceivedEvent<Boolean> implements Event<Boolean> {
    private List<String> AgentsSerials;
    private String gadget;
    private int expiryTime;
    private int duration;
    private int timeIssued;
    private String missionName;
    private MissionInfo missionInfo;

    public MissionReceivedEvent(MissionInfo info) {
        this.AgentsSerials = info.getSerialAgentsNumbers();
        this.gadget = info.getGadget();
        this.expiryTime = info.getTimeExpired();
        this.duration = info.getDuration();
        this.timeIssued = info.getTimeIssued();
        this.missionName = info.getMissionName();
        this.missionInfo=info;
    }

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public List<String> getAgentsSerials() {
        return AgentsSerials;
    }

    public String getGadget() {
        return gadget;
    }

    public int getExpiryTime() {
        return expiryTime;
    }

    public int getDuration() {
        return duration;
    }

    public int getTimeIssued() {
        return timeIssued;
    }

    public String getMissionName() {
        return missionName;
    }
}
