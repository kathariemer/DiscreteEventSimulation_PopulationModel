package populationModel;

import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class Main {
    private static void usage() {
        System.err.println("PopulationModel -i \"Input Path\" -o \"Output Path\"");
        System.exit(1);
    }

    public static void main(String[] args) {
        // parse program arguments
        final Options options = new Options();
        options.addOption(new Option("i", "input", true, "Input file path, if not specified use standard input."));
        options.addOption(new Option("o", "output", true, "Output file path, if not specified use standard output."));

        CommandLineParser parser = new DefaultParser();

        String inputPath;
        PrintWriter printWriter;
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("i")) {
                inputPath = cmd.getOptionValue("i");
            } else {
                inputPath = "src/main/init.txt";
                System.err.println("Using example parameters.");
                //usage();
                //return;
            }
            if (cmd.hasOption("o")) {
                String outputPath = cmd.getOptionValue("o");
                printWriter = new PrintWriter(outputPath);
            } else {
                printWriter = new PrintWriter(System.out);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            usage();
            return;
        } catch (FileNotFoundException e) {
            System.err.printf("Directory of output file does not exist");
            System.exit(1);
            return;
        }


        // initialize simulation with parameters specified in file
        long tic = System.currentTimeMillis();
        Simulation sim = null;
        try {
            sim = new Simulation(inputPath);
        } catch (IOException e) {
            System.err.println("bad input file");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.err.println("bad input numbers");
            System.exit(1);
        }

        // write header line to output file
        printWriter.println("# populationModel.Simulation run on " + Calendar.getInstance().getTime());
        printWriter.println(Simulation.HEADER);

        // run simulation
        while(sim.hasNext()) {
            int[] res = sim.next();
            // instead of packing apache's stringutils into the char ... i typed this out -.-
            printWriter.printf("%d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d\n", res[0],
                    res[1], res[2],
                    res[3], res[4],
                    res[5], res[6],
                    res[7], res[8],
                    res[9], res[10]
                    );
        }
        System.err.println("Run time: " + (System.currentTimeMillis() - tic)/1000.0 + " seconds");
        printWriter.close();
    }
}
