package com.winvinaya.playwrite.utils;

import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.opencsv.CSVReader;

public class PrintReport  {
	static ArrayList<String> values = new ArrayList<String>();
	public void printReport() throws IOException  {
		DateFormat dateFormat = new SimpleDateFormat("E MMM dd");
		Date date = new Date();
		String strDate = dateFormat.format(date);
		{  
			CSVReader reader = null;  
			try  
			{  
				//parsing a CSV file into CSVReader class constructor  
				reader = new CSVReader(new FileReader("target/TestResults.csv"));  
				String [] nextLine;  
				//reads one line at a time  
				while ((nextLine = reader.readNext()) != null)  
				{  
					for(String token : nextLine)  
					{  
						if(token.equalsIgnoreCase("Browser/App") || token.equalsIgnoreCase("Environment") || token.equalsIgnoreCase("Chromium") || token.equalsIgnoreCase("Winvinaya-InfoSystems-website") || 
								token.equalsIgnoreCase("TIME STAMP") || token.contains(strDate.toString())) {
			
						}
						else
							values.add(token+"  ");
					}  
					values.add("\n"); 
				}  
					}  
			catch (Exception e)   
			{  
				e.printStackTrace();  
			}  
		}  
	}  
}