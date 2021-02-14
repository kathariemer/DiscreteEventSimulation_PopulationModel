%% Calibration Section 
clc; close all; clear all;

% Comparison Data/1000 
dataArray.female = [4285 4296 4309 4328 4352 4384 4427 4460 4483 4501 4522];
dataArray.male = [4066 4078 4098 4123 4155 4200 4272 4312 4338 4357 4378];

% No. of MonteCarlo Simulations
GoNo = 100;

% Choose error function. Options: 'maxError', 'meanError', 'lastValueError'
% Choose tolerances
method = 'lastValueError'; tolf = 10; tolm = 10; 

% Calibration
[par,erff,erm,m,errcont] = CalAlgMulti(dataArray,GoNo,method,tolf,tolm); 

% Error values display for optimal fit
formatSpec1 = 'Error Values: \nmaxErr_f = %4.0f, meanErr_f = %4.0f, lastValErr_f = %4.0f\nmaxErr_m = %4.0f, meanErr_m = %4.0f, lastValErr_m = %4.0f\n\n'; 
fprintf(formatSpec1, errcont); 


%% Plot result for Validation Scenario

time1 = 2010:1:2020;
figure(1)
hold on
scatter(time1,dataArray.female(:),'r','filled');
scatter(time1,dataArray.male(:),'b','filled');
plot(time1,m.meanFemale(:),':xr'); 
plot(time1,m.meanMale(:),':xb');
legend('data f','data m', 'fit f', 'fit m','Location','southeast');
title('Calibrated Data Fit - Validation Scenario - Scale 1:1000');
annotation('textbox', [0.15, 0.8, 0.1, 0.1], 'String', ["method = " + method, "err_f = " + round((erff)), "err_m = " + round((erm))]);
hold off


%% Simulate Forecast Scenario, via MonteCarlo-ing

% Create new initial values
init = [dataArray.female(11),dataArray.male(11)];

% MonteCarlo-ing 
[f,~] = MonteCarloPop(GoNo,par,init);

forecastPop = f.meanFemale(11) + f.meanMale(11);

formatSpec2 = 'Population Forecast for 1.1.2030:\nFemale Population = %4.0f\nMale Population = %4.0f\nTotal Population = %4.0f\n'; 
fprintf(formatSpec2, f.meanFemale(11)*1000, f.meanMale(11)*1000, forecastPop*1000);

% Plot Forecast Scenario
time2 = 2020:1:2030;
figure(2)
hold on
plot(time2,f.meanFemale(:)*1000,':xr');
plot(time2,f.meanMale(:)*1000,':xb'); 
legend('forecast female pop.','forecast male pop,','Location','southeast');
title('Forecast Scenario - Scale 1:1');
annotation('textbox', [0.15, 0.75, 0.1, 0.1], 'String', ...
          ["prediction 1.1.2030", "pop_f = " + f.meanFemale(11)*1000, "pop_m = " + f.meanMale(11)*1000, "pop = " + forecastPop*1000]);
hold off