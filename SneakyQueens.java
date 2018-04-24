/*Places queens randomly on an N-sized chess board; checks if any queen can attack another*/

import java.io.*;
import java.util.*;

public class SneakyQueens
{
	public static boolean allTheQueensAreSafe(ArrayList<String> coordinateStrings, int boardSize) 
	{
		String fullCoordinateString = String.join("", coordinateStrings);
		boolean[] queenRow = new boolean[boardSize+1];
		boolean[] queenColumn = new boolean[boardSize+1];
		boolean[] positiveDiagonal = new boolean[2*boardSize+1];
		boolean[] negativeDiagonal = new boolean[2*boardSize+1];
		int columnNum;
		int rowNum = 0;
		int posDiagonalNum;
		int negDiagonalNum;
		int charLocation = 0;
		int letterCounter;
		String queenString;

		HashMap<Character, Integer> alphabetValues = new HashMap<>();
		alphabetValues.put('a', 1);
		alphabetValues.put('b', 2);
		alphabetValues.put('c', 3);
		alphabetValues.put('d', 4);
		alphabetValues.put('e', 5);
		alphabetValues.put('f', 6);
		alphabetValues.put('g', 7);
		alphabetValues.put('h', 8);
		alphabetValues.put('i', 9);
		alphabetValues.put('j', 10);
		alphabetValues.put('k', 11);
		alphabetValues.put('l', 12);
		alphabetValues.put('m', 13);
		alphabetValues.put('n', 14);
		alphabetValues.put('o', 15);
		alphabetValues.put('p', 16);
		alphabetValues.put('q', 17);
		alphabetValues.put('r', 18);
		alphabetValues.put('s', 19);
		alphabetValues.put('t', 20);
		alphabetValues.put('u', 21);
		alphabetValues.put('v', 22);
		alphabetValues.put('w', 23);
		alphabetValues.put('x', 24);
		alphabetValues.put('y', 25);
		alphabetValues.put('z', 26);

		// reads the coordinate strings
		while (charLocation < fullCoordinateString.length())
		{
			letterCounter = 0;

			// reads the column
			StringBuilder builder = new StringBuilder(5);
			while (Character.isLetter(fullCoordinateString.charAt(charLocation)))
			{
				builder.append(fullCoordinateString.charAt(charLocation));
				charLocation++;
				letterCounter++;
			}
			queenString = builder.toString();

			// converts the column's character string to an integer value
			columnNum = convertToInt(alphabetValues, queenString, letterCounter);

			// checks vertically
			if (queenColumn[columnNum])
				return false;

			queenColumn[columnNum] = true;

			rowNum = 0;
			// reads the row; turns string's number into a value
			while (charLocation < fullCoordinateString.length() && Character.isDigit(fullCoordinateString.charAt(charLocation)))
			{
				rowNum *= 10;
				rowNum += Character.getNumericValue(fullCoordinateString.charAt(charLocation));
				charLocation++;
			}

			// checks horizontally
			if (queenRow[rowNum])
				return false;
			queenRow[rowNum] = true;

			// checks positive diagonal
			posDiagonalNum = boardSize + rowNum - columnNum;
			if (positiveDiagonal[posDiagonalNum])
				return false;
			positiveDiagonal[posDiagonalNum] = true;

			//checks negative diagonal
			negDiagonalNum = rowNum + columnNum - 1;
			if (negativeDiagonal[negDiagonalNum])
				return false;	
			negativeDiagonal[negDiagonalNum] = true;
		}

		return true;
	}		

	// converts the column's character string to an integer value
	public static int convertToInt(HashMap<Character, Integer> alphabetValues, String queenString, int letterCounter)
	{
		int letterValue;
		int columnIntValue = 0;
		int digitNumFromLeft = letterCounter - 1;
		int digitNumFromRight = 0;

		while (digitNumFromLeft >= 0)
		{
			// converts letter into integer value 1-26 starting with the right-most digit
			letterValue = alphabetValues.get(queenString.charAt(digitNumFromLeft));

			// multiplies letter's value by 26^digitNumFromRight
			columnIntValue += letterValue * Math.pow(26, digitNumFromRight);

			digitNumFromLeft--;
			digitNumFromRight++;
		}
		return columnIntValue;
	}
}
