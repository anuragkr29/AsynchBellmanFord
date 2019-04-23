import java.util.ArrayList;
import java.util.List;

public class Message<T> {


    public T currentDist;
    public T UID;

    public Message(T dist, T UID) {
        this.currentDist = dist;
        this.UID = UID;
    }

}
