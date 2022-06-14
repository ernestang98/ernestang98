import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PartC {

	public static ArrayList<String> main(int totalNodes, int noOfHospitalNodes, int[] hospitalArray, ArrayList<ArrayList<String>> aL_O) {

		// Store the output in ArrayList output
		ArrayList<String> output = new ArrayList<String>();

		Scanner sc = new Scanner(System.in);

		while (true) {
			//Create a clone of the original graph 
			ArrayList<ArrayList<String>> aL = new ArrayList<>(totalNodes);
			for (int i = 0; i < aL_O.size(); i++) {
				ArrayList<String> arrayListClone = (ArrayList<String>) aL_O.get(i).clone();
				aL.add(arrayListClone);
			}

			System.out.println("Please enter source node: (Enter -1 to exit part)");
			int i = sc.nextInt();
			if (i == -1) {
				break;
			} else if (i > totalNodes) {
				System.out.println("Total number of nodes exceeded. Try again.");
				continue;
			}

			// Check if the user input node is a hospital node
			if (Arrays.stream(hospitalArray).anyMatch(j -> j == i)) {
				System.out.println("Input node is a hospital node. Please enter another node!");
				continue;
			}

			// Convert hospitalArray to ArrayList
			ArrayList<Integer> hospitalList = (ArrayList<Integer>) Arrays.stream(hospitalArray).boxed()
					.collect(Collectors.toList());

			// Add the output into the output array list
			for (int a = 0; a < 2; a++) {
				String out = print(i, totalNodes, aL, a + 1);
				//Check if the node is not now longer connected to a hospital node
				if (out.equals("END"))
					break;
				output.add(out);
			}
			output.add("\n");
		}

		return output;
	}

	// generate ordinal suffixes for output
	public static String ordinal(int i) {
		String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
		switch (i % 100) {
		case 11:
		case 12:
		case 13:
			return i + "th";
		default:
			return i + suffixes[i % 10];

		}
	}

	private static String print(int sNode, int totalNodes, ArrayList<ArrayList<String>> al, int k) {

		int[] tStore = new int[totalNodes];
		int[] dStore = new int[totalNodes];

		int nearestHospital = breadthFirstSearch(sNode, tStore, dStore, totalNodes, al);

		// Check if sNode is connected to a hospital 
		if (nearestHospital == -1) {
			return "END";
		}

		// Initialize empty LinkedList nodePath
		LinkedList<Integer> nodePath = new LinkedList<>();
		int temp = nearestHospital;
		nodePath.add(temp);
		while (tStore[temp] != -1) {
			nodePath.add(tStore[temp]);
			temp = tStore[temp];
		}

		// Use StringBuilder to append the outputs into one String
		StringBuilder sb = new StringBuilder("For node " + nodePath.get(nodePath.size() - 1) + ", " + "path length to "
				+ ordinal(k) + " nearest hospital is: " + dStore[nearestHospital]);
		sb.append("\nPath is: ");

		//Append the path to the output string 
		for (int i = nodePath.size() - 1; i >= 0; i--) {
			sb.append(nodePath.get(i) + " ");
		}
		return sb.toString();
	}

	private static int breadthFirstSearch(int sNode, int[] tStore, int[] dStore, int totalNodes,
			ArrayList<ArrayList<String>> al) {

		LinkedList<Integer> q = new LinkedList<>();

		boolean[] nodesV = new boolean[totalNodes];

		for (int i = 0; i < totalNodes; i++) {
			nodesV[i] = false;
			dStore[i] = Integer.MAX_VALUE;
			tStore[i] = -1;
		}

		nodesV[sNode] = true;
		dStore[sNode] = 0;
		q.add(sNode);

		while (!q.isEmpty()) {
			int vRemove = q.remove();
			for (int i = 0; i < al.get(vRemove).size(); i++) {
				int current = Integer.parseInt(al.get(vRemove).get(i));
				if (!nodesV[current]) {
					nodesV[current] = true;
					dStore[current] = dStore[vRemove] + 1;
					tStore[current] = vRemove;
					q.add(current);
					if (al.get(vRemove).get(i).length() >= 2) {
						// Check for marking on current node: if the first 2 chars are '00' then it is a
						// hospital node
						if (al.get(vRemove).get(i).charAt(0) == '0') {
							// Remove the marking on the current hospital node since it is the nearest hospital node
							al.get(vRemove).set(i, al.get(vRemove).get(i).substring(1));
							return current;
						}
					}
				}
			}
		}
		return -1; //sNode not connected to any hospital
	}
}
