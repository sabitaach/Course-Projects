package com.darkprograms.speech.main;

import java.io.File;
import javax.speech.EngineException;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.darkprograms.speech.recognizer.Recognizer;

import net.sourceforge.javaflacencoder.FLACFileWriter;

/**
 * Jarvis Speech API Tutorial
 * 
 * @author Aaron Gokaslan (Skylion)
 * 
 */
public class SpeechToText {
	//final String unableToHear = "I am sorry. I could not hear that. Could you please repeat it again?";

	/*
	 * public static void main(String[] args) throws EngineException {
	 * 
	 * 
	 * 
	 * SpeechToText sp=new SpeechToText(); String a=sp.converter();
	 * System.out.println(a);
	 * 
	 * }
	 */
	public GoogleResponse speechToText() throws EngineException {
	/*	int trails = 0;
		boolean recognize = true;
		String finalText = "";

		do {

			trails++;
			if (trails == 3) {
				recognize = false;
				break;
			}*/

			Microphone mic = new Microphone(FLACFileWriter.FLAC);
			File file = new File("testfile2.flac");// Name your file whatever
													// you want
			try {
				mic.captureAudioToFile(file);
			} catch (Exception ex) {// Microphone not available or some other
									// error.
				System.out.println("ERROR: Microphone is not availible.");
				ex.printStackTrace();
				// TODO Add your error Handling Here
			}
			/*
			 * User records the voice here. Microphone starts a separate thread
			 * so do whatever you want in the mean time. Show a recording icon
			 * or whatever.
			 */
			try {
				System.out.println("Recording...");
				Thread.sleep(5000);// In our case, we'll just wait 5 seconds.
				mic.close();
			} catch (InterruptedException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}

			mic.close();// Ends recording and frees the resources
			System.out.println("Recording stopped.");

			Recognizer recognizer = new Recognizer(
					Recognizer.Languages.ENGLISH_US,
					"AIzaSyA5GvZqFdnNwVHrwd3YW2Vu9co4NezIrDg"); // Specify your
																// language
																// here.
			// Although auto-detect is avalible, it is recommended you select
			// your region for added accuracy.
			
			GoogleResponse response = null;
			try {
				int maxNumOfResponses = 4;
				response = recognizer.getRecognizedDataForFlac(
						file, maxNumOfResponses, (int) mic.getAudioFormat()
								.getSampleRate());
				String theResponse = response.getResponse();
				System.out.println("Google Response: " + theResponse);
				//Double confidence = 0.0;

				//confidence = Double.parseDouble(response.getConfidence()) * 100;
				//CheckSpeechInput checkspeech = new CheckSpeechInput();
				//checkspeech.checkInput(confidence, theResponse);

			} catch (Exception ex) {
				// TODO Handle how to respond if Google cannot be contacted
				System.out.println("ERROR: Google cannot be contacted");
				ex.printStackTrace();
			}

			file.deleteOnExit();// Deletes the file as it is no longer
								// necessary.

		/*} while (finalText.equalsIgnoreCase("unable to understand"));

		if (recognize == false) {

			ts = new TextToSpeech();
			ts.speaker("Could you please write it down for me?", ts.synthesizer);
			Scanner scan = new Scanner(System.in);
			String inputFromUser = scan.nextLine();
			finalText = inputFromUser;
			scan.close();
		}
*/
		return response;
	}

}