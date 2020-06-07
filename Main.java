
/*
* Copyright (c) 2000-2020 TeamDev Ltd. All rights reserved.
* TeamDev PROPRIETARY and CONFIDENTIAL.
* Use is subject to licence terms.
* 
* This uses V 7.7.1, which I downloaded on May 25, 2020.
*/
import static com.teamdev.jxbrowser.engine.RenderingMode.*;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.view.swing.BrowserView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public final class Main
{
	
	private static final int MIN_ZOOM = 0;
    private static final int MAX_ZOOM = 21;
    

    private static int zoomValue = 4;
	
    private static JTextField gasPrice, fuelConsumption;
    private static JTextField start, destination;
	
	public static void main(String[] args)
	{
		System.setProperty("jxbrowser.license.key", "1BNDHFSC1FVLG01PZ3V14AEJS9DM3BKJTMU73HRIZAJ8UX79RZ13OFY01N2DPT5IC4VK8N");
		// key expires 6/25/2020
		
		// Create and initialize the Engine
		EngineOptions options = EngineOptions.newBuilder(HARDWARE_ACCELERATED).build();
		Engine engine = Engine.newInstance(options);

		// Create the Browser
		Browser browser = engine.newBrowser();
		
		

		SwingUtilities.invokeLater(() ->
		{
			// Create the Swing BrowserView component
			BrowserView view = BrowserView.newInstance(browser);

			// text fields
			gasPrice = new JTextField("", 8);
			fuelConsumption = new JTextField("", 8);
			
			start = new JTextField(27);
			destination = new JTextField(27);
			
			// labels
			JLabel lblGasPrice = new JLabel("Gas price (USD per gallon): ");
			JLabel lblFuelConsumption = new JLabel("   Fuel consumption (mpg): ");
			JLabel lblOrigin = new JLabel("Origin location: ");
			JLabel lblDestination = new JLabel("   Destination location: ");
			
			
			
			
			// Make a JPanel to put the text fields on
			JPanel textfields = new JPanel();
			textfields.setLayout(new BoxLayout(textfields, BoxLayout.Y_AXIS));
			
			JPanel textfieldsTop = new JPanel();
			JPanel textfieldsBot = new JPanel();
			
			textfieldsTop.add(lblGasPrice);
			textfieldsTop.add(gasPrice);
			textfieldsTop.add(lblFuelConsumption);
			textfieldsTop.add(fuelConsumption);
			
			textfieldsBot.add(lblOrigin);
			textfieldsBot.add(start);
			textfieldsBot.add(lblDestination);
			textfieldsBot.add(destination);
			
			textfields.add(textfieldsTop);
			textfields.add(textfieldsBot);
			
			
			
			JFrame frame = new JFrame();
			frame.add(view, BorderLayout.CENTER);
			frame.add(textfields, BorderLayout.NORTH);  // add the text fields
			
			
			// CALCULATE BUTTON
			JButton button = new JButton("Calculate cost");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev)
				{
					// I think I could move this into a static class or something to organize my code more
					try {
						String startString = start.getText();
						String destString = destination.getText();
						
						startString = startString.replace(" ", "%20");
						destString = destString.replace(" ", "%20");
						System.out.println("Start received: " + startString);
						System.out.println("Dest received: " + destString);
						
						String APIRequest = String.format("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=%s&destinations=%s&key=AIzaSyD2cYYFH0TAzJqLuI92XtrE-LWraZ5RsMc", startString, destString);
				//		String APIRequest = "https://www.google.com/maps/dir/2510+Ringrose+Ct,+Missouri+City,+TX+77459-2919,+USA/Rice+University,+Main+St,+Houston,+TX/@29.6578751,-95.7728299,10z/data=!3m1!4b1!4m14!4m13!1m5!1m1!1s0x8640e60ec8f79a43:0x957c2f5a92ef19d1!2m2!1d-95.5760248!2d29.5858494!1m5!1m1!1s0x8640c064b7f18e1f:0xe2cd9cf065c43eb5!2m2!1d-95.4018312!2d29.7173941!3e0";
				//		System.out.println(APIRequest);
						
						
						// FROM ParseJSON.java
						String urlString = APIRequest;
						URL url = new URL(urlString);
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						
						int responseCode = con.getResponseCode();
						
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						
						String inputLine;
						StringBuffer response = new StringBuffer();
						
						while((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}
						in.close();
						
						System.out.println("Response: " + response.toString());
					
						
						
						
						
						JSONParser parser = new JSONParser();
						Object obj = parser.parse(response.toString());
						JSONObject jsonObj = (JSONObject) obj;
						
						JSONArray rows = (JSONArray) jsonObj.get("rows");  // put the array found after 'rows' in this variable
						
						JSONObject element1OfRows = (JSONObject) rows.get(0);
						
						JSONArray elements = (JSONArray) element1OfRows.get("elements");  // put the array found after 'elements' in this variable
						
						JSONObject element1OfElements = (JSONObject) elements.get(0);
						
						
						// DISTANCE
						
						JSONObject distance = (JSONObject) element1OfElements.get("distance");
						
						// distance in miles
						String distanceMiles = (String) distance.get("text");
					//	System.out.println(distanceMiles);
						
						// let's get the actual value (or we could use the meters to convert to miles, more efficient?)
						Scanner scan = new Scanner(distanceMiles);
						double distanceMilesVal = scan.nextDouble();
						System.out.println("Length of trip in miles: " + distanceMilesVal);
						
						
						// distance in meters (?)
						long distanceMeters = (long) distance.get("value");
						
						
						
						// DURATION
						
						JSONObject duration = (JSONObject) element1OfElements.get("duration");
					
						// duration in hours and minutes
						String durationHours = (String) duration.get("text");
					//	System.out.println(durationHours);
						
						
						// Parse the 'durationHours' string (i.e "1 hour 28 mins")
						String[] durationArray = durationHours.split(" ");
				//		System.out.println(Arrays.deepToString(durationArray));
						
						// array of 3 values: days (0), hours (1), minutes (2)
						int[] durations = new int[3];
						
						for(int i = 0; i < durationArray.length; i++)
						{
							if(durationArray[i].indexOf("hour") == 0)  // if it contains "hour"
								durations[1] = Integer.parseInt(durationArray[i-1]);
							else if(durationArray[i].indexOf("min") == 0)
								durations[2] = Integer.parseInt(durationArray[i-1]);
							else if(durationArray[i].indexOf("day") == 0)
								durations[0] = Integer.parseInt(durationArray[i-1]);
						}
						// duration of trip
						System.out.println(StaticMethods.durationOfTripString(durations[0], durations[1], durations[2]));
						// Parsing complete
						
						
						// duration in seconds (?)
						long durationSeconds = (long) duration.get("value");
						
						
						
						
						
						// Get gas price from text box
						double gasPriceDouble = Double.parseDouble(gasPrice.getText());
						double fuelConsumptionDouble = Double.parseDouble(fuelConsumption.getText());
						
						System.out.println("Gas price received: " + gasPriceDouble);
						System.out.println("FC received: " + fuelConsumptionDouble);
						
						double[] vals = StaticMethods.calculateGallonsAndPrice(distanceMilesVal, fuelConsumptionDouble, gasPriceDouble);
						
						double gals = vals[0];
						double price = vals[1];
						
						// final answer
						// right arrow: \u2192
						
						String finalAnswer = "From:  " + start.getText()
											+ "\nTo:  " + destination.getText()
											+ "\nDistance: " + distanceMilesVal + " miles"
											+ "\n" + StaticMethods.gallonsAndPriceString(gals, price);
											
				//		String finalAnswer = start.getText() + " \u2192 " + destination.getText() + " \n" +StaticMethods.gallonsAndPriceString(gals, price);
				//		String finalAnswer = StaticMethods.gallonsAndPriceString(gals, price);
						System.out.println(finalAnswer);
						JOptionPane.showMessageDialog(frame, finalAnswer);  // pop up-dialog box
					
						
					}
					catch (Exception x) {}
				}
			});
			frame.add(button, BorderLayout.SOUTH);  // add the bottom button
			
			
			frame.setSize(950, 750);
			frame.setVisible(true);
			frame.setTitle("Trip Cost Calculator");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//	browser.navigation().loadUrl("https://www.google.com/maps/dir/Rice+University,+6100+Main+St,+Houston,+TX+77005/Lamar+University,+4400+S+M+L+King+Jr+Pkwy,+Beaumont,+TX+77705,+United+States/@29.8760829,-95.3007863,9z/data=!4m13!4m12!1m5!1m1!1s0x8640c064b7f18e1f:0xe2cd9cf065c43eb5!2m2!1d-95.4018312!2d29.7173941!1m5!1m1!1s0x863eca78b5046bcb:0xa10be6fcc4ffd91f!2m2!1d-94.0755748!2d30.0399053");
			
		//	String htmlFile = "Places Autocomplete and Directions.html";
		//	browser.navigation().loadUrl("E:\\Trip Cost Calculator\\" + htmlFile);  // Windows, on a USB in the E drive (in a folder "Trip Cost Calculator")
		//	browser.navigation().loadUrl("file:///Volumes/NO%20NAME/Trip%20Cost%20Calculator/Places%20Autocomplete%20and%20Directions.html");  // Mac, USB name is "NO NAME" (in a folder "Trip Cost Calculator")
			
			browser.navigation().loadUrl("https://places-autocomplete-and-directions.davidespinosa.repl.co/");
			
		});
		
		
	}
}
