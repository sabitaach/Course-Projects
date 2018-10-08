package am.matcher.myMatcher;




import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import am.Utility;
import am.app.mappingEngine.AbstractMatcher;
import am.app.mappingEngine.Alignment;
import am.app.mappingEngine.DefaultMatcherParameters;
import am.app.mappingEngine.MappedNodes;
import am.app.mappingEngine.Mapping;
import am.app.mappingEngine.MatcherFactory;
import am.app.mappingEngine.MatcherFeature;
import am.app.mappingEngine.ReferenceEvaluationData;
import am.app.mappingEngine.AbstractMatcher.MatcherCategory;
import am.app.mappingEngine.AbstractMatcher.alignType;
import am.app.mappingEngine.Mapping.MappingRelation;
import am.app.mappingEngine.StringUtil.Normalizer;
import am.app.mappingEngine.StringUtil.NormalizerParameter;
import am.app.mappingEngine.Mapping;
import am.app.mappingEngine.Mapping.MappingRelation;
import am.app.mappingEngine.StringUtil.PorterStemmer;
import am.app.mappingEngine.StringUtil.StringMetrics;
import am.app.mappingEngine.referenceAlignment.ReferenceAlignmentMatcher;
import am.app.mappingEngine.referenceAlignment.ReferenceAlignmentParameters;
import am.app.mappingEngine.referenceAlignment.ReferenceEvaluator;
import am.app.mappingEngine.similarityMatrix.ArraySimilarityMatrix;
import am.app.mappingEngine.similarityMatrix.SimilarityMatrix;
import am.app.ontology.Node;
import am.app.ontology.Ontology;
import am.app.ontology.ontologyParser.OntoTreeBuilder;
import am.app.ontology.ontologyParser.OntologyDefinition;
import am.app.ontology.profiling.manual.ManualOntologyProfiler;
import am.app.similarity.StringSimilarityMeasure;
import am.matcher.FilterMatcher.FilterMatcher;
import am.matcher.bsm.BaseSimilarityMatcher;
import am.matcher.bsm.BaseSimilarityParameters;
import am.matcher.multiWords.MultiWordsMatcher;
import am.matcher.multiWords.MultiWordsParameters;
import am.matcher.parametricStringMatcher.ParametricStringMatcher;
import am.matcher.parametricStringMatcher.ParametricStringParameters;
import am.parsing.AlignmentOutput;
import am.utility.WordNetUtils;



public class MyMatcher_Sabita  extends AbstractMatcher  {
	private transient NormalizerParameter param1;
	private transient NormalizerParameter param2;
	private transient NormalizerParameter param3;
	private transient Normalizer norm1;
	private transient Normalizer norm2;
	private transient Normalizer norm3;
	private transient Normalizer normalizer;
	private StringSimilarityMeasure ssm;
	private static String[] confs = {"cmt","conference","confOf","edas","ekaw","iasted","sigkdd"};

	public MyMatcher_Sabita(){
		super();
		setName("MyMatcher");
		initializeVariables();
		setCategory(MatcherCategory.UNCATEGORIZED);		
	}
	

	@Override
	protected void initializeVariables() {
		super.initializeVariables();
		
		needsParam = true;
		ParametricStringParameters1 parameters = new ParametricStringParameters1();
		ssm = parameters.measure.getMeasure();
		initializeNormalizer();
		
		// setup the different normalizers that will normalize the local names of the source and target nodes
		param1 = new NormalizerParameter();
		param1.setAllTrue();
		param1.normalizeDigit = false;
		param1.stem = false;
		norm1 = new Normalizer(param1);
		
		
		param2 = new NormalizerParameter();
		param2.setAllTrue();
		param2.normalizeDigit = false;
		norm2 = new Normalizer(param2);
		
		param3 = new NormalizerParameter();
		param3.setAllTrue();
		norm3 = new Normalizer(param3);

		
		// category of the matcher
		setCategory(MatcherCategory.SYNTACTIC);
		
					
	}
	public void initializeNormalizer() {
		normalizer = new Normalizer( new NormalizerParameter() );
	}
	
		
	@Override
	protected Mapping alignTwoNodes(Node source, Node target,
			alignType typeOfNodes, SimilarityMatrix matrix) throws Exception {
		double sim=0.0d;
		double new_val=0.0d;
		/*
		 * calculateSimilarity checks if the domain and range for property match or not. It also checks
		 * if the local names of nodes match before and after normalization 
		 */
		double another_sim=calculateSimilarity(source,target,typeOfNodes);
		/*
		 * weighted_result checks if the ancestors of the nodes or the children of the nodes match.
		 * Also, it gives a weighted average of the weights of label,local name and comment based on their 
		 * relevancy. 
		 */
		double from_ancestors_child=weighted_result(source,target);
		/*
		 * calculateWordSimilarity returns a value based on the number of tokens of the local name of the source and target that match each other. 
		 */
		new_val=calculateWordSimilarity(source.getLocalName(), target.getLocalName());
		if(typeOfNodes==alignType.aligningClasses)
			new_val=new_val*0.6;
		else new_val=new_val*0.2;
		sim= another_sim*0.9+ from_ancestors_child*0.3+new_val*0.2;
		if(sim>1)sim=1;
		return new Mapping(source, target, sim,MappingRelation.EQUIVALENCE);
	}
	
	/*
	 * function for checking if the domain and range of the properties match or not.
	 */
	double check_domain_range(Node source,Node target)
	{
		String srcdomain, trgdomain, srcrange, trgrange;
		int sourceindex = source.getIndex();
		int targetindex = target.getIndex();
		double sim=0.0d;
		OntResource stringuri;

		stringuri = sourceOntology.getPropertiesList()
				.get(sourceindex).getPropertyDomain();
		srcdomain = getstring(stringuri);

		stringuri =targetOntology.getPropertiesList()
				.get(targetindex).getPropertyDomain();
		trgdomain = getstring(stringuri);

		stringuri =sourceOntology.getPropertiesList()
				.get(sourceindex).getPropertyRange();
		srcrange = getstring(stringuri);

		stringuri = targetOntology.getPropertiesList()
				.get(targetindex).getPropertyRange();
		trgrange = getstring(stringuri);

		if (srcdomain.equals(trgdomain)
				&& srcrange.equals(trgrange)) {
			sim = 1.0d;
		} else {
			sim = 0.0d;
		}
		return sim;
	}
	
	
	/*
	 * As described above, this function checks if the ancestors or siblings of the source and target node
	 * match. Also, it returns a weighted average if the label, local name and comment relevancy.
	 */
	public double weighted_result(Node source, Node target)
	{
		
		double sim=0.0d;
		WordNetUtils W = new WordNetUtils();
		double localSim = 0;
		double labelSim = 0;
		double commentSim = 0;
		double seeAlsoSim = 0;
		double isDefBySim = 0;
				
		//i need to use local varables for weights  to modify them in case of weights redistribution but without modifying global parameters
		double localWeight = 0.9;
		double labelWeight =0.3;
		double commentWeight =0.6; 
		double seeAlsoWeight = 0.05; 
		double isDefinedByWeight =0.05;

		List<Node> sourceParents = source.getParents(); // parent of source node
		List<Node> targetParents = target.getParents(); // parent of target node
		List<Node> sourceSiblingSet = new ArrayList<Node>();  // set of siblings of the source node
		for( Node sourceParent : sourceParents ) {
			for( int i = 0; i < sourceParent.getChildCount(); i++ ) {	
				Node currentChild = sourceParent.getChildAt(i);
				if( currentChild.equals(source) ) { continue; } // do not add the node to its own sibling set
				sourceSiblingSet.add( currentChild );
			}
		}
		
		List<Node> targetSiblingSet = new ArrayList<Node>();  // set of siblings of the target node
		for( Node targetParent : targetParents ) {
			for( int i = 0; i < targetParent.getChildCount(); i++ ) {	
				Node currentChild = targetParent.getChildAt(i);
				if( currentChild.equals(target) ) { continue; } // do not add the node to its own sibling set
				targetSiblingSet.add( currentChild );
			}
		}
		
		int n = sourceSiblingSet.size();  // number of siblings of the source node  i.e. sibling_count(source)
		int m = targetSiblingSet.size();  // number of siblings of the target node  i.e. sibling_count(target)
		int o=sourceParents.size();//size of the parent of the source
		int p=targetParents.size();//size of the parent of the target
		int check=0;
		//checking if the parents are same
		for(int i=0;i<o;i++){
			for (int j=0;j<p;j++){
				if((sourceParents.get(i).getLocalName().equalsIgnoreCase(targetParents.get(j).getLocalName()))||(W.areSynonyms(sourceParents.get(i).getLocalName(), targetParents.get(j).getLocalName()))==true)
				{check++;
				
			}}}
		
		if(check>=1)
		{if (0.8 > sim)
			sim = 0.8;}
		check=0;
		
		//checking if the siblings are same
		for(int i=0;i<n;i++)
			{for (int j=0;j<m;j++)
			{
				if((sourceSiblingSet.get(i).getLocalName().equalsIgnoreCase(targetSiblingSet.get(j).getLocalName()))||(W.areSynonyms(sourceSiblingSet.get(i).getLocalName(), targetSiblingSet.get(j).getLocalName()))==true)
					{
					check++;
					
			}}}
		if(check>=1)
		{if (0.8 > sim)
			sim = 0.8;}
		
			
		//The redistrubution is implicit in the weighted average mathematical formula
		//i just need to put the weight equal to 0
		//but labels should be first redistributed to localnames and vice-versa
		//we set the weight of a feature to 0 if any of the two value is irrelevant
		
			if(localWeight!=0){
				if(Utility.isIrrelevant(source.getLocalName()) || Utility.isIrrelevant(target.getLocalName())){
					//we should redistribute localname to label if label is relevant
					if(!Utility.isIrrelevant(source.getLabel()) && !Utility.isIrrelevant(target.getLabel())){
						labelWeight += localWeight;
					}
					localWeight = 0;
				}	
			}
			
			if(labelWeight!=0){
				if(Utility.isIrrelevant(source.getLabel()) || Utility.isIrrelevant(target.getLabel())){
					//we should redistribute label to localname if localname is relevant
					if(!Utility.isIrrelevant(source.getLocalName()) && !Utility.isIrrelevant(target.getLocalName())){
						localWeight+= labelWeight;
					}
					labelWeight = 0;
				}
			}

			if(commentWeight == 0 || Utility.isIrrelevant(source.getComment()) || Utility.isIrrelevant(target.getComment()))
				commentWeight = 0;
			if(seeAlsoWeight == 0 || Utility.isIrrelevant(source.getSeeAlsoLabel()) || Utility.isIrrelevant(target.getSeeAlsoLabel()))
				seeAlsoWeight = 0;
			if(isDefinedByWeight == 0 || Utility.isIrrelevant(source.getIsDefinedByLabel()) || Utility.isIrrelevant(target.getIsDefinedByLabel()))
				isDefinedByWeight = 0;
			
					
		double totWeight = localWeight + labelWeight + commentWeight + seeAlsoWeight + isDefinedByWeight; //important to get total after the redistribution
		if(totWeight > 0) {
			if(localWeight > 0) {
				localSim =  performStringSimilarity(source.getLocalName(), target.getLocalName());
				localSim *= localWeight;
			}
			if(labelWeight > 0) {
				labelSim =  performStringSimilarity(source.getLabel(), target.getLabel());
				labelSim *= labelWeight;
			}
			if(commentWeight > 0) {
				commentSim = performStringSimilarity(source.getComment(), target.getComment());
				commentSim *= commentWeight;
			}
			if(seeAlsoWeight > 0) {
				seeAlsoSim = performStringSimilarity(source.getSeeAlsoLabel(), target.getSeeAlsoLabel());
				seeAlsoSim *= seeAlsoWeight;
			}
			if(isDefinedByWeight > 0) {
				isDefBySim = performStringSimilarity(source.getIsDefinedByLabel(), target.getIsDefinedByLabel());
				isDefBySim *= isDefinedByWeight;
			}
			
			
			double before_sim=localSim + labelSim + commentSim + seeAlsoSim + isDefBySim;
			
			//Weighted average, this normalize everything so also if the sum of  weights is not one, the value is always between 0 and 1. 
			//this also automatically redistribute 0 weights.
			before_sim/=totWeight;
			if(sim>before_sim)//
			sim=before_sim;
			}
			
	sim=sim*0.8;
	return sim;
	}
	
	/*
	 * checking how similar two strings are after normalizing them
	 */
public double performStringSimilarity(String sourceString, String targetString) {
		
		if(sourceString == null || targetString == null )
			return 0; //this should never happen because we set string to empty string always
			
		//PREPROCESSING
		
			sourceString = normalizer.normalize(sourceString);
			targetString = normalizer.normalize(targetString);
		
		//usually empty strings shouldn't be compared, but if redistrubute weights is not selected 
		//in the redistribute weights case this can't happen because the code won't arrive till here
		if(sourceString.equals("")) 
			if(targetString.equals(""))
				return 1;
			else return 0;
		else if(targetString.equals(""))
			return 0;
return ssm.getSimilarity(sourceString, targetString);
	}

/*
 * extracting a string after removing the # symbol
 */
	private String getstring(OntResource str) {
		String stringuri;
		if (str == null)
			return "";
		else
			stringuri=str.toString();
		// TODO Auto-generated method stub
		String result;

		int loc = 0, j = 0;
		for (int i = 0; i < stringuri.length(); i++) {
			if (stringuri.charAt(i) == '#') {
				loc = i + 1;
				break;
			}
		}

		result = stringuri.substring(loc).toLowerCase();

		return result;
	}


/*
 * calculateSimilarity checks if the local names match before normalizing and after normalizing or not.
 * Also, this functions calls the other function that checks out if the domain range values match.
 */
	private double calculateSimilarity(Node source, Node target,alignType typeOfNodes ) {
		String sourceName=source.getLocalName();
		String targetName=target.getLocalName();
		WordNetUtils W = new WordNetUtils();
		double temp_val=0.0d;
		double for_checking=0.0d;
		
		if(typeOfNodes==alignType.aligningProperties){
			for_checking=check_domain_range(source, target);
		}

		if( sourceName.equalsIgnoreCase(targetName))
		temp_val=1.0d;
		// Step 1:		run treatString on each name to clean it up
		//              treatString removes (and replaces them with a space): _ , .
		sourceName = treatString(sourceName);
		targetName = treatString(targetName);
		
		if( sourceName.equalsIgnoreCase(targetName))
temp_val=1.0d;
		
		else if( W.areSynonyms(sourceName, targetName)==true)
			temp_val=1.0d;
		
		else if((sourceName.indexOf(targetName)!= -1)||(targetName.indexOf(sourceName)!= -1))
		{
			temp_val=0.5d;
			
		}
		
			// all normalization without stemming and digits return 0.95
			String sProcessed = norm1.normalize(sourceName);
			String tProcessed= norm1.normalize(targetName);
			if(sProcessed.equals(tProcessed))//||(W.areSynonyms(sProcessed, tProcessed))==true) 
				{if(0.95>temp_val)
				temp_val= 0.95d;}
			
			// all normalization without digits return 0.90
			sProcessed = norm2.normalize(sourceName);
			tProcessed= norm2.normalize(targetName);
			if(sProcessed.equals(tProcessed))//||(W.areSynonyms(sProcessed, tProcessed))==true)
				{if(0.9>temp_val)
					temp_val= 0.9d;
				}
			// all normalization return 0.85
			sProcessed = norm3.normalize(sourceName);
			tProcessed = norm3.normalize(targetName);
			if(sProcessed.equals(tProcessed))//||(W.areSynonyms(sProcessed, tProcessed))==true)
				{if(0.85>temp_val)
				temp_val= 0.85d;
			}
		
			if(typeOfNodes==alignType.aligningProperties){
				temp_val=for_checking+(0.3*temp_val);
				
			}
			// none of the above
			return temp_val;
		
	}
	
	
	
	/*
	 * This function takes two word DEFINITIONS, stems them, 
	 * removes non-content and repeated words, then determines how many words
	 * are in common between the definitions, and calculates a similarity 
	 * based on the number of common words found.
	 * 
	 */
	protected float calculateWordSimilarity(String d1, String d2){
		   d1=removeNonChar(d1);
		   d2=removeNonChar(d2);
		if(d1.equalsIgnoreCase(d2)) return 1;
		    
		// treat the long descriptions
		d1 = treatString(d1); 
		d2 = treatString(d2);
		    
		if(d1.equalsIgnoreCase(d2)) return 1; // the definitions are exactly equal
		    
		ArrayList<String> d1Tokens = new ArrayList<String>(); 
		ArrayList<String> d2Tokens = new ArrayList<String>();
		PorterStemmer ps = new PorterStemmer();

		String word;
		
		// Tokenize the first description, using space as the token separator
		// then remove non-content and repeated words.
		StringTokenizer st = new StringTokenizer(d1);
		
		while(st.hasMoreTokens()){
		  word = st.nextToken();
		   word = ps.stem(word);
		   if(!isNonContent(word) && isNotRepeated(word,d1Tokens) && !word.equalsIgnoreCase("Invalid term"))
		    d1Tokens.add(word);
		}
		 
		st = new StringTokenizer(d2);
		
		while(st.hasMoreTokens()){
		  word = st.nextToken();
		  word = ps.stem(word);
		   if(!isNonContent(word) && isNotRepeated(word,d2Tokens) && !word.equalsIgnoreCase("Invalid term"))
		    d2Tokens.add(word);
		}

				 
		
		String [] def1 = new String[ d1Tokens.size()];
		String [] def2 = new String[d2Tokens.size()];
		
		for(int i=0; i<d1Tokens.size(); i++)
		    def1[i] = d1Tokens.get(i);
		   
		   
		
		for(int i=0; i<d2Tokens.size(); i++)
		    def2[i] = d2Tokens.get(i);
		    
		if(def1.length == 0 || def2.length == 0)
		    return 0;
		
		
		int counter =0;
		
		// count how many words the lists has in common
		for(int i=0; i<def1.length; i++)
		    for(int j=0; j<def2.length; j++)
		        if(def1[i].equalsIgnoreCase(def2[j]) )
		            counter++;
		    
				
		// return the computed similarity (based on the common words)
		return (((float)counter) /((float) (def1.length + def2.length )));
		 
	}

	/*
	 * checks if a word falls under the elements listed below
	 */
	public static boolean isNonContent(String s){
	    
		if(s.equalsIgnoreCase("the") || 
		   s.equalsIgnoreCase("is") || 
		   s.equalsIgnoreCase("this") || 
		   s.equalsIgnoreCase("are") || 
		   s.equalsIgnoreCase("to") || 
		   s.equalsIgnoreCase("a") ||
		   s.equalsIgnoreCase("e") ||
		   s.equalsIgnoreCase("an") || 
		   s.equalsIgnoreCase("in") ||
		   s.equalsIgnoreCase("or") ||
		   s.equalsIgnoreCase("and") || 
		   s.equalsIgnoreCase("for") || 
		   s.equalsIgnoreCase("that") ) 
		{
			return true;
		}
			
		return false;
		       
		}
	
	
	/*
	 * Remove anything from a string that isn't a Character or a space
	 */
	private String removeNonChar(String s){
        
		String result = "";
		for(int i=0; i<s.length(); i++)
			if(Character.isLetter(s.charAt(i)) || s.charAt(i)==' ')
				result += s.charAt(i);
             
		return result;   
	}

/*
 * checks if a word is there in the sentence or not	
 */
private boolean isNotRepeated(String word,ArrayList<String> sentence){
	    
		for(int i=0; i<sentence.size(); i++)
			if(word.equalsIgnoreCase( sentence.get(i) ))
				return false;
		 
		
		return true;
	}
	
/*
 * removes the unwanted symbols from a string
 */
 public static String treatString(String s) {
		 
		 
		 String s2 = s.replace("_"," ");
		 s2 = s2.replace("-"," ");
		 s2 = s2.replace("."," ");	
	    
	    
	    for(int i=0;i<s2.length()-1; i++){
	    	if( Character.isLowerCase(s2.charAt(i)) &&  Character.isUpperCase(s2.charAt(i+1)) ){
	    		s2 = s2.substring(0, i + 1) + " " + s2.substring(i + 1);
	    	}
		}
	    
	    return s2;
	 }
	
	
	private ArrayList<Double> referenceEvaluation(AbstractMatcher mm,String pathToReferenceAlignment)
			throws Exception {

	
		// Run the reference alignment matcher to get the list of mappings in
		// the reference alignment file
		ReferenceAlignmentMatcher refMatcher = new ReferenceAlignmentMatcher();


		// these parameters are equivalent to the ones in the graphical
		// interface
		ReferenceAlignmentParameters parameters = new ReferenceAlignmentParameters();
		parameters.fileName = pathToReferenceAlignment;
		parameters.format = ReferenceAlignmentMatcher.OAEI;
		parameters.onlyEquivalence = false;
		parameters.skipClasses = false;
		parameters.skipProperties = false;
		
		refMatcher.setSourceOntology(mm.getSourceOntology());
		refMatcher.setTargetOntology(mm.getTargetOntology());
		
		

		// When working with sub-superclass relations the cardinality is always
		// ANY to ANY
		if (!parameters.onlyEquivalence) {
			parameters.maxSourceAlign = AbstractMatcher.ANY_INT;
			parameters.maxTargetAlign = AbstractMatcher.ANY_INT;
		}

		refMatcher.setParam(parameters);

		// load the reference alignment
		refMatcher.match();
		
		Alignment<Mapping> referenceSet;
		if (refMatcher.areClassesAligned() && refMatcher.arePropertiesAligned()) {
			referenceSet = refMatcher.getAlignment(); // class + properties
		} else if (refMatcher.areClassesAligned()) {
			referenceSet = refMatcher.getClassAlignmentSet();
		} else if (refMatcher.arePropertiesAligned()) {
			referenceSet = refMatcher.getPropertyAlignmentSet();
		} else {
			// empty set? -- this should not happen
			referenceSet = new Alignment<Mapping>(Ontology.ID_NONE,
					Ontology.ID_NONE);
		}

		// the alignment which we will evaluate
		Alignment<Mapping> myAlignment;

		if (refMatcher.areClassesAligned() && refMatcher.arePropertiesAligned()) {
			myAlignment = mm.getAlignment();
		} else if (refMatcher.areClassesAligned()) {
			myAlignment = mm.getClassAlignmentSet();
		} else if (refMatcher.arePropertiesAligned()) {
			myAlignment = mm.getPropertyAlignmentSet();
		} else {
			myAlignment = new Alignment<Mapping>(Ontology.ID_NONE,
					Ontology.ID_NONE); // empty
		}

		// use the ReferenceEvaluator to actually compute the metrics
		ReferenceEvaluationData rd = ReferenceEvaluator.compare(myAlignment,
				referenceSet);

		// optional
		setRefEvaluation(rd);

		// output the report
		StringBuilder report = new StringBuilder();
		report.append("Reference Evaluation Complete\n\n").append(getName())
				.append("\n\n").append(rd.getReport()).append("\n");
		
		
		double precision=rd.getPrecision();
		double recall=rd.getRecall();
		double fmeasure=rd.getFmeasure();
		
		ArrayList<Double> results=new ArrayList<Double>();
		results.add(precision);
		results.add(recall);
		results.add(fmeasure);
		
		return results;
		
			}

		
	public static void main(String[] args) throws Exception {
	
		
		MyMatcher_Sabita mymatcher = new MyMatcher_Sabita();
		DefaultMatcherParameters myparam= new DefaultMatcherParameters();
		myparam.threshold=0.6;
		myparam.maxSourceAlign=1;
		myparam.maxTargetAlign=1;
		mymatcher.setParam(myparam);
		
		
		String ONTOLOGY_BASE_PATH ="C:/Users/prakash/Desktop/sabita_UIC/sem-3/web semantics/conference_dataset/conference_dataset/"; // Use your base path
		double precision=0.0d;
		double recall=0.0d;
		double fmeasure=0.0d;
		int size=21;
		for(int i = 0; i < confs.length-1; i++)
		{
			for(int j = i+1; j < confs.length; j++)
			{
				Ontology source = OntoTreeBuilder.loadOWLOntology(ONTOLOGY_BASE_PATH + "/"+confs[i]+".owl");
				Ontology target = OntoTreeBuilder.loadOWLOntology(ONTOLOGY_BASE_PATH + "/"+confs[j]+".owl");
				
				OntologyDefinition def1=new OntologyDefinition(true, source.getURI(), null, null);
				OntologyDefinition def2=new OntologyDefinition(true, target.getURI(), null, null);
		
				def1.largeOntologyMode=false;
				source.setDefinition(def1);
				def2.largeOntologyMode=false;
				target.setDefinition(def2);
				ManualOntologyProfiler mop=new ManualOntologyProfiler(source, target);

			mymatcher.setSourceOntology(source);
				mymatcher.setTargetOntology(target);
				
				try {
						
					mymatcher.match();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				/*
				 * writing output to the file
				 */
			
			/*					
				try {
					String alignmentFilename = "C:/Users/prakash/Desktop/result/"+confs[i]+"-"+confs[j]+"-"+"alignment.rdf";//source-target-alignment.rdf";
					AlignmentOutput output = new AlignmentOutput(mymatcher.getAlignment(), alignmentFilename);
					String sourceUri = source.getURI();
					String targetUri = target.getURI();
					output.write(sourceUri, targetUri, sourceUri, targetUri, mymatcher.getName());
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
				*/	
				
			
				
			ArrayList<Double> results=	mymatcher.referenceEvaluation(mymatcher,ONTOLOGY_BASE_PATH + confs[i]+"-"+confs[j]+".rdf");
			
			precision+=results.get(0);
			
			recall+=results.get(1);
		
			fmeasure+=results.get(2);
						
			}
		}

		StringBuilder sb= new StringBuilder();
		precision/=size;
		recall/=size;
		fmeasure/=size;
		
		String pPercent = Utility.getOneDecimalPercentFromDouble(precision);
		String rPercent = Utility.getOneDecimalPercentFromDouble(recall);
		String fPercent = Utility.getOneDecimalPercentFromDouble(fmeasure);
		
		
		sb.append("Precision = Correct/Discovered: "+ pPercent+"\n");
		sb.append("Recall = Correct/Reference: "+ rPercent+"\n");
		sb.append("Fmeasure = 2(precision*recall)/(precision+recall): "+ fPercent+"\n");
	
		String report=sb.toString();
		System.out.println("Evaulation results:");
		System.out.println(report);

	}
	
}
