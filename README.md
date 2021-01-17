# DiscreteEventSimulation_PopulationModel

## Calibration

A folder of matlab scripts which integrate the Java application

### setupJava.m

Update the matlab environment to invoke methods from the java program. This only has to be run once (but should do no harm when repeated).  

It adds the path to the jar file, and therefore allows to run the simulation in MATLAB's environment. Since I do not know your folder structure, the script relies on the relative path from the ```Calibration``` folder to the ```JAR```. Make sure to run this in the ```Calibration``` folder. 

<!--
### runBetterSimu.m

A minimal demonstation of using the ```ConfigurableSimulation``` class in matlab. It initializes an instance, and demonstates a subset of the still limited number of methods.
-->

### runSimu.m

This should not be dependent on ```setupJava.m```. Since MATLAB is shipped with Java 8, this should run out of the box.  

It shows how to use MATLAB as a shell and runs the Java program's main method.



## PopulationModel

A Java/Maven Project

* Usage: ```program [-i <path_to_input_file> -o <path_to_output_file> -r <number_of_repetitions>]```
    + if no input file is specified, the project's example file is used
    + if no output file is specified, the program prints to stdout
    + if no repetition count is specified, the program loops through the simulation once
    + the simulation's runtime is printed to stderr (in seconds)
* Input file: specify parameters in format ```parameterName = parameterValue```
    + example parameter file in ```PopulationModel/init.txt```
* Output: population statistics in csv-format
   + 1st line: Date of program execution
   + 2nd line: csv header

#### Building and running the JAR file

In ```PopulationModel/``` directory run

1. ```mvn install```
1. ```mvn compile```
1. ```mvn package```
1. ```java -cp target/PopulationModel-1.0-SNAPSHOT.jar populationModel.Main``` with desired program arguments.
