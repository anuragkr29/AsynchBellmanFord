public class Synchronizer {


    private int numberOfProcesses;
    private Process root;



    public static int getSynchRound()
    {
        return round;
    }

    private static int round;

    public Synchronizer(int numOfProcesses, Process root) {
        this.numberOfProcesses = numOfProcesses;
        this.root = root;
    }

    public void start() {
        Round r = new Round(numberOfProcesses);
        round = 0;

        while (!root.isProcessCompleted()) {
            try{
                if (Round.threadCount.get() == 0) {
                    round++;
                    if (round == 1) {
//                    Message m = new Message();
//                    m.setRoot(true);
//                    m.setRoundNum(1);
//                    m.setLevel(1);
//                    root.putMessage(m);
                    }
                    System.out.println("Waiting for Input");
                    Thread.currentThread().sleep(1000);
                    r.nextRound(numberOfProcesses);
                    System.out.println("Started round : " + (round));
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}