# DiscreteEventSimulation_PopulationModel

## Calibration

### setupJava.m

Update the matlab environment to invoke methods from the java program. This only has to be run once (but should do no harm when repeated).  

It adds the path to the jar file, and therefore allows to run the simulation in MATLAB's environment. Since I do not know your folder structure, the script relies on the relative path from the ```Calibration``` folder to the ```JAR```. Make sure to run this in the ```Calibration``` folder. 

### Overview of scripts

A folder of Matlab scripts which integrate the Java application and execute the parameter calibration. The table gives an overview of the files and their purpose.

|File Name |   Purpose |
|---|---|
|    `init.txt`               |   `*.txt` file holding the initial configuration |
|    `runBetterSimu.m`        |   Brief setup to demonstrate Java-integration in Matlab |
|    `setupJava.m`            |   Java-Matlab-connection setup |
|    `CalAlgMulti.m`          |   Calibration algorithm function |
|    `CalibrationScript.m`    |   Script for calibration and forecasting |
|    `dataArray.m`            |   Matlab-class holding the data from _Statistik Austria_ |
|    `err.m`                  |   Error function used in `CalAlgMulti.m` |
|    `meanArray.m`            |   Matlab-class holding the mean values from the Monte Carlo Simulation|
|    `MonteCarloPop.m`        |   Monte Carlo Simulation function |

The working file is called ```CalibrationScript.m```. It contains the setup for running the calibration function ```CalAlgMulti.m```, which is the implementation of the Simulated Annealing algorithm, and the forecast scenario simulation in a separate section. Both ```CalibrationScript.m``` and ```CalAlgMulti.m``` use the Monte Carlo Simulation function ```MonteCarloPop.m```, which runs 100 simulations under the current parameter setup and returns the average population numbers for each year. 

As can be seen in the table above, ```err.m``` is the error function used in the Simulated Annealing algorithm. The classes ```dataArray``` and ```meanArray``` have been defined for easier handling of the data and find application throughout all files. The function ```MonteCarloPop.m``` is the only one calling the Java simulation, so the initial conditions in ```init.txt``` are only used by this function. 


## PopulationModel

A Java/Maven Project

* Usage: ```program [-i <path_to_input_file> -o <path_to_output_file> -r <number_of_repetitions>]```
    + if no input file is specified, the project's example file is used
    + if no output file is specified, the program prints to stdout
    + if no repetition count is specified, the program loops through the simulation once
    + the simulation's runtime is printed to stderr (in seconds)
* Input file: specify parameters in format ```parameterName = parameterValue```
    + example parameter file in ```Calibration/init.txt```
* Output: population statistics in csv-format
   + 1st line: Date of program execution
   + 2nd line: csv header

#### Building and running the JAR file

In the ```PopulationModel/``` directory run

1. ```mvn install```
1. ```mvn compile```
1. ```mvn package```
1. ```java -cp target/PopulationModel-1.0-SNAPSHOT.jar populationModel.Main``` with desired program arguments.
