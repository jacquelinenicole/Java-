/*Binary Search Tree that works for all data types; unique values only*/
import java.io.*;
import java.util.*;

class Node<T extends Comparable<T>>
{
	// T for AnyType
	T data;
	Node<T> left, right;

	Node(T data)
	{
		this.data = data;
	}
}

public class GenericBST<T extends Comparable<T>>
{
	private Node<T> root;

	public void insert(T data)
	{
		root = insert(root, data);
	}

	// adds node according to BST ordering properties
	private Node<T> insert(Node<T> root, T data)
	{
		if (root == null)
		{
			return new Node<>(data);
		}

		// use compareTo so any type of data can be compared
		int comparisonValue = data.compareTo(root.data);

		// BSTs put smaller data in the left child node
		if (comparisonValue < 0)
		{
			root.left = insert(root.left, data);
		} 

		// no duplicates permitted, so the data is larger and travels to the right child
		else if (comparisonValue > 0)
		{
			root.right = insert(root.right, data);
		}

		return root;
	}

	// so delete() can be called without adding "root" to the argument each time
	public void delete(T data)
	{
		root = delete(root, data);
	}

	private Node<T> delete(Node<T> root, T data)
	{
		int comparisonValue = data.compareTo(root.data);

		// incase tree is already empty
		if (root == null)
		{
			return null;
		}

		// recursive call on left subtree
		else if (comparisonValue < 0)
		{
			root.left = delete(root.left, data);
		}

		// recursive call on right subtree
		else if (comparisonValue > 0)
		{
			root.right = delete(root.right, data);
		}

		// this block is executed once the value to be deleted is found
		else
		{
			// leaf node -- value is deleted
			if (root.left == null && root.right == null)
			{
				return null;
			}

			// no right child so left child replaces deleted node
			else if (root.right == null)
			{
				return root.left;
			}

			// no left child so right child replaces deleted node
			else if (root.left == null)
			{
				return root.right;
			}

			// two children so max value of left subtree replaces deleted node
			else
			{
				root.data = findMax(root.left);
				root.left = delete(root.left, root.data);
			}
		}

		return root;
	}

	private T findMax(Node<T> root)
	{
		// largest value is in the furthest-right node
		while (root.right != null)
		{
			root = root.right;
		}

		return root.data;
	}

	// so contains() can be called without adding "root" to the argument each time
	public boolean contains(T data)
	{
		return contains(root, data);
	}

	private boolean contains(Node<T> root, T data)
	{
		int comparisonValue = data.compareTo(root.data);
		if (root == null)
		{
			return false;
		}
		else if (comparisonValue < 0)
		{
			return contains(root.left, data);
		}
		else if (comparisonValue > 0)
		{
			return contains(root.right, data);
		}
		else
		{
			return true;
		}
	}

	public void inorder()
	{
		System.out.print("In-order Traversal:");
		inorder(root);
		System.out.println();
	}

	// prints in "left, root, right" order
	private void inorder(Node<T> root)
	{
		if (root == null)
			return;

		inorder(root.left);
		System.out.print(" " + root.data);
		inorder(root.right);
	}

	public void preorder()
	{
		System.out.print("Pre-order Traversal:");
		preorder(root);
		System.out.println();
	}

	// prints in "root, left, right" order
	private void preorder(Node<T> root)
	{
		if (root == null)
			return;

		System.out.print(" " + root.data);
		preorder(root.left);
		preorder(root.right);
	}

	public void postorder()
	{
		System.out.print("Post-order Traversal:");
		postorder(root);
		System.out.println();
	}

	// prints in "left, right, root" order
	private void postorder(Node<T> root)
	{
		if (root == null)
			return;

		postorder(root.left);
		postorder(root.right);
		System.out.print(" " + root.data);
	}
}