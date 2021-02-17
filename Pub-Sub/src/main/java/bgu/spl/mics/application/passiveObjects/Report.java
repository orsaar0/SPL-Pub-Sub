package bgu.spl.mics.application.passiveObjects;

import java.util.List;

/**
 * Passive data-object representing a delivery vehicle of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Report {
    private String missionName;
    private int M;
    private int moneypenny;
    private List<String> agentsSerialNumbers;
    private List<String> agentsNames;
    private String gadgetName;
    private int timeIssued;
    private int QTime;
    private int timeCreated;

    public Report(MissionInfo info, List<String> names, int moneypenny, int M, int QTime, int timeCreated) {
        this.missionName = info.getMissionName();
        this.agentsSerialNumbers = info.getSerialAgentsNumbers();
        this.gadgetName = info.getGadget();
        this.timeIssued = info.getTimeIssued();
        this.agentsNames=names;
        this.M = M;
        this.moneypenny = moneypenny;
        this.QTime = QTime;
        this.timeCreated = timeCreated;
    }

    /**
     * Retrieves the mission name.
     */
    public String getMissionName() {
        return missionName;
    }

    /**
     * Sets the mission name.
     */
    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    /**
     * Retrieves the M's id.
     */
    public int getM() {
        return M;
    }

    /**
     * Sets the M's id.
     */
    public void setM(int m) {
        this.M = m;
    }

    /**
     * Retrieves the Moneypenny's id.
     */
    public int getMoneypenny() {
        return moneypenny;
    }

    /**
     * Sets the Moneypenny's id.
     */
    public void setMoneypenny(int moneypenny) {
        this.moneypenny = moneypenny;
    }

    /**
     * Retrieves the serial numbers of the agents.
     * <p>
     *
     * @return The serial numbers of the agents.
     */
    public List<String> getAgentsSerialNumbers() {
        return agentsSerialNumbers;
    }

    /**
     * Sets the serial numbers of the agents.
     */
    public void setAgentsSerialNumbers(List<String> agentsSerialNumbers) {
        agentsSerialNumbers.addAll(agentsSerialNumbers);
    }

    /**
     * Retrieves the agents names.
     * <p>
     *
     * @return The agents names.
     */
    public List<String> getAgentsNames() {
        return agentsNames;
    }

    /**
     * Sets the agents names.
     */
    public void setAgentsNames(List<String> agentsNames) {
        agentsNames.addAll(agentsNames);
    }

    /**
     * Retrieves the name of the gadget.
     * <p>
     *
     * @return the name of the gadget.
     */
    public String getGadgetName() {
        return gadgetName;
    }

    /**
     * Sets the name of the gadget.
     */
    public void setGadgetName(String gadgetName) {
        this.gadgetName = gadgetName;
    }

    /**
     * Retrieves the time-tick in which Q Received the GadgetAvailableEvent for that mission.
     */
    public int getQTime() {
        return QTime;
    }

    /**
     * Sets the time-tick in which Q Received the GadgetAvailableEvent for that mission.
     */
    public void setQTime(int qTime) {
        this.QTime = qTime;
    }

    /**
     * Retrieves the time when the mission was sent by an Intelligence Publisher.
     */
    public int getTimeIssued() {
        return timeIssued;
    }

    /**
     * Sets the time when the mission was sent by an Intelligence Publisher.
     */
    public void setTimeIssued(int timeIssued) {
        this.timeIssued = timeIssued;
    }

    /**
     * Retrieves the time-tick when the report has been created.
     */
    public int getTimeCreated() {
        return timeCreated;
    }

    /**
     * Sets the time-tick when the report has been created.
     */
    public void setTimeCreated(int timeCreated) {
        this.timeCreated = timeCreated;
    }
}
