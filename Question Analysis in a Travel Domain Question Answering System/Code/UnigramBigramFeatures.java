//used for generating feature set that comprises of both unigrams //and bigrams

package FeatureExtractionAndWeka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import com.opencsv.CSVWriter;

public class UnigramBigramFeatures {

	public static void main(String[] args) throws IOException {
		
		/** Unigrams**/
		
		Hashtable<String,ArrayList<Integer>> the_table=new Hashtable<String,ArrayList<Integer>>();		
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
		new_unigramsACT = 	function_for_uni(files[1],taglistUnigrams);
		new_unigramsATT = function_for_uni(files[2],taglistUnigrams);
		new_unigramsLOC = function_for_uni(files[3],taglistUnigrams);
		new_unigramsTRP = function_for_uni(files[4],taglistUnigrams);
			
		new_unigrams.addAll(new_unigramsACC);
		new_unigrams.addAll(new_unigramsACT);
		new_unigrams.addAll(new_unigramsATT);
		new_unigrams.addAll(new_unigramsLOC);
		new_unigrams.addAll(new_unigramsTRP);
			
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
		
		/** BIGRAMS**/
		
		Hashtable<String,ArrayList<Integer>> Bigramtable=new Hashtable<>();
		
		HashSet<String> new_bigramsACC=new HashSet<String>();
		HashSet<String> new_bigramsACT=new HashSet<String>();
		HashSet<String> new_bigramsATT=new HashSet<String>();
		HashSet<String> new_bigramsLOC=new HashSet<String>();
		HashSet<String> new_bigramsTRP=new HashSet<String>();
		HashSet<String> new_bigrams=new HashSet<String>();
				
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
		
		
		/**POSLIST**/
		HashSet<String> new_POSlistACC=new HashSet<String>();
		HashSet<String> new_POSlistACT=new HashSet<String>();
		HashSet<String> new_POSlistATT=new HashSet<String>();
		HashSet<String> new_POSlistLOC=new HashSet<String>();
		HashSet<String> new_POSlistTRP=new HashSet<String>();
		HashSet<String> new_POSlist=new HashSet<String>();
		Hashtable<String, ArrayList<Integer>> POStable = new Hashtable<String, ArrayList<Integer>>(); 
		
			HashSet<String> taglist = new HashSet<String>();
		//	taglist.add("JJ"); taglist.add("JJR");taglist.add("JJS");
			taglist.add("NN");taglist.add("NNS"); taglist.add("EX");taglist.add("MD");
			//taglist.add("RB");taglist.add("RBR");taglist.add("RBS");
			taglist.add("VB");taglist.add("VBD");taglist.add("VBG");taglist.add("VBN");taglist.add("VBP");taglist.add("VBZ");
			taglist.add("WDT");taglist.add("WP");taglist.add("WP$");taglist.add("WRB");
				
		new_POSlistACC = function_for_pos(files[0],taglist);
		new_POSlistACT = function_for_pos(files[1],taglist);
		new_POSlistATT = function_for_pos(files[2],taglist);
		new_POSlistLOC = function_for_pos(files[3],taglist);
		new_POSlistTRP = function_for_pos(files[4],taglist);
	
		
		new_POSlist.addAll(new_POSlistACC);
		new_POSlist.addAll(new_POSlistACT);
		new_POSlist.addAll(new_POSlistATT);
		new_POSlist.addAll(new_POSlistLOC);
		new_POSlist.addAll(new_POSlistTRP);
		
		for(String val:new_POSlist)
		{
			ArrayList<Integer> POSnumbers=new ArrayList<Integer>();
			
			int from0=count_the_occurence_pos(val,files[0],taglist);
			int from1=count_the_occurence_pos(val,files[1],taglist);
			int from2=count_the_occurence_pos(val,files[2],taglist);
			int from3=count_the_occurence_pos(val,files[3],taglist);
			int from4=count_the_occurence_pos(val,files[4],taglist);
			
			POSnumbers.add(from0);POSnumbers.add(from1);POSnumbers.add(from2);POSnumbers.add(from3);POSnumbers.add(from4);
			POStable.put(val,POSnumbers);
		}
		
		
	//	genCSVFeatureSet(new_unigrams,the_table,taglistUnigrams,new_POSlist,POStable,taglist);
		
		genCSVFeatureSet(new_unigrams,the_table,taglistUnigrams,new_POSlist,POStable,taglist,new_bigrams,Bigramtable);
		

	}
	
	
	//extracting all the unigrams

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
	
//counting the number of times the unigrams occur

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
	
//generating a csv file from the features and their counts

	private static void genCSVFeatureSet(HashSet<String> new_unigrams,
			Hashtable<String, ArrayList<Integer>> the_table, HashSet<String>taglistUnigrams,HashSet<String> new_POSlist,
			Hashtable<String, ArrayList<Integer>> POStable, HashSet<String>taglist,HashSet<String> new_bigrams,
			Hashtable<String, ArrayList<Integer>> Bigramtable) throws IOException {
		
		String[] files={"accommodationPOS","activityPOS", "attractionPOS","locationPOS","travelprepPOS"};
		String line ="";
		
		String featcsv = "UniBiPOS.csv";		
		CSVWriter writer = new CSVWriter(new FileWriter(featcsv,true));
		String label ="";
		String[] text;
		String word ="";
		String tag ="";
		int uniIndex =0;
		
		String uniRecord ="" ;
		String posRecord ="";
		String biRecord ="";
		String finRecord ="";
		
		
		//int tot_attr_size = the_table.size()+POStable.size();
		int tot_attr_size = the_table.size()+Bigramtable.size()+POStable.size();
		
		StringBuilder sb = new StringBuilder();
		
		for(int i =0;i<tot_attr_size;i++){
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
					
					uniRecord = genRecord(1,line,uniIndex,new_unigrams,taglistUnigrams,the_table);
					posRecord = genRecord(2,line,uniIndex,new_POSlist,taglist,POStable);
					biRecord =  genRecord(3,line,uniIndex,new_bigrams,taglistUnigrams,Bigramtable);

				//	finRecord = uniRecord.concat(posRecord).concat(label);
					finRecord = uniRecord.concat(biRecord).concat(posRecord).concat(label);
					record = finRecord.split("###");
					writer.writeNext(record);				
					writer.flush();
				}
				
			}
			
			br.close();
		}
		
		writer.close();
		System.out.println("Done");
	}

	
	private static String genRecord(int flag, String line,int uniIndex,HashSet<String> hashlist, HashSet<String> taglist, Hashtable<String, ArrayList<Integer>> tablelist) {
		// TODO Auto-generated method stub
		String record ="";
		String[] text = line.split(" ");
		String tag = "";
		String word = "";
		
		StringBuilder sbfeat = new StringBuilder();
		
		if(flag == 1){
			HashSet<String> lineUnigram = new HashSet<String>();
			for(String tword:text){
				
				tag = tword.split("/")[1];				
				if(taglist.contains(tag)){
					word = tword.split("/")[0].toLowerCase();
					lineUnigram.add(word);			
				}
				
			}
			
			for(String val: hashlist){
				if(!lineUnigram.isEmpty()){
					
					if(lineUnigram.contains(val)){								
						sbfeat.append(Integer.toString(tablelist.get(val.toLowerCase()).get(uniIndex)));
					}else{
						sbfeat.append(Integer.toString(0));
					}
					
					sbfeat.append("###");
					
				}
			}
			
		record = sbfeat.toString();
			
		}else if(flag == 2){
			
			String prevtag ="";
			String poslist ="";
			StringBuilder sbtag = new StringBuilder();
			for(String eachWord:text){
				
			tag=eachWord.split("/")[1];					
			if(taglist.contains(tag) && !prevtag.equalsIgnoreCase(tag)){
					sbtag.append(tag);
					sbtag.append("##");	
					prevtag = tag;
				}					
			}
			
			poslist = sbtag.toString();
			for(String posval : hashlist){
				if(!poslist.isEmpty()){
					
					if(posval.contentEquals(sbtag)){								
						sbfeat.append(Integer.toString(tablelist.get(posval).get(uniIndex)));
					}else{
						sbfeat.append(Integer.toString(0));
					}
					
					sbfeat.append("###");
					
				}
			}
				
			record = sbfeat.toString();
		}else if(flag ==3){
			
			HashSet<String> lineBigram = new HashSet<String>();
			String modLine = "##/##"+" "+line;
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
					
					String combinedWord=unigram1+" "+unigram2;
					lineBigram.add(combinedWord);
					
				}
					
			}
			
		
			for(String val: hashlist){
				if(!lineBigram.isEmpty()){
					
					if(lineBigram.contains(val)){								
						sbfeat.append(Integer.toString(tablelist.get(val.toLowerCase()).get(uniIndex)));
					}else{
						sbfeat.append(Integer.toString(0));
					}
					
					sbfeat.append("###");
					
				}
			}
			
		record = sbfeat.toString();
				
			
		}

		return record;
	}


//counting the number of times the POS sequence occurs

	private static int count_the_occurence_pos(String posval, String filename,
			HashSet<String> taglist) throws IOException {
		
		String path=filename+".txt";
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line="";
		int poslistCnt=0;
		String tag ="";
		String prevtag ="";
		
		
		while((line = br.readLine())!=null){
			if(!line.isEmpty()){
				
				
				StringBuilder sbtag = new StringBuilder();
				String[] linecontent = line.split(" ");
				
				for(String eachWord:linecontent){
					
					tag=eachWord.split("/")[1];					
					if(taglist.contains(tag) && !prevtag.equalsIgnoreCase(tag)){
						sbtag.append(tag);
						sbtag.append("##");	
						prevtag = tag;
					}					
				}
				
				if(sbtag.length()>0 && posval.contentEquals(sbtag) ){					
						poslistCnt++;
				}
						
			}
		}
		
		br.close();
		
		return poslistCnt++;
	}

	private static HashSet<String> function_for_pos(String filename,
			HashSet<String> taglist) throws IOException {
		
		HashSet<String> templist = new HashSet<String>();
		
		String word ="";
		String tag ="";
		String prevtag ="";
	
		int linecnt = 0;
		
		filename = filename+".txt";	
		String line="";
		BufferedReader br = new BufferedReader(new FileReader(filename));
		while((line = br.readLine())!=null){
			linecnt++;
			if(!line.isEmpty()){
				
				StringBuilder sbword = new StringBuilder();
				StringBuilder sbtag = new StringBuilder();
				String[] linecontent = line.split(" ");
				
				for(String eachWord:linecontent){
					
					word=eachWord.split("/")[0];
					tag=eachWord.split("/")[1];
					
					if(taglist.contains(tag) && !prevtag.equalsIgnoreCase(tag)){
						
						sbtag.append(tag);
						sbtag.append("##");	
						
						sbword.append(word);
						sbword.append("$$");
						prevtag = tag;
					}					
				}
				
				
				if(sbtag.length()>0 && !templist.contains(sbtag.toString())){
				//	System.out.println("LINE:  "+line);
					templist.add(sbtag.toString());
				//	System.out.println("TAGWORD: "+sbtag.toString()+"@@"+sbword.toString());
				}
				
				
			}
			

		
		}
		
	br.close();
		
		
		/*System.out.println("Org No of"+filename+"    Taglist: "+ linecnt);
		System.out.println("Unique"+filename+"   taglist: "+ templist.size());*/
		return templist;
	}

//function for extracting the bigrams

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
	
//counting the number of times bigrams occur
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

