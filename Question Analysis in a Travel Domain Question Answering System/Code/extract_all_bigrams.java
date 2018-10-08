
package FeatureExtractionAndWeka;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import com.opencsv.CSVWriter;



public class extract_all_bigrams {

	public static void main(String[] args) throws IOException {
		Hashtable<String,ArrayList<Integer>> Bigramtable=new Hashtable<>();
		
		HashSet<String> new_bigramsACC=new HashSet<String>();
		HashSet<String> new_bigramsACT=new HashSet<String>();
		HashSet<String> new_bigramsATT=new HashSet<String>();
		HashSet<String> new_bigramsLOC=new HashSet<String>();
		HashSet<String> new_bigramsTRP=new HashSet<String>();
		HashSet<String> new_bigrams=new HashSet<String>();
		
		
		String[] files={"accommodationPOS","activityPOS", "attractionPOS","locationPOS","travelprepPOS"};
		new_bigramsACC = function_for_bi(files[0]);
		new_bigramsACT = 	function_for_bi(files[1]);
		new_bigramsATT = function_for_bi(files[2]);
		new_bigramsLOC = function_for_bi(files[3]);
		new_bigramsTRP = function_for_bi(files[4]);
	
	
		new_bigrams.addAll(new_bigramsACC);
		new_bigrams.addAll(new_bigramsACT);
		new_bigrams.addAll(new_bigramsATT);
		new_bigrams.addAll(new_bigramsLOC);
		new_bigrams.addAll(new_bigramsTRP);
				
		/**
		 0 - accommodationPOS
		 1 - activityPOS
		 2 - attractionPOS
		 3 - locationPOS
		 4 - travelprepPOS
		 
		 **/
		
		for(String val:new_bigrams)
		{
			ArrayList<Integer> bigramnumbers=new ArrayList<Integer>();
			
			int from0=count_the_occurence_big(val,files[0]);
			int from1=count_the_occurence_big(val,files[1]);
			int from2=count_the_occurence_big(val,files[2]);
			int from3=count_the_occurence_big(val,files[3]);
			int from4=count_the_occurence_big(val,files[4]);
			
			bigramnumbers.add(from0);bigramnumbers.add(from1);bigramnumbers.add(from2);bigramnumbers.add(from3);bigramnumbers.add(from4);
			Bigramtable.put(val,bigramnumbers);
		}
		for(String value1:new_bigrams)
		{
			System.out.println(value1+"  "+Bigramtable.get(value1));
		}
		
		System.out.println("size:"+ Bigramtable.size());
		
		genCSVFeatureSet(new_bigrams,Bigramtable);
		
		
	}
	
	private static void genCSVFeatureSet(HashSet<String> new_bigrams,
			Hashtable<String, ArrayList<Integer>> the_table) throws IOException {
		
		String[] files={"accommodationPOS","activityPOS", "attractionPOS","locationPOS","travelprepPOS"};
		String line ="";
		
		String featcsv = "travelqaFeatBigramsNew.csv";		
		CSVWriter writer = new CSVWriter(new FileWriter(featcsv,true));
		String label ="";
		
		String[] text;
		String word ="";
		String tag ="";
		int uniIndex =0;
		
		
		StringBuilder sb = new StringBuilder();
		
		for(int i =0;i<the_table.size();i++){
			 sb.append("attr" + Integer.toString(i+1));
			 sb.append("###");			
		}
		
		sb.append("label");
		
		String [] record = sb.toString().split("###");
		writer.writeNext(record);
		
		for(String filename:files){
			BufferedReader br = new BufferedReader(new FileReader(filename+".txt"));
			
			label = filename.substring(0, filename.length()-3);
			//System.out.println(label);
			if(label.equalsIgnoreCase("accommodation")){
				uniIndex = 0;
				
			}else if(label.equalsIgnoreCase("activity")){
				uniIndex = 1;
				
			}else if(label.equalsIgnoreCase("attraction")){
				uniIndex = 2;
				
			}else if(label.equalsIgnoreCase("location")){
				uniIndex = 3;
				
			}else if(label.equalsIgnoreCase("travelprep")){
				uniIndex = 4;
			}
			
			while((line = br.readLine())!=null){
				//System.out.println(line);
				if(!line.isEmpty()){
					StringBuilder sbfeat = new StringBuilder();
					HashSet<String> lineBigram = new HashSet<String>();
					text = line.split(" ");
					for(int i=0;i<text.length-2;i++){
						String each_word=text[i];
						String each_word2=text[i+1];
						String unigram_word=each_word.split("/")[0].toLowerCase();
						String unigram_word2=each_word2.split("/")[0].toLowerCase();
						String combined_word=unigram_word+" "+unigram_word2;
						lineBigram.add(combined_word);
					}
					
					/*for(String tword:text){
						
						tag = tword.split("/")[1];
						if(taglist.contains(tag)){
							word = tword.split("/")[0].toLowerCase();
							lineBigram.add(word);			
						}
						
					}*/
					
					for(String val: new_bigrams){
						if(!lineBigram.isEmpty()){
							
							if(lineBigram.contains(val)){								
								sbfeat.append(Integer.toString(the_table.get(val.toLowerCase()).get(uniIndex)));
							}else{
								sbfeat.append(Integer.toString(0));
							}
							
							sbfeat.append("###");
							
						}
					}
					
					if(lineBigram.isEmpty()){
						continue;
					}
					sbfeat.append(label);
					
					record = sbfeat.toString().split("###");
					writer.writeNext(record);
				
					writer.flush();
				}
				
			}
			
			br.close();
		}
		
		writer.close();
	}

	public static HashSet<String> function_for_bi(String filename) throws IOException
	{
		
		HashSet<String> new_bigrams = new HashSet<String>();
		filename = filename+".txt";	
		String line="";
		String modLine = "";
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String combinedWord = "";
		while((line = br.readLine())!=null){
			
			modLine = "##/##"+" "+line;
			String[] lineContent=modLine.split(" ");
			for(int i=0;i<lineContent.length-1;i++){
			String eachWord1=lineContent[i];
			String eachWord2=lineContent[i+1];
			String unigram_word1=eachWord1.split("/")[0].toLowerCase();
			String unigram_word2=eachWord2.split("/")[0].toLowerCase();
			
			String tagword1=eachWord1.split("/")[1];
			String tagword2=eachWord2.split("/")[1];
			
			if(!(tagword1.equals("NNP") || tagword2.equals("NNP"))){
				
				combinedWord=unigram_word1+" "+unigram_word2;
				new_bigrams.add(combinedWord);
			}
			
					
				//System.out.println("the word:  "+unigram_word+" the tags:"+tags+" the final term:  "+if_value_is_there);
			}					
		}
	
	br.close();
	
	return new_bigrams;
		
		}
	
	public static int count_the_occurence_big(String bigram_word,String thefile) throws IOException
	{
	
		String path=thefile+".txt";
		String modLine = "";
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line="";
		int term_count=0;
		String combinedWord = "";
				
		while((line = br.readLine())!=null){
			
			modLine = "##/##"+" "+line;
			String[] lineContent=modLine.split(" ");
			
			for(int i=0;i<lineContent.length-1;i++)
			{					
				String eachWord1 =lineContent[i];
				String eachWord2 =lineContent[i+1];
								
				String unigram1=eachWord1.split("/")[0].toLowerCase();
				String unigram2=eachWord2.split("/")[0].toLowerCase();
				
				String tagword1=eachWord1.split("/")[1];
				String tagword2=eachWord2.split("/")[1];
				
				if(!(tagword1.equals("NNP") || tagword2.equals("NNP"))){
					
					combinedWord=unigram1+" "+unigram2;
					if(combinedWord.equalsIgnoreCase(bigram_word)){
						term_count++;
					}
					
				}
			
				
			}
			}
		
		br.close();
		return term_count;
	}
	
	
	
}
