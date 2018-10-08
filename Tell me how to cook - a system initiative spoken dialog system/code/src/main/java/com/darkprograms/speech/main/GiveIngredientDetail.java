package com.darkprograms.speech.main;

import java.util.ArrayList;

import javax.speech.EngineException;
import javax.swing.JLabel;

import com.darkprograms.speech.recognizer.GoogleResponse;
import com.sql.database.configuration.DatabaseConnection;

public class GiveIngredientDetail {
	
	public static void main(String[] args) throws EngineException
	{
		GiveIngredientDetail ingDetail=new GiveIngredientDetail();
		String conversation="";//this is the conversation that we were having before
		String dishName="bacon potato pie";
		DatabaseConnection dbconn=new DatabaseConnection();
		dbconn.initializeDatabase();
		JLabel lblNewLabel_1=new JLabel("");
		
		ingDetail.givingDetails(conversation, lblNewLabel_1, dishName, dbconn);
		
		
	}
	
	
	
	public void givingDetails(String conversation, JLabel lblNewLabel_1,String dishName,DatabaseConnection dbconn) throws EngineException
	{
		Text_to_speech tts=new Text_to_speech();
		String ingredient="So lets start with the ingredients. Are you ready to begin?";
		tts.speaker(ingredient);
		conversation = conversation+"<br> <b>System: </b> "+ingredient+". ";
		lblNewLabel_1.setText(conversation + "</html>");
		SpeechToText stt=new SpeechToText();
		GoogleResponse userUtterance = stt.speechToText();
		CheckSpeechInput checkspeech=new CheckSpeechInput();
		checkspeech.checkRsponseAndWait(userUtterance, tts,"Are you ready now?");
		conversation = conversation+"<br> <b>User: </b> "+userUtterance.getResponse()+". ";
		lblNewLabel_1.setText(conversation + "</html>");
		//now we will give all the ingredient details
		ArrayList<String> ingredients = new ArrayList<String>();
		ingredients = dbconn.getIngredients(dishName);
		String response2="Let's check out the stuffs that we are are going to need. So take :  ";
		tts.speaker(response2, tts.synthesizer);
		conversation = conversation+"<br> <b>System: </b> "+response2+". ";
		lblNewLabel_1.setText(conversation + "</html>");
		int i=0;
		
		
		for(String a:ingredients)
			{
			i++;
			tts.speaker(a, tts.synthesizer);
			conversation = conversation+"<br> "+a;
			lblNewLabel_1.setText(conversation + "</html>");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("out thread again!!");
			}
			/*if(i==6)
			{
				tts.speaker("Are you following? ");
				userUtterance = stt.speechToText();
				if(PosNegWords.affirmation.contains(userUtterance))
				{
					//continue telling the other ones
				}
				else
				{
					//repeat the contents that you just said--the las
				}
				
				
				
			}*/
			}
	
		
		
		
		
		
		
		
		
		
		
		
	}
	
	

}
