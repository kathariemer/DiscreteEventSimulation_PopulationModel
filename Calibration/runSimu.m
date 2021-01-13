%javaclasspath

programPath = fullfile("..", "PopulationModel", "target", "PopulationModel-1.0-SNAPSHOT.jar");

resFile = "result01.csv";
system(strcat("java -cp ", programPath, " populationModel.Main -i init.txt -o ", resFile));

res = readtable(resFile);
mean(res.populationF + res.populationM)