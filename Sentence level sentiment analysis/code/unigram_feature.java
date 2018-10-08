package setiment_analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.jar.Attributes;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.mi.supportVector.*;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.Instances;
import wlsvm.WLSVM;



public class unigram_feature {

	private StringBuffer path;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		 Hashtable<String,Double> lexicon=new Hashtable<String,Double>();
		 lexicon= gethashtable();
		 int numAtts = lexicon.size();
		 System.out.println("size of lexicon:"+numAtts);	
		gen_csvfile(lexicon);
		gen_featvector(numAtts,lexicon);
		
		
		
		
		
		 System.out.println("done");


		
	}

	public static  Hashtable<String,Double>  gethashtable() throws ParserConfigurationException, SAXException, IOException  {
		
		Hashtable<String,Double>temp_hashtable=new Hashtable<String,Double>();
		
		
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		String file_nm="C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/pos_lexicon.xml";
		
		    SAXParser parser = parserFactor.newSAXParser();		
		    SAXHandler handler = new SAXHandler();		
		    parser.parse(file_nm, handler);		    
		    temp_hashtable=handler.lexicon;
		    return temp_hashtable;		
		
		
		
	}

	private static void gen_csvfile(Hashtable<String, Double> lexicon) throws IOException {		
		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/featCSV.csv"));
		
		StringBuilder sbhead=new StringBuilder();
		String head=null;
		// writing header portion of csv file reqd while converting to arff format
		for(int ind=0;ind<lexicon.size();ind++){
			head="att"+(ind+1);
			sbhead.append(head);
			sbhead.append(",");
		}
		
		sbhead.append("result");
		writer.write(sbhead.toString());
		writer.newLine();
		writer.close();
		
		BufferedWriter writer1 = new BufferedWriter(new FileWriter("C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/featCSV.csv",true));
		
		BufferedReader reader = new BufferedReader(new FileReader("C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/train_data.txt"));
		String line=null;
		String result=null;
		
		while((line=reader.readLine())!=null){	
			
		ArrayList<String> array=new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
	    result= gen_featarr(lexicon,array,line);		
		
		for (String element : array) {
		 sb.append(element);
		 sb.append(",");
		}

		sb.append(result);
		writer1.write(sb.toString());
		writer1.newLine();		
		
		}
		
		writer1.close();
		reader.close();
		
		
		
		
		
		
	}

	private static String gen_featarr(Hashtable<String, Double> lexicon,
			ArrayList<String> array,String line) throws IOException {
		
		String mod_line=null;
		String[] txt=null;
		int start_pt=2;
		String act_word=null;
		String result=null;
		String defvalue="0";
		char class_res='+';
		
					
			if(line.charAt(0)==class_res){
				result="yes";
			
			}else{
				result="no";
			}
			
			mod_line=line.substring(start_pt);			
			txt=mod_line.split(" ");
			
			Enumeration ene=lexicon.keys();
			String str1=null;
			boolean flag=false;
			
			while(ene.hasMoreElements()){
				str1=ene.nextElement().toString();
				flag=false;
				for(String word:txt){
					if(checkPOStag(word)){					
						act_word=getWord(word);					
						if(str1.equals(act_word)){
							flag=true;
							break;
						}
					}else{
						continue;
					}
				}
				
				if(flag==true){
					array.add(lexicon.get(act_word).toString());						
				}else{
					array.add(defvalue);
				}
				
				
			}
			
			
			
			return result;
			
	


		
		
		
	}

	private static void gen_featvector(int numAtts,Hashtable<String,Double>lexicon) throws IOException {
		
		 InputStream reader = new FileInputStream("C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/featCSV.csv");   
	     File writer = new File("C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/featARFF.arff");
	  //   File writer1 = new File("featARFF.arff");
	     
	      
		
		
		 	CSVLoader loader = new CSVLoader();
		    loader.setSource(reader);
		    Instances data = loader.getDataSet();	  
		 
		    // save ARFF
		    ArffSaver saver = new ArffSaver();
		    saver.setInstances(data);
		    saver.setFile(writer);
		//    saver.setDestination(writer1);
		    saver.writeBatch();
		
		
		
		
	}

	
	

	private static String getWord(String word) {
		String act_word=null;
		if(word.contains("//")){
			act_word=word.split("//")[0].toLowerCase();	
			//System.out.println(act_word);
	
		}else if(word.contains("/")){
			
			act_word=word.split("/")[1].toLowerCase();
			
								
		}
		
		return act_word;
	}

	private static boolean checkPOStag(String word) {
		// TODO Auto-generated method stub
		boolean flag=false;
		ArrayList<String> reqd_POS=new ArrayList<String>();
//		reqd_POS.add("FW");
		reqd_POS.add("JJ");
		reqd_POS.add("JJR");
		reqd_POS.add("JJS");
		reqd_POS.add("NN");
		reqd_POS.add("NNS");
//		reqd_POS.add("NNP");
//		reqd_POS.add("NNPS");
//		reqd_POS.add("PDT");
		reqd_POS.add("RB");
		reqd_POS.add("RBR");		
		reqd_POS.add("RBS");
		reqd_POS.add("VB");
		reqd_POS.add("VBN");
		reqd_POS.add("VBP");
		reqd_POS.add("VBZ");
		
		for(String pos:reqd_POS){
		
			if(word.contains(pos)){
				flag=true;
				break;			
			}
		
		}
		
		return flag;
	}

}

class SAXHandler extends DefaultHandler {
	
	 Hashtable<String,Double> lexicon=new Hashtable<String,Double>();
	 String key;
	 Double tfidf=0.0d;
	 String content;


	 public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			
			   }

	 @Override	
	   public void endElement(String uri, String localName,String qName) throws SAXException {
	 
	    switch(qName){
	
	      //Add the entry to hashtable once end tag is found	
	      case "void":	
	        lexicon.put(key, tfidf);  	
	        break;
	
	      //For all other end tags the employee has to be updated.	 
	      case "string": 
	        key = content;
	        break;
	 
	      case "double":	 
	        tfidf = Double.parseDouble(content);
	        break;		
	    }

	   }
	 
	 
	 @Override
	
	   public void characters(char[] ch, int start, int length)throws SAXException {
	
	     content = String.copyValueOf(ch, start, length).trim();
	 
	   }


	 
	 
	 
	 
	 
	
}		
	
	