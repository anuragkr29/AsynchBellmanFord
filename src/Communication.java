/**
 * @author Anurag Kumar
 */

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class helps processes communicate among each other
 */
public class Communication {

    // processMap is a mapping of the process references based on UID as array index
    private static Process[] processMap;
    private static AtomicInteger numberOfMessages = new AtomicInteger(0);


    public Communication(Process[] processMap) {
        this.processMap = processMap;
    }

    public static int getNumberOfMessages() {
        return numberOfMessages.get();
    }

    /**
     * A synchronized method used by threads to send a message
     * @param m a message to be sent containing the details of the message
     * @return void
     */
    public static synchronized void sendMessage(Message m) {
        int receiverIndex = (int) m.getTo_UID();
        Process neighbor = processMap[receiverIndex];
        System.out.println("sending message from " + m.getFrom_UID() + " to : " + m.getTo_UID() + " type : " + m.getType() + " dist : " + m.getCurrentDist());
        if (neighbor != null) {
            send(m, neighbor);
        }
    }

    /**
     * A private method to send a single message to a given process
     * @param m a message to be sent containing the details of the message
     * @param p the reference of the receiver process
     * @return void
     */
    private static void send(Message m, Process p) {
        p.putMessage(m);
        numberOfMessages.incrementAndGet();
    }


}
