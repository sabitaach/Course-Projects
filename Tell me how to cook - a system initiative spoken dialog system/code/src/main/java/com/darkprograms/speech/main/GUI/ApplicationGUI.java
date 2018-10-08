package com.darkprograms.speech.main.GUI;

import java.awt.EventQueue;
import java.awt.Image;

import javax.speech.EngineException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;


import javax.swing.SwingConstants;

import com.darkprograms.speech.main.CheckDatabaseForUserInput;
import com.darkprograms.speech.main.CheckSpeechInput;
import com.darkprograms.speech.main.GiveIngredientDetail;
import com.darkprograms.speech.main.PosNegWords;
import com.darkprograms.speech.main.SpeechToText;
import com.darkprograms.speech.main.SystemWelcome;
import com.darkprograms.speech.main.TextToSpeech;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.sql.database.configuration.DatabaseConnection;

public class ApplicationGUI {

	private JFrame frame;

	public String conversation = "";
	
	
	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException, EngineException {
		final ApplicationGUI window = new ApplicationGUI() ;
		//window.startConversation(window.conversation);
		
		/*EventQueue.invokeAndWait(new Runnable() {
			public void run() {
				try {
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	*/
		//ApplicationGUI gui = null;

	}

	/**
	 * Create the application.
	 * @throws EngineException 
	 * @throws InterruptedException 
	 */
	public ApplicationGUI() throws EngineException, InterruptedException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws EngineException 
	 * @throws InterruptedException 
	 */
	private void initialize() throws EngineException, InterruptedException {
		
		//all variables
		
		PosNegWords posneg =  new PosNegWords();
		posneg.getThePosNegTerms();
		DatabaseConnection dbconn = new DatabaseConnection();
		dbconn.initializeDatabase();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		final JTextArea textArea = new JTextArea();
		textArea.setBounds(6, 228, 355, 44);
		frame.getContentPane().add(textArea);
		textArea.setEditable(false);
		
		final JLabel lblNewLabel_1 = new JLabel();
		lblNewLabel_1.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_1.setBackground(new Color(255, 250, 240));
		lblNewLabel_1.setOpaque(true);
		lblNewLabel_1.setBounds(6, 32, 355, 192);
		frame.getContentPane().add(lblNewLabel_1);
		
	
		JLabel lblNewLabel = new JLabel("Tell Me How To Cook");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		lblNewLabel.setBackground(new Color(255, 182, 193));
		lblNewLabel.setOpaque(true);
		lblNewLabel.setBounds(6, 6, 438, 24);
		frame.getContentPane().add(lblNewLabel);
		
		
		ImageIcon imageIcon = new ImageIcon("pizza.jpg"); // load the image to a imageIcon
		Image image = imageIcon.getImage(); // transform it
		Image newimg = image.getScaledInstance(78, 63,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
		imageIcon = new ImageIcon(newimg); 
		
		
		JLabel label = new JLabel();
		label.setIcon( imageIcon);
		label.setBounds(366, 32, 78, 63);
		frame.getContentPane().add(label);
		
		ImageIcon imageIcon2 = new ImageIcon("indianfood.jpeg"); // load the image to a imageIcon
		Image image2 = imageIcon2.getImage(); // transform it
		Image newimg2 = image2.getScaledInstance(78, 63,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
		imageIcon2 = new ImageIcon(newimg2); 
		
		JLabel label_1 = new JLabel();
		label_1.setIcon( imageIcon2);
		label_1.setBounds(366, 98, 78, 63);
		frame.getContentPane().add(label_1);
		
		ImageIcon imageIcon3 = new ImageIcon("dessert.jpeg"); // load the image to a imageIcon
		Image image3 = imageIcon3.getImage(); // transform it
		Image newimg3 = image3.getScaledInstance(78, 63,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
		imageIcon3 = new ImageIcon(newimg3);
		
		JLabel label_2 = new JLabel();
		label_2.setIcon( imageIcon3);
		label_2.setBounds(366, 161, 78, 63);
		frame.getContentPane().add(label_2);
		
	
		final JButton btnNewButton = new JButton("SEND");
		btnNewButton.setBackground(new Color(153, 255, 204));
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//clear text area
				conversation = "<html> <b>System: </b>" + conversation + "<br> <b>User: </b>" + textArea.getText();
				textArea.setText("");
				textArea.setEditable(false);
				lblNewLabel_1.setText(conversation + "</html>");
				btnNewButton.setEnabled(false);
			}
		});
		btnNewButton.setBounds(364, 223, 80, 55);
		frame.getContentPane().add(btnNewButton);
		
		SystemWelcome sw = new SystemWelcome();
		conversation = sw.systemWelcome() + " How may I help you? You can start by saying the recipe name or ask me for a list of recipes.";

		//ApplicationGUI gui = new ApplicationGUI();
		/* tts = new TextToSpeech();
		 tts.speaker(conversation, tts.synthesizer);*/
		
		//conversation= "<html> <b>System:</b> "+ conversation + "<br>" + "<b>User:</b>" + "</br>";
		//conversation= "<html> <b>System:</b> "+ conversation + "<br>" + "<b>User:</b>" + "</br>";
		
		lblNewLabel_1.setText("<html> <b>System: </b>" + conversation + "</html>");
		
		frame.setVisible(true);
			
		TextToSpeech tts = new TextToSpeech();
		SpeechToText stt = new SpeechToText();
		 tts.speaker(conversation, tts.synthesizer);
		 
		//fucntion call for user
		String userTag =  "<br> <b>User: </b>";
		
		GoogleResponse userUtterance = stt.speechToText();
		
		CheckSpeechInput checkspeech = new CheckSpeechInput();
		String userResponse = checkspeech.checkResponse(userUtterance, tts);
		
		//
		if(userResponse != null && !userResponse.equalsIgnoreCase(""))
		{
			System.err.println("Final user response" + userResponse);
			conversation = "<html> <b>System: </b>" + conversation + "<br> <b>User: </b>" +userResponse + ".";
	
			lblNewLabel_1.setText(conversation + "</html>");
		}
		else
		{
			tts.speaker("I did not understand it. Could you please write it?", tts.synthesizer);
			//textArea.setEnabled(true);
			
			textArea.setEditable(true);
			userResponse = textArea.getText();
			btnNewButton.setEnabled(true);
			Thread.sleep(30000);
			
			
		}
		
		
		int u=0;
		do{
		ArrayList<String> dishes = new ArrayList<String>();
		dishes = dbconn.getDishes();
		
		CheckDatabaseForUserInput checkInput=new CheckDatabaseForUserInput();
		
		if(userResponse.trim().isEmpty())
			System.out.println("Empty string here!!");
			
		int presenceResult=checkInput.checkPresence(dishes, userResponse);
		if(presenceResult==1)
		{
			String foundDish="Oh!! I found that one in our directory.";
			tts.speaker(foundDish, tts.synthesizer);
			conversation = conversation+"<br> <b>System: </b>"+foundDish ;
			
			lblNewLabel_1.setText(conversation + "</html>");
			u=1;	//the dish is present in the database
		}
		else
		{
			String givingInfo="Sorry!!! That dish is not present in our directory. Here are the ones that we have:";
			tts.speaker(givingInfo, tts.synthesizer);
			conversation = conversation+"<br> <b>System: </b>"+givingInfo ;
			
			lblNewLabel_1.setText(conversation + "</html>");
			int i=0;
			for(String j:dishes)
			{
				tts.speaker(j, tts.synthesizer);
				conversation = conversation+"<br> "+(i+1)+". " +j ;
				
				lblNewLabel_1.setText(conversation + "</html>");
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("the problem is with the Thread.sleep--check it out!! ");
				}
				
				
			}
			
			//now we will ask the user to choose any one of the dishes that we have
			tts.speaker("Which one of these dishes would you like to make today?", tts.synthesizer);
			
			
			
			userUtterance = stt.speechToText();
			System.out.println("this is the check for user utterance: "+userUtterance.getResponse());
			checkspeech=new CheckSpeechInput();
			userResponse = checkspeech.checkResponse(userUtterance, tts);
			 conversation = conversation+"<br> <b>User: </b>"+userResponse ;
			lblNewLabel_1.setText(conversation + "</html>");
			
		
		}
		}while(u==0);
		//giving details about the  servings and difficulty level and time for preparation
		/*Hashtable<String,String> timeAndDifficulty = new Hashtable<String, String>();
		timeAndDifficulty = dbconn.getTimeServingDiffLevel(userResponse);
		String serving= "This recipe is "+timeAndDifficulty.get("servings")+"   ";
		String difficulty="As far as difficulty level is concerned, this can be considered as a"+timeAndDifficulty.get("difficulty")+"one    ";
		String timeRequired="The total time required is "+timeAndDifficulty.get("time");
		tts.speaker(serving,tts.synthesizer);
		conversation = conversation+"<br> <b>System: </b>"+serving+". ";
		lblNewLabel_1.setText(conversation + "</html>");
		tts.speaker(difficulty,tts.synthesizer);
		conversation = conversation+"<br> "+difficulty+". ";
		lblNewLabel_1.setText(conversation + "</html>");
		tts.speaker(timeRequired,tts.synthesizer);
		conversation = conversation+"<br> "+timeRequired+". ";
		lblNewLabel_1.setText(conversation + "</html>");
		
		//when we reach here we will already know what the dish is for sure. Now we proceed to get the ingredients
		GiveIngredientDetail detail=new GiveIngredientDetail();
		detail.givingDetails(conversation, lblNewLabel_1,userResponse,tts,dbconn);
				
		
		
		*/
		
			//read all the contents 
		
		
		
		/*String userUtterance = stt.speechToText(tts);
		userUtterance = userTag + userUtterance + ".";
		
		conversation = "<html> <b>System: </b>" + conversation + userUtterance;
		
		lblNewLabel_1.setText(conversation + "</html>");
		 */
		 
	}
	
}
