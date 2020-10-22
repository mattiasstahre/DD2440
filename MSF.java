import java.util.Arrays;
import java.util.Scanner;


public class MSF{

  int N;
  int maxWeight;
  int maxQueries;

  // Allocate array for all nodes
  int matrix[][];

  
  public static void main(String arg[]){


    scanner = new Scanner(System.in);
    N = scanner.nextInt();
    maxWeight = scanner.nextInt();
    maxQueries = scanner.nextInt();

    // Allocate array for all nodes
    matrix = new int[N][N];

    // Query edges from given node.
    int edges[] = getNode(4);


  }


  public static void getNode(int node){

    System.out.println(node);

    // Read the answer
    Scanner scanner = new Scanner(System.in);
    String line = scanner.nextLine();
    
    // Parse the answer into an array and return it.
    final int[] edges = Arrays.stream(line.split(" "))
        .mapToInt(Integer::parseInt)
        .toArray();

    
    // Fill matrix with values from request
    // Example: getNode(7) => 3 1 3 6 2 3 1

    // Read all edges and put their weight in matrix at corresponding place.
    for(int i = 1; i < edges.length; i++){
        matrix[node][edges[i] = edges[i+1];
    }




  }


  
 

}