import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        // initialize simulation with parameters specified in file
        Simulation sim = new Simulation("src/main/init.txt");

        // write header line to output file
        FileWriter fileWriter = new FileWriter("target/generated-sources/res.csv");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        
        printWriter.println("# Simulation run on " + Calendar.getInstance().getTime());
        printWriter.println(Simulation.getHeader());

        // define starting time and initialize simulation
        int t = 0;
        sim.init(t);

        long tic = System.currentTimeMillis();
        // run simulation
        for (; t < sim.getMaxSteps(); t++) {
            printWriter.printf(sim.step(t));
        }
        System.out.println("Run time: " + (System.currentTimeMillis() - tic) + " Millis");
        printWriter.close();
    }
}
