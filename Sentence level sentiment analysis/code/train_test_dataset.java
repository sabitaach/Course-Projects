package setiment_analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class train_test_dataset {

	public static void main(String[] args) throws IOException {
	
		
		BufferedReader reader= new BufferedReader(new FileReader("C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/dataset.txt"));
		
		gen_train_test_data(reader);
		
		

	}

	private static void gen_train_test_data(BufferedReader reader)throws IOException {
		
		 File file1 = new File("C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/train_data.txt");
         BufferedWriter wtrain = new BufferedWriter(new FileWriter(file1,true));
         
         File file2 = new File("C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/test_data.txt");
         BufferedWriter wtest = new BufferedWriter(new FileWriter(file2,true));
              
         File file3 = new File("C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/pos_data.txt");
         File file4 = new File("C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/neg_data.txt");
         
         
         
		String line=null;
		char result;
		int c_pos=0;
		int c_neg=0;
		int c_neu=0;
		int c_mix=0;
			
		int pos_max=2380;
		int neg_max=950;
		int mix_max=250;
		int neu_max = 1000;
		int line_cnt=0;
		
		while((line=reader.readLine())!=null){
					System.out.println(line);	
			result=line.charAt(0);
			
	/*		if(result=='+' && ( c_pos<=pos_max)){					
				wtrain.write(line);
				wtrain.newLine();
				wtrain.flush();				
				 
			     BufferedWriter wpos = new BufferedWriter(new FileWriter(file3,true));
			     wpos.write(line);
			     wpos.newLine();
			     wpos.flush();
				
				c_pos++;
			
			}else if(result=='-' && ( c_neg<=neg_max)){		
				wtrain.write(line);
				wtrain.newLine();
				wtrain.flush();				
				
				c_neg++;
			
			}else if(result=='=' && ( c_neu<=neu_max)){		
				wtrain.write(line);
				wtrain.newLine();
				wtrain.flush();				
				c_neu++;
				
			}else if(result=='*' && ( c_mix<=mix_max)){		
				wtrain.write(line);
				wtrain.newLine();
				wtrain.flush();				
			
				c_mix++;
			}
			
			else{
				
				wtest.write(line);
				wtest.newLine();
				wtest.flush(); 
				
			}*/
			
			
			if(result=='+'){					

				wtrain.write(line);
				wtrain.newLine();
				wtrain.flush();				
				
			     BufferedWriter wpos = new BufferedWriter(new FileWriter(file3,true));
			     wpos.write(line);
			     wpos.newLine();
			     wpos.flush();
				
				
			
			}else if(result=='-'){	
				wtrain.write(line);
				wtrain.newLine();
				wtrain.flush();	
				

			     BufferedWriter wpos = new BufferedWriter(new FileWriter(file4,true));
			     wpos.write(line);
			     wpos.newLine();
			     wpos.flush();
				
				
			}
			
			
		}
		

		wtrain.close();
		System.out.println(c_pos);
		System.out.println("Finished");
		
	}



}


