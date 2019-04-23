
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
    public ArrayList<Edge> getNeighbors(int process_index, ArrayList<Integer> connectionMatrix){
        ArrayList<Edge> temp = new ArrayList<>();
        for(int i=0 ; i<connectionMatrix.size();i++){
            int weight = connectionMatrix.get(i);
            if(weight != -1 && i != process_index ){
                Edge e = new Edge(weight, i);
                temp.add(e);
            }
        }
        return temp;
    }

}
