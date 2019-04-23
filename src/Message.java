/**
 * @author Anurag Kumar
 */


public class Message {
    private boolean parent=false;
    private int inUID;
    private int process_index;
    private boolean search=false;
    private boolean root;
    private int roundNum = 0;
    private boolean reject = false;
    private int level=0;
    private boolean done = false;

    public Message(){}

    public Message(boolean parent, int inUID, boolean search, int process_index, int level) {
        this.parent = parent;
        this.inUID = inUID;
        this.search = search;
        this.process_index = process_index;
        this.level = level;
    }
    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }

    public int getRoundNum() {
        return roundNum;
    }

    public int getProcess_index() {
        return process_index;
    }

    public boolean isReject() {
        return reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public int getInUID() {
        return inUID;
    }

    public void setInUID(int inUID) {
        this.inUID = inUID;
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }
}
