package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {

    private static Inventory inventory;
    private static M[] m;
    private static Moneypenny[] moneypenny;
    private static Intelligence[] intelligences;
    private static Squad squad;
    private static TimeService timeService;
    private static CountDownLatch countDownLatch;

    public static void main(String[] args) {
///users/studs/bsc/2020/saaro/Downloads/inputFile.json
        parse(args[0]);
        List<Thread> threads = new LinkedList<>();
        int i = 1;
        for (Intelligence intelligence : intelligences) {
            Thread _intelligence = new Thread(intelligence);
            _intelligence.setName("Intelligence  " + (i));
            i++;
            threads.add(_intelligence);
            _intelligence.start();
        }
        i = 1;
        for (M m_ : m) {
            Thread _m = new Thread(m_);
            _m.setName("M  " + (i));
            i++;
            threads.add(_m);
            _m.start();
        }
        i = 1;
        for (Moneypenny mp : moneypenny) {
            Thread _moneypenny = new Thread(mp);
            _moneypenny.setName("Moneypenny  " + (i));
            i++;
            threads.add(_moneypenny);
            _moneypenny.start();
        }

        Thread _q = new Thread(new Q(countDownLatch));
        _q.setName("Q");
        threads.add(_q);
        _q.start();

        Thread _timeService = new Thread(timeService);
        _timeService.setName("TimeService");

        try {
            countDownLatch.await();
        }
        catch (InterruptedException e){
            System.out.println("CDL ex");
        }
        _timeService.start();
        // wait for all to finish before terminate
        joinThreads(threads);

        Inventory.getInstance().printToFile(args[1]);//"/users/studs/bsc/2020/saaro/Downloads/assignment2/inventory.json"
        Diary.getInstance().printToFile(args[2]);//"/users/studs/bsc/2020/saaro/Downloads/assignment2/diary.json"

    }

    public static void joinThreads(List<Thread> threads) {
        for (Thread t : threads) {
            try {
                t.join();
            } catch (Exception e) {
                System.out.println("EXCEPTION CAUGHT");
            }
        }
    }

    public static void parse(String s) {
        Gson gson = new Gson();
        Agency json = null;

        try {
            json = gson.fromJson(new FileReader(s), Agency.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        countDownLatch = new CountDownLatch(json.services.intelligence.length + json.services.Moneypenny + json.services.M + 1);

        //------------------create subscribers/publishers------------------------------------

        //Create timeservice
        timeService = new TimeService(json.services.time);

        // Create intelligence
        intelligences = new Intelligence[json.services.intelligence.length];
        int index = 0;
        for (Agency.IntelligenceSource _intelligence : json.services.intelligence) {
            List<MissionInfo> missions = new LinkedList<>();
            for (Agency.MissionInfo mission : _intelligence.missions) {
                MissionInfo missionInfo = new MissionInfo(mission.name, mission.serialAgentsNumbers, mission.gadget, mission.timeIssued, mission.timeExpired, mission.duration);
                missions.add(missionInfo);
            }
            intelligences[index] = new Intelligence(missions, countDownLatch);
            index++;
        }

        // Create Inventory
        inventory = Inventory.getInstance();
        inventory.load(json.inventory);

        // Create Squad
        Agent[] agents = new Agent[json.squad.length];
        for (int i = 0; i < json.squad.length; i++) {
            agents[i] = new Agent();
            agents[i].setName(json.squad[i].name);
            agents[i].setSerialNumber(json.squad[i].serialNumber);
        }
        squad = Squad.getInstance();
        squad.load(agents);

        // Create Moneypenny
        moneypenny = new Moneypenny[json.services.Moneypenny];
        for (int i = 1; i <= json.services.Moneypenny; i++) {
            moneypenny[i - 1] = new Moneypenny(i, countDownLatch);
        }

        // Create M
        m = new M[json.services.M];
        for (int i = 1; i <= json.services.M; i++) {
            m[i - 1] = new M(i, countDownLatch);
        }

    }
}
