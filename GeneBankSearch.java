import java.io.File;


public class GeneBankSearch {
	
	
	private static File btreeFile;
	private static File queryFile;
	private static BufferedReader bufferReader;
	private static FileReader fileReader;
	
	public static void main(String[] args) {
			
			int ifCache = 0;
			int cacheSize = 0;
			int debugLevel = 0;
			//File btreeFile;
			//File queryFile;
			
			// check if the length of args are valid
			if (args.length != 4 && args.length != 5 && args.length != 6){
				errorMessage();
				System.exit(1);
			}

			// try-catch block to check if the args are legal format and print the error message
			try {
				ifCache = Integer.parseInt(args[0]);
				btreeFile = new File(args[1]);
				queryFile = new File(args[2]);

				// parse the value of debug level
				if (args.length == 3 ) {
					debugLevel = 0;
				} else if (args.length == 4) {
					cacheSize = Integer.parseInt(args[4]);
				} else if(args.length == 5) {
					cacheSize = Integer.parseInt(args[4]);
					debugLevel = Integer.parseInt(args[5]);
					} 
			}catch (IllegalArgumentException e) {
				errorMessage();
				System.exit(1);
			}

                        // if the user choose to use cache, the cache size shall be specified
                        if(ifCache == 1 && args.length == 3){
                 	System.out.println("ERROR: Please specify the cache size.")
                        }
			// check if the args are correct according to the program design and
			// print the error message
			if ((ifCache < 0 || ifCache > 1) || (cacheSize <0) || (debugLevel<0 || debugLevel>1)) {
				errorMessage();
				System.exit(1);
			}
			
			
			
			//read the query file and catch exception if not found
			try{
			fileReader = new FileReader(queryFile);
			bufferReader = new BufferReader(fileReader);
                  	}catch(FileNotFoundException e){
                  		System.out.println("Query file not found")
                  		System.exit(1);
                  	}
                  	
                  	//get the time to build the BTree
                  	long startBuild = System.currentTimeMillis();
                  	/////////TODO EDITING building the BTree //////////
                  	BTree btree = new BTree(btreeFile);              //
                        ///////////////////////////////////////////////////
                  	//get the time of finishing BTree building
                  	long endBuild = System.currentTimeMillis();
                  	
                  	if(debugLevel == 0){
                  		System.out.println("It takes " + (endBuild - startBuild) + " s to build the BTree.");
                  	}
                  	
                  	
                  	
                  	
                  	// get the time to start search
                  	long startSearch = System.currentTimeMillis();
                  	// read the object from query file and search the frequency of each object in the BTree
			try{
			        String str;
				str = (String) bufferReader.readLine();
				while(str!=null){
					//1. 
					//2. 
					if(debugLevel == 0){
						if(the object is in BTree){
							System.out.println(str + " " + ....getfrequency());
						}else{
					         	System.out.println(str + " not found in BTree.");	
						}
						
					}
					
				str = (String) bufferReader.readLine();	
				}
				}catch(IOException e){
					e.printStackTrace();
					System.exit(1);
				}
			// get the time of finishing search	
			long endSearch = System.currentTimeMillis();
			
			if(debugLevel == 0){
				System.out.println("It takes " + (endSearch - startSearch) + " s to search the frequency of objects.");
			}
		}
			
			
		
		private static void errorMessage() {
			System.err.println("It should have 4 or 5 or 6 command-line arguments, "
					+ "\n <0/1(no/with Cache> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]"
					+ "\n The <0/1(no/with Cache> should be 0 or 1 depending on with or without cache."
					+ "\n the <degree> should be a positive integer from 1."
					+ "\n The <gbk file> should a string representing the name of a file."
					+ "\n the <sequene length> should be a integer between 1 and 31(inclusive)."
					+ "\n the [<cache size>] is optional and it should be a positive integer."
					+ "\n the [<debug level>] is optional with a default value of 0. And it should be 0 or 1");
		}

	}
