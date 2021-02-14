# DiscreteEventSimulation_PopulationModel

## Calibration

A folder of Matlab scripts which integrate the Java application and execute the parameter calibration. The table gives an overview of the files and their purpose.

\begin{table}
    \begin{center}
    \begin{tabularx}{14cm}{|Sl|X|}
    \hline
    File Name                &   Purpose \\
    \hline \hline
    !init.txt!               &   !*.txt! file holding the initial configuration \\
    \hline
    !runBetterSimu.m!        &   Brief setup to demonstrate \Java~integration in \Matlab \\
    \hline
    !setupJava.m!            &   \Java-\Matlab~connection setup \\
    \hline
    \hline
    !CalAlgMulti.m!          &   Calibration algorithm function \\
    \hline
    !CalibrationScript.m!    &   Script for calibration and forecasting \\
    \hline
    !dataArray.m!            &   \Matlab~class holding the data from \textit{Statistik Austria} \\
    \hline
    !err.m!                  &   Error function used in !CalAlgMulti.m! \\
    \hline
    !meanArray.m!            &   \Matlab~class holding the mean values from the Monte Carlo Simulation \\
    \hline
    !MonteCarloPop.m!        &   Monte Carlo Simulation function \\
    \hline
    \end{tabularx}
    \end{center}
\end{table}

The working file is called ```CalibrationScript.m```. It contains the setup for running the calibration function ```CalAlgMulti.m```, which is the implementation of the SA algorithm, and the forecast scenario simulation in a separate section. Both ```CalibrationScript.m``` and ```CalAlgMulti.m``` use the Monte Carlo Simulation function ```MonteCarloPop.m```, which runs 100 simulations under the current parameter setup and returns the average population numbers for each year. 

As can be seen in the table above, ```err.m``` is the error function used in the SA algorithm. The classes ```dataArray``` and ```meanArray``` have been defined for easier handling of the data and find application throughout all files. The function ```MonteCarloPop.m``` is the only one calling the Java simulation, so the initial conditions in ```init.txt``` are only used by this function. 


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
