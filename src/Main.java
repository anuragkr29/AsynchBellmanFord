import sun.jvm.hotspot.utilities.Assert;

import java.util.ArrayList;
import java.util.Arrays;
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
        int numberOfRounds = 15;
        Round r = new Round(numberOfProcesses);
        int rootUID = fileReadObj.getRoot();
        int[] UIDs = fileReadObj.getUIDs();
        boolean complete = false;
        System.out.println("--------- Input File read ----------");
        System.out.println(" Number of Processes : " + numberOfProcesses);
        System.out.println("------------------------------------");
        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfProcesses);
        Process processMap[] = new Process[numberOfProcesses];
        for (int i = 0; i < numberOfProcesses; i++) {
            Process p = new Process(UIDs[i], numberOfProcesses,utility.getNeighbors(i,connectionMatrix[i]),Integer.MAX_VALUE, rootUID);
            processMap[i] = p;
        }
        Communication channel = new Communication(processMap, numberOfProcesses);
        Process root = processMap[rootUID];
        root.setDist(0);
        for (int i = 0; i < numberOfProcesses; i++) {
            threadPool.submit(processMap[i]);
        }
        Synchronizer asynch = new Synchronizer(numberOfProcesses, root, r);
        asynch.runBellmanFord();
        System.out.println("All rounds finishied . Closing Thread pool");
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            System.out.println("Thread pool closed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}




