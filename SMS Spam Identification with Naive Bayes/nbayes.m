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
lengthOfTest=java.util.HashMap;


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

% populating the length of each line in the test
%docAndLength={};
for(jj=1:length(test_examples))
    theLength=length(test_examples(jj).words);
    allKey=keys(lengthOfTest)
        a=lengthOfTest.get(theLength);
        a(end+1)=jj;
        lengthOfTest.put(theLength,a);
    else
        lengthOfTest.put(theLength,jj);
    end
        
end
   
       
for(k=1:length(test_examples))
    probHam=1;
    probSpam=1;
    for(p=1:length(test_examples(i).words))
        probSpam=probSpam* spamcounts.get(p)/(numspamwords+alpha*20000)
        probHam=probHam*  hamcounts.get(p)/(numhamwords+alpha*20000)   
        %spamcounts.get('free')/(numspamwords+alpha*20000)   % probability of word 'free' given spam
        %hamcounts.get('free')/(numhamwords+alpha*20000)   % probability of word 'free' given ham
    end
end
    
% will need to check if count is empty!

% ... 