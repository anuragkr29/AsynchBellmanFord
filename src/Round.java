
/**
 * @author Anurag Kumar
 */

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

class Round {
    /**
     * This class manages the next round and helps the master thread to change rounds
     */
    public static AtomicIntegerArray round ;
    public static AtomicInteger globalRoundNumber = new AtomicInteger(0);
    public static AtomicInteger threadCount= new AtomicInteger(0) ;
    private static AtomicBoolean stopAllThreads = new AtomicBoolean(false);

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

    public Round(int number) {
        round = new AtomicIntegerArray(number);
    }

    /**
     * This helps to announce the next round to the threads
     * @param number integer value - here 100 is a default value that kickoffs the thread
     * @param roundNumber the round number
     */
    public  void nextRound(int number, int roundNumber) {
        globalRoundNumber.set(roundNumber);
        threadCount.set(number);
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
    public static int update(int index, int updatedValue) {
        threadCount.decrementAndGet();
        return round.getAndSet(index,updatedValue);
    }

    public int getVal(int index){
        return round.get(index);
    }

    public int getThreadCount(){
        return threadCount.get();
    }


    public static int getGlobalRoundNumber() {
        return globalRoundNumber.get();
    }
}
