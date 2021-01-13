# DiscreteEventSimulation_PopulationModel

## PopulationModel

A Java/Maven Project

* Usage: ```program [-i <path_to_input_file> -o <path_to_output_file>]```
    + if no input file is specified, the project's example file is used
    + if no output file is specified, the program prints to stdout
    + the simulation's runtime is printed to stderr (in seconds)
* Input file: specify parameters in format ```parameterName = parameterValue```
    + example parameter file in ```PopulationModel/src/main/init.txt```
* Output: population statistics in csv-format
   + 1st line: Date of program execution
   + 2nd line: csv header

#### Building and running the JAR file

In ```PopulationModel/``` directory run

1. ```mvn install```
1. ```mvn package```
1. ```java -cp target/PopulationModel-1.0-SNAPSHOT.jar populationModel.Main``` with desired program arguments.

## Calibration

A folder of matlab scripts which integrate the Java application

### runSimu.m

In ```PopulationModel``` there is a JAR which was built with java 8, therefore it is possible to invoke the command line call from ```matlab``` (which comes with java 8)

### setupJava.m

Under construction.  
Update the matlab environment to invoke methods from the java program. In it's current version, this is very unappealing, as the ```main``` method gets run with command line arguments. -.- 
