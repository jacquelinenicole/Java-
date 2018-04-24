/*Skip list data structure using generics*/

import java.util.*;

class Node<AnyType extends Comparable<AnyType>>
{
	private ArrayList<Node<AnyType>> nextTower;
	private AnyType data;

	Node(int height)
	{
		// head node stores null in data field
		data = null;
		nextTower = new ArrayList<Node<AnyType>>(height);

		for (int i = 0 ; i < height ; i++)
			nextTower.add(null);
	}

	Node(AnyType data, int height)
	{
		this.data = data;
		nextTower = new ArrayList<Node<AnyType>>(height);

		for (int i = 0 ; i < height ; i++)
			nextTower.add(null);
	}

	public AnyType value()
	{
		return data;
	}

	public int height()
	{
		return nextTower.size();
	}

	public Node<AnyType> next(int level)
	{
		// -1 because height starts at 1 but level starts at 0
		if (level < 0 || level > height()-1)
			return null;

		return nextTower.get(level);
	}

	public void setNext(int level, Node<AnyType> node)
	{ 
		nextTower.set(level, node);
	}

	public void grow()
	{
		nextTower.add(null);
	}

	public boolean maybeGrow()
	{
		// 50% chance this tower will grow by 1 level
		if (Math.round(Math.random()) == 1)
		{
			grow();
			return true;
		}

		return false;
	}

	public void trim(int level)
	{
		nextTower.remove(level);
	}
}

public class SkipList<AnyType extends Comparable<AnyType>>
{	
	private Node<AnyType> headNode;
	private int numTowers = 0;

	// head node doesn't factor into tower count
	SkipList()
	{
		headNode = new Node<AnyType>(1);
	}

	SkipList(int height)
	{
		if (height < 1)
			height = 1;

		headNode = new Node<AnyType>(height);
	}

	public int size()
	{
		return numTowers;
	}

	public int height()
	{
		return headNode.height();
	}

	public Node<AnyType> head()
	{
		return headNode;
	}

	public void insert(AnyType data)
	{
		insert(data, generateRandomHeight(getMaxHeight(numTowers+1)));
	}

	public void insert(AnyType data, int height)
	{
		numTowers++;

		growSkipList();

		int currentLevel = height()-1;
		Node<AnyType> insertedNode = new Node<AnyType>(data, height);
		Node<AnyType> currentNode = head();
		Node<AnyType> checkAhead = head().next(currentLevel);

		while (currentLevel >= 0)
		{
			// goes to next tower of nodes while the inserted value is > the next tower's value
			// when checkAhead is null, skip list has ended at current level
			while (checkAhead != null && compareData(insertedNode.value(), checkAhead.value()) == 1)
			{
				currentNode = checkAhead;
				checkAhead = currentNode.next(currentLevel);
			}

			if (currentLevel <= height-1)
			{
				// new node takes node to left's reference
				insertedNode.setNext(currentLevel, checkAhead);

				// node to left of new node refers to new node
				currentNode.setNext(currentLevel, insertedNode);
			}
			
			currentLevel--;
			checkAhead = currentNode.next(currentLevel);
		}
	}

	public void delete(AnyType data)
	{
		int currentLevel = height()-1;
		Node<AnyType> currentNode = head();
		Node<AnyType> checkAhead = head().next(currentLevel);
		ArrayList<Node<AnyType>> potentialReferencesToNode = new ArrayList<Node<AnyType>>();
		ArrayList<Node<AnyType>> potentialReferencesFromNode = new ArrayList<Node<AnyType>>();
		int deletedNodeHeight = 0;

		while (currentLevel >= 0)
		{
			// goes to next tower of nodes while value to be deleted is > the next tower's value
			// when checkAhead is null, skip list has ended at current level
			while (checkAhead != null && compareData(data, checkAhead.value()) == 1)
			{
				currentNode = checkAhead;
				checkAhead = checkAhead.next(currentLevel);
			}

			// value to be deleted is found
			if (checkAhead != null && compareData(data, checkAhead.value()) == 0)
			{
				// nodes referring to deleted value added to ArrayList
				potentialReferencesToNode.add(0, currentNode);

				// nodes the deleted value is referring to is added to ArrayList
				potentialReferencesFromNode.add(0, checkAhead.next(currentLevel));

				// once all references have been added to ArrayLists, index 'n' corresponds to reference on level 'n'

				// gets height of the first node with the value to be deleted
				if (currentLevel == 0)
				{
					deletedNodeHeight = checkAhead.height();

					// node(s) referring to the deleted value now refer to the node(s) the deleted value referred to
					for (int i = 0 ; i < deletedNodeHeight ; i++)
						potentialReferencesToNode.get(i).setNext(i, potentialReferencesFromNode.get(i));

					numTowers--;
					while (height() > getFormulaMaxHeight(numTowers))
						trimSkipList();
				}
			}

			currentLevel--;
			checkAhead = currentNode.next(currentLevel);
		}			
	}

	public boolean contains(AnyType data)
	{
		int currentLevel = height()-1;
		Node<AnyType> currentNode = head();
		Node<AnyType> checkAhead = head().next(currentLevel);

		while (currentLevel >= 0)
		{
			// goes to next tower of nodes while value checking for is > the next tower's value
			// when checkAhead is null, skip list has ended at current level
			while (checkAhead != null && compareData(data, checkAhead.value()) == 1)
			{
				currentNode = checkAhead;
				checkAhead = currentNode.next(currentLevel);
			}
			
			// value has been found
			if (checkAhead != null && compareData(data, checkAhead.value()) == 0)
				return true;

			// drop down a level
			currentLevel--;
			checkAhead = currentNode.next(currentLevel);
		}

		// value searched for is not present
		return false;
	}

	// similar to contains() function; true --> return node; false --> return null
	public Node<AnyType> get(AnyType data)
	{
		int currentLevel = height()-1;
		Node<AnyType> currentNode = head();
		Node<AnyType> checkAhead = head().next(currentLevel);

		while (currentLevel >= 0)
		{
			// goes to next tower of nodes while value checking for is > the next tower's value
			// when checkAhead is null, skip list has ended at current level
			while (checkAhead != null && compareData(data, checkAhead.value()) == 1)
			{
				currentNode = checkAhead;
				checkAhead = currentNode.next(currentLevel);
			}
			
			if (checkAhead != null && compareData(data, checkAhead.value()) == 0)
				return currentNode.next(currentLevel);

			currentLevel--;
			checkAhead = currentNode.next(currentLevel);
		}

		// value searched for is not present
		return null;
	}

	public int getMaxHeight(int n)
	{
		int formulaMaxHeight = (int)Math.ceil(Math.log(n)/Math.log(2));

		// returns height of headNode
		if (height() > formulaMaxHeight && height() > 1)
			return height();

		// if no special cases, max height is returned using the formula
		return getFormulaMaxHeight(n);
	}

	public int getFormulaMaxHeight(int n)
	{
		if (n <= 2)
			return 1;
		
		return 
			(int)Math.ceil(Math.log(n)/Math.log(2));
	}

	private static int generateRandomHeight(int maxHeight)
	{
		int height = 1;

		// 1 acts as heads, 0 acts as tails (tails increments height)
		while (Math.round(Math.random()) == 0 && height != maxHeight)
			height++;

		return height;
	}

	private int compareData(AnyType desiredData, AnyType checkAheadData)
	{
		// returns -1 if desiredData < checkAheadData
		// tells method called from to drop down an index in ArrayList "checkTower"

		// returns 0 if desiredData == checkAheadData

		// returns 1 if desiredData > checkAheadData
		// tells method called from to look ahead another tower at this level

		return desiredData.compareTo(checkAheadData);
	}

	// adds level to skip list if head's height is less than max height
	private void growSkipList()
	{
		int maxHeight = getFormulaMaxHeight(numTowers);

		// checks if skip list height needs to increase
		if (height() >= maxHeight)
			return;

		int maxLevel = maxHeight-1;
		int previousMaxLevel = maxLevel-1;
		Node<AnyType> lastGrownNode = head();
		Node<AnyType> currentNode = head().next(previousMaxLevel);

		// head node will always have max height
		head().grow();

		// all nodes with previous max height have a 50% chance to grow
		while (currentNode != null)
		{
			if (currentNode.maybeGrow())
			{
				// reference on newly-created level is updated
				lastGrownNode.setNext(maxLevel, currentNode);
				lastGrownNode = currentNode;
			}

			currentNode = currentNode.next(previousMaxLevel);
		}
	}

	// removes top layer of skip list if head's height exceeds max height
	private void trimSkipList()
	{
		int maxHeight = getFormulaMaxHeight(numTowers);

		// checks if skip list height needs to decrease
		if (height() <= maxHeight)
			return;

		int previousMaxLevel = height()-1;
		Node<AnyType> placeholderNode = head();
		Node<AnyType> currentNode = head();

		// all nodes with height > max height need to be trimmed to = current max height
		do 
		{
			placeholderNode = currentNode.next(previousMaxLevel);
			currentNode.trim(previousMaxLevel);
			currentNode = placeholderNode;
		} while (currentNode != null);
	}

	// print out contents of skiplist from level height-1 to 0
	public void printList()
	{
		Node<AnyType> currentNode = head();
		Node<AnyType> previousNode = head();
		int currentLevel = height()-1;

		while (currentLevel >= 0)
		{
			currentNode = head();
			do
			{
				System.out.print("[ " + currentNode.value() + " ] --> ");
				previousNode = currentNode;
				currentNode = previousNode.next(currentLevel);
			} while (currentNode != null);

			System.out.println();
			currentLevel--;
			currentNode = previousNode.next(currentLevel);
		}
	}
}