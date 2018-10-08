fid = fopen('SMSSpamCollection');            % read file
data = fread(fid);
fclose(fid);
lcase = abs('a'):abs('z');
ucase = abs('A'):abs('Z');
caseDiff = abs('a') - abs('A');
caps = ismember(data,ucase);
data(caps) = data(caps)+caseDiff;     % convert to lowercase
%data(data == 9) = abs(' ');          % convert tabs to spaces
validSet = [9 10 abs(' ') lcase];         
data = data(ismember(data,validSet)); % remove non-space, non-tab, non-(a-z) characters

char(data(1:100))';                   % view the first 100 characters
data = char(data);                    % convert from vector to characters
words = strread(data, '%s');         % convert from character to strings

% split into examples
count = 0;
examples = {};

for (i=1:length(words))%the 'words' contains all the strings that are there in the text. 
   if (strcmp(words{i}, 'spam') || strcmp(words{i}, 'ham'))%if either the "spam" or "ham"tem occurs, then the count is incremented
       count = count+1;
       examples(count).spam = strcmp(words{i}, 'spam');%saves if the term is a spam or a ham
       examples(count).words = [];%initializes an array where the terms of the particular line are going to go
   else
       examples(count).words{length(examples(count).words)+1} = words{i};% this does the job of filling in the words in the array initialized above
   end
end
%display('here are the things that I want to test')
%display(examples(1).spam)
%display('\n and the words that are in it')
%display(examples(1).words)

%split into training and test
random_order = randperm(length(examples));
train_examples = examples(random_order(1:floor(length(examples)*.9)));
test_examples = examples(random_order(floor(length(examples)*.9)+1:end));

% count occurences for spam and ham

spamcounts = java.util.HashMap;
numspamwords = 0;
hamcounts = java.util.HashMap;
numhamwords = 0;
lengthsOfTest=java.util.HashMap;

alpha = 0.1;

for (i=1:length(train_examples))
    for (j=1:length(train_examples(i).words))
        word = train_examples(i).words{j};
        if (train_examples(i).spam == 1)
            numspamwords = numspamwords+1;
            current_count = spamcounts.get(word);
            if (isempty(current_count))
                spamcounts.put(word, 1+alpha);    % initialize by including pseudo-count prior
            else
                spamcounts.put(word, current_count+1);  % increment
            end
        else
            numhamwords = numhamwords+1;
            current_count = hamcounts.get(word);
            if (isempty(current_count))
                hamcounts.put(word, 1+alpha);    % initialize by including pseudo-count prior
            else
                hamcounts.put(word, current_count+1);  % increment
            end
        end
    end    
end

% populating the length of each line in the test--all the unique lengths
% and the test number that have those length are stored in the lengthsOfTest
% hashmap

for(jj=1:length(test_examples))
    theLength=length(test_examples(jj).words);
    a=lengthsOfTest.get(theLength);
    if(isempty(a))
        lengthsOfTest.put(theLength,jj);
    else
        a(end+1)=jj;
        lengthsOfTest.put(theLength,a);
   end
        
end

theKey=cell2mat(lengthsOfTest.keySet.toArray.cell)
% here we are trying to group the test examples according to their length

groupedTest=java.util.HashMap;
maxVal=max(theKey);minVal=min(theKey);
%creating an interval of 5
i=minVal;
while i<=maxVal
    theRange=i;
    for(b=1:length(theKey))
        theIdFromTest=theKey(b);
        if(theIdFromTest>=i & theIdFromTest<=i+1)
            arr1=lengthsOfTest.get(theIdFromTest);
            if(isempty(groupedTest.get(theRange)))
                groupedTest.put(theRange,arr1);
            else
                t1=groupedTest.get(theRange);
                arr1=union(t1,arr1);
                groupedTest.put(theRange,arr1);
            end
        end
    end
    i=i+2;
end

                
    
    
theKey11=cell2mat(groupedTest.keySet.toArray.cell)%these are the keys from the grouped hashmap




   
 %now finding out the accuracy for all the data of the same length--need to
 %check this part--Sabita
 
 
 
 lengthAndAccuracy=java.util.HashMap;
 priorOfHam=numhamwords/(numspamwords+numhamwords);
 priorOfSpam=numspamwords/(numspamwords+numhamwords);
 
for(gg=1:length(theKey11))%this is looping through all the possible lengths
     c=groupedTest.get(theKey11(gg));
     tp=0;tn=0;fp=0;fn=0;
     for(ff=1:length(c))%getting each test data for the length
         testId=c(ff);
         probHam=1;spam1=0;ham1=0;
         probSpam=1;
         for(ww=1:length(test_examples(testId).words))
             theWord=test_examples(testId).words(ww);
             %display(theWord)
             %t1=spamcounts.get(java.lang.String(theWord))
             %t2=(probSpam*spamcounts.get(java.lang.String(theWord)))
             %t3=(numspamwords+alpha*20000)
             countFromSpam=spamcounts.get(java.lang.String(theWord));
             if(isempty(countFromSpam))
                 countFromSpam=0;
             end
             countFromHam=hamcounts.get(java.lang.String(theWord));
             if(isempty(countFromHam))
                 countFromHam=0;
             end
             probSpam=probSpam*(countFromSpam+alpha)/(numspamwords+alpha*20000);
             probHam=probHam*(countFromHam+alpha)/(numhamwords+alpha*20000);
         end
         probHam=probHam*priorOfHam;
         probSpam=probSpam*priorOfSpam;
         if(probSpam>=probHam)
             spam1=1;
         else
             ham1=1;
         end
         if spam1==1 & test_examples(testId).spam == 1
             tp=tp+1;
         elseif spam1==1 & test_examples(testId).spam==0
             fn=fn+1;
         elseif spam1==0 & test_examples(testId).spam==1
             fp=fp+1;
         else
             tn=tn+1;
         end
     end
     display('***now the results***')
     accuracy=(tp+tn)/(tp+fp+fn+tn)
     display(theKey11(gg))
     display(tp+tn)
     display(tp+fp+fn+tn)
     lengthAndAccuracy.put(theKey11(gg),accuracy);
     display('******************')
end

  keysInOrder=sort(theKey11);
  pd=1;
  for(nn=1:length(keysInOrder))
      theLength(pd)=keysInOrder(nn);%strcat(num2str(theKey11(nn)),'-',num2str(theKey11(nn)+4));
      accuracy(pd)=lengthAndAccuracy.get(keysInOrder(nn));
      pd=pd+1;
  end
  
  scatter(theLength,accuracy)
 %plot(theLength,accuracy)
% histogram(accuracy,lengthOfTest)
%  xlabel('length of data')
 % ylabel('accuracy')
 
 
 
% for(gg=1:length(theKey))
 %    theKey(gg);
  %   lengthsOfTest.get(theKey(gg));
   %  display(length(lengthsOfTest.get(theKey(gg))))
 %end
     

 

    
% will need to check if count is empty!

% ... 