import sun.jvm.hotspot.utilities.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        Utils utility = new Utils();
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the input file path : ");
        ReadFile fileReadObj = new ReadFile(s.nextLine());
        int numberOfProcesses = fileReadObj.getNumberOfProcesses();
        ArrayList<Integer>[] connectionMatrix = fileReadObj.getConnectionMatrix();
        assert numberOfProcesses == connectionMatrix.length : "Incorrect Matrix";
        int numberOfRounds = numberOfProcesses;
        int rootUID = fileReadObj.getRoot();
        int[] UIDs = fileReadObj.getUIDs();
        System.out.println("--------- Input File read ----------");
        System.out.println(" Number of Processes : " + numberOfProcesses);
        System.out.println("------------------------------------");
        Round r = new Round(numberOfProcesses);
        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfProcesses);
        Process processMap[] = new Process[numberOfProcesses];
        for (int i = 0; i < numberOfProcesses; i++) {
            Process p = new Process(i, numberOfProcesses,utility.getNeighbors(i,connectionMatrix[i]),Integer.MAX_VALUE, UIDs[i]);
            processMap[i] = p;
        }
        Communication channel = new Communication(processMap, numberOfProcesses);
        Process root = processMap[rootUID];
        root.setDist(0);
        for (int i = 0; i < numberOfProcesses; i++) {
            threadPool.submit(processMap[i]);
        }

        int round = 0;
        while (round <= numberOfRounds-1) {
            try {
                if (Round.threadCount.get() == 0) {
                    round++;
                    Thread.currentThread().sleep(1000);
                    r.nextRound(numberOfProcesses, round);
                    System.out.println("Started round : " + (round+1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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




