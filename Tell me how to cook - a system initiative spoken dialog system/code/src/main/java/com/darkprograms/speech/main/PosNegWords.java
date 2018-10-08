package com.darkprograms.speech.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class PosNegWords {

	public static HashSet<String> affirmation=new HashSet<String>();//contains the affirmation words read from the file
	public static HashSet<String> refusal=new HashSet<String>();//contains the refusal terms obtained from the file
	
	public void getThePosNegTerms()
	{
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("confirmationWords.txt"));
			String content="";
			while((content=reader.readLine())!=null)
			{
				affirmation.add(content.trim().toLowerCase());
			}
			reader.close();
			
			BufferedReader reader1=new BufferedReader(new FileReader("negationWords.txt"));
			String content1="";
			while((content1=reader1.readLine())!=null)
			{
				refusal.add(content1.trim().toLowerCase());
			}
			reader1.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			
	}
	
	int lookingForConfiramtion(String theResponse)
	{
		
		int confirmation=2;//2 is the default value--if the response is neither affirmation nor refusal, it will have this value
		if(theResponse.contains(" "))
		{
			String[] parts=theResponse.split(" ");
			for(String a:parts)
			{
				if(affirmation.contains(a.trim()))
				{	confirmation=1;
				break;
					
				}
				if(refusal.contains(a.trim()))
				{
					confirmation=0;
					break;
					}
				
			}
		}
			else
			{
				if(affirmation.contains(theResponse.trim()))
				{	confirmation=1;
					
				}
				else if(refusal.contains(theResponse.trim()))
				{
					confirmation=0;
				}	
			}
		return confirmation;
			
		}
	
}


