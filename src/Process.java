/**
 * @author Anurag Kumar
 */

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Process implements Runnable{

    private int UID;
    private int rootUID;
    private int parentIndex;
    private boolean marked;
    private ArrayList<Integer> neighbors;
    private ArrayList<Integer> children;
    private int process_index;
    private BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);
    private int level;
    private boolean leaf = false;
    private int count = 0;
    private boolean processCompleted = false;
    private int doneCount = 0;

    public Process(int UID, boolean is_marked, ArrayList<Integer> neighbors, int process_index, int rootUID) {
        this.UID = UID;
        this.marked = is_marked;
        this.neighbors = neighbors;
        this.children = new ArrayList<>();
        this.process_index = process_index;
        this.rootUID = rootUID;
        this.parentIndex = rootUID;
    }
    public void putMessage(Message m){
        queue.add(m);
    }
    public boolean is_marked() {
        return marked;
    }

    public void set_marked(boolean is_marked) {
        this.marked = is_marked;
    }

    public ArrayList<Integer> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<Integer> neighbors) {
        this.neighbors = neighbors;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public int getProcess_index() {
        return process_index;
    }

    public boolean isProcessCompleted() {
        return processCompleted;
    }

    public void setProcess_index(int process_index) {
        this.process_index = process_index;
    }

    /**
     * This function checks a message in the queue and adds to the child of the current node if true
     * @param m message in the queue
     */
    public void checkAndSetChild(Message m){
        if(m.isParent())
        {
            this.children.add(m.getProcess_index());
            this.queue.remove(m);
        }
    }

    /**
     * This method processes the queue associated to this process. Any message in the queue is processes here
     * @param isMyRound true if the current round is the round where the process sends messages to its neigbours
     * @return returns the parent_index of the parent of the current node
     */
    public int processQueue(boolean isMyRound){
        int maxUIDIndex = -1;
        Message maxUIDMsg = new Message(false,-1,true,-1,Integer.MAX_VALUE);
        ArrayList<Integer> nonParent = new ArrayList<Integer>();
        try {
            while(!this.queue.isEmpty())
            {
                Message m = this.queue.take();
                if(m.isSearch() && isMyRound){
                    nonParent.add(m.getProcess_index());
                    maxUIDMsg = this.compareParent(maxUIDMsg,m);
                }
                else if(m.isSearch() && !isMyRound  && this.marked){
                    Message rejectMsg = new Message();
                    rejectMsg.setReject(true);
                    rejectMsg.setInUID(this.UID);
                    Communication.sendMessage(rejectMsg,m.getProcess_index());
                }
                else if(m.isParent() && !m.isRoot()){
                    this.children.add(m.getProcess_index());
                }
                else if(m.isReject()){
                    count++;
                    if(count == this.neighbors.size())
                    {
                        this.leaf = true;
                        this.sendDone(this.parentIndex);
                    }
                }
                else if(m.isRoot()){
                    return -1;
                }
                if(m.isDone()){
                    this.doneCount++;
                    if(this.doneCount == this.children.size())
                    {
                        if(this.UID == rootUID)
                        {
                            Thread.currentThread().sleep(500);
                            this.processCompleted = true;
                            System.out.println("Process completed for : " + this.UID);
                        }
                        else
                        {
                            this.sendDone(this.parentIndex);
                            Thread.currentThread().sleep(500);
                            this.processCompleted = true;
                            System.out.println("Process completed for : " + this.UID);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        maxUIDIndex = maxUIDMsg.getProcess_index();
        if(maxUIDIndex != -1){
            this.parentIndex = maxUIDIndex;
            nonParent.remove(nonParent.indexOf(maxUIDIndex));
            this.sendReject(nonParent);
        }
        return maxUIDIndex;
    }

    /**
     * Compares two messages and decides the parent based on level.If level is same then it chooses the max UID
     * @param a first message
     * @param b second message
     * @return message who wins
     */
    public Message compareParent(Message a , Message b){
        int level_a = a.getLevel();
        int level_b = b.getLevel();
        if(level_a < level_b){
            return a;
        }
        else if(level_a > level_b){
            return b;
        }
        else{
            int max =  Math.max(a.getProcess_index(), b.getProcess_index());
            if (max == a.getProcess_index())
                return a;
            else
                return b;
        }
    }

    /**
     * helps to send reject messages to the non parents of a node
     * @param nonParent the list of nodes except the parent which sends a search message
     */
    public void sendReject(ArrayList<Integer> nonParent){
        Message rejectMsg = new Message();
        rejectMsg.setReject(true);
        rejectMsg.setInUID(this.UID);
        Communication.sendMessage(rejectMsg,nonParent);
    }

    /**
     * Tells the parent process if the current node is done working with no more tasks left
     * @param recipient the recipient (the parent) node
     */
    public void sendDone(Integer recipient){
        Message rejectMsg = new Message();
        rejectMsg.setDone(true);
        rejectMsg.setInUID(this.UID);
        Communication.sendMessage(rejectMsg,recipient);
        if(this.children.size() == 0)
        {
            this.processCompleted = true;
            System.out.println("Process completed for : " + this.UID);
        }
    }

    @Override
    public void run(){
        Thread.currentThread().setName("" + process_index);
        int parentUID = 0;
        while(true)
        {
            if(Round.round.get(this.process_index) == 100)
            {
                int globalRoundNum = Round.getGlobalRoundNumber();
                try {
                    if(!this.queue.isEmpty())
                    {
                        Message m = this.queue.peek();
                        if(globalRoundNum != m.getRoundNum())
                            this.processQueue(false);

                        if(globalRoundNum == m.getRoundNum() && !this.marked)
                        {
                            this.level = globalRoundNum;
                            parentUID = this.processQueue(true);

                            if(parentUID != -1)
                            {
                                Message toSend = new Message(true,this.UID,false,this.process_index, this.level);
                                toSend.setRoundNum(-1);
                                Communication.sendMessage(toSend,parentUID);
                            }

                                Message toSend = new Message(false,this.UID,true,this.process_index,this.level);
                                toSend.setRoundNum(globalRoundNum + 1);
                                Thread.sleep(1000);
                                Communication.sendMessage(toSend,this.neighbors);
                                this.marked = true;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Round.update(this.process_index,0);
            }

            if(Round.getStopAllThreads()){
                System.out.println("Shutting down UID : " + UID + " with children : " + this.children + " with parent : " +
                        "" + this.parentIndex + " is leaf : " + this.leaf + " Neighbor size : " + this.neighbors.size()
                        + " reject count : " + count);
                break;
            }
        }

    }
}