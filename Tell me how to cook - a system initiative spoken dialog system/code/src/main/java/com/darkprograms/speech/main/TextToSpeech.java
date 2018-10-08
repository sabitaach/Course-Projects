package com.darkprograms.speech.main;

import java.util.Locale;

import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;



public class TextToSpeech {
	public Synthesizer  synthesizer;
	//public static void main(String[] args) throws IllegalArgumentException, EngineException{
	public TextToSpeech() throws EngineException {
		// TODO Auto-generated constructor stub
	
	
		  System.setProperty("freetts.voices",
				    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
				    
				   Central.registerEngineCentral
				    ("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
		   synthesizer =
				    Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
		  synthesizer.allocate();
		
		
	}
	/*public static void main(String[] args) throws EngineException
	{
		TextToSpeech ts=new  TextToSpeech();
		ts.speaker("Hellloooooo",ts.synthesizer);
		ts.speaker("I am inside!!",ts.synthesizer);
		ts.synthesizer.deallocate();
	}*/
	public void speaker(String contentToBeSpoken,Synthesizer  synthesizer){
		 try
		 {
		 
		   //Synthesizer  synthesizer =
		    //Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
		  
		 
		  
		   synthesizer.resume();
		   synthesizer.speakPlainText(contentToBeSpoken, null);
		  
		   
		  
		   synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
		  // 
		   
		  }
		   catch(Exception e)
		   {
		     e.printStackTrace();
		   }
		 }
	
}