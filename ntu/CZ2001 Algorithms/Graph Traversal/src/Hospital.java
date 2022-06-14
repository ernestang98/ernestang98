import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Hospital {

	public Hospital() {

	}

	public void generate(int n, int h) throws FileNotFoundException {

		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			list.add(i);
		}
		Collections.shuffle(list);

		List<String> data = new ArrayList<String>();
		Scanner scanner = new Scanner(new FileInputStream("roadNet.txt"));
		try {
			while (scanner.hasNextLine()) {
				data.add(scanner.nextLine());
			}
		} finally {
			scanner.close();
		}

		ArrayList<Integer> nodeList = new ArrayList<Integer>();
		for (int i = 0; i < data.size(); i++) {
			String st = (String) data.get(i);
			StringTokenizer star = new StringTokenizer(st, "[\t\r\n ]+");
			if (st.charAt(0) == '#')
				continue;
			nodeList.add(Integer.parseInt(star.nextToken().trim()));
			nodeList.add(Integer.parseInt(star.nextToken().trim()));
		}

		try {
			FileWriter fw = new FileWriter("Hospital_List.txt");
			fw.write("#" + h + "\n");
			int i = -1;
			int x = 0;
			while (x < h) {
				i+=1;
				if (!nodeList.contains(list.get(i))) {
					continue;
				}
				fw.write(String.valueOf(list.get(i)));
				fw.write("\n");
				x += 1;

			}
			fw.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
