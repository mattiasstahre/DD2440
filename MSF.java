import java.util.Arrays;
import java.util.Scanner;


public class MSF{



  
  public static void main(String arg[]){


    Scanner scanner = new Scanner(System.in);
    int N = scanner.nextInt();
    int maxWeight = scanner.nextInt();
    int maxQueries = scanner.nextInt();

    // Allocate array for all nodes
    int nodes[][] = new int[N][];

    // Query edges from given node.
    int edges[] = getNode(4);
   



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


  
 

}