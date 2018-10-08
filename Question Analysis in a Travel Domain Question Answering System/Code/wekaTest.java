/*package FeatureExtractionAndWeka;
//used for performing various tests on Weka
//takes in the training and testing files in arff format and //experiments with different classifiers
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Instances;
import wlsvm.WLSVM;

public class wekaTest {

	public static void main(String[] args) throws Exception {

		
		String filenm="travelqaFeatARFF.arff";
	//	String test_arff= path +"/testARFF.arff";
		
		
		BufferedReader breader=new BufferedReader(new FileReader(filenm));
		BufferedReader treader=new BufferedReader(new FileReader(test_arff));
		
		int seed=1;
		int folds=5;
		
		Instances data=new Instances(breader);
		Instances tdata=new Instances(treader);
		
		
		
		 Random rand = new Random(seed);   // create seeded number generator
		 Instances randData = new Instances(data);   // create copy of original data
		 randData.randomize(rand);
		 
		 Random trand = new Random(seed);   // create seeded number generator
		 Instances trandData = new Instances(tdata);   // create copy of original data
		 trandData.randomize(trand);

	//	 randData.stratify(folds);
	//	train.setClassIndex(train.numAttributes()-1);
		breader.close();
		treader.close();
		
		
		int temp=0;
		for (int n = 0; n < folds; n++) {
			temp=n+1;
			System.out.println("-------------Iteration"+temp+"-----------");
			   Instances train = randData.trainCV(folds, n,rand);
			  
			   train.setClassIndex(train.numAttributes()-1);
			   Instances test = trandData.testCV(folds, n);
			   test.setClassIndex(test.numAttributes()-1);
			   
			  			 
			   // further processing, classification, etc.

				NaiveBayesMultinomial svm = new NaiveBayesMultinomial();
				//	WLSVM svm= new WLSVM();

				svm.buildClassifier(train);
				
				Evaluation eval=new Evaluation(test);
				eval.crossValidateModel(svm, test,10, new Random(1));
				
				
				System.out.println(eval.toSummaryString("\nResults\n******\n",true));
				System.out.println();
				System.out.println();
				
				System.out.println(eval.toClassDetailsString());
				
				System.out.println("-------------Iteration"+temp+" completed-----------");
				
			
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println(eval.toMatrixString());
				
				System.out.println();
				System.out.println();
				System.out.println();
				
				System.out.println(eval.toClassDetailsString());
				System.out.println();
				System.out.println();
				System.out.println();
				
				
				
				System.out.println(eval.fMeasure(1)+" "+eval.precision(1)+" "+eval.recall(1));

				System.out.println("False positive:"+eval.numFalsePositives(1) );
				System.out.println("False negative"+eval.numFalseNegatives(1));
				
				
				
			 }
	
	    

	}



}
*/