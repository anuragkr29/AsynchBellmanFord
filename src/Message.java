import java.util.ArrayList;
import java.util.List;

public class Message<T> {


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

    public void setType(Type type) {
        this.type = type;
    }

    public T getCurrentDist() {
        return currentDist;
    }

    public void setCurrentDist(T currentDist) {
        this.currentDist = currentDist;
    }

    public T getFrom_UID() {
        return from_UID;
    }

    public void setFrom_UID(T from_UID) {
        this.from_UID = from_UID;
    }

    public T getTo_UID() {
        return to_UID;
    }

    public void setTo_UID(T to_UID) {
        this.to_UID = to_UID;
    }

    public T getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(T roundNum) {
        this.roundNum = roundNum;
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
