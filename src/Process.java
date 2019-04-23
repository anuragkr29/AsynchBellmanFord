import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Process implements Runnable {

    private BlockingQueue<Message> queue;
    private int index;
    private int decision = -1;
    private int key;
    private int numberOfprocesses;
    private int numberOfRounds;
    public boolean decisionDone = false;
    private int dist;
    private int parentUID;
    private ArrayList<Edge> neighbors;

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public Process(int index, int numberOfprocesses, ArrayList<Edge> neighbors, int dist, int parentUID) {
        this.index = index;
        this.numberOfprocesses = numberOfprocesses;
        this.key = -1;
        this.neighbors = neighbors;
        this.queue =  new ArrayBlockingQueue<>(numberOfprocesses*10);
        this.dist = dist;
        this.parentUID = parentUID;
    }

    public void putMessage(Message m){
        queue.add(m);
    }

    public void processQueue() {
        try{
           if(!this.queue.isEmpty()) {
               while (!this.queue.isEmpty()) {
                   Message m = this.queue.take();

               }
               }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        }

//        public void processDecision() {
//            Optional<Integer> rowResult = this.inputVal.stream().reduce((a, b) -> (a & b));
//            this.decision = (this.key != -1 && this.level.get(this.index) >= this.key && rowResult.get() == 1) ? 1 : 0;
//            this.decisionDone = true;
//        }

    @Override
    public void run() {
        Thread.currentThread().setName("Process : " + this.index);
        while (!this.decisionDone) {

            if (Round.round.get(this.index) == 100) {
                int globalRoundNum = Round.getGlobalRoundNumber();
                try {

                        if (globalRoundNum == 0 && this.index == 0) {
                            Random rand = new Random();
                            key = rand.nextInt(numberOfRounds-1) + 1;
                        } else if (globalRoundNum != 0) {
                            this.processQueue();
                        }


                        Message<Integer> toSend = new Message<>(this.dist, this.index);
                        Thread.sleep(1000);

                        Communication.sendMessage(toSend, this.index);

                        if(globalRoundNum == numberOfRounds-1) {
//                            this.processDecision();
                        }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Round.update(this.index, 0);
            }
        }
        System.out.println("Shutting down " +  Thread.currentThread().getName());
    }

}