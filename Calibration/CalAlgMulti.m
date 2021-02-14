function [p,ef,em,j,f] = CalAlgMulti(dataArray,GoNo,opt,tolf,tolm)
%CalAlg is a calibration algorithm for population model via multivariant normal distribution, uses
%MonteCarloPop, err, dataArray class and meanArray class

% MonteCarlo-ing
[meanMC,par] = MonteCarloPop(GoNo,[],[]);

% Initial Error according to chosen error function
[erff,erm] = err(meanMC,dataArray,opt); % disp(erff); disp(erm);
fprintf('Chosen error function: '); disp(opt);
    
% Set temperature, decline
temp = 100; d = 0.8;

% Initial Gauss distribution
mu = par; sigma = diag([10 0.01 10 0.02 1 0.02 100 1 10 0.001]);

% Counter
count = 1;

% Saves correct values in meanArray class, errors
j = meanArray;
f = zeros(3,2);

while (erff >= tolf || erm >= tolm) && count < 101 
    aux = mvnrnd(mu,sigma,1);
    gaussrand = aux;
    gaussrand(2) = abs(aux(2)); gaussrand(4) = abs(aux(4)); gaussrand(6) = abs(aux(6)); gaussrand(10) = abs(aux(10));
    % fixed problem with too rapid birth rate
    if gaussrand(2) > 0.03
        gaussrand(2) = gaussrand(2)*0.01;
    end
    [k,~] = MonteCarloPop(GoNo,gaussrand,[]);
    [erfTemp,ermTemp] = err(k,dataArray,opt);
    fprintf('Run %d \nerfTemp = %d \nermTemp = %d \n\n', count, erfTemp, ermTemp)
    if (erfTemp < erff && ermTemp < erm) || rand() < exp(-(erfTemp-erff)/temp) || rand() < exp(-(ermTemp-erm)/temp)
        erff = erfTemp;
        erm = ermTemp;
        par = gaussrand;
        j.meanFemale = k.meanFemale; 
        j.meanMale = k.meanMale; 
        % Save max error, mean error, lastValError of solution for comparison
        f(1,1) = max(abs(k.meanFemale(:)-dataArray.female(:)));
        f(2,1) = mean(abs(k.meanFemale(:)-dataArray.female(:)));
        f(3,1) = abs(k.meanFemale(11)-dataArray.female(11)); 
        f(1,2) = max(abs(k.meanMale(:)-dataArray.male(:))); 
        f(2,2) = mean(abs(k.meanMale(:)-dataArray.male(:)));
        f(3,2) = abs(k.meanMale(11)-dataArray.male(11));
    end
    mu = par;
    sigma = sigma*temp/100;
    temp = temp*d;
    count = count + 1;
end
    
p = par;
ef = erff;
em = erm; 

end