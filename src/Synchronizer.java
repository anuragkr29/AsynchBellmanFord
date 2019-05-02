/**
 * @author Anurag Kumar
 */

/**
 * This class acts as a synchronizer on top of the asynchronous network and ensures that after all the delays, messages
 * are finally delivered to all the processes sent in a synchronizer's round
 */
public class Synchronizer {

    /**
     * timeUnit - a reference of the Round class to be used to control rounds
     * numberOfRounds - acts as time Units for delays
     * numProcesses - total number of processes
     * root - reference to root process to check if it is complete
     */
    public Round timeUnit;
    private int numberOfRounds = 15;
    private int numProcesses ;
    private Process root;

    public Synchronizer(int numProcesses, Process p, Round r) {
        this.timeUnit = r;
        this.numProcesses = numProcesses;
        this.root = p;
    }

    /**
     * The method to run the Asynchronous Bellman Ford Algorithms
     * @return void
     */
    public void runBellmanFord() {
        int synchRound = 0;
        while(!this.root.isProcessCompleted())
        {
            System.out.println("---------Starting synchronizer round : " + synchRound + " ----------------------------");
            startRound(synchRound);
            synchRound++;

        }
        this.timeUnit.setStopAllThreads(true);
    }
    /**
     * The method helps the above method to change rounds based on synchronizer rounds
     * Two types of rounds - RoundType.synchronizer which is the synchronizer round and RoundType.timeUnit to produce
     * delays for given time Units where the round number is the value of the delay time unit
     * @return void
     */
    private void startRound(int sRound){
        int round = 0;
        Round.synchronizergGlobalRoundNumber.set(sRound);
        while (true){
            if(this.timeUnit.getThreadCount()==0){
                this.timeUnit.nextRound(this.numProcesses, round, Round.RoundType.synchronizer);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (this.timeUnit.getThreadCount()!=0);
                break;
            }
        }
        while (round <= numberOfRounds - 1) {
            try {
                if (this.timeUnit.threadCount.get() == 0) {
                    this.timeUnit.nextRound(this.numProcesses, round, Round.RoundType.timeUnit);
                    Thread.sleep(1000);
                    round++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("-----------synchronizer round finished-------------------------------");
    }
}
