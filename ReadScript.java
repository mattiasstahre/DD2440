import java.util.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class ReadScript {

    public static String read(int node)
    {
        /*try {
            final Process proc = new ProcessBuilder(
                        "/bin/bash", "-c", 
                        "for i in `seq 1 10`; do echo $i; sleep $((i % 2)); done")
                    .start();
            try(InputStreamReader isr = new InputStreamReader(proc.getInputStream())) {
                int c;
                while((c = isr.read()) >= 0) {
                    System.out.print((char) c);
                    System.out.flush();
                }
            } catch(Exception e){}
        }catch(Exception e) {}*/
    //    System.out.println("habibi");
        String string = "";
        try{
            String[] command = {"/bin/bash", "input.sh", Integer.toString(node)};
            ProcessBuilder p = new ProcessBuilder(command);
            Process p2 = p.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p2.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                string = line;
               // System.out.println(line);
            }
            br.close();
        }catch(Exception e){}

        //System.out.println("node: " + node);
        return string;
    }
}