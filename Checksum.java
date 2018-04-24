/*
Program to receive two arguments: [bitsize: 8, 16, or 32] [input file]
Calculates checksum value for inputfile with given bitsize
*/

import java.io.*;
import java.util.*;
import java.lang.*;

public class Checksum {
	public static void main(String[] args) {
		// check if program was compiled correctly
		if (args.length != 2) {
			System.err.println("Invalid arguments passed in. \n Compilation format: java checksum [number] [input file]");
			System.exit(-1);
		}
		else if (!args[1].equals("8") && !args[1].equals("16") && !args[1].equals("32")) {
			System.err.println("The number " + args[1] + " is not a valid checksum size: valid checksum sizes are 8, 16, or 32.");
			System.exit(-1);
		}

		// echo file input
		String fileData = printFile(Integer.parseInt(args[1]), args[0]);

		// get checksum value in hex
		String checksumString = checksumValue(Integer.parseInt(args[1]), fileData);

		System.out.printf("%2d bit checksum is %8s for all %4d chars\n", Integer.parseInt(args[1]), checksumString, fileData.length());
	}

	// prints out file; allows 80 characters max per line
	public static String printFile(int bitSize, String inputArgument) {
		try {
			Scanner scanner = new Scanner(new File(inputArgument));
			String fileData = scanner.useDelimiter("\\Z").next() + "\n";

			// pad with Xs
			while (fileData.length() % (bitSize/8) != 0)
				fileData += "X";

			for (int i = 0 ; i < fileData.length() ; i++) {
				if (i%80 == 0)
					System.out.println();

				System.out.print(fileData.charAt(i));
			}

			System.out.println();
			scanner.close();
			return fileData;
		}
		catch (Exception e) {
			System.err.println("Exception caught: " + e.getMessage());
		}

		return "null";
	}

	// computes binary representation of characters, adds values in decimal, then converts answer to hex
	public static String checksumValue(int bitSize, String fileData) {
		String binary;
		double decimal = 0;
		int numChars = bitSize/8;

		for (int i = 0 ; i < fileData.length() ; i = i + numChars) {
			binary = "";

			for (int j = 0 ; j < numChars ; j++)
				binary += addLeadingZeros(Integer.toBinaryString((int)fileData.charAt(i+j)));

			decimal += (double)Integer.parseInt(binary, 2);
		}

		decimal %= Math.pow(2, bitSize);
		return Integer.toHexString((int)decimal);
	}

	// adds zeros to left of binary string so length is always 8
	public static String addLeadingZeros(String binary) {
		while (binary.length() != 8)
			binary = "0" + binary;

		return binary;
	}
}