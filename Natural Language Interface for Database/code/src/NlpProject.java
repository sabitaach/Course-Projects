	
	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileReader;
	import java.io.IOException;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	import java.util.ArrayList;
	import java.util.Collection;
	import java.util.Hashtable;
	import java.util.Scanner;

	import edu.stanford.nlp.ling.TaggedWord;
	import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
	import edu.stanford.nlp.trees.GrammaticalStructure;
	import edu.stanford.nlp.trees.GrammaticalStructureFactory;
	import edu.stanford.nlp.trees.PennTreebankLanguagePack;
	import edu.stanford.nlp.trees.Tree;
	import edu.stanford.nlp.trees.TreebankLanguagePack;
	import edu.stanford.nlp.trees.TypedDependency;


	public class NlpProject {
		public static String currentQuery = new String();
		public static String entire_sentence=new String();
		public static String for_check=new String();
		public static ArrayList<TaggedWord> parsed_one=new ArrayList<>();
		public static void main(String[] args) throws ClassNotFoundException, IOException 
		  {
			LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
			System.out.println("Welcome to the Olympics QA System.");
			System.out.println("Please ask a question. Type 'q' when finished.");
			System.out.println();
			String input;
			Scanner keyboard = new Scanner(System.in);
			
			do{		
				input =keyboard.nextLine().trim();
				
				if(!input.equalsIgnoreCase("q")){
					currentQuery = input;
					System.out.println("Query: "+currentQuery);
					String query=intialization(lp);
					//TODO perform any query processing
					printSQL(query); //TODO implement method below
					String result=database_work(query);
					printAnswer(result); //TODO implement method below
					System.out.println();
				}
			}while(!input.equalsIgnoreCase("q"));
			
			keyboard.close();
			System.out.println("Goodbye.");

			
		  }
		
		
		public static void printSQL(String query) throws IOException
		{
			if(!query.isEmpty())
			System.out.println("<SQL> :  "+query);
		}
		
		public static void printAnswer(String result)
		{
				if(result.isEmpty())
				System.out.println("");
				else 
					System.out.println("<ANSWER> : "+result);
		}
		
		public static String intialization(LexicalizedParser lp) throws IOException//gets the parse tree and the dependencies
		{
			entire_sentence="";
			Hashtable<String ,String> nationality=load_nationality();
			
			TreebankLanguagePack tlp = new PennTreebankLanguagePack();
			GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
			Tree parse = lp.parse(currentQuery);
			GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
			Collection tdl = gs.typedDependenciesCCprocessed();
			//String entire_sentence="";
			Object[] list = tdl.toArray();
			TypedDependency typedDependency;
			String query = "";
			for (Object object : list) {
			typedDependency = (TypedDependency) object;
			entire_sentence=entire_sentence+" "+typedDependency;
			      		      }
			String[] node=entire_sentence.split(" ");
			String val=node[node.length-4];
			//System.out.println("val***************"+val);
			
			
			parsed_one=parse.taggedYield();
			if(parsed_one.get(1).toString().contains("DT")&&(parsed_one.get(2).toString().contains("JJ"))&&entire_sentence.contains("nsubj")&&entire_sentence.contains("root")&&(entire_sentence.contains("prepc_in")||entire_sentence.contains("prep_in"))&&(entire_sentence.contains("dobj")||entire_sentence.contains("advmod")))
			//complex did question
			 query= for_aux_complex_case(list,parse,entire_sentence,nationality);
			else  if(entire_sentence.contains("aux")&&entire_sentence.contains("aux")&&entire_sentence.contains("nsubj")&&entire_sentence.contains("root")&&(entire_sentence.contains("prepc_in")||entire_sentence.contains("prep_in"))&&(entire_sentence.contains("dobj")||entire_sentence.contains("advmod")))//normal did
			 query= for_aux_case(list,parse,entire_sentence);	
			else  if(parsed_one.get(0).toString().contains("WP")&& (entire_sentence.contains("prepc_in")||entire_sentence.contains("prep_in"))&& entire_sentence.contains("nsubj")&&entire_sentence.contains("root")&&(entire_sentence.contains("dobj")||entire_sentence.contains("advmod")))//who question
					{
					 query= for_wh_case(list);
					}
			else 
				System.out.println("The format in which you have entered the queston does not match the specified format. Please try another one.");
			return query;
		}
		
		
		public static String for_aux_case(Object[] list, Tree parsed, String entire_sentence) //for normal did questions
		{		
				String select= "Select count(*)";
				String from_proper_noun=Proper_noun_sem(parsed);
				String from_vp=vp_sem(from_proper_noun,list,entire_sentence);
				String query=select+" "+from_vp;
				return query;
			
		}
		
		
		public static String for_wh_case(Object[] list) //for who questions
		{
			String select="Select R.winner";
			String from_vp=vp_sem1(list);
			String query=select+" "+from_vp;
			return query;
		
			
		}
		
		public static String for_aux_complex_case(Object[] list, Tree parsed,String dependency, Hashtable<String, String> nationality) //for complex did type
		{
			String select= "Select count(*)";
			String[] np_comp=np_sem_with_adj(list,nationality);
			String country=np_comp[0];
			String male_female=np_comp[1];
			String from_vp=vp_sem2(list,country,male_female);
			String query=select+" "+from_vp;
			return query;
		
		}
		
		
		
		public static String[] np_sem_with_adj(Object[] list, Hashtable<String, String> nationality)//returns the contents from a np branch
		{	String noun,the_noun = "", the_adj = null, actual_value;
			String[] all = new String[10];
			for(Object individual:list)
			{			
				if(individual.toString().contains("amod"))
				{
					noun=individual.toString().split(",")[0].split("-")[0].trim();
					the_noun=noun.substring(noun.indexOf("(")+1,noun.length());
					the_adj=individual.toString().split(",")[1].split("-")[0].trim();
					break;
						
				}}
					actual_value=nationality.get(the_adj.toLowerCase()).toString().trim();
					all[0]=actual_value.trim();
			if(the_noun.equals("man"))
			all[1]="M";
			else all[1]="F";
				
			return all;
		}
		
		public static String vp_sem2( Object[] list,String country,String male_female)//returns contents under vp
		{
			String distance=pp_sem(list)[0].toLowerCase();
			String from_pp=pp_sem(list)[1].toLowerCase();
			String from_np=np_sem(list).toLowerCase();
			String from_verb=lambda2(from_pp,from_np,country,male_female,distance);
			return from_verb;
			
		}
		
		
		public static String vp_sem1( Object[] list)
		{
			String distance=pp_sem(list)[0].toLowerCase();
			String from_pp=(pp_sem(list)[1].toLowerCase());
			String from_np=np_sem(list).toLowerCase();
			String from_verb=lambda1(from_pp,from_np,distance);
			return from_verb;
			
		}
		
		public static String vp_sem(String f, Object[] list, String entire_sentence)
		{
			String from_proper_noun=f.toLowerCase();
			String distance=pp_sem(list)[0].toLowerCase();
			String from_pp=pp_sem(list)[1].toLowerCase();
			String from_np=np_sem(list).toLowerCase();
			String from_verb=lambda(from_proper_noun,from_pp,from_np,distance);
			return from_verb;
			
		}
		
		/*
		 * The following three functions create sql statements for different categories of questions
		 */
		public static String lambda2(String game,String medal, String country, String male_female, String distance)
		{String from_where;
			if(distance.equalsIgnoreCase("nothing"))
			 from_where="from athletes A INNER JOIN results R ON A.name=R.winner INNER JOIN competitions C on R.comp_id=C.comp_id where  R.medal= '"+medal+"' and C.name= '"+game+"'and A.nationality= '"+country+"' and A.gender= '"+male_female+"';";
			else
				from_where="from athletes A INNER JOIN results R ON A.name=R.winner INNER JOIN competitions C on R.comp_id=C.comp_id where  R.medal= '"+medal+"' and C.name= '"+game+"'and A.nationality= '"+country+"' and A.gender= '"+male_female+"' and C.type = '"+distance+"';";		
			return from_where;
			
		}
		
		
		public static String lambda(String proper_noun,String game,String medal, String distance)
		{String from_where;
			if(distance.equalsIgnoreCase("nothing"))
			 from_where="from results R INNER JOIN competitions C on R.comp_id=C.comp_id where R.winner like "+"'%"+proper_noun+"%'"+" and R.medal= '"+medal+"' and C.name= '"+game+"';";
			else
				from_where="from results R INNER JOIN competitions C on R.comp_id=C.comp_id where R.winner like "+"'%"+proper_noun+"%'"+" and R.medal= '"+medal+"' and C.name= '"+game+"' and C.type = '"+distance+"';";	
			return from_where;
			
		}
		public static String lambda1(String game,String medal,String distance)
		{String from_where;
		if(distance.equalsIgnoreCase("nothing"))
			 from_where="from results R INNER JOIN competitions C on R.comp_id=C.comp_id where  R.medal= '"+medal+"' and C.name= '"+game+"';";
		else
			 from_where="from results R INNER JOIN competitions C on R.comp_id=C.comp_id where  R.medal= '"+medal+"' and C.name= '"+game+"' and C.type = '"+distance+"';";	
			return from_where;
			
		}
		
		
		
		public static String np_sem(Object[] list)
		{
			int flag=0;
			
			String the_noun, final_noun = null;
			for(Object individual:list)
			{
				if(individual.toString().contains("dobj"))//if it is win
				{	 
					
					the_noun=individual.toString().split(",")[1];
					
					final_noun=the_noun.split("-")[0].trim();
					//System.out.println("see"+final_noun);
					
					if(final_noun.equalsIgnoreCase("third")){
						final_noun="bronze";
					}
					flag=1;
					
				}else if(individual.toString().contains("acomp")||individual.toString().contains("advmod"))//if it is arrived
				{
					the_noun=individual.toString().split(",")[1].split("-")[0].trim();
					if(the_noun.equalsIgnoreCase("first"))
						final_noun="gold";
					else if(the_noun.equalsIgnoreCase("second"))
						final_noun="silver";
					else if(the_noun.equalsIgnoreCase("third")){
						final_noun="bronze";
					flag=1;
				}
				}
				
				if(flag==1)
					break;
			
				}
				return final_noun;
		}
		
		
		
		public static String[] pp_sem(Object[] list)
		{
			String pp_from_parse1,pp_from_parse=null;
			String distance,distance2,distance1 = null;
			String[] entire_item=new String[10];
			String[] for_meters=entire_sentence.split(" ");
			entire_item[0]="nothing";
			for(Object individual:list)
			{					
				if(individual.toString().contains("prep_in")||individual.toString().contains("prepc_in"))
				{	pp_from_parse1=individual.toString().split(",")[1].trim();
					pp_from_parse=pp_from_parse1.substring(0,pp_from_parse1.lastIndexOf("-"));
					
				}
				if(individual.toString().contains("num")||individual.toString().contains("nn"))
				{	distance=individual.toString().split(",")[1].trim();
					distance1=distance.substring(0,distance.lastIndexOf("-"));
					entire_item[0]=distance1;
				
				}
				 
							
				}
			if(for_meters[for_meters.length-4].contains("amod"))
			{
				distance=for_meters[for_meters.length-3].toString().split("-")[0].trim();
				if(distance.equals("largehill"))
					distance1="lh";
				else if(distance.equals("nearhill"))
					distance1="nh";
				else distance1=distance.substring(0,distance.length()-1);
				entire_item[0]=distance1;
			}
			
			entire_item[1]=pp_from_parse;
			
			return entire_item;
		}
		
		public static String Proper_noun_sem(Tree parsed)
		
		{String pnoun_from_parse="";
			ArrayList<TaggedWord> from_parse_tree=parsed.taggedYield();
			for(int i=0;i<from_parse_tree.size();i++)
				
			{
				if(from_parse_tree.get(i).toString().contains("NNPS")||from_parse_tree.get(i).toString().contains("NNP")||from_parse_tree.get(i).toString().contains("NN"))
					{pnoun_from_parse=from_parse_tree.get(i).toString().split("/")[0];
					break;}}
			return pnoun_from_parse;
			
		}
		
		public static Hashtable load_nationality() throws IOException//loads the name of nation from the nationality
		{ String currentDirectory = new File("").getAbsolutePath();
		BufferedReader buffer=new BufferedReader(new FileReader(currentDirectory+"\\Country_nationality"));
		Hashtable<String,String> nationality=new Hashtable<>();
		String line;
		while((line=buffer.readLine())!=null)
		{String[] parts=line.split(" ");
		String one=parts[0];
		String part_two ="";
		for(int i=1;i<parts.length;i++)
			part_two=part_two+parts[i]+" ";
		nationality.put(one,part_two);
			
		}
		return nationality;
			
		}
		
		public static String database_work(String query) throws ClassNotFoundException//sends sql queries to the database and returns the retrieved results for printing
		{
			int identity = 0;
			if(parsed_one.get(1).toString().contains("DT")&&(parsed_one.get(2).toString().contains("JJ"))&&entire_sentence.contains("nsubj")&&entire_sentence.contains("root")&&(entire_sentence.contains("prepc_in")||entire_sentence.contains("prep_in"))&&(entire_sentence.contains("dobj")||entire_sentence.contains("advmod")))
			//complex did question
				 identity=3;
			else  if(entire_sentence.contains("aux")&&entire_sentence.contains("aux")&&entire_sentence.contains("nsubj")&&entire_sentence.contains("root")&&(entire_sentence.contains("prepc_in")||entire_sentence.contains("prep_in"))&&(entire_sentence.contains("dobj")||entire_sentence.contains("advmod")))//normal did
				 identity=1;	
			else  if(parsed_one.get(0).toString().contains("WP")&& (entire_sentence.contains("prepc_in")||entire_sentence.contains("prep_in"))&& entire_sentence.contains("nsubj")&&entire_sentence.contains("root")&&(entire_sentence.contains("dobj")||entire_sentence.contains("advmod")))//who question
						identity=2;
			
			
			 
		    Class.forName("org.sqlite.JDBC");
	String final_answer = "";
		    Connection connection = null;
		    try
		    {
		     
		      connection = DriverManager.getConnection("jdbc:sqlite::resource:Nlp.sqlite");
		      Statement statement = connection.createStatement();
		      statement.setQueryTimeout(30);  // set timeout to 30 sec.
		      ResultSet rs = statement.executeQuery(query);
		      int flag=0;
		      if(identity==1)
		    	
		    	  {if(rs.getString(1).equalsIgnoreCase("0"))
		    		final_answer=" no";
		    	else final_answer="yes";
		    	  }
		      if(identity==2)
		      {
		    	  while(rs.next())
		    	  {
		    		 final_answer= final_answer+ rs.getString("winner")+"   "; 
		    	  }
		      }
		      if(identity==3)
		      {
		    	  if(rs.getString(1).equalsIgnoreCase("0"))
		    		  final_answer=" no";
			    	else final_answer="yes";
		      }
		     
		  	    }
		    catch(SQLException e)
		    {
		      System.err.println("");
		    }
		   return final_answer;
		}
		
		}
		


		
		
			
		





