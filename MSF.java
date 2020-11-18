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
  public static int[] elementCounter;
  public static HashMap<Integer, Boolean> hasVisitedOne;
  public static HashMap<Integer, LinkedList<Pair>> subgraph;
  public static HashMap<Integer, LinkedList<Pair>> PrevSubgraph;
  public static int numberOfSamples;

  public static LinkedList<Pair> edges;
  public static Iterator<Integer> iteratorSubgraph;
  public static LinkedList<Pair> tempListSub;
  public static Set<Integer> keySetApprox;
  public static List<Integer> keyListApprox;

  public static void main(String args[]) {

    numberOfNodes = io.getInt();
    maxWeight = io.getInt();
    maxQueries = io.getInt(); 
    io.flush();
    maxQueries = maxQueries;
    numberOfSamples = maxQueries;

    initialize();

    // For a test replace nextRequest!
    if (args.length > 0) {
      System.out.println("Running Test");
      
      while(io.hasMoreTokens() == true){        
        int from = io.getInt();
        int to = io.getInt();
        int weight = io.getInt();
        io.flush();
        
        putNode(1, from, to, weight);
      }

    } else {
        nextRequestBFS();

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
    hasVisitedOne = new HashMap<Integer, Boolean>();
    subgraph = new HashMap<Integer, LinkedList<Pair>>();
    elementCounter = new int[maxWeight];

    epsillon = 0.1;
    delta = 0.1;
    F = maxWeight;
    d = 0;
  }


  public static void nextRequestBFS(){

    hasVisitedOne.clear();

    int counter = numberOfSamples;
    int batchSize = 100;
    int batchCounter = batchSize;
    int node;

    while(counter > 0)
    {
      Queue<Integer> queueOne = new LinkedList<Integer>();
      int randomNode = rand.nextInt(numberOfNodes);
      batchCounter = batchSize;

      if(hasVisitedOne.get(randomNode) == null)
      {
        hasVisitedOne.put(randomNode, true);
        queueOne.add(randomNode);
        while (!queueOne.isEmpty() && counter > 0 && batchCounter > 0) {
          counter--;
          batchCounter--;
          node = queueOne.poll();
          getNode(node);
          //edges.clear();
          edges = graph.get(node);
          if(edges != null){
            for (Pair pair : edges){
              if (hasVisitedOne.get(pair.getKey()) == null) {
                queueOne.add(pair.getKey());
                hasVisitedOne.put(pair.getKey(), true);
              }
            }
          }
        }
      }
    }
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

    putEmptyNode(originNode);
    
    for(int i = 0; i < numberOfEdges; i++){
      to = io.getInt();
      weight = io.getInt();
      putNode(1, originNode, to, weight);
      //putNode(graph, to, originNode, weight);
    }
  }

  public static void putEmptyNode(int sourceNode) {
        LinkedList<Pair> neighbour = new LinkedList<Pair>();
        graph.put(sourceNode, neighbour);
  }
  
  // Function to add an edge with a weight between source and destination node
  public static void putNode(int whichGraph, int sourceNode, int DstNode, int weight) {
    if(whichGraph == 1){
      elementCounter[weight - 1] = elementCounter[weight - 1] + 1;
      if(graph.containsKey(sourceNode)){
        Pair newPair = new Pair(DstNode, weight);
        graph.get(sourceNode).add(newPair); 
      }else {
        LinkedList<Pair> neighbour = new LinkedList<Pair>();
        Pair myPair = new Pair(DstNode, weight);
        neighbour.add(myPair);
        graph.put(sourceNode, neighbour);
      }
    }else{
      if(subgraph.containsKey(sourceNode)){
        Pair newPair = new Pair(DstNode, weight);
        subgraph.get(sourceNode).add(newPair); 
      }else {
        LinkedList<Pair> neighbour = new LinkedList<Pair>();
        Pair myPair = new Pair(DstNode, weight);
        neighbour.add(myPair);
        subgraph.put(sourceNode, neighbour);
      }
    }
  }

  // Compute MSF
  public static double mstApporoximation() {
    double componentSum = 0.0;
    double prevComponentSum = 0.0;
    for (int i = 1; i < F; i++) {
      getSubgraph(i); // gör endast en subgrpah om vi har en ny vikt?  Vill iterera alla vikter i storleksordning?

      if(elementCounter[i - 1] == 0 && i != 1)
      {
        componentSum += prevComponentSum;
      }
      else
      {
        prevComponentSum = approximationComponents(epsillon / (2 * F), delta / F);
        componentSum += prevComponentSum;
      }
    }

    getSubgraph(F);

    double approximation = numberOfNodes - F * approximationComponents(epsillon / (2 * F), delta / F) + componentSum;
    return approximation;
  }

  // Returns an approximation of how many trees there's in the graph
  public static double getApproximationNumberOfTrees()
  {
    return 1.0;
  }


  // Create a subgraph with max weight - maxEdgeWeight
  public static void getSubgraph(int maxEdgeWeight) {
    subgraph.clear();
    iteratorSubgraph = graph.keySet().iterator();
    int weight, dstNode, sourceNode;

    while(iteratorSubgraph.hasNext()){
      sourceNode = iteratorSubgraph.next();
      tempListSub = graph.get(sourceNode);
      subgraph.put(sourceNode, new LinkedList<Pair>());

        for (Pair pair: tempListSub) {
          weight = pair.getValue();
          if (weight<= maxEdgeWeight){ 
            dstNode = pair.getKey();
            putNode(2, sourceNode, dstNode, weight);
          }
        }
      }
  }

  public static int[] pickedVertices;
  public static int k;
  public static double m;
  public static double sumAppx;
  public static int randIdx;
  public static double tempAPPX;
  public static int randomPickedElementAPPX;
  public static int sizeAPPX;
  // Approximate number of connected components in a subgraph 
  public static double approximationComponents(double newEpsillon, double newDelta) {
    k = (int)((double)(1.0 / (newEpsillon * newEpsillon)) * (double)Math.log(1 / newDelta));
    pickedVertices = new int[k];
    sumAppx = 0.0;
    keySetApprox = subgraph.keySet();
    keyListApprox = new ArrayList<>(keySetApprox);
    sizeAPPX = keyListApprox.size();

    for (int i = 0; i < k; i++) {
      
      randIdx = rand.nextInt(sizeAPPX);
      randomPickedElementAPPX = keyListApprox.get(randIdx);
      m = breadthFirstSearch(randomPickedElementAPPX, newEpsillon);

    if ((double)m < (2.0 / newEpsillon)){
        tempAPPX = 1.0 / m;
        sumAppx = sumAppx + tempAPPX;
    }
      else{
        tempAPPX = (1.0 / (2.0 / newEpsillon));
        sumAppx = sumAppx + tempAPPX; 
      }
    }
    sumAppx = sumAppx * ((double)numberOfNodes / (double)k);
    
    //System.out.println("APPROXIMATION OF NUMBER OF COMPONENTS " + sumAppx);
    return (int)Math.round(sumAppx);
  }

  // Taken from https://www.sanfoundry.com/java-program-traverse-graph-using-bfs/
  // Computes number of connected nodes from source node
  public static double breadthFirstSearch(int source, double newEpsillon) {
    hasVisited.clear();
    hasVisited.put(source, true);
    queue.add(source);
    int counter = 0;

    while (!queue.isEmpty() && queue.size() < 10000) {
      int node = queue.poll();
      counter++;
      edgesBFS = subgraph.get(node);

      if(edgesBFS != null){
        for (Pair pair : edgesBFS){
          if (hasVisited.get(pair.getKey()) == null) {
            queue.add(pair.getKey());
            hasVisited.put(pair.getKey(), true);
          }
        }
      }
      if(counter >=  2.0 / newEpsillon)
        return counter;
    }
    
    return counter;
  }
}