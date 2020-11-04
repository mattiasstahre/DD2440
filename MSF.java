import java.util.*;

public class MSF {
  public static int numberOfNodes;
  public static int maxWeight;
  public static int maxQueries;
  public static LinkedList<Integer> visitedNodes = new LinkedList<Integer>();
  public static Kattio io = new Kattio(System.in, System.out);
  public static HashMap<Integer, LinkedList<Pair>>  map;

  public static void main(String args[]) {

    numberOfNodes = io.getInt();
    maxWeight = io.getInt();
    maxQueries = io.getInt(); 
    maxQueries = maxQueries;

    initialize();

    double epsillon = 0.1;
    double delta = 0.1;
    int F = maxWeight;
    int d = 0; 

    // For a test replace nextRequest!
    if (args.length > 0) {
      System.out.println("Running Test");
      
      while(io.hasMoreTokens() == true){        
        int from = io.getInt();
        int to = io.getInt();
        int weight = io.getInt();
        io.flush();
        
        putNode(map, from, to, weight);
        putNode(map, to, from, weight);
      }
    } else {
      nextRequest();
    }
    double approximation = mstApporoximation(epsillon, delta, F, d);
    io.println("end " + approximation); 
    io.flush();
  }

  // Does some preparation
  public static void initialize() {
    map = new HashMap<Integer, LinkedList<Pair>>();
  }

  // Dumb function that get next request.
  public static void nextRequest() {
    Random rand = new Random();
    for (int i = 0; i < maxQueries; i++) {
      io.flush();
      randomRequest(rand);
    }
  }

  public static double approximationComponents(HashMap<Integer, LinkedList<Pair>> subgraph, double epsillon, double delta) {
    int k =  160; //(int)((1 / (epsillon * epsillon)) * (Math.log(1 / delta)));

    
    int[] pickedVertices = new int[k];
    Random random = new Random();
    double m;
    double sum = 0.0;

    for (int i = 0; i < k; i++) {
     Set<Integer> keySet = subgraph.keySet();
     List<Integer> keyList = new ArrayList<>(keySet);

     int size = keyList.size();
     int randIdx = new Random().nextInt(size);

     int randomPickedElement = keyList.get(randIdx);
     m = breadthFirstSearch(subgraph, randomPickedElement);
      
    if (m < (2 / epsillon)){
        double temp = (1 / m);
        sum = sum + temp;
        // sum = sum + (double)(1/m)
    }
      else{
        double temp = (1 / (2 / epsillon));
        sum = sum + temp; 
      }
    }

    sum = sum * ((double)numberOfNodes / (double)k);
    return sum;
  }

  public static double mstApporoximation(double epsillon, double delta, int F, int d) {
    double componentSum = 0.0;
    HashMap<Integer, LinkedList<Pair>> subMap;
    for (int i = 1; i < F; i++) {
      subMap = getSubgraph(i);
      if(subMap.size() != 0){
        componentSum += approximationComponents(subMap, epsillon / (2 * F), delta / F);
      }
    }

    double approximation = numberOfNodes - F + componentSum;

    return approximation;
  }

  public static HashMap<Integer, LinkedList<Pair>> getSubgraph(int maxEdgeWeight) {
    HashMap<Integer, LinkedList<Pair>> subMap = new HashMap<Integer, LinkedList<Pair>>();
    Iterator<Integer> iterator = map.keySet().iterator();

    while(iterator.hasNext()){
      int sourceNode = iterator.next();
      LinkedList<Pair> tempList = map.get(sourceNode);
        for (Pair pair: tempList) {
          int weight = pair.getValue();
          if (weight<= maxEdgeWeight){ 
            int dstNode = pair.getKey();
            putNode(subMap, sourceNode, dstNode, weight);
            putNode(subMap, dstNode, sourceNode, weight);
          }
        }
      }
    return subMap;
  }

  // Does a request to a random node which has not yet been visited.
  public static void randomRequest(Random randomGenerator) {
    int random = randomGenerator.nextInt(numberOfNodes);
    getNode(random);
  }
  
  // Function to add an edge with a weight between source and destination node
  public static void putNode(HashMap<Integer, LinkedList<Pair>> internalMap, int sourceNode, int DstNode, int weight) {
  
      if(internalMap.containsKey(sourceNode)){
          LinkedList<Pair> temp = internalMap.get(sourceNode);
          Pair newPair = new Pair(DstNode, weight);
          temp.add(newPair); 
      }
      else {
          LinkedList<Pair> edges = new LinkedList<Pair>();
          Pair myPair = new Pair(DstNode, weight);
          edges.add(myPair);
          internalMap.put(sourceNode, edges);
      }
  }

  // gets edges from requested node.
  public static void getNode(int originNode) {
    
    io.println(originNode);
    io.flush();
    int numberOfEdges = io.getInt();
    int to, weight;

    for(int i = 0; i < numberOfEdges; i++){
      to = io.getInt();
      weight = io.getInt();
      putNode(map, originNode, to, weight);
      putNode(map, to, originNode, weight);
    }

  }


  // Taken from https://www.sanfoundry.com/java-program-traverse-graph-using-bfs/
  public static double breadthFirstSearch(HashMap<Integer, LinkedList<Pair>> subGraph, int source) {
    //boolean[] visited = new boolean[subGraph.size()]; 
    HashMap<Integer, Boolean> hasVisited = new HashMap<Integer, Boolean>();

    hasVisited.put(source, true);
    Queue<Integer> queue = new LinkedList<Integer>();
    queue.add(source);
    int counter = 0;
    while (!queue.isEmpty()) {
      int node = queue.poll();
      counter++;
      
      LinkedList<Pair> edges = subGraph.get(node);
      if(edges != null){
        for (Pair pair : edges){
          if (pair.getValue() > 0 && hasVisited.get(pair.getKey()) == null) {
            queue.add(pair.getKey());
            hasVisited.put(pair.getKey(), true);
          }
        }
      }
    }
    return counter;
  }
}
