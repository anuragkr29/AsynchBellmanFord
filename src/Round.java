
/**
 * @author Anurag Kumar
 */

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Round {
    /**
     * This class manages the next round and helps the Syncronizer to change rounds
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

    public static RoundType getrType() {
        return rType;
    }

    public static void setrType(RoundType rType) {
        Round.rType = rType;
    }

    public int getSynchronizergGlobalRoundNumber() {
        return Round.synchronizergGlobalRoundNumber.get();
    }

    public void setSynchronizergGlobalRoundNumber(int synchronizergGlobalRoundNumber) {
        Round.synchronizergGlobalRoundNumber.set(synchronizergGlobalRoundNumber);
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
     * @return
     */
    public static synchronized int update(int index, int updatedValue) {
        threadCount.decrementAndGet();
        return round.getAndSet(index,updatedValue);
    }

    public int getVal(int index){
        return round.get(index);
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

