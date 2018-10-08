package sabitaWork;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;

import advis.uic.cs.edu.annotation.AnnotatedText;
import advis.uic.cs.edu.stanfordCoreNLP.Dependency;
import advis.uic.cs.edu.stanfordCoreNLP.StanfordAnnotation;
import advis.uic.cs.edu.stanfordCoreNLP.StanfordCoreNLPWrapper;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class PreparingTestData {

	static HashSet<String> paragraph = new HashSet<>();
	static HashSet<String> triple = new HashSet<>();
	static ArrayList<String> triples_from_gold = new ArrayList<String>();// triples from our gold set
	static ArrayList<String> text_from_gold = new ArrayList<String>();// all the sentences from the gold set
																		
	static Hashtable<String, ArrayList<String>> collection_of_similar_triples = new Hashtable<String, ArrayList<String>>();// all triples that have the same predicate
																															
	static ArrayList<String> triples_from_other_data = new ArrayList<String>();// triples from rest of the data
																				
	static Hashtable<String, ArrayList<String>> triple_and_sentences = new Hashtable<>();// will have triples and sentences that have the subject and object of triple from neg dataset
																							
	static Hashtable<String, String> nationalities = new Hashtable<String, String>();

	public static void main(String[] args) throws IOException {
		populate(); // here we read the nationality, all the triples from
					// gold_standard, all triples and text from the other data
		String[] properties_value = { "nationality" };// spouse_s"};//,"profession","place_of_birth","education","nationality"};

		// 1.Given a property, the first step consists in extracting from the
		// RDF knowledge base all triples containing that property.
		extract_same_triples(properties_value);

		// 2. For each triple (s; p; o), that was extracted for a property p, we
		// retrieve all sentences from the domain corpus in which the labels of
		// both entities s and o occur.
		extract_sentences_for_triples();// positive sentences from gold_set

	}

	public static void extract_sentences_for_triples() {
		for (int i = 0; i < triples_from_gold.size(); i++) {
			String each_triple_from_gold = triples_from_gold.get(i);
			// System.out.println(each_triple_from_gold);
			String each_sentence_from_gold = text_from_gold.get(i);
			each_sentence_from_gold = each_sentence_from_gold.substring(3);

			String[] the_triples = each_triple_from_gold.split(" ");
			String subject = the_triples[0].trim();
			subject = subject.replace("_", " ");
			// System.out.println(subject);
			String predicate = the_triples[1].trim();
			// System.out.println(predicate);
			String object = the_triples[2].trim();
			// System.out.println("here:"+object);
			object = object.replace("_", " ").trim();
			if (each_sentence_from_gold.contains("?"))
				each_sentence_from_gold = each_sentence_from_gold.replace("?",
						"");
			if (each_sentence_from_gold.contains("."))
				each_sentence_from_gold = each_sentence_from_gold.replace(".",
						"");
			if (each_sentence_from_gold.contains("\""))
				each_sentence_from_gold = each_sentence_from_gold.replace("\"",
						"");
			if (each_sentence_from_gold.contains(","))
				each_sentence_from_gold = each_sentence_from_gold.replace(",",
						"");

			check_if_gold_same(each_sentence_from_gold, subject, object);// this
																			// is
																			// fine

		}
	}

	// function to check if the subject and object are present in the particular
	// paragraph
	public static void check_if_gold_same(String paragraph, String sub,
			String obj) {
		int checking = 0;
		String the_nationality = "";
		int present = 0;
		Enumeration<String> the_nation = nationalities.keys();
		while (the_nation.hasMoreElements()) {
			if (the_nation.nextElement().equalsIgnoreCase(obj)) {
				present = 1;
				break;
			}

		}
		if (present == 1)
			the_nationality = nationalities.get(obj);
		// System.out.println("yetaaaaaaaaaaaaaaaa:"+nationalities.get(obj));

		if (paragraph.matches(".*\\b" + sub + "\\b.*") == true
				&& paragraph.matches(".*\\b" + obj + "\\b.*") == true) {
			checking = 1;
			paragraph = paragraph.replace(sub, "XX");
			paragraph = paragraph.replace(obj, "YY");

		} else if (paragraph.matches(".*\\b" + sub + "\\b.*") == true
				&& !the_nationality.equals("")
				&& paragraph.matches(".*\\b" + the_nationality + "\\b.*") == true) {
			checking = 1;
			paragraph = paragraph.replace(sub, "XX");
			paragraph = paragraph.replace(the_nationality, "YY");

		}

		else {// int check=1;
			String[] sub_parts = sub.split(" ");
			String[] obj_parts = obj.split(" ");

			String the_object_mnemonic = "";
			if (obj.contains(" ")) {
				for (String objPart : obj_parts) {
					char first_letter = objPart.charAt(0);
					if (Character.isUpperCase(first_letter))
						the_object_mnemonic = the_object_mnemonic
								+ first_letter;
				}
			}

			for (String from_sub : sub_parts) {
				// if((paragraph.contains(from_sub)&&paragraph.contains(obj))||(paragraph.contains(from_sub)&&
				// !(the_object_mnemonic.equals(""))&&paragraph.contains(the_object_mnemonic))||(paragraph.contains(sub+" ")&&paragraph.contains(" "+obj)))
				if ((paragraph.matches(".*\\b" + from_sub + "\\b.*") == true && paragraph
						.matches(".*\\b" + obj + "\\b.*") == true)
						|| (paragraph.matches(".*\\b" + from_sub + "\\b.*") == true
								&& !(the_object_mnemonic.equals("")) && paragraph
								.matches(".*\\b" + the_object_mnemonic
										+ "\\b.*") == true)
						|| (paragraph.matches(".*\\b" + from_sub + "\\b.*") == true
								&& !(the_nationality.equals("")) && paragraph
								.matches(".*\\b" + the_nationality + "\\b.*") == true)) {

					checking = 1;
					if (paragraph.matches(".*\\b" + from_sub + "\\b.*") == true
							&& paragraph.matches(".*\\b" + obj + "\\b.*") == true) {
						paragraph = paragraph.replace(from_sub, "XX");
						paragraph = paragraph.replace(obj, "YY");
					} else if (paragraph.matches(".*\\b" + from_sub + "\\b.*") == true
							&& !(the_object_mnemonic.equals(""))
							&& paragraph.matches(".*\\b" + the_object_mnemonic
									+ "\\b.*") == true) {
						paragraph = paragraph.replace(from_sub, "XX");
						paragraph = paragraph
								.replace(the_object_mnemonic, "YY");

					} else if (paragraph.matches(".*\\b" + from_sub + "\\b.*") == true
							&& !(the_nationality.equals(""))
							&& paragraph.matches(".*\\b" + the_nationality
									+ "\\b.*") == true) {
						paragraph = paragraph.replace(from_sub, "XX");
						paragraph = paragraph.replace(the_nationality, "YY");

					}
				}

			}

		}

		paragraph = remove_unwanted_content(paragraph);
		write_data_to_file(paragraph);
		System.out.println("check gold: " + paragraph);

		//

	}

	// removing unwanted symbols from the sentence

	public static String remove_unwanted_content(String paragraph) {
		if (paragraph.startsWith("("))
			paragraph = paragraph.substring(3);
		if (paragraph.startsWith(")"))
			paragraph = paragraph.substring(1);
		// System.out.println(paragraph);
		if (paragraph.contains("{{"))
			paragraph = paragraph.replaceAll("\\{\\{.*\\}\\}", "");
		return paragraph;

	}

	// 1. Given a property, the first step consists in extracting from the RDF
	// knowledge
	// base all triples containing that property.

	public static void extract_same_triples(String[] prop) {
		// HashSet<String> check=new HashSet<>();
		int count = 0;
		for (String indiv : prop) {
			ArrayList collection_of_triples = new ArrayList<String>();
			for (String again_indiv : triple) {// System.out.println(again_indiv);
				String[] againcomp = again_indiv.split("\t");
				String pred1 = againcomp[1];
				// System.out.println(pred1);
				if (indiv.equalsIgnoreCase(pred1)) {
					String edited_triple = getting_new_predicate(again_indiv);
					collection_of_triples.add(edited_triple);
					triples_from_other_data.add(edited_triple);
					// this was done for spouse
					// String
					// edited_predicate=edit_triple_to_proper_format(again_indiv);
					// collection_of_triples.add(edited_predicate);
					count++;
				}
			}
			collection_of_similar_triples.put(indiv, collection_of_triples);

		}
	}

	// trying to insert _ symbol in between the first names and second name so
	// that it becomes easier to process later
	public static String getting_new_predicate(String triples) {// System.out.println(triples);
		String[] portions = triples.split("\t");
		String subject = portions[0];
		// System.out.println(subject);
		String predicate = portions[1];
		// System.out.println("subject :"+subject+"predicate:"+predicate);
		String object = triples.substring(triples.indexOf(predicate)
				+ predicate.length(), triples.length());
		// System.out.println("the obj:"+object);
		String modified_object = "";
		if (object.contains(" ")) {
			String[] the_parts_of_object = object.split(" ");
			for (String each_one : the_parts_of_object) {
				modified_object = modified_object + each_one + "_";

			}
		} else
			modified_object = object;
		String final_string = subject + " " + predicate + " " + modified_object;
		return final_string;

	}

	// reading the contents of different files that have the data that will be
	// needed for various tasks
	public static void populate() throws IOException {
		BufferedReader reader = new BufferedReader(
				new FileReader(
						"C:/Users/prakash/Documents/contentselection/ContentSelection/all_text.txt"));

		String str = "";
		while ((str = reader.readLine()) != null) {
			paragraph.add(str);
		}
		reader.close();
		BufferedReader reader1 = new BufferedReader(
				new FileReader(
						"C:/Users/prakash/Documents/contentselection/ContentSelection/all_triples.txt"));

		String str1 = "";
		while ((str1 = reader1.readLine()) != null) {

			triple.add(str1);

		}

		reader1.close();
		BufferedReader reader2 = new BufferedReader(
				new FileReader(
						"C:/Users/prakash/Documents/contentselection/ContentSelection/gold_triple.txt"));
		String str2 = "";
		while ((str2 = reader2.readLine()) != null) {

			triples_from_gold.add(str2);

		}

		reader2.close();

		BufferedReader reader4 = new BufferedReader(
				new FileReader(
						"C:/Users/prakash/Documents/contentselection/ContentSelection/gold_text.txt"));
		String strr = "";
		while ((strr = reader4.readLine()) != null) {

			text_from_gold.add(strr);

		}

		reader4.close();
		BufferedReader reader3 = new BufferedReader(
				new FileReader(
						"C:/Users/prakash/Documents/contentselection/ContentSelection/nationalities.txt"));
		String str3 = "";
		while ((str3 = reader3.readLine()) != null) {
			String[] str4 = str3.split("/");
			String country_name = str4[0].trim();
			String nationality_name = str4[1].trim();
			nationalities.put(country_name, nationality_name);

		}

		reader3.close();

	}
	
	public static void write_data_to_file(String line) {
		String all_the_text = "C:\\Users\\prakash\\Documents\\contentselection\\ContentSelection\\myfinaldata.txt";
		try {
			FileWriter fw1 = new FileWriter(all_the_text, true);
			fw1.write(line + "\n");
			fw1.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
