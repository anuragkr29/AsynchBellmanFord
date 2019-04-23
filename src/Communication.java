/**
 * @author Anurag Kumar
 */

import java.util.ArrayList;
import java.util.HashMap;

public class Communication {
    public static HashMap<Integer,Process> processUIDMap;
    private static int[] UIDs;

    /**
     *@param h is the hashmap with process thread reference
     *@param UIDs array mapping of index to UID of processes
     */
    public  Communication(HashMap<Integer,Process> h, int[] UIDs) {
        processUIDMap = h;
        this.UIDs = UIDs;
    }

    /**
     *@param m message to be sent
     *@param neighbors list of neighbors to send the message to
     */
    public static void sendMessage(Message m, ArrayList<Integer> neighbors){
        for (int n:neighbors) {
            Process neighbor = processUIDMap.get(UIDs[n]);
            send(m,neighbor);
        }
    }
    /**
     * Overridden method sendMessage for a single process communication
     *@param m message to be sent
     *@param neighborIndex index of neighbor to send the message to
     */
    public static void sendMessage(Message m, Integer neighborIndex){
            Process neighbor = processUIDMap.get(UIDs[neighborIndex]);
            send(m,neighbor);
    }

    /**
     *@param m message to be sent
     *@param p reference to the process thread to communicate with
     */
    private static void send(Message m , Process p){
        p.putMessage(m);
    }

}
