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
        options.addOption(new Option("r", "repetitions", true, "Number of time the simulation should be repeated. Default = 1."));

        CommandLineParser parser = new DefaultParser();

        String inputPath;
        PrintWriter printWriter;
        int reps = 1;
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("i")) {
                inputPath = cmd.getOptionValue("i");
            } else {
                inputPath = "../init.txt";
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
            if (cmd.hasOption("r")) {
                reps = Integer.parseInt(cmd.getOptionValue("r"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            usage();
            return;
        } catch (FileNotFoundException e) {
            System.err.println("Directory of output file does not exist");
            System.exit(1);
            return;
        }


        // initialize simulation with parameters specified in file
        long tic = System.currentTimeMillis();
        Simulation sim = null;
        try {
            sim = new Simulation(inputPath);
        } catch (IOException e) {
            System.err.printf("Bad input file %s. \nCurrent directory: %s\n", inputPath, System.getProperty("user.dir"));
            System.exit(1);
        } catch (NumberFormatException | NullPointerException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        // write header line to output file
        printWriter.println("# populationModel.Simulation run on " + Calendar.getInstance().getTime());
        printWriter.println("# " + sim);
        printWriter.println(Simulation.HEADER);

        // run simulation
        for (int i = 0; i < reps; i++) {
            sim.reset();
            while (sim.hasNext()) {
                int[] res = sim.next();
                // instead of packing apache's stringutils into the char ... i typed this out -.-
                printWriter.printf("%d, %d, %d, %d, %d, %d, %d, %d, %d\n", res[0],
                        res[1], res[2],
                        res[3], res[4],
                        res[5], res[6],
                        res[7], res[8]
                );
            }
        }
        System.err.println("Run time: " + (System.currentTimeMillis() - tic) / 1000.0 + " seconds");
        printWriter.close();
    }
}
