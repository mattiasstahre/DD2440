import java.util.*;

public class MSF {
  public static int numberOfNodes;
  public static int maxWeight;
  public static int maxQueries;
  public static double epsillon;
  public static double delta;
  public static int F;
  public static Kattio io = new Kattio(System.in, System.out);
  public static HashMap<Integer, LinkedList<Pair>> graph;
  public static LinkedList<Pair> edges;
  public static Random rand;

  /* 
  Set to true if running localy with test script.
  Set to false if running on kattis 
  */
  public static Boolean isTest = false;

  public static void main(String args[]) {

    initialize();

    double approximation = mstApporoximation();
    approximation = approximation*1.005;
    io.println("end " + approximation); 
    io.flush();
  }

  // Initialize global variables and objects 
  public static void initialize() {
    numberOfNodes = io.getInt();
    maxWeight = io.getInt();
    maxQueries = io.getInt(); 
    io.flush();

    rand = new Random(10000);

    epsillon = 0.05;
    F = maxWeight;
  }

  // Gets edges from requested node and add the node to graph
  public static void getNode(int originNode) {
    io.println(originNode);
    io.flush();
    int numberOfEdges = io.getInt();
    int to, weight;

    putEmptyNode(originNode);
    
    for(int i = 0; i < numberOfEdges; i++){
      to = io.getInt();
      weight = io.getInt();
      putNode(1, originNode, to, weight);
    }
  }

  public static void getNodeScript(int originNode) {
    String[] result = ReadScript.read(originNode).split("\\s+");
    io.flush();
    int numberOfEdges = Integer.parseInt(result[0]);
    int to, weight;

    putEmptyNode(originNode);
    for(int i = 0; i < numberOfEdges; i++){
      to = Integer.parseInt(result[i*2 + 1]);
      weight = Integer.parseInt(result[i*2 + 2]);
      putNode(1, originNode, to, weight);
    }
  }

  public static void putEmptyNode(int sourceNode) {
        LinkedList<Pair> neighbour = new LinkedList<Pair>();
        graph.put(sourceNode, neighbour);
  }
  
  // Function to add an edge with a weight between source and destination node
  public static void putNode(int whichGraph, int sourceNode, int DstNode, int weight) {
    Pair newPair = new Pair(DstNode, weight);
    graph.get(sourceNode).add(newPair); 
  }

  // Compute MSF
  public static double mstApporoximation() {
    double componentSum = 0.0;

    for (int i = 1; i < F; i++) 
      componentSum += approximationComponents(epsillon / (2 * F), delta / F, i);

    double numberOfTrees = approximationComponents(epsillon / (2 * F), delta / F, F);

    return numberOfNodes - F * numberOfTrees + componentSum;
  }

  public static double approximationComponents(double newEpsillon, double newDelta, int maxWeight)
  {
    double sum = 0.0;
    int randomNode;
    int k = 73;//(int)((double)(1.0 / (newEpsillon * newEpsillon)) * (double)Math.log(1 / newDelta));
    double m;

    for(int i = 0; i < k; i++)
    {
      randomNode = rand.nextInt(numberOfNodes);
      m = breadthFirstSearch(randomNode, newEpsillon, maxWeight);
     if ((double)m < (2.0 / newEpsillon)){
        sum += 1.0 / m;
      }
      else{
        sum += (1.0 / (2.0 / newEpsillon)); 
      }
    }

    //System.out.println("APPROXIMATED COMPONENTS: " + sum * ((double)numberOfNodes / (double)k) + " MAX WEIGHT: " + maxWeight);
    return sum * ((double)numberOfNodes / (double)k);
  }

  // Taken from https://www.sanfoundry.com/java-program-traverse-graph-using-bfs/
  // Computes number of connected nodes from source node
  public static double breadthFirstSearch(int source, double newEpsillon, int maxWeight) {
    HashMap<Integer, Boolean> hasVisited = new HashMap<Integer, Boolean>();
    Queue<Integer> queue = new LinkedList<Integer>();
    graph = new HashMap<Integer, LinkedList<Pair>>();

    hasVisited.put(source, true);
    queue.add(source);
    

    int MAX_SEARCH = 154; //rand.nextInt(10) + 149;//154;
    int treeSize = 0;

    while (!queue.isEmpty() && treeSize < MAX_SEARCH) {
      int node = queue.poll();
      treeSize++;

      if (isTest)
        getNodeScript(node);
      else
        getNode(node);

      edges = graph.get(node);

      if(edges != null){
        for (Pair pair : edges){
          if (hasVisited.get(pair.getKey()) == null && pair.getValue() <= maxWeight) {
            queue.add(pair.getKey());
            hasVisited.put(pair.getKey(), true);
          }
        }
      }
    }

    return treeSize;
  }
}