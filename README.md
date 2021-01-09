# DiscreteEventSimulation_PopulationModel

Java/Maven Project

* Usage: ```program -i <path_to_input_file> [-o <path_to_output_file>]```
    + if no output file is specified, the program prints to stdout
    + the simulation's runtime is printed to stderr (in milliseconds)
* Input file: specify parameters in format ```parameterName = parameterValue```
    + example parameter file in ```PopulationModel/src/main/init.txt```
* Output: population statistics in csv-format
   + 1st line: Date of program execution
   + 2nd line: csv header
