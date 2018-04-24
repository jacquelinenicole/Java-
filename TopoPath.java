/*Check if a graph has an ordering of vertices that is a path and a topological sort*/

import java.io.*;
import java.util.*;

public class TopoPath
{
	boolean [][] adjMatrix;

	// converts text file's graph into an adjacency matrix
	public TopoPath(String filename) throws IOException
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

	public static boolean hasTopoPath(String filename) throws IOException
	{
		TopoPath t = new TopoPath(filename);
		return t.checkTopoPath();
	}

	// from Webcourses
	private boolean checkTopoPath()
	{
		int [] indegree = new int[adjMatrix.length];
		int numNextPossibleVertices  = 0;
		int numVerticesVisited = 0;
		int currentVertex;
		Queue<Integer> queue = new ArrayDeque<Integer>();

		// determine indegree for each vertex
		for (int i = 0; i < adjMatrix.length; i++)
			for (int j = 0; j < adjMatrix.length; j++)
				indegree[j] += (adjMatrix[i][j] ? 1 : 0);

		// vertices with no incoming edges are added to the queue
		for (int i = 0; i < adjMatrix.length; i++)
			if (indegree[i] == 0)
			{
				queue.add(i);
				numNextPossibleVertices++;
			}

		// more than one starting point means no path possible
		if (numVerticesAdded != 1)
			return false;

		while (!queue.isEmpty())
		{
			numVerticesVisited++;
			numNextPossibleVertices = 0;

			// all vertices visited
			if (numVerticesVisited == adjMatrix.length)
				return true;

			// pull off next vertex in path
			currentVertex = queue.remove();
			
			// if current vertex is last edge to next vertex, add next vertex to queue
			// otherwise, next vertex's edge count is decremented by 1
			for (int i = 0; i < adjMatrix.length; i++)
				if (adjMatrix[currentVertex][i] && --indegree[i] == 0)
				{
					queue.add(i);
					numNextPossibleVertices++;
				}

			// all vertices (except last) should lead to exactly 1 vertex
			if (numNextPossibleVertices != 1)
				return false;
		}

		// did not reach all vertices in graph
		return false;
	}
}