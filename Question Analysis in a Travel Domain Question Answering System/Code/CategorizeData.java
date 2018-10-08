package cs521Project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CategorizeData {

	public static void main(String[] args) throws IOException {
		
		String pathdir = "C:/Amruta/SNLP_PROJ_NOVEMBER/categorizedData/7DecCategoriesEvening/";
	//	String path = "C:/Amruta/SNLP_PROJ_NOVEMBER/";
		String ext = ".txt";
		
		BufferedReader br = new BufferedReader(new FileReader("QuestionSet7DecEvening.txt"));
		BufferedWriter bw1 = new BufferedWriter(new FileWriter(pathdir+"location"+ext));
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(pathdir+"attraction"+ext));
		BufferedWriter bw3 = new BufferedWriter(new FileWriter(pathdir+"activity"+ext));
		BufferedWriter bw4 = new BufferedWriter(new FileWriter(pathdir+"travelprep"+ext));
		BufferedWriter bw5 = new BufferedWriter(new FileWriter(pathdir+"accomodation"+ext));
		BufferedWriter bw6 = new BufferedWriter(new FileWriter(pathdir+"food"+ext));
		BufferedWriter bw7 = new BufferedWriter(new FileWriter(pathdir+"deals"+ext));
		BufferedWriter bw8 = new BufferedWriter(new FileWriter(pathdir+"information"+ext));
		int finalcnt =0;
		String line ="";
		String[] text;
		int[] cnt = new int[8];
				
		while((line = br.readLine())!=null){
			finalcnt++;
			if(!line.isEmpty()){
				System.out.println(line);
				text = line.split("###");
				
				if(text[1].contains("Location")){
					bw1.write(Integer.toString(finalcnt));
					bw1.write(" ");
					bw1.write("location");
					bw1.write(" ");
					bw1.write(text[0]);
					bw1.newLine();
					cnt[0]+=1;
					
				}
				
				if(text[1].contains("Attraction")){
					bw2.write(Integer.toString(finalcnt));
					bw2.write(" ");
					bw2.write("attraction");
					bw2.write(" ");
					bw2.write(text[0]);
					bw2.newLine();
					cnt[1]+=1;
				}
				
				if(text[1].contains("Activity")){
					bw3.write(Integer.toString(finalcnt));
					bw3.write(" ");
					bw3.write("activity");
					bw3.write(" ");
					bw3.write(text[0]);
					bw3.newLine();
					cnt[2]+=1;
				}
				
				if(text[1].contains("TravelPrep")){
					bw4.write(Integer.toString(finalcnt));
					bw4.write(" ");
					bw4.write("travelprep");
					bw4.write(" ");
					bw4.write(text[0]);
					bw4.newLine();
					cnt[3]+=1;
				}
				
				if(text[1].contains("Accomodation")){
					bw5.write(Integer.toString(finalcnt));
					bw5.write(" ");
					bw5.write("accomodation");
					bw5.write(" ");
					bw5.write(text[0]);
					bw5.newLine();
					cnt[4]+=1;
				}
				
				if(text[1].contains("Food")){
					bw6.write(Integer.toString(finalcnt));
					bw6.write(" ");
					bw6.write("food");
					bw6.write(" ");
					bw6.write(text[0]);
					bw6.newLine();
					cnt[5]+=1;
				}
				
				if(text[1].contains("Deals")){
					bw7.write(Integer.toString(finalcnt));;
					bw7.write(" ");
					bw7.write("deals");
					bw7.write(" ");
					bw7.write(text[0]);
					bw7.newLine();
					cnt[6]+=1;
				}
				
				if(text[1].contains("Information")){
					bw8.write(Integer.toString(finalcnt));
					bw8.write(" ");
					bw8.write("information");
					bw8.write(" ");
					bw8.write(text[0]);
					bw8.newLine();
					cnt[7]+=1;
				}
			}
			
			
			
		}
		
		
		bw1.close();
		bw2.close();
		bw3.close();
		bw4.close();
		bw5.close();
		bw6.close();
		bw7.close();
		bw8.close();		
		br.close();
		
			
		for(int i=0;i<8;i++){
			System.out.println("Cnt"+(i+1)+":  "+cnt[i]);
		}
		
		System.out.println("FinalCnt:  "+finalcnt);
		System.out.println("Done");
		
	}

}

