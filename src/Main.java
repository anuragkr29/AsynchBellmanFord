/**
 * @author Anurag Kumar
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Utils utility = new Utils();
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the input file path : ");
        ReadFile fileReadObj = new ReadFile(s.nextLine());
        int numberOfProcesses = fileReadObj.getNumberOfProcesses();
        ArrayList<Integer>[] connectionMatrix = fileReadObj.getConnectionMatrix();
        assert numberOfProcesses == connectionMatrix.length : "Incorrect Matrix";
        Round r = new Round(numberOfProcesses);
        int rootUID = fileReadObj.getRoot();
        int[] UIDs = fileReadObj.getUIDs();
        System.out.println("--------- Input File read ----------");
        System.out.println(" Number of Processes : " + numberOfProcesses);
        System.out.println(" Root UID : " + rootUID);
        System.out.println("------------------------------------");
        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfProcesses);
        Process processMap[] = new Process[numberOfProcesses];
        Communication channel = new Communication(processMap);

        // instantiate processes and save their references for future use to communicate
        for (int i = 0; i < numberOfProcesses; i++) {
            Process p = new Process(UIDs[i], numberOfProcesses,utility.getNeighbors(i,connectionMatrix[i]),Integer.MAX_VALUE, rootUID);
            processMap[i] = p;
        }

        // get the root process reference and set the distance to 0
        Process root = processMap[rootUID];
        root.setDist(0);

        // start the threadPool i.e. spawn all the threads
        for (int i = 0; i < numberOfProcesses; i++) {
            threadPool.submit(processMap[i]);
        }

        // Inititalize the synchronizer class
        Synchronizer asynch = new Synchronizer(numberOfProcesses, root, r);

        // Run the Bellman Ford algorithm from the synchroizer
        asynch.runBellmanFord();
        System.out.println("All rounds finishied . Closing Thread pool");

        // close the threadpool once the convergecast is complete
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            System.out.println("Total messages sent : " + Communication.getNumberOfMessages());
            System.out.println("Thread pool closed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}




