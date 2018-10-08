package setiment_analysis;

import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.jar.Attributes;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class feature_unigram_new {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
	
		String file_name = "C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/pos_data.txt";
	//	String file_name = "C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/neg_data.txt";
		Hashtable<String,Double> lexicon=new Hashtable<String,Double>();
		Hashtable<String,Double> stopword_list=new Hashtable<String,Double>();
		stopword_list.put("be", 0.0d);
		stopword_list.put("have", 0.0d);
		stopword_list.put("do", 0.0d);
		stopword_list.put("get", 0.0d);
		stopword_list.put("not", 0.0d);
		
		
		double tot_sent=get_filesize(file_name);
		generate_lexicon(lexicon,file_name,stopword_list);
		
		gen_lexicon_tfidf(lexicon,file_name,tot_sent);
		
		System.out.println("done");

	}
	
private static double get_filesize(String file_name) throws IOException {
		double fsize=0.0d;
		BufferedReader reader=new BufferedReader(new FileReader(file_name));
		while(reader.readLine()!=null){
			fsize++;
		}
		return fsize;
	}

private static void gen_lexicon_tfidf(Hashtable<String, Double> lexicon,
			String file_name,double tot_sent) throws IOException {
	
	 
	  double df=0.0d;
	  double tf_idf=0.0d;
	  double idf=0.0d;
	  double tf=0.0d;
	  DecimalFormat dis_format=new DecimalFormat("0.00");
	  
	
		Enumeration<String> lex_list=lexicon.keys();
		
		int keycnt=0;
	//	System.out.println("Size b4 getCount:"+lexicon.size());
		while(lex_list.hasMoreElements()){
			BufferedReader reader=new BufferedReader(new FileReader(file_name));
			String lex_word=lex_list.nextElement();	
			tf=getCount(lex_word);
			df=getdocCount(lex_word,reader);
		    idf=Math.log(tot_sent/df);
		    tf_idf=tf*idf;
		   
		    lexicon.put(lex_word, tf_idf);
	//	    System.out.println(lex_word+":"+tf+"	tf-idf:	"+dis_format.format(tf_idf));
				
			keycnt++;
		           
	}
		
		FileOutputStream fos = new FileOutputStream("C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/pos_lexicon.xml");
		XMLEncoder e = new XMLEncoder(fos);
		e.writeObject(lexicon);
		e.close();
	
	
		
	}

private static double getdocCount(String lex_word,BufferedReader reader) throws IOException {
	double dcount=0;		
	String line=null;
	String mod_line=null;
	String[] txt=null;
	int start_pt=2;
	String act_word=null;

	while((line=reader.readLine())!=null){
		mod_line=line.substring(start_pt);			
		txt=mod_line.split(" ");
		for(String word:txt){
			if(checkPOStag(word)){					
				act_word=getWord(word);		
												 
			}else{
				continue;
			}
			
			if(lex_word.equals(act_word)){
				dcount++;
				break;
			}
		}
		
   }	
	
	
	
	
	
	return dcount;
}

private static void generate_lexicon(Hashtable<String, Double> lexicon,String file_name,Hashtable<String,Double> stopword_list) throws IOException, ParserConfigurationException, SAXException {
		
		HashSet<String>temp_hashtable=new HashSet<String>();
	
	
	SAXParserFactory parserFactor = SAXParserFactory.newInstance();
	String file_nm="C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/posSenti_lexicon.xml";
	
	    SAXParser parser = parserFactor.newSAXParser();		
	    SAXHandler1 handler = new SAXHandler1();		
	    parser.parse(file_nm, handler);		    
	    temp_hashtable=handler.lexicon;
	
	
	
	
		BufferedReader reader= new BufferedReader(new FileReader(file_name));
		
		String line=null;
		String mod_line=null;
		String[] txt=null;



		int start_pt=2;
		String act_word=null;
		int line_cnt=0;
		
	//	boolean negflag=false;

		while((line=reader.readLine())!=null){
			mod_line=line.substring(start_pt);			
			txt=mod_line.split(" ");
			for(String word:txt){
				if(checkPOStag(word)){					
					act_word=getWord(word);	
					if(act_word.length()<=2 || checkStopword(act_word,stopword_list)){
						continue;
					}
					if(!lexicon.containsKey(act_word) && temp_hashtable.contains(act_word)){
					//	System.out.println(act_word);
						lexicon.put(act_word,0.0d);	
					//	negflag=true;
					}					
				}else{
					continue;
				}
			}
	//		if(negflag==true){
	//			System.out.println(mod_line);
	//			negflag=false;
				
	//		}
			line_cnt++;		
		}	


		
		reader.close();

		Enumeration<String> lex_list=lexicon.keys();
		double lex_cnt=0;
		int keycnt=0;
		System.out.println("Size b4 getCount:"+lexicon.size());
		while(lex_list.hasMoreElements()){
			String lex_word=lex_list.nextElement();			
			lex_cnt=getCount(lex_word);
			if(lex_cnt<=3 ){
				//System.out.println(lex_word+" :"+lex_cnt);
			
				lexicon.remove(lex_word);
			}else{
				System.out.println(lex_word+" :"+lex_cnt);
				lexicon.put(lex_word, lex_cnt);
			}	
			keycnt++;
		//	System.out.println(keycnt);	
                  
		}
		

		System.out.println("Size after getCount:"+lexicon.size());
		
		
		
		
		
	}


private static int getCount(String lex_word) throws IOException {
	
	int count=0;		
	BufferedReader reader= new BufferedReader(new FileReader("C:/Users/hitesh/sentiment_analysis/setiment_analysis/src/setiment_analysis/pos_data.txt"));
	String line=null;
	String mod_line=null;
	String[] txt=null;
	int start_pt=2;
	String act_word=null;

	while((line=reader.readLine())!=null){
		mod_line=line.substring(start_pt);			
		txt=mod_line.split(" ");
		for(String word:txt){
			if(checkPOStag(word)){					
				act_word=getWord(word);		

								 
			}else{
				continue;
			}
			
			if(lex_word.equals(act_word)){
				count++;
			}
		}
		
   }	
	
	
	
	
	
	return count;
}

private static boolean checkStopword(String act_word,Hashtable<String,Double> stopword_list) {
	boolean flag=false;
	
	
	if(stopword_list.containsKey(act_word)){
		flag=true;
	}
	

	return flag;
}

private static String getWord(String word) {
	String act_word=null;
	if(word.contains("//")){
		act_word=word.split("//")[0].toLowerCase();						

	}else{
		act_word=word.split("/")[1].toLowerCase();
							
	}
		
	return act_word;
}

private static boolean checkPOStag(String word) {
	// TODO Auto-generated method stub
	boolean flag=false;
	ArrayList<String> reqd_POS=new ArrayList<String>();
//	reqd_POS.add("FW");
	reqd_POS.add("JJ");
	reqd_POS.add("JJR");
	reqd_POS.add("JJS");
	reqd_POS.add("NN");
	reqd_POS.add("NNS");
//	reqd_POS.add("NNP");
//	reqd_POS.add("NNPS");
//	reqd_POS.add("PDT");
	reqd_POS.add("RB");
	reqd_POS.add("RBR");		
	reqd_POS.add("RBS");
	reqd_POS.add("VB");
	reqd_POS.add("VBN");
	reqd_POS.add("VBP");
	reqd_POS.add("VBZ");
	
	for(String pos:reqd_POS){
	
		if(word.contains(pos)){
			flag=true;
			break;			
		}
	
	}
	
	return flag;
}

}

class SAXHandler1 extends DefaultHandler {
	
	 HashSet<String> lexicon=new HashSet<String>();
	 String key;
	 //Double tfidf=0.0d;
	 String content;


	 public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			
			   }

	 @Override	
	   public void endElement(String uri, String localName,String qName) throws SAXException {
	 
	    switch(qName){
	
	      //Add the entry to hashtable once end tag is found	
	      case "void":
	    	  lexicon.add(key);
	       	
	        break;
	
	      //For all other end tags the employee has to be updated.	 
	      case "string": 
	        key = content;
	        break;
	 
	      	
	    }

	   }
	 
	 
	 @Override
	
	   public void characters(char[] ch, int start, int length)throws SAXException {
	
	     content = String.copyValueOf(ch, start, length).trim();
	
	   }


	 
	 
	 
	 
	 
	
}	
