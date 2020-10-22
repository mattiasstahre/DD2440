import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;


public class MSF{  
  public static int N;
  public static int maxWeight;
  public static int maxQueries;
  public static Random random = new Random();
  public static ArrayList notSeenNodes;

  public static void main(String arg[]){
    Scanner scanner = new Scanner(System.in);
    N = scanner.nextInt();
    maxWeight = scanner.nextInt();
    maxQueries = scanner.nextInt();

    ArrayList notSeenNodes = new ArrayList(N);

    for (int i = 0; i < N; i++)
      notSeenNodes.set(i, i);

    // Allocate array for all nodes
    int nodes[][] = new int[N][];

    // Query edges from given node.
    int edges[] = getNode(4);

    for (int i = 0; i < maxQueries; i++)
    {
      int nodeIndex = heuristic();
    }

    double approximation = getApproximation();

    outputApproximation(approximation);
  }

  public static int[] getNode(int node){
    System.out.println(node);

    // Read the answer
    Scanner scanner = new Scanner(System.in);
    String line = scanner.nextLine();
    
    // Parse the answer into an array and return it.
    final int[] ints = Arrays.stream(line.split(" "))
        .mapToInt(Integer::parseInt)
        .toArray();

    return ints;
  }

  public static int heuristic()
  {
    int nodeIndex = random.nextInt(notSeenNodes.size());
    notSeenNodes.remove(nodeIndex);

    return nodeIndex; 
  }

  public static double getApproximation()
  {
    return 0.0;
  }

  public static void outputApproximation(double approximation)
  {
    return;
  }
}