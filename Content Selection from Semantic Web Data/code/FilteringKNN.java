package sabitaWork;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class FilteringKNN {
	public static ArrayList<TaggedWord> parsed_one = new ArrayList<>();
	static LexicalizedParser lp = LexicalizedParser
			.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
	
	static ArrayList<Double> finalPrecision=new ArrayList<>();
	static ArrayList<Double> finalRecall=new  ArrayList<Double>();
	static ArrayList<Double> finalFscore=new  ArrayList<Double>();

	public static void main(String[] args) throws IOException {
		HashSet<String> posdata=new HashSet<String>();
		HashSet<String> negdata=new HashSet<String>();
		/*HashSet<String> traindata = new HashSet<>();
		HashSet<String> testdata = new HashSet<String>();*/
		ArrayList<String> features_pos = new ArrayList<>();
		ArrayList<String> features_neg = new ArrayList<String>();
		
		//reading data from the file and populating them into out data structures

		extractNegPosData(posdata, negdata);
		extractFeatures(features_pos, posdata);
		extractFeatures(features_neg, negdata);

		System.out.println("size of the positive: " + features_pos.size());
		System.out.println("size of the negative: " + features_neg.size());
		
		for(int i=0;i<10;i++)
		{
		ArrayList<String> collection_of_pos_train = new ArrayList<String>();
		ArrayList<String> collection_of_neg_train = new ArrayList<String>();
		ArrayList<String> testdata=new ArrayList<String>();
		Collections.shuffle(features_pos, new Random(System.nanoTime()));// trying to randomize the data so that we can have unique train/test sets
		Collections.shuffle(features_neg, new Random(System.nanoTime()));
		
		//for spouse
		List<String> pos_train=features_pos.subList(0, 25);
		List<String> pos_test=features_pos.subList(25, 32);
		List<String> neg_train=features_neg.subList(0, 20);
		List<String> neg_test=features_neg.subList(20, 25);
		//for nationality
		
		/*List<String> pos_train=features_pos.subList(0, 43);
		List<String> pos_test=features_pos.subList(43, 54);
		List<String> neg_train=features_neg.subList(0, 34);
		List<String> neg_test=features_neg.subList(34, 45);*/
		for(String s:pos_train)
			collection_of_pos_train.add(s);
		for(String s1:neg_train)
			collection_of_neg_train.add(s1);
		for(String s2:pos_test)
			testdata.add(s2);
		for(String s3:neg_test)
			testdata.add(s3);
		
		checkTheClass(testdata,collection_of_pos_train,collection_of_neg_train);
		
		/*for(String test:testdata)
			System.out.println(test);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");*/
		
		}
		
		Double avgPrecision=0.0, avgRecall=0.0,avgFscore=0.0;
		for(Double a:finalPrecision)
		avgPrecision=avgPrecision+a;
		for(Double a1:finalRecall)
			avgRecall=avgRecall+a1;
		for(Double a2:finalFscore)
			avgFscore=avgFscore+a2;
		System.out.println("prescision:"+avgPrecision);
		System.out.println("recall:"+avgRecall);
		System.out.println("fscore:"+avgFscore);
		System.out.println("the final precision:"+avgPrecision/10);
		System.out.println("the final recall:"+avgRecall/10);
		System.out.println("the final fscore:"+avgFscore/10);
		
		

		
		/*
		 * System.out.println("size of +ve:"+collection_of_pos_train.size());
		 * System
		 * .out.println("size of negative:"+collection_of_neg_train.size());
		 */
		

		/*
		 * System.out.println("from the test data"); for(String
		 * see:features_neg) System.out.println(see);
		 */

	}
//calculating the evaluation metrices
	public static void checkTheClass(ArrayList<String> featuresToTest, ArrayList<String>collection_of_pos_train,ArrayList<String>collection_of_neg_train) {
		double true_positive = 0.0;
		double precision = 0.0;
		double recall = 0.0;
		double fscore = 0.0;
		double true_neagtive = 0.0;
		double false_positive = 0.0;
		double false_negative = 0.0;
		// int length_of_Test=featuresToTest.size();
		for (String each_test : featuresToTest) {
			int val = check_polarity(each_test, collection_of_pos_train,collection_of_neg_train);
			if (each_test.contains("Pos") && (val == 1))
				true_positive++;
			else if (each_test.contains("Pos") && (val == 0))
				false_negative++;
			else if (each_test.contains("Neg") && (val == 1))
				false_positive++;
			else if (each_test.contains("Neg") && (val == 0))
				true_neagtive++;

			// System.out.println("result: "+val+"  the data:"+each_test);
		}
		precision = true_positive / (true_positive + false_positive);
		recall = true_positive / (true_positive + false_negative);
		fscore = (2 * precision * recall) / (precision + recall);
		finalPrecision.add(precision);
		finalRecall.add(recall);
		finalFscore.add(fscore);
		
		//System.out.println("Precision inside: " + precision);
	//	System.out.println("Recall inside: " + recall);
	//	System.out.println("F-Score inside" + fscore);
		System.out.println("TP:"+true_positive);
		System.out.println("FP:"+false_positive);
		System.out.println("TN:"+true_neagtive);
		System.out.println("FN:"+false_negative);

	}
//function that checks out the polarity of the test data and returns a value which indicates the class that has been assigned to the test data
	public static int check_polarity(String test_sentence,ArrayList<String>collection_of_pos_train,ArrayList<String>collection_of_neg_train) {// System.out.println("test sentence :"+test_sentence);
		String[] the_part_test_sentence = test_sentence.split("///");
		String test_sentence_part1 = the_part_test_sentence[0].substring(1);
		String test_sentence_part2 = the_part_test_sentence[1];
		String test_sentence_part3 = the_part_test_sentence[2];
		test_sentence_part3 = test_sentence_part3.substring(0,
				(test_sentence_part3.length() - 1));
		/*
		 * System.out.println(test_sentence_part1);
		 * System.out.println(test_sentence_part2);
		 * System.out.println(test_sentence_part3);
		 */
		int length_of_pos_train = collection_of_pos_train.size();
		int length_of_neg_train = collection_of_neg_train.size();
		double[] positive = new double[length_of_pos_train];
		double[] negative = new double[length_of_neg_train];
		int i = 0;
		for (String pos_train : collection_of_pos_train) {
			;

			String[] the_part_train_sentence = pos_train.split("///");
			String train_sentence_part1 = the_part_train_sentence[0]
					.substring(1);
			String train_sentence_part2 = the_part_test_sentence[1];
			String train_sentence_part3 = the_part_test_sentence[2];
			double sub_part = check_similarity_of_entire_relations(
					train_sentence_part1, test_sentence_part1);
			double obj_part = check_similarity_of_entire_relations(
					train_sentence_part2, test_sentence_part2);
			double final_sim = (sub_part + obj_part) / 2.0;
			positive[i] = final_sim;
			i++;
		}
		int j = 0;
		for (String neg_train : collection_of_neg_train) {
			// System.out.println("even up: "+neg_train);
			String[] the_part_train_sentence = neg_train.split("///");
			String train_sentence_part1 = the_part_train_sentence[0]
					.substring(1);
			String train_sentence_part2 = the_part_test_sentence[1];
			String train_sentence_part3 = the_part_test_sentence[2];
			double sub_part = check_similarity_of_entire_relations(
					train_sentence_part1, test_sentence_part1);
			double obj_part = check_similarity_of_entire_relations(
					train_sentence_part2, test_sentence_part2);
			double final_sim = (sub_part + obj_part) / 2.0;
			negative[j] = final_sim;
			j++;
		}
		Arrays.sort(positive);
		Arrays.sort(negative);
		double sum_of_neg = 0.0, sum_of_pos = 0.0;
		int k = 10;
		for (int jj = length_of_neg_train - 1; jj >= length_of_neg_train - 1
				- k; jj--) {
			sum_of_neg = sum_of_neg + negative[jj];
		}
		for (int ii = length_of_pos_train - 1; ii >= length_of_pos_train - 1
				- k; ii--) {
			sum_of_pos = sum_of_pos + positive[ii];
		}
		if (sum_of_pos > sum_of_neg)
			return 1;
		else
			return 0;

	}
//module of filtering to get the similsrity between two components
	public static double check_similarity_of_entire_relations(String s1,
			String s2) {// System.out.println("up:"+s1+" "+s2);
		String[] s1_parts = new String[50];
		String[] s2_parts = new String[50];
		// String s1="{ ( nsubj-right-VBD ),( nsubj-left-VBD ), }";
		// String s2="{ ( nn-left-NNP ),( prep_of-left-NNP ), }";
		if ((s1.contains("{ }")) || (s2.contains("{ }")))
			return 0.0;
		s1 = s1.replace("{", "");
		s1 = s1.replace("}", "");
		s2 = s2.replace("{", "");
		s2 = s2.replace("}", "");

		// System.out.println(s1);
		if (s1.contains(","))
			s1_parts = s1.split(",");
		else
			s1_parts[0] = s1;
		if (s2.contains(","))
			s2_parts = s2.split(",");
		else
			s2_parts[0] = s2;
		int length_s1 = s1_parts.length;
		int length_s2 = s2_parts.length;
		double sum = 0;
		for (int i = 0; i < length_s1 - 1; i++) {
			for (int j = 0; j < length_s2 - 1; j++) {
				if (s1_parts[i].isEmpty() || s2_parts[j].isEmpty())
					break;
				// System.out.println("1  "+s1_parts[i]+"   "+"2:  "+s2_parts[j]);
				sum = sum + check_subcontents_sim(s1_parts[i], s2_parts[j]);

			}
		}
		double average = sum / ((length_s1 - 1) * (length_s2 - 1));
		return average;

	}
//submodules of filtering process 
	public static double check_subcontents_sim(String first, String second) {// System.out.println("the sentence:"+first+"  "+second);
		first = first.replace(")", "");
		first = first.replace("(", "");
		second = second.replace(")", "");
		second = second.replace("(", "");
		// System.out.println("first"+first);
		// System.out.println("second"+second);
		String[] sub_first = first.split("-");
		String[] sub_second = second.split("-");
		String relation_sub = sub_first[0].trim();
		String tense_sub = "";

		// System.out.println("relation:"+relation_sub);
		String direction_sub = sub_first[1].trim();
		// System.out.println("direction:"+direction_sub);
		if (relation_sub.equalsIgnoreCase("root"))
			tense_sub = "root";
		else
			tense_sub = sub_first[2].trim();
		// System.out.println("tense: "+tense_sub);
		String relation_obj = sub_second[0].trim();
		String direction_obj = sub_second[1].trim();
		String tense_obj = "";
		if (relation_obj.equalsIgnoreCase("root"))
			tense_obj = "root";
		else
			tense_obj = sub_second[2].trim();
		double alpha1 = 0.4, alpha2 = 0.2, alpha3 = 0.4;
		double relation_sub_obj = check_if_word_same(relation_sub, relation_obj);
		double direction_sub_obj = check_if_word_same(direction_sub,
				direction_obj);
		double tense_sub_obj = check_if_word_same(tense_sub, tense_obj);
		double result = (relation_sub_obj * alpha1)
				+ (direction_sub_obj * alpha2) + (tense_sub_obj * alpha3);
		return result;

	}

	public static double check_if_word_same(String s1, String s2) {
		if (s1.equalsIgnoreCase(s2))
			return 1.0;
		else
			return 0.0;
	}

	public static void extractNegPosData(HashSet<String> posdata,
			HashSet<String> negdata) throws IOException {
		String pathOfPos = "C:/Users/prakash/Documents/contentselection/ContentSelection/positiveSpouse.txt";
		String pathOfNeg = "C:/Users/prakash/Documents/contentselection/ContentSelection/negativeSpouse.txt";
		BufferedReader reader = new BufferedReader(new FileReader(pathOfPos));
		String str = "";
		while ((str = reader.readLine()) != null) {
			posdata.add(str);
		}
		reader.close();

		BufferedReader reader1 = new BufferedReader(new FileReader(pathOfNeg));
		String str1 = "";
		while ((str1 = reader1.readLine()) != null) {
			negdata.add(str1);
		}
		reader1.close();

	}

	public static void extractFeatures(ArrayList<String> features_pos,
			HashSet<String> posdata) throws IOException {
		// String
		// sentence="XX SinJie (born 23 January 1976 in Kedah YY) is a YYn Chinese film actress and pop singer##Pos";
		for (String train : posdata) {// System.out.println("trainnnnnnnnnnnnn"+train);
			get_dependency(train, features_pos);
		}

	}
//gives us the dependendency  structure as well as formats the features to a desired form
	public static void get_dependency(String currentQuery,
			ArrayList<String> features_pos) throws IOException// gets the
																// parse tree
																// and the
																// dependencies
	{
		String entire_sentence = "";
		String theclass = currentQuery.split("##")[1];
		currentQuery = currentQuery.split("##")[0];

		// Hashtable<String ,String> nationality=load_nationality();

		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		Tree parse = lp.parse(currentQuery);
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);

		parsed_one = parse.taggedYield();// gives the pos tags
		String the_pos_sentence = parsed_one.toString();
		the_pos_sentence.replace("[", "");
		the_pos_sentence.replace("]", "");

		Collection tdl = gs.typedDependenciesCCprocessed();

		Object[] list = tdl.toArray();

		TypedDependency typedDependency;

		String query = "";
		String final_subfeat = "";
		String final_objfeat = "";
		for (Object object : list) {
			typedDependency = (TypedDependency) object;
			// System.out.println("****"+typedDependency);
			String governer = typedDependency.gov().toOneLineString();
			// System.out.println("the governer:"+governer);
			String dependent = typedDependency.dep().toOneLineString();
			String typeOfRelation = typedDependency.reln().toString();
			if ((!governer.contains("XX")) && (!governer.contains("YY"))
					&& (!dependent.contains("YY"))
					&& (!dependent.contains("XX")))
				continue;
			String subfeat1 = "";
			String objfeat1 = "";
			String side = "";
			String pos = "";
			int n1 = 0;
			int n2 = 0;
			if ((governer.contains("XX")) || (dependent.contains("XX"))) {

				String position_gov = governer.split("-")[1];
				String position_dep = dependent.split("-")[1];
				if((position_gov.equalsIgnoreCase("RSB"))||(position_dep.equalsIgnoreCase("RSB")))
					continue;
				String[] the_parts = the_pos_sentence.split(",");
				if (governer.contains("XX")) {
					n1 = Integer.valueOf(position_gov);
					n2 = Integer.valueOf(position_dep);
					if (n1 > n2)
						side = "left";
					else
						side = "right";
					for (String eachpart : the_parts) {
						if (eachpart.contains(dependent.split("-")[0])) {
							pos = eachpart.split("/")[1];
							break;
						}
					}

				} else {
					n1 = Integer.valueOf(position_gov);
					n2 = Integer.valueOf(position_dep);
					if (n1 > n2)
						side = "right";
					else
						side = "left";
					for (String eachpart : the_parts) {
						if (eachpart.contains(governer.split("-")[0])) {
							pos = eachpart.split("/")[1];
							break;
						}
					}
				}

				subfeat1 = "( " + typeOfRelation + "-" + side + "-" + pos
						+ " )";
				final_subfeat = final_subfeat + subfeat1 + ",";

			} else if ((governer.contains("YY")) || (dependent.contains("YY"))) {

				String position_gov = governer.split("-")[1];
				String position_dep = dependent.split("-")[1];
				String[] the_parts = the_pos_sentence.split(",");
				if (governer.contains("YY")) {
					n1 = Integer.valueOf(position_gov);
					n2 = Integer.valueOf(position_dep);
					if (n1 > n2)
						side = "left";
					else
						side = "right";
					for (String eachpart : the_parts) {
						if (eachpart.contains(dependent.split("-")[0])) {
							pos = eachpart.split("/")[1];
							break;
						}
					}

				} else {
					n1 = Integer.valueOf(position_gov);
					n2 = Integer.valueOf(position_dep);
					if (n1 > n2)
						side = "right";
					else
						side = "left";
					for (String eachpart : the_parts) {
						if (eachpart.contains(governer.split("-")[0])) {
							pos = eachpart.split("/")[1];
							break;
						}
					}
				}

				objfeat1 = "( " + typeOfRelation + "-" + side + "-" + pos
						+ " )";
				final_objfeat = final_objfeat + objfeat1 + ",";

			}

			entire_sentence = entire_sentence + " " + typedDependency;
			// System.out.println("sub:"+subfeat1+" "+"obj: "+objfeat1);

		}
		String the_last_one = "( " + "{ " + final_subfeat + " }" + "/// { "
				+ final_objfeat + " }" + "/// { " + theclass + " } " + ")";
		features_pos.add(the_last_one);

	}

}
