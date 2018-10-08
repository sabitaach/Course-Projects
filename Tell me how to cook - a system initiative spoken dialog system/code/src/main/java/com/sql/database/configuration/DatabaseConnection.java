package com.sql.database.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

public class DatabaseConnection {

	/**
	 * @param args
	 */
	
	 // JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost:3306/recipedb";

	   //  Database credentials
	   static final String USER = "root";
	   static final String PASS = "root";
	   static Connection conn = null;
	   static Statement stmt = null;
	   
	public void initializeDatabase() {
		  
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);

		      //STEP 4: Execute a query
		      System.out.println("Creating statement...");
		     
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }
		   System.out.println("Goodbye!");
		}//end main

	
	public ArrayList<String> getDishes()
	{
		ArrayList<String> dishList = new ArrayList<String>();
		
		 try {
			stmt = conn.createStatement();
			 String sql;
		      sql = "SELECT * FROM dishes";
		      ResultSet rs = stmt.executeQuery(sql);

		      //STEP 5: Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		        // int id  = rs.getInt("id");
		         //int age = rs.getInt("age");
		    	  String nameOfDish=rs.getString("name");
		         dishList.add(nameOfDish);

		      }
		      //stmt.close();
		     //conn.close();
		      //rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     System.out.println("this is the size of the arrylist from db: "+dishList.size());
	   return dishList;
	}
	
	
	
	public ArrayList<String> getIngredients(String dishName)
	{
		System.out.println("this is the dish name:"+dishName);
		ArrayList<String> ingredients = new ArrayList<String>();
		
		 try {
			stmt = conn.createStatement();
			 String sql;
		      sql = "Select  ingredient from ingredients,dishes where ingredients.dishId=dishes.id and dishes.name='"+dishName+"'";
		      ResultSet rs = stmt.executeQuery(sql);

		      //STEP 5: Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		        // int id  = rs.getInt("id");
		         //int age = rs.getInt("age");
		    	  String nameOfIngredient=rs.getString(1);
		         ingredients.add(nameOfIngredient);
		        
		      }
		     // stmt.close();
		     //conn.close();
		      //rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     System.out.println("this is the size of the arrylist for ingredients from db: "+ingredients.size());
	   return ingredients;
	}
	
	public ArrayList<String> getCustomizedIngredients(String dishName,int servingsNew)
	{
		System.out.println("this is the dish name:"+dishName);
		ArrayList<String> ingredients = new ArrayList<String>();
		int servingsOrg=0;
		try{
			stmt = conn.createStatement();
			 String sql;
		      sql = "Select  servings from dishes where dishes.name='"+dishName+"'";
		      ResultSet rs = stmt.executeQuery(sql);
		      while(rs.next()){
		    	   servingsOrg=rs.getInt(1);
		      }

		}	
catch(SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		System.out.println("this is the original servings: "+servingsOrg+" this is the customized amount: "+servingsNew);
		
		
		
		 try {
			stmt = conn.createStatement();
			 String sql;
		      sql = "Select  amount,measure,ingredient_name from ingredients,dishes where ingredients.dishId=dishes.id and dishes.name='"+dishName+"'";
		      ResultSet rs = stmt.executeQuery(sql);

		      //STEP 5: Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		        String amount=  rs.getString(1);
		        String measure=  rs.getString(2);
		        String ingredient_name=  rs.getString(3);
		         //int age = rs.getInt("age");
		    	int amountModified=(int)Math.ceil(Double.parseDouble(amount)/servingsOrg)*servingsNew;
		    	System.out.println("amount org: "+amount+" this is the customized: "+amountModified);
		    	String theCombinedContent=amountModified+" "+measure+" "+ingredient_name;
		         ingredients.add(theCombinedContent);
		        
		      }
		     // stmt.close();
		     //conn.close();
		      //rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     System.out.println("this is the size of the arrylist for ingredients from db: "+ingredients.size());
	   return ingredients;
	}
	
	
	public ArrayList<String> getRecipes(String dishName)
	{
		System.out.println("this is the dish name inside the database:"+dishName);
		ArrayList<String> theRecipes = new ArrayList<String>();
		
		 try {
			stmt = conn.createStatement();
			 String sql;
		      sql = "Select steps from recipes,dishes where recipes.dishId=dishes.id and dishes.name='"+dishName+"'";
		      ResultSet rs = stmt.executeQuery(sql);

		      //STEP 5: Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		        // int id  = rs.getInt("id");
		         //int age = rs.getInt("age");
		    	  String nameOfRecipe=rs.getString(1);
		         theRecipes.add(nameOfRecipe);
		        
		      }
		     // stmt.close();
		     //conn.close();
		      //rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("the error is in extracting the recipe details from the database");
		}
	     System.out.println("this is the size of the arrylist for recipe from db: "+theRecipes.size());
	   return theRecipes;
	}
	
	public Hashtable<String,String> getTimeServingDiffLevel(String dishName)
	{
		System.out.println("this is the dish name:"+dishName);
		Hashtable<String,String> itemsAndValues=new Hashtable<String, String>();
		
		 try {
			stmt = conn.createStatement();
			 String sql;
		      sql = "Select servings, total_time, difficulty from dishes where dishes.name='"+dishName+"'";
		      ResultSet rs = stmt.executeQuery(sql);

		      //STEP 5: Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		        // int id  = rs.getInt("id");
		         //int age = rs.getInt("age");
		    	  String servings=rs.getString(1);
		    	  String time=rs.getString(2);
		    	  String difficultyLevel=rs.getString(3);
		         itemsAndValues.put("servings",servings);
		         itemsAndValues.put("time",time);
		         itemsAndValues.put("difficultyLevel",difficultyLevel);
		        
		      }
		     // stmt.close();
		     //conn.close();
		      //rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	   return itemsAndValues;
	}
	
	
	public ArrayList<String> getNotesFromDb(String dishName)
	{
		System.out.println("this is the dish name:"+dishName);
		ArrayList<String> notes=new ArrayList<String>();
		
		 try {
			stmt = conn.createStatement();
			 String sql;
		      sql = "Select note from notes,dishes where notes.dishId=dishes.id and dishes.name='"+dishName+"'";
		      ResultSet rs = stmt.executeQuery(sql);

		      //STEP 5: Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		        // int id  = rs.getInt("id");
		         //int age = rs.getInt("age");
		    	  String eachNote=rs.getString(1);
		    	 notes.add(eachNote);
		        
		      }
		     // stmt.close();
		     //conn.close();
		      //rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	   return notes;
	}
	
	public ArrayList<String> getContentByDifficulty(String difficulty)
	{
		
		
		System.out.println("this is the difficulty level:"+difficulty);
		ArrayList<String> recipe=new ArrayList<String>();
		
		 try {
			stmt = conn.createStatement();
			 String sql;
		      sql = "Select name from dishes where difficulty='"+difficulty+"'";
		      ResultSet rs = stmt.executeQuery(sql);

		      //STEP 5: Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		        // int id  = rs.getInt("id");
		         //int age = rs.getInt("age");
		    	  String eachNote=rs.getString(1);
		    	 recipe.add(eachNote);
		        
		      }
		     // stmt.close();
		     //conn.close();
		      //rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	   return recipe;
	}
	

	public ArrayList<String> getContentByTime(int timeUserHas)
	{
		
		
		System.out.println("this is the time the user has:"+timeUserHas);
		ArrayList<String> recipe=new ArrayList<String>();
		
		 try {
			stmt = conn.createStatement();
			 String sql;
		      sql = "Select name from dishes where (before_prep_time+prep_time+after_prep_time)<"+timeUserHas;
		      ResultSet rs = stmt.executeQuery(sql);

		      //STEP 5: Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		        // int id  = rs.getInt("id");
		         //int age = rs.getInt("age");
		    	  String eachNote=rs.getString(1);
		    	 recipe.add(eachNote);
		        
		      }
		     // stmt.close();
		     //conn.close();
		      //rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	   return recipe;
	}
	}
