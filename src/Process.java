/**
 * @author Anurag Kumar
 */

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * This is a data structure which helps in bookkeeping to help convergecast
 */
class Node {
    private int UID;
    private boolean ack;

    public Node(int UID, boolean ack) {
        this.UID = UID;
        this.ack = ack;
    }

    public boolean isAck() {
        return ack;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }

    public int getUID() {
        return UID;
    }
}

class Process implements Runnable {

    private BlockingQueue<Message> queue;
    Comparator<Message<Integer>> comparator = new MessageComparator();
    private PriorityQueue<Message<Integer>> sendQ = new PriorityQueue<Message<Integer>>(100, comparator);
    private int index;
    private boolean distChanged = false;
    private int numberOfprocesses;
    private int dist;
    private int parentUID = -10;
    private int rootUID;
    private ArrayList<Edge> neighbors;
    private ArrayList<Node> childrens;
    private boolean processCompleted = false;
    private ArrayList<Integer> receiversInCurrentRound = new ArrayList<>(100);

    public void setDist(int dist) {
        this.dist = dist;
    }

    public boolean isProcessCompleted() {
        return processCompleted;
    }

    public class MessageComparator implements Comparator<Message<Integer>> {
        @Override
        public int compare(Message<Integer> x, Message<Integer> y) {
            if (x.getRoundNum() < y.getRoundNum()) {
                return -1;
            } else if (x.getRoundNum() > y.getRoundNum()) {
                return 1;
            }
            return 0;
        }
    }

    public Process(int index, int numberOfprocesses, ArrayList<Edge> neighbors, int dist, int rootUID) {
        this.index = index;
        this.numberOfprocesses = numberOfprocesses;
        this.neighbors = neighbors;
        this.queue = new ArrayBlockingQueue<>(numberOfprocesses * 10);
        this.dist = dist;
        this.rootUID = rootUID;
        this.childrens = new ArrayList<>(numberOfprocesses);
    }

    private int getEdgeWeight(int UID) {
        for (Edge e : neighbors) {
            if (e.getUID() == UID) {
                return e.weight;
            }
        }
        return Integer.MAX_VALUE;
    }

    public void putMessage(Message m) {
        queue.add(m);
    }

    private void setResetUID(int UID, boolean flag) {
        for (int i = 0; i < this.childrens.size(); i++) {
            Node n = this.childrens.get(i);
            if (n.getUID() == UID) {
                n.setAck(flag);
            }
        }
    }

    private synchronized void removeNode(int UID) {
        for (int i = 0; i < this.childrens.size(); i++) {
            Node n = this.childrens.get(i);
            if (n.getUID() == UID) {
                this.childrens.remove(n);
            }
        }
    }
    /**
     * This function helps to check if the current process is done (for convergecast)
     * @return boolean value which is true or false
     */
    private boolean isDone() {
        for (Node n : this.childrens) {
            if (!n.isAck()) {
                return false;
            }
        }
        return true;
    }

    private void checkDone() throws InterruptedException {
        if (this.isDone()) {
            if (this.index == this.rootUID) {
                this.processCompleted = true;
            } else {
                this.processCompleted = true;
                if (this.parentUID != -10) {
                    enqueueMessage(this.parentUID, -1, this.index, Message.Type.Ack);
                }
            }
        }
    }
    /**
     * This class processes the message Queue i.e. the received messages are processed once received by a process
     * @return void
     */

    public void processQueue() {
        try {
            if (!this.queue.isEmpty()) {
                while (!this.queue.isEmpty()) {
                    Message m = this.queue.take();
                    switch (m.getType()) {
                        case Relax:
                            int edgeWeight = getEdgeWeight((int) m.getFrom_UID());
                            if (edgeWeight == Integer.MAX_VALUE) {
                                continue;
                            }
                            int newDist = (int) m.getCurrentDist() + edgeWeight;
                            if (newDist < this.dist) {
                                this.dist = newDist;
                                System.out.println("distance updated : " + newDist + " thread : " + Thread.currentThread().getName());
                                if (this.parentUID != -10) {
                                    enqueueMessage(this.parentUID, -1, this.index, Message.Type.NAck, this.parentUID);
                                }
                                this.parentUID = (int) m.getFrom_UID();
                                this.distChanged = true;
                                this.childrens.clear();
                                for (Edge e : neighbors) {
                                    enqueueMessage(e.getUID(), dist, index, Message.Type.Relax);
                                    this.childrens.add(new Node(e.getUID(), false));
                                }
                            } else {
                                enqueueMessage((int) m.getFrom_UID(), -1, this.index, Message.Type.NAck);
                            }
                            break;
                        case Ack:
                            this.setResetUID((int) m.getFrom_UID(), true);
                            this.checkDone();
                            break;
                        case NAck:
                            if (m.getOldParentId() != null) {
                                if (this.index == (int) m.getOldParentId()) {
                                    this.removeNode((int) m.getFrom_UID());
                                }
                            } else {
                                this.setResetUID((int) m.getFrom_UID(), true);
                            }
                            this.checkDone();
                            break;
                        case Dummy:
                            System.out.println("Dummy");
                            break;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * This class processes the send Queue for sending the messages to the neighbors which fires after a given time Unit
     * delay
     * @return void
     */
    public void processSendQ() {
        try {
            if (!this.sendQ.isEmpty()) {
                while (!this.sendQ.isEmpty()) {
                    Message m = this.sendQ.peek();
                    if ((int) m.getRoundNum() == Round.getGlobalRoundNumber()) {
                        Communication.sendMessage(m);
                        this.sendQ.remove();
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }


    @Override
    public void run() {
        Thread.currentThread().setName("Process : " + this.index);
        System.out.println("Starting " + Thread.currentThread().getName());
        while (true) {
            if (Round.getRound(this.index) == 100) {
                if (Round.getrType() == Round.RoundType.synchronizer) {
                    try {
                        if (this.index == this.rootUID && Round.synchronizergGlobalRoundNumber.get() == 0) {
                            System.out.println("root here");
                            for (Edge e : neighbors) {
                                enqueueMessage(e.getUID(), dist, index, Message.Type.Relax);
                                this.childrens.add(new Node(e.getUID(), false));
                            }
                        }
                        this.processQueue();
                        Thread.sleep(1000);
                        if (!this.distChanged){
                            for (Edge e : neighbors) {
                                if(!this.receiversInCurrentRound.contains(e.getUID())) {
                                    enqueueMessage(e.getUID(), dist, index, Message.Type.Dummy);
                                }
                            }
                            this.receiversInCurrentRound.clear();
                        }
                        this.distChanged = false;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (Round.getrType() == Round.RoundType.timeUnit) {
                    this.processSendQ();
                }
                Round.update(this.index, 0);
            }
            if (Round.getStopAllThreads()) {
                System.out.println("Shutting down " + Thread.currentThread().getName() + " with final distance : " + this.dist);
                break;
            }
        }
    }

    private void enqueueMessage(int receiverUID, int dist, int index, Message.Type type) throws InterruptedException {

        Random rand = new Random();
        this.receiversInCurrentRound.add(receiverUID);
        int randRoundNum = rand.nextInt(14) + 1;
        System.out.println("Putting in sendQ for thread : " + receiverUID + " random num : " + randRoundNum + " from : " + this.index + " type :" + type);
        Message<Integer> toSend = new Message<>(dist, index, receiverUID, randRoundNum, type);
        Thread.sleep(1000);
        this.sendQ.add(toSend);

    }

    private void enqueueMessage(int receiverUID, int dist, int index, Message.Type type, int oldParent) throws InterruptedException {

        Random rand = new Random();
        this.receiversInCurrentRound.add(receiverUID);
        int randRoundNum = rand.nextInt(14) + 1;
        System.out.println("Putting in sendQ for oldparent: " + receiverUID + " random num : " + randRoundNum + " from : " + this.index + " type :" + type);
        Message<Integer> toSend = new Message<>(dist, index, receiverUID, randRoundNum, type, oldParent);
        Thread.sleep(1000);
        this.sendQ.add(toSend);

    }

}