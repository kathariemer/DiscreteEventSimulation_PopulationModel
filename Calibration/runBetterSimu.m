% make sure that javapath is current - in case of doubt run 'setupJava.m'
% script

% make java object with parameters from init, e.g. 5 time steps
simu = javaObject("populationModel.ConfigurableSimulation", "init.txt");

% print array-semantic (?)
simu.HEADER

% (unsuccessfully) try to set duration to 3 steps
simu.resetDuration(3)

% run simulation (if "out-of-time", an empty array is returned)
for i = 1:6
    simu.step()
end

% (successfully) change duration-parameter of simulation
simu.resetDuration(3)
simu.step()