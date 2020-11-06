import java.util.*;

public class MSF {
  public static int numberOfNodes;
  public static int maxWeight;
  public static int maxQueries;
  public static double epsillon;
  public static double delta;
  public static int F;
  public static int d; 
  public static LinkedList<Integer> visitedNodes;
  public static Kattio io = new Kattio(System.in, System.out);
  public static HashMap<Integer, LinkedList<Pair>> graph;
  public static Queue<Integer> queue;
  public static LinkedList<Pair> edgesBFS;
  public static HashMap<Integer, Boolean> hasVisited;
  public static Random rand;
  //public static HashMap<Integer, LinkedList<Pair>> subgraph;

  public static void main(String args[]) {

    numberOfNodes = io.getInt();
    maxWeight = io.getInt();
    maxQueries = io.getInt(); 
    io.flush();
    //maxQueries = maxQueries;

    initialize();

    // For a test replace nextRequest!
    if (args.length > 0) {
      System.out.println("Running Test");
      
      while(io.hasMoreTokens() == true){        
        int from = io.getInt();
        int to = io.getInt();
        int weight = io.getInt();
        io.flush();
        
        putNode(graph, from, to, weight);
        putNode(graph, to, from, weight);
      }
    } else {
      nextRequest();
    }
    double approximation = mstApporoximation();
    io.println("end " + approximation); 
    io.flush();
  }

  // Initialize global variables and objects 
  public static void initialize() {
    graph = new HashMap<Integer, LinkedList<Pair>>();
    visitedNodes = new LinkedList<Integer>();
    queue = new LinkedList<Integer>();
    edgesBFS = new LinkedList<Pair>();
    hasVisited = new HashMap<Integer, Boolean>();
    rand = new Random();
    //subgraph = new HashMap<Integer, LinkedList<Pair>>();

    epsillon = 0.1;
    delta = 0.1;
    F = maxWeight;
    d = 0;
  }


  // Dumb function that get next request
  public static void nextRequest() {
    int randomNode;
    for (int i = 0; i < maxQueries; i++) {
      io.flush();
      randomNode = rand.nextInt(numberOfNodes);
      getNode(randomNode);
    }
  }

  // Gets edges from requested node and add the node to graph
  public static void getNode(int originNode) {
    
    io.println(originNode);
    io.flush();
    int numberOfEdges = io.getInt();
    int to, weight;

    for(int i = 0; i < numberOfEdges; i++){
      to = io.getInt();
      weight = io.getInt();
      putNode(graph, originNode, to, weight);
      putNode(graph, to, originNode, weight);
    }
  }
  
  // Function to add an edge with a weight between source and destination node
  public static void putNode(HashMap<Integer, LinkedList<Pair>> internalMap, int sourceNode, int DstNode, int weight) {

    if(internalMap.containsKey(sourceNode)){
      Pair newPair = new Pair(DstNode, weight);
      internalMap.get(sourceNode).add(newPair); 
    }else {
      LinkedList<Pair> edges = new LinkedList<Pair>();
      Pair myPair = new Pair(DstNode, weight);
      edges.add(myPair);
      internalMap.put(sourceNode, edges);
    }
  }

  // Compute MSF
  public static double mstApporoximation() {
    double componentSum = 0.0;
    HashMap<Integer, LinkedList<Pair>> subgraph;
    for (int i = 1; i < F; i++) {
      subgraph = getSubgraph(i);
      if(subgraph.size() != 0){
        componentSum += approximationComponents(subgraph, epsillon / (2 * F), delta / F);
      }
    }
    double approximation = numberOfNodes - F + componentSum;
    return approximation;
  }

  // Create a subgraph with max weight - maxEdgeWeight
  public static HashMap<Integer, LinkedList<Pair>> getSubgraph(int maxEdgeWeight) {
    //subgraph.clear();
    HashMap<Integer, LinkedList<Pair>> subgraph = new HashMap<Integer, LinkedList<Pair>>();
    Iterator<Integer> iterator = graph.keySet().iterator();
    LinkedList<Pair> tempList;
    int weight;
    int dstNode;
    int sourceNode;

    while(iterator.hasNext()){
      sourceNode = iterator.next();
      tempList = graph.get(sourceNode);
        for (Pair pair: tempList) {
          weight = pair.getValue();
          if (weight<= maxEdgeWeight){ 
            dstNode = pair.getKey();
            putNode(subgraph, sourceNode, dstNode, weight);
            putNode(subgraph, dstNode, sourceNode, weight);
          }
        }
      }
      return subgraph;
  }

  // Approximate number of connected components in a subgraph 
  public static double approximationComponents(HashMap<Integer, LinkedList<Pair>> subgraph, double newEpsillon, double newDelta) {
    int k = (int)((1 / (newEpsillon * newEpsillon)) * (Math.log(1 / newDelta)));
    
    int[] pickedVertices = new int[k];
    double m;
    double sum = 0.0;
    int randIdx;
    int randomPickedElement;
    double temp;

    Set<Integer> keySet = subgraph.keySet();
    ArrayList<Integer> keyList = new ArrayList<>(keySet);
    int size = keyList.size();

    for (int i = 0; i < k; i++) {
     randIdx = rand.nextInt(size);
     randomPickedElement = keyList.get(randIdx);
     m = breadthFirstSearch(subgraph, randomPickedElement);
      
    if (m < (2 / newEpsillon)){
        temp = (1 / m);
        sum = sum + temp;
        // sum = sum + (double)(1/m)
    }
      else{
        temp = (1 / (2 / newEpsillon));
        sum = sum + temp; 
      }
    }
    sum = sum * ((double)numberOfNodes / (double)k);
    return sum;
  }

  // Taken from https://www.sanfoundry.com/java-program-traverse-graph-using-bfs/
  // Computes number of connected nodes from source node
  public static double breadthFirstSearch(HashMap<Integer, LinkedList<Pair>> subgraph, int source) {
    //boolean[] visited = new boolean[subGraph.size()]; 
    hasVisited.put(source, true);
    queue.add(source);
    int counter = 0;
    while (!queue.isEmpty()) {
      int node = queue.poll();
      counter++;
      
      edgesBFS = subgraph.get(node);
      if(edgesBFS != null){
        for (Pair pair : edgesBFS){
          if (pair.getValue() > 0 && hasVisited.get(pair.getKey()) == null) {
            queue.add(pair.getKey());
            hasVisited.put(pair.getKey(), true);
          }
        }
      }
    }
    edgesBFS.clear();
    hasVisited.clear();
    return counter;
  }
}