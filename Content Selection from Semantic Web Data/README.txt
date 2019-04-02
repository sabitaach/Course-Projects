This project is based on the content selection challenge. Its main objective can be stated as: Given a set of RDF triples and text from Wikipedia, identify triples that are reflected in the target text.


There are 4 classes in total.

1)ListFilesUtil.java 
Since the data provided by the content selection challenge is grouped according to the names of different people,this class goes through each folder and extracts the triples. Also, the triples provided by the challenge do not have the subject name included. So we extract the name of the person from the folder name and append it to the triples so that we have triples in  subject-predicate-object format.

2) ExtractAllText.java
This class  goes through each folder and extracts all the sentences(from Wikipedia) that are provided. All the sentence are then copied over to a single text folder so that we do not need to explore the folders each time we  run the code

3)PreparingTestData
This class helps in generating patterns from the sentences by replacing the subject and object by variables. For nationality, we are also using a list of nation/nationality pair that is in nationalities.txt so that those sentences where nationality is given insted of the nation name can be identified. Also, we check for abbreviations of nation name. For example, United Nations can be represented by UN. For nationality property, the subject is always a person . So we check to see if either the full name or the first name or the last name is present in the sentence too.

4)FilteringKNN.java
Once all the patterns are extracted from 3, this class tries to remove incorrect patterns by applying K nearest neighbor algorithm. Since we need training and testing data for this, from the patterns obtained above, we perform cross validation in order to prepare different datasets for training and testing. From the training data, we seperate positive and negative data into different data structures. Then we pass all the training and testing patterns through dependency parser in order to extract features. All the obtained features are again stored. By now we have 3 different sets. One has the features for positive training data, another has the features for negative training data and the third one has features for the testing data.
Then, for each feature from testing set, we compare the similarity with all elements of both positive and negative training sets  and determine whether the test sentence has positive label or negative label.
