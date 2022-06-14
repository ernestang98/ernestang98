import java.io.*;  
import java.net.*; 

public class Client {
	public static void main(String[] args) {  
		try {
			boolean debug = false;
			if (args.length == 1) {
				if (args[0] == "True") {
					debug = true;
				}
			}
			Socket soc=new Socket("192.168.34.34", 65432);  
			DataInputStream din = new DataInputStream(soc.getInputStream());
			DataOutputStream dout=new DataOutputStream(soc.getOutputStream());  
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			if (debug == true) {
				dout.writeUTF("ALG|AND|TEST-CONNECTION");
				dout.flush();
			}
			String str1 = "";
			System.out.print("Send message to RPI >> ");
			while (!str1.equals("stop")) {
				str1 = br.readLine();
				dout.writeUTF(str1);
				dout.flush();
				System.out.print("Send message to RPI >> ");
			}
			dout.close();
			soc.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}  
	}
}  

// https://www.javatpoint.com/socket-programming

