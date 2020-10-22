import java.util.Arrays;
import java.util.Scanner;
import java.util.*;


public class MSF {

  public static int numberOfNodes;
  public static int maxWeight;
  public static int maxQueries;
  public static LinkedList<Integer> visitedNodes = new LinkedList<Integer>();

  // Allocate array for all nodes
  public static int matrix[][];

  public static void main(String arg[]) {

    Scanner scanner = new Scanner(System.in);
    numberOfNodes = scanner.nextInt();
    maxWeight = scanner.nextInt();
    maxQueries = scanner.nextInt();

    initialize();
    nextRequest();
    print2D(matrix);
  }

  // Does some preparation
  public static void initialize() {
    // Allocate array for all nodes
    matrix = new int[numberOfNodes][numberOfNodes];

    // Prepare linkedList by putting all nodes in it. Will remove one for each
    // request later on.

    for (int i = 0; i < numberOfNodes; i++) {
      visitedNodes.add(i);
    }
  }

  // Dumb function that get next request.
  public static void nextRequest() {

    for (int i = 0; i < maxQueries; i++) {
      randomRequest();

    }

    outputApproximation(approximation);
  }

  public static int getComponents() {

  }

  public static double getAverageWeight() {
    
  }

  public static int[][] getMSF() {

  }


  // Does a request to a random node which has not yet been visited.
  public static void randomRequest() {

    Random rand = new Random();

    // Get current size of visitedNodes
    int random = rand.nextInt(visitedNodes.size());
    // Read value at random position in visitedNodes
    int nextValue = visitedNodes.get(random);

    // Store visited nodes in linkedList
    visitedNodes.remove(random);

    System.out.println("getNode " + nextValue);

    // Request node found in visitedNodes
    getNode(nextValue);
  }

  // gets edges from requested node.
  public static void getNode(int node) {

    System.out.println(node);

    // Read the answer
    Scanner scanner = new Scanner(System.in);
    String line = scanner.nextLine();

    // Parse the answer into an array and return it.
    final int[] edges = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();

    // Fill matrix with values from request
    // Example: getNode(7) => 3 1 3 6 2 3 1

    // Read all edges and put their weight in matrix at corresponding place.
    for (int i = 1; i < edges.length; i = i + 2) {
      matrix[node][edges[i]] = edges[i + 1];
    }

  }

  // Simple function to print 2D arrays, input is matrix.
  public static void print2D(int mat[][]) {
    // Loop through all rows
    for (int[] row : mat)

      // converting each row as string
      // and then printing in a separate line
      System.out.println(Arrays.toString(row));
  }
}
