
/**
 * @author Anurag Kumar
 */

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * This class manages the next round and helps the Syncronizer to change rounds
 * also helps to generate delays based on time units
 */
public class Round {
    /**
     * round - which helps process threads to start a round based on their index
     * globalRoundNumber - current round number which is available to all processes to decide when to send the message
     * threadCount - total number of threads to be run - this helps to terminate a round which changes based on a process completion
     * synchronizergGlobalRoundNumber - the synchronizer global round number
     * stopAllThreads - a boolean value used to stop all the running threads when the process is complete
     * rType - type of round i.e. a synchronizer round or the timeUnit round
     */
    public static AtomicIntegerArray round ;
    public static AtomicInteger globalRoundNumber = new AtomicInteger(0);
    public static AtomicInteger threadCount= new AtomicInteger(0) ;
    public static AtomicInteger synchronizergGlobalRoundNumber= new AtomicInteger(0) ;
    private static AtomicBoolean stopAllThreads = new AtomicBoolean(false);
    public enum RoundType {
        synchronizer, timeUnit;
    };
    public static RoundType rType;
    public Round(int number) {
        round = new AtomicIntegerArray(number);
    }

    /**
     * This helps to announce the next round to the threads
     * @param number integer value - here 100 is a default value that kickoffs the thread
     * @param roundNumber the round number
     */
    public  void nextRound(int number, int roundNumber, RoundType type) {
        globalRoundNumber.set(roundNumber);
        threadCount.set(number);
        Round.setrType(type);
        for(int i=0; i<number;i++){
            round.set(i,100);
        }
    }

    /**
     * updates the threadcount i.e. the number of threads in the graph to keep a count
     * @param index index which a thread looks to start the its next round - each index is associated with a thread
     * @param updatedValue the value to be updated after the thread is done with the current round
     * @return the round number before the update
     */
    public static synchronized int update(int index, int updatedValue) {
        threadCount.decrementAndGet();
        return round.getAndSet(index,updatedValue);
    }

    public int getThreadCount(){
        return threadCount.get();
    }

    public static synchronized int getRound(int index) {
        return round.get(index);
    }

    public static int getGlobalRoundNumber() {
        return globalRoundNumber.get();
    }

    public static RoundType getrType() {
        return rType;
    }

    public static void setrType(RoundType rType) {
        Round.rType = rType;
    }
    /**
     * function to stop all threads
     * @param state boolean value : true sets the stopAllthreads
     */
    public void setStopAllThreads(boolean state) {
        this.stopAllThreads.set(state);
    }

    public static boolean getStopAllThreads(){
        return stopAllThreads.get();
    }
}

