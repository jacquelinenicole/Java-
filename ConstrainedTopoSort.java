/* Determines if a directed graph has a valid topological sort in which 
some vertex 'x' comes before some other vertex 'y'
*/

import java.io.*;
import java.util.*;

public class ConstrainedTopoSort
{
	boolean [][] adjMatrix;

	// converts text file's graph into an adjacency matrix
	public ConstrainedTopoSort(String filename) throws IOException
	{
		try
		{
			Scanner in = new Scanner(new File(filename));
			int numVertices = in.nextInt();
			adjMatrix = new boolean [numVertices][numVertices];
			int numEdges;

			for (int i = 0 ; i < numVertices ; i++)
			{
				// grab first integer of each line
				numEdges = in.nextInt();

				// initializes matrix based on the adjacency list given
				for (int j = 0 ; j < numEdges ; j++)
					adjMatrix[i][in.nextInt()-1] = true;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	// determine if x can come before y in a topological sort
	public boolean hasConstrainedTopoSort(int x, int y)
	{
		// x and y are the same vertex
		if (x == y)
			return false;

		// check if graph can be sorted topologically
		if (checkIfCycle())
			return false;

		HashSet<Integer> alreadyChecked = new HashSet<>();
		Queue<Integer> queue = new ArrayDeque<Integer>();
		alreadyChecked.add(x);
		queue.add(x);

		while (!queue.isEmpty())
		{
			x = queue.remove();

			// y is a requirement for x
			if (adjMatrix[y-1][x-1])
				return false;
			
			// add vertices that lead to x to the queue
			for (int j = 1 ; j <= adjMatrix.length ; j++)
				if (adjMatrix[j-1][x-1] && !alreadyChecked.contains(j))
					queue.add(j);
		}

		// x is a requirement for y/they can be accessed independent of each other
		return true;
	}

	// from WebCourses
	public boolean checkIfCycle()
	{
		int [] indegree = new int[adjMatrix.length];
		int numUniqueVertices = 0;

		// determine indegree for each vertex
		for (int i = 0; i < adjMatrix.length; i++)
			for (int j = 0; j < adjMatrix.length; j++)
				indegree[j] += (adjMatrix[i][j] ? 1 : 0);

		Queue<Integer> queue = new ArrayDeque<Integer>();

		// vertices with no incoming edges are added to the queue
		for (int i = 0; i < adjMatrix.length; i++)
			if (indegree[i] == 0)
				queue.add(i);

		while (!queue.isEmpty())
		{
			int vertex = queue.remove();
			
			numUniqueVertices++;

			// if current vertex is last edge to next vertex, add next vertex to queue
			// otherwise, next vertex's edge count is decremented by 1
			for (int i = 0; i < adjMatrix.length; i++)
				if (adjMatrix[vertex][i] && --indegree[i] == 0)
					queue.add(i);
		}

		// if not all vertices were visited, a cycle exists
		return (numUniqueVertices != adjMatrix.length);
	}
}