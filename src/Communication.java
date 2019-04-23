import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Communication {

    private static Process[] processMap;
    private static int messageDropNum;
    private static int numberOfProcesses;
    private static AtomicIntegerArray messageCounter;
    private static AtomicInteger messageNum = new AtomicInteger(0);
    
    
    public Communication(Process[] processMap, int numberOfProcesses)
    {
        this.processMap = processMap;
        this.numberOfProcesses = numberOfProcesses;
//        this.messageCounter = new AtomicIntegerArray(numberOfProcesses);
    }




    public static synchronized void sendMessage(Message m,int senderIndex){

        int globalRoundNum = Round.getGlobalRoundNumber();
        if(globalRoundNum == 0)
        {
            messageNum.set(messageCounter.get(senderIndex) + (((numberOfProcesses-1))*senderIndex) + (globalRoundNum*(numberOfProcesses-1)*(numberOfProcesses)));
        }
        else
        {
            messageNum.set(messageCounter.get(senderIndex) + ((numberOfProcesses-1)*(numberOfProcesses-1)));
        }
        messageCounter.set(senderIndex,messageNum.get());
        int i=0;
        for(i=0;i<numberOfProcesses;i++)
        {
            if(i == senderIndex)
                continue;
            else if(messageNum.get()%messageDropNum != 0) {
                Process receiver = processMap[i];
                send(m, receiver);
            }
            else
                System.out.println("Dropping Message Number : " + messageNum.get());
            messageCounter.getAndIncrement(senderIndex);
            messageNum.set(messageCounter.get(senderIndex));
        }
    }


    private static void send(Message m , Process p){
        p.putMessage(m);
    }


}
