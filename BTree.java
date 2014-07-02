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
	public BTreeNode BTree_Search(BTreeNode x, Key key){
		int i = 1;
		while (i <= x.getNumKeys() && key.getKey() > x.getKey(i)){
			i = i+1;//Increments i
		}
		if (i <= x.getNumKeys() && key.getKey() == x.getKey(i)){
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
		BTreeNode x = new BTreeNode(0); //Creates initial empty node.
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
		BTreeNode z = new BTreeNode(0); //Initialized at 0,0. SHOULD CHANGE
		//TODO: Create method that returns child at index i. Should deal with Disk_Read
		BTreeNode y = x.child(i);
		z.isLeaf = x.isLeaf;
		z.numKeys = t- 1; //t represents degree. Minimum degree = t -1 
		for (int j = 1; j < (t-1); j++){
			Key tempKey = new Key(y.getKey(i), y.getFrequency(i));;
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
	public void BTree_Insert(BTreeNode[] T, Key key){
		BTreeNode r = T.root;
		if (r.numKeys == 2t -1){
			//TODO: Need to not initialize as 0,0
			BTreeNode s = new BTreeNode(0);

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
	public void BTree_Insert_NonFull(BTreeNode x, Key key){
		int i = (int) x.numKeys;
		if (x.isLeaf == true){
			while (i >= 1 && key.getKey() < x.getKey( i)){
				Key tempKey = new Key(x.getKey(i), x.getFrequency(i));
				x.setKey(tempKey,  i + 1);
			}
			x.setKey(key, i + 1);
			x.numKeys = x.numKeys + 1;
			Disk_Write(x);
		}
		else{
			while(i >= 1 && key.getKey() < x.getKey(i)){
				i = i-1;
			}
			i = i + 1;
			Disk_Read(x.child(i));
			if (x.child(i).numKeys == (2*t -1)){
				BTree_Split_Child(x, i);
				if (key.getKey() > x.getKey(i)){
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
	public static class BTreeNode {               //Class for BTreeNode
		private long key= 0, position = 0; //Long's for key value and position in BTree
		private int frequency = 0;         //Number of times same node has been encountered in BTree
		//TODO degree is inherited from BTree class
		private int degree = 0;            //Degree is zero NEEDS TO NOT BE HERE> DON"T WANT SQUIGGLY RED LINES!
		private long numKeys = 0;          //Represents current number of keys in Node. Pg. 488. 
		private boolean isLeaf;            //Boolean for if current node is a leaf. Pg. 488.
		private Key[] keys;
		private long[] pointers;


		/**
		 * Creates an empty BTreeNode Object
		 * @param key
		 * @param position
		 */
		public BTreeNode(){
			keys = new Key[2*degree -1];   //Array of keys 
			pointers = new long[2*degree];  //Array of pointers
			numKeys = 0;                   //Initial number of keys is zero.
			isLeaf = true;                 //Initially sets node up as a leaf node. 
		}
		
		public BTreeNode(boolean isLeaf, Key[] keys, long[] pointers)	{
			this.isLeaf = isLeaf;
			this.keys = keys;
			this.pointers = pointers;
			//TODO figure out numkeys?
		}

		/**
		 * Method that returns the key value 
		 * held by the BTreeNode object at a 
		 * specific index.
		 * @return key
		 */
		public long getKey(int i){
			return keys[i].getKey();//Return key

		}

		/**
		 * Returns the array of keys stored by a BTreeNode
		 * 
		 * @return The array of keys
		 */
		public Key[] getKeys()	{
			return keys;
		}

		/**
		 * The offset used in the RandomAccessFile for each child of this node
		 * 
		 * @return An array of pointers to this node's children
		 */
		public long[] getPointers()	{
			return pointers;
		}

		/**
		 * Method that returns the frequency
		 * value held by the BTreeNode object
		 * at a specific index
		 * @param i represents index
		 * @return frequency
		 */
		public int getFrequency(int i){
			return keys[i].getFrequency();
		}

		/**
		 * Method setter that sets a key
		 * in the BTreeNode to the key 
		 * specified at a specific index.
		 * @param key
		 * @param index
		 */
		public void setKey(Key key, int index){
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
		public long compareTo(BTreeNode obj2, int i){
			return key - obj2.keys[i].getKey(); //Returns BTreeNode key - 
			//Second BTreeNode key
		}


	}

	/**
	 * Key class creates an object used for storing key, as well as the 
	 * frequency for that particular key value. Getter methods are 
	 * included.
	 * Key objects are stored within an array in the BTreeNode object.
	 * @author Josh White
	 *
	 */
	public static class Key{
		private long key;
		private int frequency;

		/**
		 * Key Object
		 * @param key
		 * @param frequency
		 */
		public Key(long key, int frequency){
			this.key = key; //Inserts the key into the object
			this.frequency = frequency; //Inserts the frequency into the object
		}

		/**
		 * Getter method for returning the key at a particular index.
		 * @return Key
		 */
		public long getKey(){
			return key;
		}
		/**
		 * Getter method for returning the frequency at a particular 
		 * index
		 * @return frequency
		 */
		public int getFrequency(){
			return frequency;
		}
		/**
		 * Method that increments BTreeNode's 
		 * frequency by one. This is used when 
		 * a duplicate BTreeNode is encountered 
		 * in the BTree
		 */
		private void incrementFrequency(){
			frequency++; //Increment frequency by one
		}
	}

}
