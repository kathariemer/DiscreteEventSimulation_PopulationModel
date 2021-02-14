function [ef,em] = err(meanArray,dataArray,method)
    switch method
        case{'maxError'}
            ef = max(abs(meanArray.meanFemale(:)-dataArray.female(:)));
            em = max(abs(meanArray.meanMale(:)-dataArray.male(:))); 
        case{'meanError'}
            ef = mean(abs(meanArray.meanFemale(:)-dataArray.female(:)));
            em = mean(abs(meanArray.meanMale(:)-dataArray.male(:)));
        case{'lastValueError'}
            ef = abs(meanArray.meanFemale(11)-dataArray.female(11)); 
            em = abs(meanArray.meanMale(11)-dataArray.male(11));
    end
end