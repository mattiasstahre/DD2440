import java.util.*;

public class Test {


  public static void main(String args[]) {


    // Testcases

    // 

    
  }


  public static putNodeTest() {

    //putNode(HashMap<Integer, LinkedList<Pair>> internalMap, int sourceNode, int DstNode, int weight)
    HashMap<Integer, LinkedList<Pair>>  test = new HashMap<Integer, LinkedList<Pair>>();
    HashMap<Integer, LinkedList<Pair>>  expected = putNode(test, 1, 2, 10);
    //HashMap<Integer, LinkedList<Pair>>  output = putNode(test, 1, 2, 10);    

    
  }


  public static void bfsTest()
  {
    // arrange
    int source = 1

    String stringGraph = 
                "0 1 2
                1 2 1
                2 3 2
                3 0 1"

    HashMap<Integer, LinkedList<Pair>> hashmapGraph = convertGraphToHashmap(stringGraph);
    EdgeWeightedGraph edgeWeighterGraph = convertGraphToEdgeWeight(stringGraph, 4, 4);


    PrimMST primMST = PrimMST(edgeWeighterGraph, source);
    primMST.prim();

    int expectedValue = primMST.weight();

    // act
    int returnedValue = MSF.breadthFirstSearch(hashmapGraph, source);     

    // assert
    if(expectedValue == returnedValue)
        System.out.println("BFS TEST PASSED");
    else 
        System.out.println("BFT TEST FAILED");    
  }

  private static EdgeWeightedGraph convertGraphToEdgeWeight(String stringGraph, int numberOfV, int numberOfE)
  {
      EdgeWeightedGraph edgeWeightGraph = new EdgeWeightedGraph(stringGraph, numberOfV, numberOfE);

      return edgeWeightGraph;
  }

  private static HashMap<Integer, LinkedList<Pair>> convertGraphToHashmap(String stringGraph)
  {
      String[] splittedStringGraph = stringGraph.split();
      HashMap<Integer, LinkedList<Pair>> map = new HashMap<Integer, LinkedList<Pair>>();

      for(int i = 0; i < splittedStringGraph.length / 3; i++)
      {
        int from = splittedStringGraph[i];
        int to = splittedStringGraph[i + 1];
        int weight = splittedStringGraph[i + 2];
        
        putNode(map, from, to, weight);
        putNode(map, to, from, weight);
      }

      return map;
  }
}