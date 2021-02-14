function [meanVal,p] = MonteCarloPop(GoNo,par,init)
%MonteCarloPop summarises the output of GoNo runs for 11 years (2010-2020),
%but time_steps MUST BE 11 in init.txt, needs meanArray.m class
%definition in folder Calibration
    
    % Auxiliary Variables
    N = zeros(11,9); 
    M = zeros(GoNo,22);
    
    % MonteCarlo-ing
    if isempty(par) == 1
        for j = 1:GoNo
            simu = javaObject("populationModel.ConfigurableSimulation","init.txt");               
            for i = 1:11
                N(i,:) = simu.step();
            end
                M(j,1:11) = N(:,2);
                M(j,12:22) = N(:,3);
        end
    else
        for j = 1:GoNo
           % need to sim through with original values, otherwise warning
           % Simulation still runnable occurs. THEN reset parameters. 
           simu = javaObject("populationModel.ConfigurableSimulation","init.txt"); 
           for i = 1:11
                N(i,:) = simu.step();
           end
           simu.resetAll(par);
           if isempty(init) == 0
               simu.resetInitialPopulation(init(1),init(2));
           end
           for i = 1:11
                N(i,:) = simu.step();
            end
                M(j,1:11) = N(:,2);
                M(j,12:22) = N(:,3);
        end
    end
        
    % Put values in meanArray struct
    meanArray.meanFemale(:) = round(mean(M(:,1:11)));
    meanArray.meanMale(:) = round(mean(M(:,12:22)));

    % return struct
    meanVal = meanArray;
    p = simu.getParams();

end

