package com.darkprograms.speech.main;
//this is the new tts
import java.io.InputStream;

import com.darkprograms.speech.synthesiser.SynthesiserV2;
import com.gtranslate.Audio;
import com.mysql.jdbc.Constants;

public class Text_to_speech {
		
	public void speaker(String contentToBeSppoken)
	{
		String apiKey="insert the key";
		Audio audio = Audio.getInstance();
		SynthesiserV2 synth = new SynthesiserV2(apiKey);
		try {
		    InputStream system_speech_output = synth.getMP3Data(contentToBeSppoken);
		    audio.play(system_speech_output);
		} catch (Exception e) {
		    e.printStackTrace();
		    return;
		}	
	}
	
	
}
