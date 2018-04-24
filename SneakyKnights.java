/*Places knights randomly on an N-sized chess board; checks if any knight can attack another*/

import java.io.*;
import java.util.*;
import java.awt.*;

public class SneakyKnights
{	
	static final int MAX_LENGTH = 8;
	
	public static boolean allTheKnightsAreSafe(ArrayList<String> coordinateStrings, int boardSize)
	{
		String fullCoordinateString = String.join("", coordinateStrings);
		int columnNum;
		int rowNum;
		int charLocation = 0;
		int columnDifference;
		HashSet<Point> knightLocations = new HashSet<>();
	
		// reads the coordinate strings
		while (charLocation < fullCoordinateString.length())
		{
			// largest string value is 6OJ8ION so won't read more than 7 characters
			StringBuilder builder = new StringBuilder(MAX_LENGTH);

			// reads the column
			while (Character.isLetter(fullCoordinateString.charAt(charLocation)))
			{
				builder.append(fullCoordinateString.charAt(charLocation));
				charLocation++;
			}

			// converts the column's character string to an integer value
			columnNum = convertToInt(builder.toString(), builder.length()-1);

			rowNum = 0;
			// reads the row; turns string's numbers into a value
			while (charLocation < fullCoordinateString.length() && Character.isDigit(fullCoordinateString.charAt(charLocation)))
			{
				rowNum *= 10;
				rowNum += Character.getNumericValue(fullCoordinateString.charAt(charLocation));
				charLocation++;
			}

			// checks if a knight is already placed in any of the 8 locations the current knight can attack
			for (int currentRow = rowNum-2 ; currentRow <= rowNum+2 ; currentRow++)
			{
				if (currentRow == rowNum)
					continue;

				// knights move 2 rows when they move 1 column, and vice-versa
				columnDifference = Math.abs(rowNum-currentRow)%2 + 1;
				
				if (knightLocations.contains(new Point(currentRow, columnNum-columnDifference)) || 
					knightLocations.contains(new Point(currentRow, columnNum+columnDifference)))
					return false;
			}

			// adds current knight to HashSet to be compared with future knights
			knightLocations.add(new Point(rowNum, columnNum));
		}

		return true;
	}

	public static int convertToInt(String columnString, int digitNumFromLeft)
	{
		int letterValue;
		int columnIntValue = 0;
		int digitNumFromRight = 0;

		while (digitNumFromLeft >= 0)
		{
			// converts a letter into an integer value 1-26 starting with the right-most letter
			letterValue = columnString.charAt(digitNumFromLeft) - 'a' + 1;

			// multiplies letter's value by 26^digitNumFromRight to go from base 26 to base 10
			columnIntValue += letterValue * Math.pow(26, digitNumFromRight);

			digitNumFromLeft--;
			digitNumFromRight++;
		}
		
		return columnIntValue;
	}
}