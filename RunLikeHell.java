/* Dynamic programming problem: maximum value by hitting blocks; can't hit two blocks in a row
Optimized space & time complexity
*/

import java.io.*;
import java.util.*;
import java.lang.*;

public class RunLikeHell
{
	public static int maxGain(int [] blocks)
	{
		// checking base cases
		if (blocks.length == 3)
			return Math.max(blocks[1], blocks[0] + blocks[2]);

		else if (blocks.length == 2)
			return Math.max(blocks[0], blocks[1]);

		else if (blocks.length == 1)
			return blocks[0];

		else if (blocks.length == 0)
			return 0;

		int [] optimalKnowledge = new int [3];

		optimalKnowledge[0] = blocks[0];
		optimalKnowledge[1] = blocks[1];
		optimalKnowledge[2] = blocks[0] + blocks[2];

		// choosing whether to hit a block or not
		for (int i = 3 ; i < blocks.length ; i++)
		{
			// hitting current block
			if (optimalKnowledge[(i-2)%3] + blocks[i] > optimalKnowledge[(i-1)%3] || optimalKnowledge[i%3] + blocks[i] > optimalKnowledge[(i-1)%3])
				optimalKnowledge[i%3] = Math.max(optimalKnowledge[(i-2)%3], optimalKnowledge[i%3]) + blocks[i];

			// skipping current block
			else
				optimalKnowledge[i%3] = optimalKnowledge[(i-1)%3];
		}

		// highest value is stored in the last index accessed
		return optimalKnowledge[(blocks.length-1)%3];
	}
}