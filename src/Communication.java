import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Communication {

    private static Process[] processMap;
    private static int messageDropNum;
    private static int numberOfProcesses;
    
    
    public Communication(Process[] processMap, int numberOfProcesses)
    {
        this.processMap = processMap;
        this.numberOfProcesses = numberOfProcesses;
//        this.messageCounter = new AtomicIntegerArray(numberOfProcesses);
    }




    public static synchronized void sendMessage(Message m){
        int receiverIndex = (int) m.getTo_UID();
        Process neighbor = processMap[receiverIndex];
        System.out.println("sending message from " + m.getFrom_UID() + " to : " + m.getTo_UID() + " type : " + m.getType() + " dist : " + m.getCurrentDist());
        if(neighbor != null){
            send(m, neighbor);
        }
    }


    private static void send(Message m , Process p){
        p.putMessage(m);
    }


}
