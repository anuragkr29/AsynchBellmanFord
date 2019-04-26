/**
 * @author Anurag Kumar
 */

/**
 * This class defines the components required in a message
 */
public class Message<T> {

    /**
     * currentDist - distance of the sender node to the receiver node
     * from_UID - the sender's UID
     * to_UID - the receiver UID
     * roundNum - the round number in which the message will be sent (used as time unit for delays)
     * oldParentId - optional - sent only when a node changes its parent if it gets a better distance
     * type - the type of message - based on the enum Type (Ack, NAck, Dummy, Relax)
     */
    private T currentDist;
    private T from_UID;
    private T to_UID;
    private T roundNum;
    private T oldParentId;

    public enum Type {
        Ack, NAck, Dummy, Relax;
    };

    private Type type;

    public Type getType() {
        return type;
    }

    public T getCurrentDist() {
        return currentDist;
    }

    public T getFrom_UID() {
        return from_UID;
    }

    public T getTo_UID() {
        return to_UID;
    }

    public T getRoundNum() {
        return roundNum;
    }

    public Message(T dist, T fromUID, T toUID, T roundNum, Type type) {
        this.currentDist = dist;
        this.from_UID = fromUID;
        this.to_UID = toUID;
        this.roundNum = roundNum;
        this.type = type;
    }

    public T getOldParentId() {
        return oldParentId;
    }

    public Message(T dist, T fromUID, T toUID, T roundNum, Type type, T id) {
        this.currentDist = dist;
        this.from_UID = fromUID;
        this.to_UID = toUID;
        this.roundNum = roundNum;
        this.type = type;
        this.oldParentId = id;
    }

}
