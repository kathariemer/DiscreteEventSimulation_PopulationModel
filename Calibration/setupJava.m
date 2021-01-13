% JUST CONSIDER runSimu.m !!!
% before running the program from matlab, we need to tell matlab where the
% file can be found. 
% Therefore we append the path to the JAR file to the javaclasspath.txt
% file in prefdir:

% file destination
prefdir

% IMPORTANT: run this section in git folder!!!
% build absolute path on your machine
jarName = "PopulationModel-1.0-SNAPSHOT.jar";
path = fullfile(what("..").path, "PopulationModel", "target", jarName);

% open (create if not exists) file and append 1 line 
fid = fopen(fullfile(prefdir, 'javaclasspath.txt'),'a');
fprintf(fid, "%s\n", path);
fclose(fid);

% NOW: restart matlab
% now all java methods are available
% i.e. in class Simulation we have method next()
% could call ```populationModel.Simulation.next()```
% but actually, this sucks :/
