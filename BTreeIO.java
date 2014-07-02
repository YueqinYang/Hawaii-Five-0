import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


public class BTreeIO {

//	private String fileName;
	private int degree;

	//Leaf boolean = 0 offset
	//N key value= (N * (long + int)) + int
	//N key freq = (N * (long + int)) + int + long
	//pointer N = int + 2 * (degree - 1) + N * int

	private RandomAccessFile file;

	/**
	 * Creates a RandomAccessFile with the file name "fileName + '.gbk.btree.' + sequenceLength" that will be used to store BTree nodes on disk.
	 * This will read in and return BTreeNodes as well as write them to the file.
	 * 
	 * @param fileName The name of the test file used when creating the BTree.
	 * @param sequenceLength The length of the DNA sequences. Used in this class only for the file name.
	 * @param degree The degree of the BTreeNodes to be written to disk.
	 */
	public BTreeIO(String fileName, int sequenceLength, int degree)	{
//		this.fileName = fileName;
		this.degree = degree;

		try {
			file = new RandomAccessFile(fileName + ".gbk.btree." + sequenceLength, "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Writes the specified BTreeNode to disk. The node will write itself to a binary file in order of
	 * leaf boolean, key data, pointer data.
	 * 
	 * @param node The node to be written to disk.
	 * @param offset The offset of the node.
	 */
	public void writeDisk(BTree.BTreeNode node, int offset) throws IOException	{
		file.seek(offset);
		file.writeBoolean(node.getLeafStatus());

		//Write keys
		for (int i = 0; i < node.getKeys().length; i++)	{
			if (node.getKeys()[i] != null)	{
				file.writeLong(node.getKeys()[i].getKey());
				file.writeInt(node.getKeys()[i].getFrequency());
			}
			else	{
				//If there is no key, write zeroes instead to keep all nodes the same size. If all nodes are not the same size,
				//then read / write errors will occur later
				file.writeLong(0);
				file.writeInt(0);
			}
		}

		//Write Pointers
		for (int i = 0; i < node.getPointers().length; i++)	{
			file.writeLong(node.getPointers()[i]);		//TODO might be able to use ints here to save space
		}
	}

	/**
	 * Creates a BTreeNode by reading in the contents of the RandomAccessFile at a given offset
	 * 
	 * @param offset The offset of the node in the RandomAccessFile
	 * @return The node that was stored in binary
	 */
	public BTree.BTreeNode readDisk(long offset) throws IOException	{

		file.seek(offset);
		boolean isLeaf = file.readBoolean();
		BTree.Key[] keys = new BTree.Key[2 * degree - 1];
		long[] pointers = new long[2 * degree];

		//TODO store number of keys in the file and use that instead of breaking at 0.
		for (int i = 0; i < 2 * degree - 1; i++)	{
			long value = file.readLong();
			int frequency = file.readInt();

			if (value == 0)	{
				break;
			}
			keys[i] = new BTree.Key(value, frequency);
		}

		file.seek(offset + (2 * degree - 1));
		for (int i = 0; i < 2 * degree; i++)	{
			long value = file.readLong();
			if (value == 0)	{
				break;
			}
			pointers[i] = file.readLong();
		}

		return new BTree.BTreeNode(isLeaf, keys, pointers);


	}

}
