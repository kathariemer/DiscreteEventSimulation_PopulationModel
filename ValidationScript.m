clc; close all; clear all; 

% Comparison Data/1000 
dataArray.female = [4285 4296 4309 4328 4352 4384 4427 4460 4483 4501 4522];
dataArray.male = [4066 4078 4098 4123 4155 4200 4272 4312 4338 4357 4378];

% No. of MonteCarlo Simulations
GoNo = 100;

% MonteCarlo-ing
[m,par] = MonteCarloPop(GoNo,[],[]);

% Error Calculation
[maxF,maxM] = err(m,dataArray,'maxError'); 
[meanF,meanM] = err(m,dataArray,'meanError'); 
[lastValF,lastValM] = err(m,dataArray,'lastValueError');

% Return ErrorValues to Command Window
formatSpec1 = 'Error Values: \nmaxErr_f = %4.0f, meanErr_f = %4.0f, lastValErr_f = %4.0f\nmaxErr_m = %4.0f, meanErr_m = %4.0f, lastValErr_m = %4.0f\n\n'; 
fprintf(formatSpec1, maxF,meanF,lastValF,maxM,meanM,lastValM); 

% Plot for initial values
time1 = 2010:1:2020;
figure(1)
hold on
scatter(time1,dataArray.female(:),'r','filled');
scatter(time1,dataArray.male(:),'b','filled');
plot(time1,m.meanFemale(:),':xr'); 
plot(time1,m.meanMale(:),':xb');
legend('data f','data m', 'fit f', 'fit m','Location','southeast');
title('Initial Data Fit - Validation Scenario - Scale 1:1000');
hold off