package populationModel;

import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Scanner;

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
                usage();
                return;
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

        // define starting time and initialize simulation
        int t = 0;

        long tic = System.currentTimeMillis();

        // run simulation
        while(sim.hasNext()) {
            String output = sim.next();
            printWriter.printf(output);
        }
        System.err.println("Run time: " + (System.currentTimeMillis() - tic) + " Millis");
        printWriter.close();
    }
}
