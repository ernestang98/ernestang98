import java.util.*;

public class Main {
	public static void main(String[] args) {

		// declare variables to store number of total nodes and hospital nodes
		int totalNodes = 0;
		int noOfHospitalNodes = 0;

		// File names
		String graphFile = "";
		String hospitalFile = "";

		// Print menu to select graph input type
		int graphChoice = 0;
		int partChoice = 0;

		boolean flag = false;

		System.out.println("=======================\n" + 
				"Graph input types:\n" + 
				"1. Small input graph\n" + 
				"2. Real road network\n" + 
				"3. New graph input\n" + 
				"=======================\n" + 
				"Please select graph input type:");
		Scanner scan = new Scanner(System.in);
		graphChoice = scan.nextInt();
		switch (graphChoice) {
		case 1:
			hospitalFile = "Small_Hospital_List.txt";
			graphFile = "Small_Input_Graph.txt";
			break;
		case 2:
			hospitalFile = "Large_Hospital_List.txt";
			graphFile = "roadNet.txt";
			break;
		case 3:
			hospitalFile = "Hospital_List.txt";
			//Insert graph name here
			graphFile = "INSERT GRAPH NAME HERE.txt";
			flag = true;
			break;
		}

		try {
			totalNodes = FileIO.totalNodeNum(graphFile);
		} catch (Exception e) {
			System.out.println("IOException: " + e.getMessage());
		}

		// Generate new hospital list file for new graph input
		if (flag == true) {
			System.out.println("Enter number of hospital nodes desired:");
			int num = scan.nextInt();
			Hospital h = new Hospital();
			try {
				h.generate(totalNodes, num);
			} catch (Exception e) {
				System.out.println("IOException: " + e.getMessage());
			}
			flag=false;
		}
		
		try {
			noOfHospitalNodes = FileIO.hospitalNodeNum(hospitalFile);
		} catch (Exception e) {
			System.out.println("IOException: " + e.getMessage());
		}


		// Store the randomly generated hospital nodes from the file into hospitalArray
		int[] hospitalArray = new int[noOfHospitalNodes];

		try {
			hospitalArray = FileIO.readHospital(totalNodes, noOfHospitalNodes, hospitalFile);
		} catch (Exception e) {
			System.out.println("IOException: " + e.getMessage());
		}

		// aL is the adjacency list
		ArrayList<ArrayList<String>> aL = new ArrayList<>(totalNodes);

		// pairs stores the pairs of adjacent nodes read from the graph file
		ArrayList<IntPair> pairs = new ArrayList<IntPair>();
		try {
			pairs = FileIO.readGraph(graphFile);

		} catch (Exception e) {
			System.out.println("IOException: " + e.getMessage());
		}

		// Create empty graph
		for (int i = 0; i < totalNodes; i++)
			aL.add(new ArrayList<>());

		// Adding the vertices & edges from the file
		for (int i = 0; i < pairs.size(); i++) {
			IntPair p = (IntPair) pairs.get(i);
			String v = "";
			String nV = "";

			// Insert new vertex at a particular node
			// v is the vertex which you want to add new vertex (nV)

			v = String.valueOf(p.getFrom());
			nV = String.valueOf(p.getTo());

			if (Arrays.stream(hospitalArray).anyMatch(j -> j == p.getFrom())) {
				v = "0" + String.valueOf(p.getFrom());
				aL.get(Integer.valueOf(v.substring(1))).add(nV);
				aL.get(Integer.valueOf(nV)).add(v);
			} else if (Arrays.stream(hospitalArray).anyMatch(j -> j == p.getTo())) {
				nV = "0" + String.valueOf(p.getTo());
				aL.get(Integer.valueOf(v)).add(nV);
				aL.get(Integer.valueOf(nV.substring(1))).add(v);
			} else {
				aL.get(Integer.valueOf(v)).add(nV);
				aL.get(Integer.valueOf(nV)).add(v);
			}

		}

		ArrayList<String> output = new ArrayList<String>();

		// Print menu to choose the part to display
		while (true) {
			System.out.println("===========\n" + "1. Part A & Part B\n" + "2. Part C\n" + "3. Part D\n"
					+ "4. Save output to file\n" + "===========\n" + "Please enter your choice: ");
			partChoice = scan.nextInt();

			switch (partChoice) {
			case 1:
				output.add("\nPart A/B:\n");
				output.addAll(PartB.main(totalNodes, noOfHospitalNodes, hospitalArray, aL));
				break;
			case 2:
				output.add("\nPart C:\n");
				output.addAll(PartC.main(totalNodes, noOfHospitalNodes, hospitalArray, aL));
				break;
			case 3:
				output.add("\nPart D:\n");
				output.addAll(PartD.main(totalNodes, noOfHospitalNodes, hospitalArray, aL));
				break;
			case 4:
				// Pass the output ArrayList to output method in FileIO.java to store in txt
				// file
				try {
					FileIO.output(output);

				} catch (Exception e) {
					System.out.println("IOException:" + e.getMessage());
				}
				System.exit(0);
			}
		}
	}
}