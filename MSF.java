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
    maxQueries = 10;

    initialize();

    double epsillon = 0.1;
    double delta = 0.5;
    int F = maxWeight;
    int d = 0; // degree?

    // For a test replace nextRequest!
    if (args.length > 0) {
      System.out.println("Running Test");
      
      while(io.hasMoreTokens() == true){        
        // break free
        //if(line.equals("end")) break;

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

    // print2D(matrix);

    io.println("end " + approximation); 
    io.flush();
    
  }
  
 
  

  // Does some preparation
  public static void initialize() {
    // Allocate array for all nodes
   map = new HashMap<Integer, LinkedList<Pair>>();

    // Prepare linkedList by putting all nodes in it. Will remove one for each
    // request later on.
    /*for (int i = 0; i < numberOfNodes; i++) {
      visitedNodes.add(i);


    }*/
  }

  // Dumb function that get next request.
  public static void nextRequest() {
    for (int i = 0; i < maxQueries; i++) {
      io.flush();
      randomRequest();
    }

    // outputApproximation(approximation);
  }

  public static double approximationComponents(HashMap<Integer, LinkedList<Pair>> subgraph, double epsillon, double delta) {
    int k = maxQueries;// (int)(1 / (epsillon * epsillon) * Math.log(1 / delta));

    // System.out.println("K equals " + k);
    //LinkedList<Integer> nonPickedVertices = new LinkedList<Integer>();
    int[] pickedVertices = new int[k];
    Random random = new Random(0);
    int m;
    double sum = 0.0;

    //for (int i = 0; i < numberOfNodes; i++)
    //  nonPickedVertices.add(i);

   // int randomIndex;
   // for (int i = 0; i < k; i++) {
      // System.out.println("Random Int " + nonPickedVertices.size());
     // randomIndex = random.nextInt(nonPickedVertices.size());
      //randomIndex = random.nextInt(numberOfNodes);
      //pickedVertices[i] = nonPickedVertices.get(randomIndex);
      //nonPickedVertices.remove(randomIndex);
    //}

    for (int i = 0; i < k; i++) {
     // m = breadthFirstSearch(subgraph, pickedVertices[i]);
      m = breadthFirstSearch(subgraph, random.nextInt(numberOfNodes));

      // Calculate "m with tilde on top"
      if (m < (2 / epsillon))
        sum += 1 / m;
      else
        sum += 1 / (2 / epsillon);
    }

    sum *= numberOfNodes / k;

    return sum;
  }

  public static double mstApporoximation(double epsillon, double delta, int F, int d) {
    double componentSum = 0.0;
    HashMap<Integer, LinkedList<Pair>> subMap;
    for (int i = 1; i < F; i++) {
      subMap = getSubgraph(i);
      componentSum += approximationComponents(subMap, epsillon / (2 * F), delta / F);
    }

    double approximation = numberOfNodes - F + componentSum;

    return approximation;
  }

  public static HashMap<Integer, LinkedList<Pair>> getSubgraph(int maxEdgeWeight) {
    HashMap<Integer, LinkedList<Pair>> subMap = new HashMap<Integer, LinkedList<Pair>>();
    // Get matrix that consists of all weights up to and including maxEdgeWeight
    for (int i = 0; i < numberOfNodes; i++) {
      for (int k = 0; k < numberOfNodes; k++) {
        if (getWeight(map,i,k)<= maxEdgeWeight && getWeight(map,i,k) != -1){ 
          int temp = getWeight(map,i,k);
          putNode(subMap, i, k, temp);
          putNode(subMap, k, i, temp);
        }
      }
    }

    return subMap;
  }

  // Does a request to a random node which has not yet been visited.
  public static void randomRequest() {

    Random rand = new Random(0);

    // Get current size of visitedNodes
    int random = rand.nextInt(numberOfNodes);
    // Read value at random position in visitedNodes
    //int nextValue = visitedNodes.get(random);


    // Store visited nodes in linkedList
    //visitedNodes.remove(random);

    // System.out.println("getNode " + nextValue);

    // Request node found in visitedNodes
    // System.out.println("FROM NODE: " + nextValue);
    getNode(random);
  }
  
  
  public static int getWeight(HashMap<Integer, LinkedList<Pair>> graph, int sourceNode, int DstNode){
  
      if(map.containsKey(sourceNode)){
         LinkedList<Pair> temp = graph.get(sourceNode);
       
        /* for(Pair keyValue: temp){
             if(keyValue.getKey() == DstNode) return keyValue.getValue();
         }
*/
        Iterator<Pair> i = temp.iterator();
        while (i.hasNext()) {
          Pair pointer = i.next();
          if(pointer.getKey() == DstNode) return pointer.getValue();
        }
         
      }
      return -1;
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

            /* 
            Example how to get key or value
            Integer key = myPair.getKey();
            String value = myPair.getValue();
            */  
      }
  }

  // gets edges from requested node.
  public static void getNode(int originNode) {
    // Read the answer

    io.println(originNode);
    io.flush();
    // Fill matrix with values from request
    // Example: getNode(7) => 3 1 3 6 2 3 1

    int numberOfEdges = io.getInt();

    // visitedNodes.add(originNode);

    int to, weight;
    for(int i = 0; i < numberOfEdges; i++)
    {
      to = io.getInt();
      weight = io.getInt();
      //System.out.println("TO: " + to);
      //System.out.println("MATRIX DIM: " + matrix.length + "x" + matrix[0].length);
      //matrix[originNode][to] = weight;
      //matrix[to][originNode] = weight;

      putNode(map, originNode, to, weight);
      putNode(map, to, originNode, weight);

      //print2D(matrix);
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
  public static int breadthFirstSearch(HashMap<Integer, LinkedList<Pair>> graph, int source) {
    boolean[] visited = new boolean[graph.size()];
    visited[source] = true;
    Queue<Integer> queue = new LinkedList<Integer>();
    queue.add(source);
    int counter = 0;
    while (!queue.isEmpty()) {
      int x = queue.poll();
      counter++;
      int i;
      for (i = 0; i < graph.size(); i++) {
        //System.out.println("x and i " + x + i);
        if (getWeight(graph,x,i) > 0 && visited[i] == false) {
          queue.add(i);
          visited[i] = true;
        }
      }
    }

    return counter;
  }
}
