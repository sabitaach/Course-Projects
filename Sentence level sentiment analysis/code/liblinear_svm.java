package setiment_analysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import wlsvm.WLSVM;

public class liblinear_svm {

	public static void main(String[] args) throws Exception {

		String filenm="C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/featARFF.arff";
		BufferedReader breader=new BufferedReader(new FileReader(filenm));

		Instances train=new Instances(breader);
		train.setClassIndex(train.numAttributes()-1);
		breader.close();

		WLSVM svm = new WLSVM();
	//	NaiveBayes svm=new NaiveBayes();
	//	svm.setNormalize(1);
		svm.buildClassifier(train);
		Evaluation eval=new Evaluation(train);
		
		
		eval.crossValidateModel(svm, train,10, new Random(1));
		
		System.out.println(eval.toSummaryString("\nResults\n******\n",true));
		
	
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
