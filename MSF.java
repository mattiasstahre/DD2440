import java.util.Arrays;
import java.util.Scanner;
import java.util.*;
import java.lang.Math;

public class MSF {
  public static int numberOfNodes;
  public static int maxWeight;
  public static int maxQueries;
  public static LinkedList<Integer> visitedNodes = new LinkedList<Integer>();
  public static Kattio io = new Kattio(System.in, System.out);

  // Allocate array for all nodes
  public static int matrix[][];

  public static void main(String args[]) {

    numberOfNodes = io.getInt();
    maxWeight = io.getInt();
    maxQueries = io.getInt(); 

    initialize();

    double epsillon = 0.1;
    double delta = 0.5;
    int F = maxWeight;
    int d = 0; // degree?

    // For a test replace nextRequest!
    if (args.length > 0) {
      System.out.println("Running Test");
      
      while(io.hasMoreTokens() == true){
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        
        // break free
        if(line.equals("end")) break;
        
        final int[] edges = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
        matrix[edges[0]][edges[1]] = edges[2];
        System.out.println("This is the read values " + edges[0] + edges[1] + edges[2]);
      }

    } else {
      nextRequest();
    }
    double approximation = mstApporoximation(matrix, epsillon, delta, F, d);

    // print2D(matrix);

    io.println("end " + approximation);
    io.close();

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

    // outputApproximation(approximation);
  }

  public static double approximationComponents(int[][] subgraph, double epsillon, double delta) {
    int k = maxQueries;// (int)(1 / (epsillon * epsillon) * Math.log(1 / delta));
    // System.out.println("K equals " + k);
    LinkedList<Integer> nonPickedVertices = new LinkedList<Integer>();
    int[] pickedVertices = new int[k];
    Random random = new Random(0);
    int m;
    double sum = 0.0;

    for (int i = 0; i < numberOfNodes; i++)
      nonPickedVertices.add(i);

    int randomIndex;
    for (int i = 0; i < k; i++) {
      // System.out.println("Random Int " + nonPickedVertices.size());
      randomIndex = random.nextInt(nonPickedVertices.size());
      pickedVertices[i] = nonPickedVertices.get(randomIndex);
      nonPickedVertices.remove(randomIndex);
    }

    for (int i = 0; i < k; i++) {
      m = breadthFirstSearch(subgraph, pickedVertices[i]);

      // Calculate "m with tilde on top"
      if (m < (2 / epsillon))
        sum += 1 / m;
      else
        sum += 1 / (2 / epsillon);
    }

    sum *= numberOfNodes / k;

    return sum;
  }

  public static double mstApporoximation(int[][] nodes, double epsillon, double delta, int F, int d) {
    double componentSum = 0.0;
    int[][] subgraph;
    for (int i = 1; i < F; i++) {
      subgraph = getSubgraph(i);
      componentSum += approximationComponents(subgraph, epsillon / (2 * F), delta / F);
    }

    double approximation = numberOfNodes - F + componentSum;

    return approximation;
  }

  public static int[][] getSubgraph(int maxEdgeWeight) {

    int[][] subgraph = new int[numberOfNodes][numberOfNodes];
    // Get matrix that consists of all weights up to and including maxEdgeWeight
    for (int i = 0; i < numberOfNodes; i++) {
      for (int k = 0; k < numberOfNodes; k++) {
        if (matrix[i][k] <= maxEdgeWeight) {
          subgraph[i][k] = matrix[i][k];
        }
      }
    }
    return subgraph;
  }

  // Does a request to a random node which has not yet been visited.
  public static void randomRequest() {

    Random rand = new Random(0);

    // Get current size of visitedNodes
    int random = rand.nextInt(visitedNodes.size());
    // Read value at random position in visitedNodes
    int nextValue = visitedNodes.get(random);

    // Store visited nodes in linkedList
    visitedNodes.remove(random);

    // System.out.println("getNode " + nextValue);

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

  // Taken from https://www.sanfoundry.com/java-program-traverse-graph-using-bfs/
  public static int breadthFirstSearch(int[][] matrix, int source) {
    boolean[] visited = new boolean[matrix.length];
    visited[source] = true;
    Queue<Integer> queue = new LinkedList<>();
    queue.add(source);
    int counter = 0;
    while (!queue.isEmpty()) {
      int x = queue.poll();
      counter++;
      int i;
      for (i = 0; i < matrix.length; i++) {
        //System.out.println("x and i " + x + i);
        if (matrix[x][i] > 0 && visited[i] == false) {
          queue.add(i + 1);
          visited[i] = true;
        }
      }
    }

    return counter;
  }
}
