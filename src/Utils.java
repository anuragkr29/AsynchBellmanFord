
/**
 * @author Anurag Kumar
 */

import java.util.ArrayList;

public class Utils {
    /**
     * Utility function to get the neighbors of a node in a graph(the adjacency matrix)
     * @param process_index the index of a node
     * @param connectionMatrix the adjacency matrix given in the input file
     * @return
     */
    public ArrayList<Integer> getNeighbors(int process_index, ArrayList<Integer> connectionMatrix){
        ArrayList<Integer> temp = new ArrayList<>();
        for(int i=0 ; i<connectionMatrix.size();i++){
            if(connectionMatrix.get(i) == 1 && i != process_index ){
                temp.add(i);
            }
        }
        return temp;
    }

}
