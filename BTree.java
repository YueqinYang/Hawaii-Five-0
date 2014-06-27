/**
 * Creates the BTree. It also contains the search method.
 * @author Josh White
 *
 */
public class BTree {

	/** 
	 * Searches the BTree for a position to insert. It 
	 * also compares keys with Objects throughout the 
	 * BTree
	 * @param x
	 * @param key
	 * @return
	 */
	public BTreeNode BTree_Search(BTreeNode x, long key){
		if (x == null || key == x.getKey()){
			return x;
		}
//		long left =  (2*x.position) + 1;
//		long right = (2*x.position) + 2; 
		if (key < x.getKey()){
			return BTree_Search(x.left(), key);
		}
		else{
			return BTree_Search(x.right(), key);
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
		 * held by the BTreeNode object.
		 * @return key
		 */
		public long getKey(){
			return key; //Return key
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
