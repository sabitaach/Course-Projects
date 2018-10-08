//reads in the input data and prepares it so that it can be fed //to the topic modelling system
package cs521Project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;

public class InputDataForTopicModeling {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		String path = "C:/Amruta/SNLP_PROJ_NOVEMBER/categorizedData/";
		String ext = ".txt";
	
	    String dir2 ="qa_sample_data/";
		
		HashSet<String> taglist = new HashSet<String>();
	//	taglist.add("JJ");taglist.add("JJR");taglist.add("JJS");
		taglist.add("NN");taglist.add("NNS");
	//	taglist.add("RB");taglist.add("RBR");taglist.add("RBS");
		taglist.add("VB");taglist.add("VBD");taglist.add("VBG");taglist.add("VBN");taglist.add("VBP");taglist.add("VBZ");
		taglist.add("WDT");taglist.add("WP");taglist.add("WP$");taglist.add("WRB");
		
	//	BufferedReader br = new BufferedReader(new FileReader(path+"QuestionsSet2Tagged"+ext));
		BufferedReader br = new BufferedReader(new FileReader(path+"TaggedData/QuestionSetTagged"+ext));
		String line = "";
		String text = "";
		int count = 1;
		int i=0;
		LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		//	BufferedWriter bw = new BufferedWriter(new FileWriter(path+dir+"InputFiles"+ext)); 
		
	
		while((line = br.readLine())!=null){	
			
			ArrayList<TaggedWord> parsed_one=new ArrayList<>();
			line= line.split("###")[0];
			Tree parse = lp.parse(line);
			parsed_one=parse.taggedYield();
			System.out.println(parsed_one.toString());
			StringBuilder sb = new StringBuilder();
			for(TaggedWord tw:parsed_one){
				if(taglist.contains(tw.tag())){
					sb.append(tw.word());
										
					sb.append(" ");
				}					
			}
			
			sb.append(".");
			text = sb.toString();
						
			//	text = line.split("###")[0];
		//	BufferedWriter bw = new BufferedWriter(new FileWriter(path+dir2+"TaggedInputFile"+count+ext));
	//		bw.write(text);
	//		bw.newLine();
	//		bw.close();
	//		count++;
		}
	//	bw.close();
		br.close();
		System.out.println("Done");
	}

}
