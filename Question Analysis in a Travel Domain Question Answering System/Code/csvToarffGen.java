package FeatureExtractionAndWeka;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class csvToarffGen {

	public static void main(String[] args) throws IOException {
		
	//	 InputStream reader = new FileInputStream("travelqaFeat.csv"); 
	//	 File writer = new File("travelqaFeatARFF.arff");  // For unigrams only 
		 
		 
		 /*InputStream reader = new FileInputStream("travelqaFeatUnigramsPOSlist.csv"); // For unigrams and entire sentence pos list 
		 File writer = new File("travelqaFeatUnigramsPOSlistARFF.arff");*/
	/*	 
		 InputStream reader = new FileInputStream("travelqaFeatBigramsNew.csv"); // For bigrams 
		 File writer = new File("NEWtravelbigramsARFF.arff");*/
		 
		/* InputStream reader = new FileInputStream("traveltrigrams.csv"); // For trigrams 
		 File writer = new File("traveltrigramsARFF.arff");
	     */
		
		InputStream reader = new FileInputStream("UniBiPOS.csv"); // For unigrams + bigrams
		 File writer = new File("UniBiPOSARFF.arff");
	     
	 
		 	CSVLoader loader = new CSVLoader();
		    loader.setSource(reader);
		    Instances data = loader.getDataSet();	  
		 
		    // save ARFF
		    ArffSaver saver = new ArffSaver();
		    saver.setInstances(data);
		    saver.setFile(writer);
		//    saver.setDestination(writer1);
		    saver.writeBatch();
		    
		    System.out.println("Done");

	}

	
}
