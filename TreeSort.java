/*
William Armstrong
CS 3310
Cal Poly Pomona
Fall 2020
10/07/2020

TreeSort
An algorithm for sorting integer arrays by
constructing a binary tree from its members
and then extracting them in order.

-------------------------------------------
TIME COMPLEXITY:
Key operation: (root == null) (checked at each level of the tree)
-------------------------------------------
WORST-CASE:
In the worst case, there will be no branching: each node of the tree
will have at most one child. In this case, the resulting tree is
functionally identical to a linked list, meaning that the it takes
n+1 operations to insert an item into a tree with n entries. The recurrence
relationship to insert n items is thus:

W(n) = W(n-1) + n
W(1) = 1
Which is a simple summation of 1...n, or

W(n) = n(n+1)
       ------ = O(n^2)
	     2   

BEST-CASE:
In the best case, the tree will stay balanced the entire time that items
are inserted into it, & thus the # of operations to insert an item into a
tree with N elements is floor(lg n) + 1. The recurrence relationship
to insert n items is thus:
B(n) = B(n-1) + floor(lg n) + 1
B(1) = 1

To find the upper bound, recognize that
B(n-1) + floor(lg n) + 1 <= B(n-1) + lg n + 1
= (B(n-2) + lg(n-1) + 1) + lg n + 1
= ((B(n-3) + lg(n-2) + 1) + lg(n-1) + 1) + lg n + 1
...
= lg(n) + lg(n-1) + lg(n-2) + ... + lg(2) + lg(1) + n
= lg(n!) + n
= O(n lg n) [Stirling's approximation]

The lower bound can be found by recognizing that:
B(n-1) + floor(lg n) + 1 >= B(n-1) + lg n
= (B(n-2) + lg(n-1)) + lg n
= ((B(n-3) + lg(n-2) ) + lg(n-1)) + lg n
...
= lg(n) + lg(n-1) + lg(n-2) + ... + lg(2) + lg(1)
= lg(n!)
= O(n lg n) [Stirling's approximation]

AVERAGE-CASE:
The height of a self-balancing tree is always kept at floor(lg n) + 1, and thus 
even in the worst case (where items are inserted at the lowest possible node
prior to rebalancing),
A(n) = A(n-1) + ceil(lg n) + 1
A(n) = 1

The upper bound is reached similar to above:
A(n-1) + ceil(lg n) + 1 <= A(n-1) + lg n + 2
...
= lg(n!) + 2n
= O(n lg n) [Stirling's approximation]
And the lower bound likewise:
A(n-1) + ceil(lg n) + 1 >= A(n-1) + lg n + 1
...
= lg(n!) + n
= O(n lg n) [Stirling's approximation]

For an unbalanced tree, the average-case is more difficult to determine due to the inconsistent height & structure of random binary trees. If the average height of a binary tree of n nodes is taken to be sqrt(n) [http://www.dtc.umn.edu/~odlyzko/doc/arch/extreme.heights.pdf] then
A(1) = 1
A(n) <= A(n-1) + ceil(sqrt(n))
<= A(n-1) + sqrt(n) + 1
= (A(n-2) + sqrt(n-1) + 1) + sqrt(n) + 1
...
= sum i=1...n(sqrt i) + 1
<= (2/3)(n + 1/2)^(3/2) [https://arxiv.org/pdf/1204.0877.pdf]
= O(n^(3/2))


-------------------------------------------
MEMORY COMPLEXITY:
-------------------------------------------
In all cases, the # of nodes required for the tree is equal to the #
of items in the original integer array. => O(n)
*/

import java.util.Random;
import java.util.concurrent.TimeUnit;

class TreeSort  
{ 
	// Constants used for the testing loop
	static final int SMALLEST_SIZE = 10;
	static final int LARGEST_SIZE = 1000000;
	static final int ITERATIONS = 100;
	
	// Node members for the tree
    class Node  
    { 
        int key; 
        Node left, right; 
        public Node(int k)
        { 
            key = k;
			left = null;
			right = null;
        } 
		// end Node
    } 
	// end Node

	Node root = null;
	/*
	A running counter for the current array position,
	such that the recursive retrieve functions is able
	to reinsert the sorted items back into the original
	array in the correct order.
	*/
	int counter = 0;
	
	public TreeSort()
		{
			root = null;
		}
	// end TreeSort
		
	void insert (int k) {	
		root = insert(root, k);
	}
	// end insert
		
	/*
	Recursively locates an unoccupied spot on the
	tree to place a new item at, & sets the pointer
	to a newly created node
	*/
	Node insert (Node root, int k){
		if (root == null) 
			return new Node(k);
		else if (k <= root.key)
			root.left = insert(root.left, k);
		else if (k > root.key)
			root.right = insert(root.right, k);
		//end if
		return root;
	}
	// end insert
		
	//Inserts each item of an integer array into the tree in sequence
	void insert (int arr[])
	{
		for (int i=0; i < arr.length; i++)
			insert(arr[i]);
	}
	// end insert
		
	//Retrieves all items from the tree and places them into the array in order
	void retrieve(int arr[])
	{
		int counter = 0;
		retrieve(root, arr);
	}
	// end retrieve
		
	/*
	Does a recursive, preorder traversal through the tree,
	placing each item into the array in order
	*/
	void retrieve(Node root, int arr[])
	{
		if (root != null)
		{
			retrieve(root.left, arr);
			arr[counter] = root.key;
			counter++;
			retrieve(root.right, arr);
		}
	}
	// end retrieve
		
	// Places an integer array into a tree & then retrieves it in order
	void sort(int arr[])
	{
		insert(arr);
		retrieve(arr);
	}
	// end sort
  
    public static void main(String[] args)  
    { 
		Random rand = new Random();

		for (int size = SMALLEST_SIZE; size <= LARGEST_SIZE; size *= 10)
		{
			System.out.println(size + " items: ");
			long totalTime = 0;
			for (int i=0; i<ITERATIONS; i++)
			{
				int arr[] = new int[size];
				for (int j = 0; j < size; j++)
					arr[j] = rand.nextInt(size)-size/2;
				
				long startTime = System.nanoTime();
				TreeSort tree = new TreeSort();
				tree.sort(arr);
				long endTime = System.nanoTime();
				totalTime += endTime - startTime;
			}
			long avgTime = totalTime / ITERATIONS;
			System.out.println("Avg time: " + avgTime/1000 + "ms");
		}
    } 
	// end main
} 