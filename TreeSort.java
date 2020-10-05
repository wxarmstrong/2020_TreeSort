import java.util.Random;
import java.util.concurrent.TimeUnit;

class TreeSort  
{ 
	static final int SMALLEST_SIZE = 10;
	static final int LARGEST_SIZE = 10000;
	static final int ITERATIONS = 10000;
	
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
    } 

	Node root = null;
	int counter = 0;
	
	public TreeSort() { root = null; }
		
	void insert (int k) {	
		root = insert(root, k);
	}
		
	Node insert (Node root, int k){
		if (root == null) 
			return new Node(k);
		else if (k <= root.key)
			root.left = insert(root.left, k);
		else if (k > root.key)
			root.right = insert(root.right, k);
		return root;
	}
		
	void insert (int arr[])
	{
		for (int i=0; i < arr.length; i++)
			insert(arr[i]);
	}
		
	void retrieve(int arr[])
	{
		int counter = 0;
		retrieve(root, arr);
	}
		
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
		
	void sort(int arr[])
	{
		insert(arr);
		retrieve(arr);
	}
  
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
					arr[j] = rand.nextInt(10000)-5000;
				
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
} 
