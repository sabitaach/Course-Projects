Goal: Develop an approach to automatically assign labels to travel related questions based on the expected answer type.


There are 2 packages in our project.
[1] Under C521 Project, AnnotationFile, FileReduction8K and MoreAnnotation java files are created to help in manual annotation. It will display each question to the user for annotation. User needs to enter numbers for corresponding categories. The annotated data is then copied in one text file. CategorizeData file is used to divide the  queries as per the categories into seperate files. InputDataForTopicModeling is a java program written to convert the dataset format into a format required for topic modeling tools. We also created a program to test Geonames web service for location detection but we have not added that file in the submitted code.

[2] Under FeatureExtractionAndWEka package, CheckUniquePOS features, extract_all_unigrams and extract_all_bigrams,extract_all_trigrams, UnigramBigramFeatures, unigramsANDposlistFeatures are files to extract different features. csvToarffGen file converts the csv format stored feature vector into ARFF format required by WEKA tool.

Each file is run seperately and is independent of others. All files store their output either in different csv files or text files. Before running any program one has to ensure that the required input files are already created. 

Tools Used:

1. Stanford Parser
2. Stanford caseless Name Entity Recognition
3. Geonames webservice
4. Stanford Topic Model
5. MALLET
6. WEKA 

MALLET and WEKA are explained in brief in the project report. 
