package FeatureExtractionAndWeka;
//edited form of InputForTopicModeling..changed so that we can create a single hashset of unigrams

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import com.opencsv.CSVWriter;



public class extract_all_unigrams {

	public static void main(String[] args) throws IOException {
		Hashtable<String,ArrayList<Integer>> the_table=new Hashtable<>();
		
		HashSet<String> new_unigramsACC=new HashSet<String>();
		HashSet<String> new_unigramsACT=new HashSet<String>();
		HashSet<String> new_unigramsATT=new HashSet<String>();
		HashSet<String> new_unigramsLOC=new HashSet<String>();
		HashSet<String> new_unigramsTRP=new HashSet<String>();
		HashSet<String> new_unigrams=new HashSet<String>();
		
		HashSet<String> taglistUnigrams = new HashSet<String>();
		taglistUnigrams.add("JJ"); taglistUnigrams.add("JJR");taglistUnigrams.add("JJS");
		taglistUnigrams.add("NN");taglistUnigrams.add("NNS");  
		taglistUnigrams.add("RB");taglistUnigrams.add("RBR");taglistUnigrams.add("RBS");
		taglistUnigrams.add("VB");taglistUnigrams.add("VBD");taglistUnigrams.add("VBG");taglistUnigrams.add("VBN");taglistUnigrams.add("VBP");taglistUnigrams.add("VBZ");
		taglistUnigrams.add("WDT");taglistUnigrams.add("WP");taglistUnigrams.add("WP$");taglistUnigrams.add("WRB");
		
		String[] files={"accommodationPOS","activityPOS", "attractionPOS","locationPOS","travelprepPOS"};
		new_unigramsACC = function_for_uni(files[0],taglistUnigrams);
	//	System.out.println("size Accomodation: "+new_unigramsACC.size());
		
		
		new_unigramsACT = 	function_for_uni(files[1],taglistUnigrams);
	//	System.out.println("size Activity: "+new_unigramsACT.size());
		
		new_unigramsATT = function_for_uni(files[2],taglistUnigrams);
	//	System.out.println("size Attraction:  " +new_unigramsATT.size());
		
		new_unigramsLOC = function_for_uni(files[3],taglistUnigrams);
	//	System.out.println("size Location: "+new_unigramsLOC.size());
		
		new_unigramsTRP = function_for_uni(files[4],taglistUnigrams);
	//	System.out.println("size TravelPrep "+new_unigramsTRP.size());
		
		new_unigrams.addAll(new_unigramsACC);
		new_unigrams.addAll(new_unigramsACT);
		new_unigrams.addAll(new_unigramsATT);
		new_unigrams.addAll(new_unigramsLOC);
		new_unigrams.addAll(new_unigramsTRP);
		
	/*	System.out.println("size Accomodation: "+new_unigramsACC.size());
		System.out.println("size Activity: "+new_unigramsACT.size());
		System.out.println("size Attraction:  " +new_unigramsATT.size());
		System.out.println("size Location: "+new_unigramsLOC.size());
		System.out.println("size TravelPrep "+new_unigramsTRP.size());
		System.out.println("size Overall "+new_unigrams.size());*/
		
		/**
		 0 - accommodationPOS
		 1 - activityPOS
		 2 - attractionPOS
		 3 - locationPOS
		 4 - travelprepPOS
		 
		 **/
		
		for(String val:new_unigrams)
		{
			ArrayList<Integer> the_numbers=new ArrayList<>();
			
			int from0=count_the_occurence_uni(val,files[0],taglistUnigrams);
			int from1=count_the_occurence_uni(val,files[1],taglistUnigrams);
			int from2=count_the_occurence_uni(val,files[2],taglistUnigrams);
			int from3=count_the_occurence_uni(val,files[3],taglistUnigrams);
			int from4=count_the_occurence_uni(val,files[4],taglistUnigrams);
			
			the_numbers.add(from0);the_numbers.add(from1);the_numbers.add(from2);the_numbers.add(from3);the_numbers.add(from4);
			the_table.put(val,the_numbers);
		}
		/*for(String value1:new_unigrams)
		{
			System.out.println(value1+"  "+the_table.get(value1));
		}*/
		
		genCSVFeatureSet(new_unigrams,the_table,taglistUnigrams);
		
		
	}
	
	private static void genCSVFeatureSet(HashSet<String> new_unigrams,
			Hashtable<String, ArrayList<Integer>> the_table, HashSet<String>taglist) throws IOException {
		
		String[] files={"accommodationPOS","activityPOS", "attractionPOS","locationPOS","travelprepPOS"};
		String line ="";
		
		String featcsv = "travelqaFeat.csv";		
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
			System.out.println(label);
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
				System.out.println(line);
				if(!line.isEmpty()){
					StringBuilder sbfeat = new StringBuilder();
					HashSet<String> lineUnigram = new HashSet<String>();
					text = line.split(" ");
					for(String tword:text){
						
						tag = tword.split("/")[1];
						if(taglist.contains(tag)){
							word = tword.split("/")[0].toLowerCase();
							lineUnigram.add(word);			
						}
						
					}
					
					for(String val: new_unigrams){
						if(!lineUnigram.isEmpty()){
							
							if(lineUnigram.contains(val)){								
								sbfeat.append(Integer.toString(the_table.get(val.toLowerCase()).get(uniIndex)));
							}else{
								sbfeat.append(Integer.toString(0));
							}
							
							sbfeat.append("###");
							
						}
					}
					
					if(lineUnigram.isEmpty()){
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

	public static HashSet<String> function_for_uni(String filename,HashSet<String> taglist) throws IOException
	{
		
		HashSet<String> new_unigrams = new HashSet<String>();
		filename = filename+".txt";	
		String line="";
		BufferedReader br = new BufferedReader(new FileReader(filename));
		while((line = br.readLine())!=null){	
			String[] contents_of_line=line.split(" ");
			for(String each_word:contents_of_line){
				String unigram_word=each_word.split("/")[0].toLowerCase();
				String tags=each_word.split("/")[1];
				
					if(taglist.contains(tags) && unigram_word.length()>=3){
					//	System.out.println(unigram_word);
						new_unigrams.add(unigram_word);
					}					
			}
		
		}
		
	br.close();
	
	return new_unigrams;
		
		}
	
	public static int count_the_occurence_uni(String unigram_word,String thefile,HashSet<String> taglist) throws IOException
	{
	//	String path = "C:/Users/prakash/Desktop/111/6DecCategories/6DecCategories/the_files/taggedData/";
		String ext = ".txt";
		String path_of_file=thefile+ext;
		
		BufferedReader br = new BufferedReader(new FileReader(path_of_file));
		String line="";
		int term_count=0;
		while((line = br.readLine())!=null){
			String[] contents_of_line=line.split(" ");
			for(String each_word:contents_of_line)
			{
				String unigrams=each_word.split("/")[0].toLowerCase();
				String tags=each_word.split("/")[1];
				if(taglist.contains(tags)){
				if(unigrams.equalsIgnoreCase(unigram_word))
					term_count++;
				}
			}
		}
		
		br.close();
		return term_count;
	}
	
	
	
}
