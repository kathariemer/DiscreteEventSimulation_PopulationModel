package populationModel;

import org.apache.commons.cli.*;

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

    public static void main(String[] args) throws IOException {
        // parse program arguments
        final Options options = new Options();
        options.addOption(new Option("i", "input", true, "Input file path, if not specified use standard input."));
        options.addOption(new Option("o", "output", true, "Output file path, if not specified use standard output."));

        CommandLineParser parser = new DefaultParser();

        String inputPath;
        PrintWriter printWriter;
        try {
            CommandLine cmd = parser.parse(options, args);
            if(cmd.hasOption("i")) {
                inputPath = cmd.getOptionValue("i");
            } else {
                usage();
                return;
            }
            if(cmd.hasOption("o")) {
                String outputPath = cmd.getOptionValue("o");
                printWriter = new PrintWriter(outputPath);
            } else {
                printWriter = new PrintWriter(System.out);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            usage();
            return;
        }


        // initialize simulation with parameters specified in file
        Simulation sim = new Simulation(inputPath);

        // write header line to output file
        printWriter.println("# populationModel.Simulation run on " + Calendar.getInstance().getTime());
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
