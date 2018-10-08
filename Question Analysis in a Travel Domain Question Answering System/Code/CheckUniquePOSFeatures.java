package FeatureExtractionAndWeka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import com.opencsv.CSVWriter;

public class CheckUniquePOSFeatures {

	public static void main(String[] args) throws IOException {
	
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
		
		String[] files={"accommodationPOS","activityPOS", "attractionPOS","locationPOS","travelprepPOS"};
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
		
/*		System.out.println("size Accomodation POS taglist: "+new_POSlistACC.size());
		System.out.println("size Activity: POS taglist"+new_POSlistACT.size());
		System.out.println("size Attraction:  POS taglist" +new_POSlistATT.size());
		System.out.println("size Location: POS taglist"+new_POSlistLOC.size());
		System.out.println("size TravelPrep POS taglist "+new_POSlistTRP.size());
		System.out.println("size Overall  POS taglist"+new_POSlist.size());
		*/
		
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
		
		/*for(String value:new_POSlist)
		{
			System.out.println(value+"  "+POStable.get(value));
		}*/
		
	//	genCSVFeatureSet_pos(new_POSlist,POStable,taglist);

	}

	
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

}
