import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class PartB {

	public static ArrayList<String> main(int totalNodes, int noOfHospitalNodes, int[] hospitalArray,
			ArrayList<ArrayList<String>> aL) {

		// Store the output in ArrayList output
		ArrayList<String> output = new ArrayList<String>();

		while (true) {
			System.out.println("Please enter source node: (Enter -1 to exit part)");
			Scanner sc = new Scanner(System.in);
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

			// Add the output into the output array list
			String out = print(i, totalNodes, aL);
			//Check if the node is not now longer connected to a hospital node
			if (out.equals("END"))
				break;
			output.add(out);
		}

		return output;
	}

	private static String print(int sNode, int totalNodes, ArrayList<ArrayList<String>> al) {

		int[] tStore = new int[totalNodes];
		int[] dStore = new int[totalNodes];

		int nearestHospital = breadthFirstSearch(sNode, tStore, dStore, totalNodes, al);

		// Check if sNode & dNode are connected
		if (nearestHospital == -1) {
			System.out.println("Given source and destination are not connected");
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
		StringBuilder sb = new StringBuilder("For node " + nodePath.get(nodePath.size() - 1) + ", "
				+ "Shortest path length is: " + dStore[nearestHospital] + ", Path is: ");

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
						if (al.get(vRemove).get(i).charAt(0) == '0')
							return current;
					}
				}
			}
		}
		return -1;
	}
}
