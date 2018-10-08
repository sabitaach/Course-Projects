package com.darkprograms.speech.main;

import java.util.ArrayList;

public class CheckDatabaseForUserInput {
	
	
	
	
	public int checkPresence(ArrayList<String> dishes, String userResponse)
	{
		if(userResponse.trim().isEmpty())
			return 0;
		int present=0;
		
		for(String dish:dishes)//checking if the user response is contained in any of the dishes obtained from db
		{
			if(userResponse.contains(dish.trim()))
			{
				present=1;
				break;
			}
			
		}
		
		//now checking if the sentence contains any dish from the database
		if(present==0)
		{
			for(String dish:dishes)
			{
				if(dish.contains(userResponse))
				{	present=1;
					break;
				}
				
			}
			
		}
		return present;
	}
}
