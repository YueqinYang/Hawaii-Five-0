/**
 * Creates the BTree. It also contains the search method.
 * @author Josh White
 *
 */
public class BTree {
	//TODO: set t as the degree specified. t = 0 is temporary
	private int t = 0; 
	BTreeNode[] node;
	/** 
	 * Searches the BTree for a position to insert. It 
	 * also compares keys with Objects throughout the 
	 * BTree
	 * @param x
	 * @param key
	 * @return
	 */
	public BTreeNode BTree_Search(BTreeNode x, long key){
	int i = 1;
	while (i <= x.getNumKeys() && key > x.key){
		i = i+1;//Increments i
	}
	if (i <= x.getNumKeys() && key == x.key){
//		return (x, i); //Book notation? Page 492
	}
	else{
		if (x.isLeaf == true){ 
			return null; 
		}
		else{
			Disk_Read(x.child(i)){ //Book Notation. Page 492
				return BTree_Search(x.child(i), key); //csubi represents child
			}
		}
	}
	}
	
	/**
	 * Create an empty B-Tree
	 * Pulled from page 492 of textbook
	 */
	public void BTree_Create(BTreeNode[] T){
		BTreeNode x = new BTreeNode(0, 0); //Creates initial empty node.
		Disk_Write(x); //Disk Write x
		//TODO: Need to create a root node for BTreeNode array
		T.root = x; //Sets Array T's root as x
		
	}
	
	/**
	 * Operation for splitting child node
	 * i represents the index number
	 * Page 494 of textbook
	 */
	public void BTree_Split_Child(BTreeNode x,int i){
		//TODO: Change 0,0 to appropriate initial values. 
		BTreeNode z = new BTreeNode(0, 0); //Initialized at 0,0. SHOULD CHANGE
		//TODO: Create method that returns child at index i. Should deal with Disk_Read
		BTreeNode y = x.child(i);
		z.isLeaf = x.isLeaf;
		z.numKeys = t- 1; //t represents degree. Minimum degree = t -1 
		for (int j = 1; j < (t-1); j++){
			long tempKey = y.getKey(j+t);
			z.setKey(tempKey, j);
		}
		if (y.isLeaf == false){
			for (int j = 1; j < t; j++){
				//TODO: Need a method that can set the child of a node
				z.child(j) = y.child(j + t);
			}
		}
		y.numKeys = t - 1;
		for (int j =  ((int) x.numKeys +1); j > (i + 1); j--){
			x.child(j+1) = x.child(j);
		}
		x.child(i+1) = z;
		for (int j = (int) x.numKeys; j > i; j--){
			long tempKey = x.getKey(j + 1); 
			x.setKey(tempKey, j);
		}
		x.setKey(y.getKey(t), i);
		x.numKeys = x.numKeys + 1;
		Disk_Write(y);
		Disk_Write(z);
		Disk_Write(x);
	}
	
	/**
	 * Inserts a key into BTree T in a single pass
	 * down. 
	 * On page 495 of textbook
	 */
	public void BTree_Insert(BTreeNode[] T, long key){
		BTreeNode r = T.root;
		if (r.numKeys == 2t -1){
			//TODO: Need to not initialize as 0,0
			BTreeNode s = new BTreeNode(0,0);
			T.root = s;
			s.isLeaf = false;
			s.numKeys = 0;
			s.child(r); //Book specifies as child 1. 
			BTree_Split_Child(s, 1);
			BTree_Insert_NonFull(s, key);
		}
		else{
			BTree_Insert_NonFull(r, key);
		}
	}
	
	/**
	 * Inserts a key into a nonfull BTreeNode x
	 * Page 495 of textbook
	 */
	public void BTree_Insert_NonFull(BTreeNode x, long key){
		int i = (int) x.numKeys;
		if (x.isLeaf == true){
		while (i >= 1 && key < x.getKey( i)){
			long tempKey = x.getKey( i);
			x.setKey(tempKey,  i + 1);
		}
		x.setKey(key, i + 1);
		x.numKeys = x.numKeys + 1;
		Disk_Write(x);
		}
		else{
			while(i >= 1 && key < x.getKey(i)){
				i = i-1;
			}
			i = i + 1;
			Disk_Read(x.child(i));
			if (x.child(i).numKeys == (2*t -1)){
				BTree_Split_Child(x, i);
				if (key > x.getKey(i)){
					i = i+1;
				}
				BTree_Insert_NonFull(x.child(i), key);
			}
		}
	}
	


	/**
	 * Creates a BTreeNode and its methods
	 * @author Josh White, Ayrton Stout, Yueqin Yang
	 *
	 */
	public class BTreeNode { //Class for BTreeNode
		private long key= 0, position = 0; //Long's for key value and position in BTree
		private int frequency = 0; //Number of times same node has been encountered in BTree
		//TODO degree is inherited from BTree class
		private int degree = 0; //Degree is zero NEEDS TO NOT BE HERE> DON"T WANT SQUIGGLY RED LINES!
		private BTreeNode[] data; 
		private long numKeys = 0; //Represents current number of keys in Node. Pg. 488. 
		private boolean isLeaf; //Boolean for if current node is a leaf. Pg. 488.
		/**
		 * Constructor for BTreeNode.
		 * 
		 * @param obj
		 */
		public BTreeNode(BTreeNode obj){
			//////////In Progress/////////////////////////
			//////////////////////////////////////////////
			BTreeNode[] data = new BTreeNode[(degree*2) - 1];
			data[0] = obj;
			////////////////////////////////////////////////
			//////////////////////////////////////////////
		}
		
	

		/**
		 * Creates the BTreeNode Object
		 * @param key
		 * @param position
		 */
		public BTreeNode(long key, long position){
			this.key = key; //Stores the key within the object
			this.position = position; //Stores the position within the object
			frequency = 0; //Initial frequency is zero
			numKeys = 0; //Initial number of keys is zero.
			isLeaf = true; //Initially sets node up as a leaf node. 
		}
		
		/**
		 * Method that increments BTreeNode's 
		 * frequency by one. This is used when 
		 * a duplicate BTreeNode is encountered 
		 * in the BTree
		 */
		public void incrementFrequency(){
			frequency++; //Increment frequency by one
		}
		/**
		 * Method that returns the key value 
		 * held by the BTreeNode object at a 
		 * specific index.
		 * @return key
		 */
		public long getKey(int i){
			return node[i].key;//Return key
			
		}
		/**
		 * Method setter that sets a key
		 * in the BTreeNode to the key 
		 * specified at a specific index.
		 * @param key
		 * @param index
		 */
		public void setKey(long key, int index){
			//TODO Finish this method.
		}
		/**
		 * Method getter that returns 
		 * number of keys in a BTreeNode
		 * @return number of keys
		 */
		public long getNumKeys(){
			return numKeys;
		}
		/**
		 * Method that increments BTreeNode's 
		 * number of keys by one. This is used 
		 * when adding a key into the BTreeNode 
		 */
		public void incrementNumKeys(){
			numKeys++; //Increment number of keys by one
		}
		/**
		 * Getter method for finding if 
		 * current BTreeNode is a leaf 
		 * node or not. 
		 * @return true if leaf, false if not
		 */
		public boolean getLeafStatus(){
			return isLeaf;
		}
		
		/**
		 * Compares the two keys stored by the 
		 * two BTreeNode objects. 
		 * @param obj2
		 * @return key - obj2.getKey()
		 */
		public long compareTo(BTreeNode obj2){
			return key - obj2.getKey(); //Returns BTreeNode key - 
										//Second BTreeNode key
		}
		
		/**
		 * Adds the BTreeNode object into the 
		 * node on the tree
		 * @param obj
		 */
		public void add(BTreeNode obj){
			for (int i = 0; i < data.length; i++){ //Searches through the entire data array
				if (data[i].compareTo(obj) == 0){ //If the specified object is the same as an object in the array
					data[i].incrementFrequency(); //Increments frequency of same object
					break; //End add method
				}
				else if (data[i].compareTo(obj) > 0){ //If the current object is larger than the specified object
					shiftArray(i); //Shift the array to insert the object
					data[i] = obj; //Inserts the object at the current index
					break; //End add method
				}
				
			}
		}
		/**
		 * Shifts the elements over in the Node/array
		 * for insertion of BTreeNode. int i is the 
		 * index that the shifting begins at.
		 * @param i
		 */
		private void shiftArray(int i){
			for (int j = i; j < data.length; j++){ //As you go throughout the BTreeNode array
				data[i++] = data[i]; //Shifts data array objects to the right
				i++; //Increments index position
			}
		}
		
		
		
	}


}
