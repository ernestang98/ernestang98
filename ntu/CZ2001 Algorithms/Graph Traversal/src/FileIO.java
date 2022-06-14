import java.io.*;
import java.util.*;

public class FileIO {

	//Read each line in the file into a List
	public static List<String> read(String fileName) throws IOException {
		List<String> data = new ArrayList<String>();
		Scanner scanner = new Scanner(new FileInputStream(fileName));
		try {
			while (scanner.hasNextLine()) {
				data.add(scanner.nextLine());
			}
		} finally {
			scanner.close();
		}
		return data;
	}
	
	public static int totalNodeNum(String filename) throws IOException {
		// Find the largest nodeID
		ArrayList<String> stringArray = (ArrayList<String>) read(filename);
		int maxNum = 0;
		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			StringTokenizer star = new StringTokenizer(st, "[\t\r\n ]+");
			if (st.charAt(0) == '#')
				continue; 
			int from = Integer.parseInt(star.nextToken().trim());
			int to = Integer.parseInt(star.nextToken().trim());
			if (from > maxNum)
				maxNum = from;
			if (to > maxNum)
				maxNum = to;
		}
		return maxNum+1;
	}
	
	public static int hospitalNodeNum(String filename) throws IOException {
		ArrayList<String> stringArray = (ArrayList<String>) read(filename);
		String i = stringArray.get(0).substring(1);
		return Integer.parseInt(i);
	}

	public static ArrayList<IntPair> readGraph(String filename) throws IOException {
		// read String from text file
		ArrayList<String> stringArray = (ArrayList<String>) read(filename);
		ArrayList<IntPair> pairs = new ArrayList<IntPair>();

		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);

			StringTokenizer star = new StringTokenizer(st, "[\t\r\n ]+"); // pass in the string to the string tokenizer

			// ignore the comments at the first few lines of the file
			if (st.charAt(0) == '#')
				continue; 

			// get the 2 NodeIds in each row separated by white space
			int from = Integer.parseInt(star.nextToken().trim());
			int to = Integer.parseInt(star.nextToken().trim());

			//Store them together as a IntPair object
			pairs.add(new IntPair(from, to));
		}
		return pairs;
	}

	public static int[] readHospital(int n, int hos, String FileName) throws IOException {
		ArrayList<String> stringArray = (ArrayList<String>) read(FileName);
		int[] intArray = new int[hos];
		System.out.println("The " + hos + " hospital nodes are read from the " + FileName + " file\nWaiting for graph to be generated...");
		//Add the hospitals nodes into a hospitalArray
		for (int i = 0; i < stringArray.size() - 1; i++) {
			int x = Integer.valueOf((String) stringArray.get(i + 1));
			intArray[i] = x;
		}
		return intArray;
	}

	public static void output(List<String> out) throws IOException {
		FileWriter myWriter = new FileWriter("Output.txt");
		//Write each line from the out ArrayList into a line in the Output.txt file
		for (int i = 0; i < out.size(); i++) {
			myWriter.write((String) out.get(i) + "\n");
		}
		myWriter.close();
		System.out.println("\nSuccessfully wrote to output file.\n");
	}
}