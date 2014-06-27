import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 * @author astout
 * Parses the gene bank file
 * Returns the next long to be added to the BTree
 * 
 * --Maybe does other stuff?
 */
public class BTreeIO {

	private String parsedInput;
	
	public BTreeIO(String fileName)	{
	
		StringBuilder builder = new StringBuilder();
		Scanner scan = null;
		try {
			scan = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String input = "";
		while (input.compareTo("ORIGIN") != 0)	{
			input = scan.nextLine().trim();
		}
		
		while (scan.hasNext())	{
			char next = scan.next().charAt(0);
//			if (next == '/')	{
//				break;
//			}
//			if (next > '9')	{
				builder.append(next);
//			}
		}
		
		parsedInput = builder.toString();
		System.out.println(parsedInput);
//		for (int i = 0; i < parsedInput.length(); i++)	{
//			System.out.print(parsedInput.charAt(i));
//			if (i % 10 == 0)	{
//				System.out.print("\n");
//			}
//		}
	}
	
}
