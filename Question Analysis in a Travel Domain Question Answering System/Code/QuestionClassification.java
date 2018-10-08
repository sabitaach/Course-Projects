//used for automatically classifying questions according to the //tags that were already present in some of the training data
//extracted from sites

package cs521Project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;

public class QuestionClassification {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String csvFile = "C:/Amruta/SNLP_PROJ_NOVEMBER/TitlesAndTags.csv";
		FileWriter fw = new FileWriter(new File("C:/Amruta/SNLP_PROJ_NOVEMBER/Questions.txt"));
		BufferedWriter bw = new BufferedWriter(fw);
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		String tagSplitBy = ">";
		int count=0;
		
		HashSet<String> tagset = new HashSet<String>();
	 
		
	 
			br = new BufferedReader(new FileReader(csvFile));
			while (count<=100 && ((line = br.readLine()) != null)) {
	
			        // use comma as separator
				String[] tags = line.split(csvSplitBy);
				System.out.println(line);
				bw.write(line);
				bw.newLine();
				
				for(String tag:tags){
					
					if(tag.contains("<")){
				//		System.out.println(tag);
						if(tag.contains("<->"))
							continue;
						
						String[] list = tag.split(tagSplitBy);
				//		System.out.println("Length:  "+list.length);
						for(String tagword:list){
							
							if(tagword.contains("-")){
								tagword=tagword.replace("-", " ");
							}
							if(tagword.length()<=4)
								continue;
							
							if(!tagword.startsWith("<")){
								tagword = tagword.split("<")[1];
								 if(!tagset.contains(tagword)){																		
								//	System.out.println(tagword);
									tagset.add(tagword);
								}
							}else{
								tagword = tagword.substring(1, tagword.length());
								if(!tagset.contains(tagword)){									
								//	System.out.println(tagword);
									tagset.add(tagword);
								}
							}
							
							
						}
					}
				}
				
													
				count++;
	 
			}
			
/*				System.out.println("Length of tagset: "+ tagset.size());
				HashSet<String> modtagset = new HashSet<String>();
				HashSet<String> countrySet = new HashSet<String>();
			 WebService.setUserName("amruta26"); // add your username here
			 for(String countryName:tagset){
				 if (countryName.split(" ").length==1){
					  ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
					  searchCriteria.setQ(countryName);
					  searchCriteria.setLanguage("en");					  
					  ToponymSearchResult searchResult = WebService.search(searchCriteria);
					  
					  for (Toponym toponym : searchResult.getToponyms()) {
						 
						  if(toponym.getName().equalsIgnoreCase(countryName)){
							//  System.out.println(countryName+ "      Location");
							//  System.out.println(toponym.getName()+" "+ toponym.getCountryName()+ "  "+ toponym.getFeatureClassName());
							  countrySet.add(countryName);
						  }	else{
							  modtagset.add(countryName);
							//  System.out.println(countryName);
						  }
						  
						  
						  break;
					  }
					  
					//  System.out.println("Country:"+ countryName);
				 }else{
					 if(countryName.contains("citizens") ||countryName.contains("airways")|| countryName.contains("airline")||countryName.contains("north")||countryName.contains("east")||countryName.contains("south")||countryName.contains("west")||countryName.contains("country")||countryName.contains("lake") ||countryName.contains("san")||countryName.contains("las")|| countryName.contains("sea")){
						 
					 }else{
						 modtagset.add(countryName);
						// 	System.out.println(countryName);
					 }
						
					 
				 }
				 
			 }
			 */
			
			
			 
			  bw.close();
			  System.out.println("Done");
		//	  System.out.println("Modified tagset Length:  "+modtagset.size());
	 
		

	}

}
