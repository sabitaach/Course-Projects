package com.darkprograms.speech.main;

import javax.speech.EngineException;

import com.darkprograms.speech.recognizer.GoogleResponse;

public class CheckSpeechInput {
	
	int trial =1;
	
	public String checkResponse(GoogleResponse theResponse, TextToSpeech tts)
	{
		String userUtterance= "";
		System.out.println("I am inside check response");
		while(trial < 3)
		{
			System.out.println("I am inside while with traila value "+ trial);
			//System.out.println("trial:"+trial);
			userUtterance = theResponse.getResponse();
			if(userUtterance != null)
			{
				System.out.println("I am inside check response, user utterance is not null");
			try {
				tts.speaker("I understood "+ userUtterance + " Is this correct?", tts.synthesizer);
				
				SpeechToText stt = new SpeechToText();
				GoogleResponse text = stt.speechToText();
				if(text.getResponse()!=null)
				{	
					System.out.println("I am inside chcekresponse- if- if "+ text.getResponse());
					if(checkConfirmationResponse(text, tts) == 1)
					{
						System.out.println("I am here inside yes");
						trial =3;
						System.out.println("final response" + userUtterance);
						return userUtterance;
					}
					else
					{	userUtterance = null;
						return userUtterance;
					}
				}
				else
				{
					System.out.println("I am inside chcekresponse- if- if - else "+ text.getResponse());
					trial++;
					if(trial<3)
					{tts.speaker("Could you please repeat the name of the dish?", tts.synthesizer);
					stt = new SpeechToText();
					try {
						//call check response
						checkResponse(stt.speechToText(), tts);
					} catch (EngineException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				}
		
			} catch (EngineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
		{
			System.out.println("I am inside chcekrespons else ");
			trial++;
			if(trial<3)
			{tts.speaker("Could you please repeat the name of the dish?", tts.synthesizer);
			SpeechToText stt = new SpeechToText();
			try {
				//call check response
				checkResponse(stt.speechToText(), tts);
			} catch (EngineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			else return null;
		}
		}
		return userUtterance;
	}
	
	public int checkConfirmationResponse(GoogleResponse theResponse, TextToSpeech tts)
	{
		System.out.println("I am inside confirmation");
		
		String userUtterance = theResponse.getResponse();
		System.out.println("user utterance " + userUtterance);
		if(userUtterance != null)
		{
			System.out.println("I am inside if of confirmation i.e yes");
			
			PosNegWords posneg = new PosNegWords();
			int confirmation = posneg.lookingForConfiramtion(userUtterance);
			
			if(confirmation == 1)
			{
				return 1;
			}
			else
			{
				System.out.println("I am inside else of confirmation i.e no");
				//could you repeat
				trial++; //check trail here
				System.out.println();
				if(trial<3)
				{
					tts.speaker("Could you please repeat what you just said?", tts.synthesizer);
					//get user input
					SpeechToText stt = new SpeechToText();
					try {
						//call check response
						checkResponse(stt.speechToText(), tts);
					} catch (EngineException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
				else return 0;
				
			}
		}
		else
		{
			System.out.println("I am inside confirmation but string is null");
			tts.speaker("Could you please repeat what you just said?", tts.synthesizer);
		}
		return 0;
	}
	
	public void checkRsponseAndWait(GoogleResponse gr, TextToSpeech tts, String message) throws EngineException
	{
		PosNegWords pn=new PosNegWords();
		int confirmation=pn.lookingForConfiramtion(gr.getResponse());
		SpeechToText stt=new SpeechToText();
		GoogleResponse yesNo;
		if(confirmation==0)//if the user says no then wait for 10 seconds and then again ask it they are ready
		{
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("error in the thread");
			}
			tts.speaker(message, tts.synthesizer);
			yesNo = stt.speechToText();
			checkRsponseAndWait(yesNo, tts, message);
		}
		if(confirmation==2)//if the user says something that the system is not able to understand, then ask for it again without waiting
			{
				tts.speaker("I didnt get you. "+message, tts.synthesizer);
				yesNo = stt.speechToText();
				checkRsponseAndWait(yesNo, tts, message);
			}
			
			
			
			
	//this is always going to return 1 because for other cases, the method is going to loop over each time
			
		
		
		
	}
	
	
	
	
}
