
public class StaticMethods {

	/* returns two double values:
	     double[0] == gallons used
	     double[1] == price in USD
	 */
	public static double[] calculateGallonsAndPrice(double distance, double consumption, double price)
	{
		// calculation formula
		double gallonsUsed = distance / consumption;
		double totalPrice = gallonsUsed * price;
		
		return new double[] {gallonsUsed, totalPrice};
	}
	
	public static String gallonsAndPriceString(double gallonsUsed, double totalPrice)
	{
		return "You would use " + String.format("%.2f", gallonsUsed) 
		+ " gallons during this trip, which would cost $" 
		+ String.format("%.2f", totalPrice) + ".";
	}
	
	
	public static String durationOfTripString(int days, int hours, int minutes)
	{
		String result = "Duration of trip: ";
		if(days == 1)
			result += "1 day ";
		else if (days > 1)
			result += days + " days ";
		
		if(hours == 1)
			result += "1 hour ";
		else if(hours > 1)
			result += hours + " hours ";
		
		if(minutes == 1)
			result += "1 minute";
		else if(minutes > 1)
			result += minutes + " minutes";
		
		return result;
	}

}
