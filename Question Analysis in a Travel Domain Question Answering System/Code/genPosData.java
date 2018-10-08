package FeatureExtractionAndWeka;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;

public class genPosData {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("accommodation.txt"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("accommodationPOS.txt"));
		LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.caseless.ser.gz");
		String line ="";
		String[] text;
		int count =0;
		String modline="";
		while((line=br.readLine())!=null){
			count=0;
			StringBuilder sb = new StringBuilder();
			text = line.split(" ");
			for(String word:text){
				if(count>1){
					sb.append(word);
					sb.append(" ");
				}
				count++;
			}
			
			modline = sb.toString();
			ArrayList<TaggedWord> parsed_one=new ArrayList<>();
			Tree parse = lp.parse(modline);
			parsed_one=parse.taggedYield();
			
			StringBuilder sbpos = new StringBuilder();
			
			for(TaggedWord tw:parsed_one){
				sbpos.append(tw.toString());
				sbpos.append(" ");
			}
			
			sbpos.delete(sbpos.length()-1, sbpos.length());
			
			modline = sbpos.toString(); 
			
			bw.write(modline);
			bw.newLine();
			
		}

		
		bw.close();
		br.close();
		System.out.println("Done");
	}

}
