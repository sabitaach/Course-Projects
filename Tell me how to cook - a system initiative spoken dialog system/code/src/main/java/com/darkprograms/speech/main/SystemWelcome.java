package com.darkprograms.speech.main;

import java.util.ArrayList;
import java.util.Random;

public class SystemWelcome {

	/**
	 * @param args
	 */
	public String systemWelcome(){
		ArrayList<String> systemGreetings =  new ArrayList<String>();
		systemGreetings = populateGreetings();
		int index = randomNumGen(systemGreetings.size());
		
		//speaks systemGreetings.get(index);
		
		return systemGreetings.get(index);

	}
	
	int randomNumGen(int size)
	{
		Random rand = new Random();
		int  num = rand.nextInt(size);
		return num;
	}
	
	ArrayList<String> populateGreetings()
	{
		ArrayList<String> systemGreetings =  new ArrayList<String>();
		systemGreetings.add("Welcome to tell me how to cook.");
		systemGreetings.add("Hi, this is tell me how to cook application.");
		systemGreetings.add("Hi there, welcome to tell me how to cook.");
		return systemGreetings;
	}

}
