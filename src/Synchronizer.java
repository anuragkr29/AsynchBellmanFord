public class Synchronizer {

    public Round timeUnit;
    private int numberOfRounds = 15;
    private int numProcesses ;
    private Process root;

    public Synchronizer(int numProcesses, Process p, Round r) {
        this.timeUnit = r;
        this.numProcesses = numProcesses;
        this.root = p;
    }

    public void runBellmanFord() {
        int synchRound = 0;
        while(!this.root.isProcessCompleted())
        {
            System.out.println("--------------------------Started synchronizer round : " + (synchRound));
            startRound(synchRound);
            synchRound++;

        }
        this.timeUnit.setStopAllThreads(true);
    }

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
        System.out.println("---------Starting synchronizer round : " + sRound + " ---------------");
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
