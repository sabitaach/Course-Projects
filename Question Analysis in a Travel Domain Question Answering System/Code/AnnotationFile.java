package cs521Project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Scanner;


public class AnnotationFile {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String csvFile = "C:/Amruta/SNLP_PROJ_NOVEMBER/QuestionSet1.csv";
		FileWriter fw = new FileWriter(new File("C:/Amruta/SNLP_PROJ_NOVEMBER/QuestionsSet1Tagged_MORE_151_200.txt"));
		BufferedWriter bw = new BufferedWriter(fw);
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		String tagSplitBy = ">";
		int count=0;
		int setcnt=1;
		
		HashSet<String> tagset = new HashSet<String>();
	 
		
	 
			br = new BufferedReader(new FileReader(csvFile));
			
			while (count<=800 && ((line = br.readLine()) != null)) {
				if(count<=754){
					count++;
					continue;
				}
				 BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				 StringBuilder sb = new StringBuilder();
			        // use comma as separator
				String[] tags = line.split(csvSplitBy);
				System.out.println(line);
				
				String actLine = line.split(",<")[0];
				
								
				System.out.println();
				
				System.out.println("1. Location,2. Attraction,3. Activity,4. TravelPrep,5. Accomodation");
				System.out.println("6. Food,7. Deals,8. Information, 9. Ignore,M. Modify");
				
				System.out.println();
				System.out.println("Enter corresponding number for tags. Seperate each by comma");
				String tagnos=in.readLine();
				if(tagnos.contains("M")){
					System.out.println();
					System.out.println("Enter modified question:");
					BufferedReader inMod = new BufferedReader(new InputStreamReader(System.in));
					actLine=inMod.readLine();
					
				}
				
				System.out.println();
				System.out.println();
				
				sb.append(actLine);
				sb.append("###");
				String[] nos = tagnos.split(",");
				for(String no:nos){
					if(no.equals("1")){
						sb.append("<Location>");
					}else if(no.equals("2")){
						sb.append("<Attraction>");
					}else if(no.equals("3")){
						sb.append("<Activity>");
					}else if(no.equals("4")){
						sb.append("<TravelPrep>");
					}else if(no.equals("5")){
						sb.append("<Accomodation>");
					}else if(no.equals("6")){
						sb.append("<Food>");
					}else if(no.equals("7")){
						sb.append("<Deals>");
					}else if(no.equals("8")){
						sb.append("<Information>");
					}else if(no.equals("9")){					
						sb.append("<Ignore>");
					}
				}
				
				actLine = sb.toString();
															
				
				
				if(actLine.contains("Ignore")){
					continue;
				}else{
					System.out.println(setcnt+"#Tagged#:  "+actLine);
					setcnt++;
					bw.write(actLine);
					bw.newLine();
					System.out.println();
					System.out.println();
				}
				
				count++;
			}
			
				
			
			
			 
			  bw.close();
			  System.out.println("Done");
	
	 
		

	}

}
