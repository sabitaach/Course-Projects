package setiment_analysis;

import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class positive_hashtable {

	public static void main(String[] args) throws IOException {
		String fn_write="C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/pos_senti.xml";
		String fn_read="C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/positive-words.txt";
		HashSet<String> pos_senti_list=new HashSet<String>();
		BufferedReader reader=new BufferedReader(new FileReader(fn_read));
		String line=null;
		int cn=0;
		while((line=reader.readLine())!=null){
			System.out.println(line);
			pos_senti_list.add(line.toLowerCase());
			cn++;
			
		}
		
		//System.out.println(pos_senti_list.size());
		FileOutputStream fos = new FileOutputStream("C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/posSenti_lexicon.xml");
		XMLEncoder e = new XMLEncoder(fos);
		e.writeObject(pos_senti_list);
		e.close();
		
		

	}

}
