package bgu.spl.mics.application;

import java.util.List;


public class Agency {
    public String[] inventory;
    public Services services;
    public Agent[] squad;

    public class Services{
        public int M;
        public int Moneypenny;
        public IntelligenceSource[] intelligence;
        public int time;
    }

    public class IntelligenceSource {
        public MissionInfo [] missions;
    }

    public class MissionInfo{
        public List<String> serialAgentsNumbers;
        public int duration;
        public String gadget;
        public String name;
        public int timeExpired;
        public int timeIssued;
    }

    public class Agent {
        public String name;
        public String serialNumber;
    }

}