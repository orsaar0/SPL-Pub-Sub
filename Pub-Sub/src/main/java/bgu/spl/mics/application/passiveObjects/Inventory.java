package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
    private List<String> gadgets;

    /**
     * Private constructor
     */
    private Inventory() {
        this.gadgets = new LinkedList<>();
    }

    /**
     * SingleTone
     */
    private static class SingleToneHolder {
        private static Inventory instance = new Inventory();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Inventory getInstance() {
        return SingleToneHolder.instance;
    }

    /**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     *
     * @param inventory Data structure containing all data necessary for initialization
     *                  of the inventory.
     */
    public void load(String[] inventory) {
        gadgets.addAll(Arrays.asList(inventory));
    }

    /**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     *
     * @param gadget Name of the gadget to check if available
     * @return ‘false’ if the gadget is missing, and ‘true’ otherwise
     */
    public synchronized boolean getItem(String gadget) {
        if (!gadgets.contains(gadget)) return false;
        else {
            gadgets.remove(gadget);
            return true;
        }
    }

    /**
     * <p>
     * Prints to a file name @filename a serialized object List<String> which is a
     * list of all the of the gadgeds.
     * This method is called by the main method in order to generate the output.
     */
    public synchronized void printToFile(String filename) {
        Gson gson = new Gson();
        String jsonRepresentation = gson.toJson(gadgets);
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(jsonRepresentation);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
