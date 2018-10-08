package cs521Project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class FileReduction8K {
	public static String serializedClassifier = "edu/stanford/nlp/models/ner/english.all.3class.caseless.distsim.crf.ser.gz";
	public static AbstractSequenceClassifier<CoreLabel> classifier;
		

	public static void main(String[] args) throws IOException, ClassCastException, ClassNotFoundException{
		// TODO Auto-generated method stub
		
		String csvFile = "C:/Amruta/SNLP_PROJ_NOVEMBER/QuestionSet1.csv";
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		ArrayList<String> taglist = new ArrayList<String>();
		classifier= CRFClassifier.getClassifier(serializedClassifier);
	//	BufferedWriter bw1 = new BufferedWriter(new FileWriter("C:/Amruta/SNLP_PROJ_NOVEMBER/8kReducedTagbtwn1_2.txt"));
	//	BufferedWriter bw2 = new BufferedWriter(new FileWriter("C:/Amruta/SNLP_PROJ_NOVEMBER/8kReducedTagbtwn3_4.txt"));
	//	BufferedWriter bw3 = new BufferedWriter(new FileWriter("C:/Amruta/SNLP_PROJ_NOVEMBER/8kReducedTagMoreThan4.txt"));
		BufferedWriter bw4 = new BufferedWriter(new FileWriter("C:/Amruta/SNLP_PROJ_NOVEMBER/8kReducedTagEqual4.txt"));
		BufferedWriter bw5 = new BufferedWriter(new FileWriter("C:/Amruta/SNLP_PROJ_NOVEMBER/8kReducedTagEqual3.txt"));
				
		String line = "";
		int linecount =0;
		String[] text;
		String tagtext="";
		String linetext="";
		String splitreg = ",<";
		int newlinecnt1=0;
		int newlinecnt2=0;
		int newlinecnt3=0;
		int newlinecnt4=0;
		
		
		boolean locflag =false;
		
		taglist = genTagSet();
		int tagcnt =0;
		
		while((line = br.readLine())!=null){
			
			linecount++;
			
			if(linecount<=754){
				continue;
			}
			
			locflag=false;
			if(!line.contains(splitreg))
				continue;
			tagcnt =0;
			text = line.split(splitreg);
			if(text!=null && text.length!=0){
				linetext =text[0];
				tagtext = "<"+text[1];
				text = tagtext.split(">");
				for(String word: text){
					word = word.substring(1);
					word =word.replace("-", " ");
					word = checkLocation(word);
					if(word.equalsIgnoreCase("location") && locflag==false){
						locflag=true;
						tagcnt++;
					}
					for(String tagword:taglist){
						if(word.length()>3 && word.contains(tagword)){
							tagcnt++;
						}
					}
										
				}
				
				/*if(tagcnt>=1 && tagcnt<=2){
					
					System.out.println(line);
					bw1.write(line);
					bw1.newLine();
					newlinecnt1++;
				}
				else  if(tagcnt>2 && tagcnt<=4){
					
					System.out.println(line);
					bw2.write(line);
					bw2.newLine();
					newlinecnt2++;
				}else  if(tagcnt>4){
					System.out.println(line);
					bw3.write(line);
					bw3.newLine();
					newlinecnt3++;
				}*/
				
				if(tagcnt==4){
					
					System.out.println(line);
					bw4.write(line);
					bw4.newLine();
					newlinecnt4++;
				}
				
				if(tagcnt ==3){
					
					System.out.println(line);
					bw5.write(line);
					bw5.newLine();
					newlinecnt3++;
				}
				
			}
				
			
			
			
			
		
		}
		
		/*bw1.close();
		bw2.close();
		bw3.close();*/
		bw4.close();
		bw5.close();
		br.close();
	/*	System.out.println("Number of lines "+ linecount);
		System.out.println("Number of modified lines btwn 1-2 "+ newlinecnt1);
		System.out.println("Number of modified lines tag btwn 3-4 "+ newlinecnt2);
		System.out.println("Number of modified lines tag gr8ter 4 "+ newlinecnt3);*/
		System.out.println("Number of modified lines tag equal 3 "+ newlinecnt3);
		System.out.println("Number of modified lines tag equal 4 "+ newlinecnt4);
		
	}

	private static String checkLocation(String newword) throws ClassCastException, ClassNotFoundException, IOException {
		String finalWord ="";
		String word ="";
	    String ner="";
	    String[] arrlist;
	    boolean flag = false;
	    
	    word = classifier.classifyToString(newword);
    	arrlist = word.split(" ");
    	
    	for(String wordner:arrlist){
    			if(wordner.isEmpty()){
    				break;
    			}
    			
    			word = wordner.split("/")[0];
	    		ner = wordner.split("/")[1];
	    		if(ner.equalsIgnoreCase("location")){
	    		
		    		flag =  true;
		    	}else{
		    				    		
		    		flag = false;
		    	}
    		}
    		
   
    	
    	if(flag == true){
    		
    		finalWord="location";
    	//	System.out.println("Original:   "+newword+"     Modified:   "+finalWord);
    	}else{
    		finalWord = newword;
    	//	System.out.println("Original:   "+newword+"     Modified:   "+finalWord);
    	}
	   
		return finalWord;
	}

	private static ArrayList<String> genTagSet() throws IOException {
		// TODO Auto-generated method stub
		ArrayList<String> tagset = new ArrayList<String>();
		
		String filename = "C:/Amruta/SNLP_PROJ_NOVEMBER/8kModifiedTags.txt";
		BufferedReader brtag = new BufferedReader(new FileReader(filename));
		
		String word ="";
		while((word = brtag.readLine())!=null){			
			tagset.add(word);			
		}
		
		brtag.close();		
		return tagset;
		
	}

}
