package bgu.spl.mics.application.passiveObjects;

import java.util.*;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

    private Map<String, Agent> agents;

    /**
     * Private constructor
     */
    private Squad() {
        this.agents = new HashMap<String, Agent>();
    }

    /**
     * SingleTone
     */
    private static class SingletoneHolder {
        private static Squad instance = new Squad();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Squad getInstance() {
        return SingletoneHolder.instance;
    }

    /**
     * Initializes the squad. This method adds all the agents to the squad.
     * <p>
     *
     * @param agents Data structure containing all data necessary for initialization
     *               of the squad.
     */
    public void load(Agent[] agents) {
        for (Agent a : agents) {
            // if absent. doesnt need sync
            this.agents.putIfAbsent(a.getSerialNumber(), a);
        }
    }

    /**
     * Releases agents.
     */
    public void releaseAgents(List<String> serials) {
        for (String num : serials) {
            if (agents.containsKey(num)) {
                synchronized (agents.get(num)) {
                    agents.get(num).release();
                    agents.get(num).notifyAll();

                }
            }
        }
    }

    /**
     * simulates executing a mission by calling sleep.
     *
     * @param time time ticks to sleep
     */
    // sendAgents needs to be sync because the agents avaib
    public void sendAgents(List<String> serials, int time)  {
        //if (getAgents(serials)) {
        try {
            Thread.sleep(time * 100);
        }
        catch (InterruptedException i){
            System.out.println("Exception caught");
        }

        for (String s : serials)
        releaseAgents(serials);
        //}
    }

    /**
     * acquires an agent, i.e. holds the agent until the caller is done with it
     *
     * @param serials the serial numbers of the agents
     * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
     */
    // get agents is called only from sendAgents which is sync
    public boolean getAgents(List<String> serials) {
        Collections.sort(serials);
        while (true) {
            LinkedList<String> caughtAgents = new LinkedList<>();
            int i = 0;
            while (i < serials.size()) {
                if (!agents.containsKey(serials.get(i))) {
                    releaseAgents(caughtAgents);
                    return false;
                }
                // only one MP1 is alowed to be here
                synchronized (agents.get((serials.get(i)))) {
                    while (!agents.get(serials.get(i)).isAvailable()) {
                        releaseAgents(caughtAgents);
                        try {
                            agents.get((serials.get(i))).wait();
                        } catch (InterruptedException e) {
                            System.out.println("Exception caught getAgents");
                        }
                        i = 0;
                    }
                }
                agents.get(serials.get(i)).acquire();
                caughtAgents.add(serials.get(i));
                i++;
                if (serials.size() == caughtAgents.size()) {
                    return true;
                }
            }
        }
    }



    /**
     * gets the agents names
     *
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials) {
        LinkedList<String> names = new LinkedList<>();
        for (String num : serials) {
            names.add(agents.get(num).getName());
        }
        return names;
    }

}