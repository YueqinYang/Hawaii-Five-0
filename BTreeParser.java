import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 * @author astout
 * The BTreeIO class will parse the genebank file passed into its constructor and discard all non-gene data. It will then grab the first gene sequence, convert it into 
 * a long, and hold onto it until it is asked to return it through the getNextLong() method. Any time the getNextLong() method is called, this class will automatically 
 * grab the next valid gene sequence. It is recommended to always check to see if BTreeIO hasNext() before calling getNextLong(), or it will return 0s.
 */
public class BTreeParser {

	private String parsedInput;
	private int fileIndex = 0;
	private long nextLong;
	private int sequenceLength;

	/**
	 * BTreeIO when created will parse the given genebank file and discard all non-gene data that it contains. 
	 * It will then hold onto the next gene sequence to be returned.
	 * 
	 * @param fileName The file name of the gene bank file to be parsed.
	 * @param sequenceLength The length of the gene sequences to be parsed from the gene bank file.
	 */
	public BTreeParser(String fileName, int sequenceLength)	{
		this.sequenceLength = sequenceLength;

		StringBuilder builder = new StringBuilder();
		Scanner scan = null;
		try {
			scan = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//Removes everything leading up to the first set of gene sequences
		String input = "";
		while (input.compareTo("ORIGIN") != 0)	{
			input = scan.nextLine().trim();
		}

		//TODO This currently will only parse the first series of genes from a given file. It will not continue after it has encountered its first '/'
		while (scan.hasNext())	{
			String line = scan.nextLine();
			for (int i = 0; i < line.length(); i++)	{
				char next = line.charAt(i);
				if (next == '/') //This will break out of the loop when the gene sequence ended by finding this character
					break;
				if (next > '9') //This will discard all numbers from the gene sequence
					builder.append(line.charAt(next));
			}
		}

		parsedInput = builder.toString();

		nextLong = findNextLong();

	}
	
	/**
	 * Returns 1 + the next gene sequence as a long data type, or 0 if there is no more data.
	 *  
	 * @return The gene sequence plus a leading 1 to prevent loss of leading zeroes. Example, the gene sequence "AGT" would be returned as "1001011", 
	 * or "CA" would be returned as "10100".
	 */
	public long getNextLong()	{
		long rval = nextLong;
		nextLong = findNextLong();
		return rval;
	}
	
	/**
	 * Returns whether or not there is more DNA data to be returned.
	 * 
	 * @return This will return true if there is another full length valid gene sequence.
	 */
	public boolean hasNext()	{
		if (nextLong == 0)
			return false;
		return true;
	}

	/**
	 * This will attempt to locate the next long inside the gene sequence. Any invalid gene sequences are searched through and discarded.
	 * 
	 * @return The next valid gene sequence, or 0 if there are no more valid gene sequences.
	 */
	private long findNextLong()	{
		long rval = 0;
		while (rval == 0)	{
			if (sequenceLength + fileIndex > parsedInput.length())	{
				return 0;
			}
			rval = stringToLong(parsedInput.substring(fileIndex, fileIndex + sequenceLength));
			fileIndex++;
		}
		return rval;
	}

	/**
	 * Converts a string into a long where A = 00, C = 01, G = 10, and T = 11. If at any time, an invalid character is encountered (namely the 'n' character), this method
	 * will return 0.
	 * 
	 * @param input The gene sequence to be converted.
	 * @return The gene sequence converted into binary, or 0 if the gene sequence was invalid. 
	 */
	private long stringToLong(String input)	{
		long output = 1;
		for (int i = 0; i < input.length(); i++)	{
			switch(input.charAt(i))	{
			case 'a':
				output *= 100;
				break;
			case 'c':
				output *= 100;
				output += 1;
				break;
			case 'g':
				output *= 100;
				output += 10;
				break;
			case 't':
				output *= 100;
				output += 11;
				break;
			default:
				return 0;
				//TODO potential optimization of this to have it skip ahead when encountering an invalid character instead of parsing it numerous times. Very minor
			}
		}
		return output;
	}

}
