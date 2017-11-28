import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class Main {
	static String ins;
	static String addr;
	private static char[] instruction = new char[10];
	private static String address = "";
	private static String instruct = "";
	public static void main(String[] args) throws FileNotFoundException {
		fetch();
		decode();
		execute();
		mem();
		writeback();
	}
	private static void fetch() throws FileNotFoundException {
// 		readfile();//reading all instructions make a while loop for reading nstruction one by one
		BufferedReader in = null;
		String s = null;
		try {
			in = new BufferedReader(new FileReader("D:\\i.MEM"));
			s = in.readLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < 3; i++) {
			address = address + s.charAt(i);
		}
		for (int i = 4; i < s.length(); i++) {
			instruction[i - 4] = s.charAt(i);
			instruct = instruct + s.charAt(i);
		}
		System.out.println("FETCH: Fetch instruction " + instruct + " from address " + address);
	}
	private static void writeback() {
		// TODO Auto-generated method stub
		
	}

	private static void mem() {
		// TODO Auto-generated method stub
		
	}

	private static void execute() {
		// TODO Auto-generated method stub
		
	}

	private static void decode() {
		// TODO Auto-generated method stub
		
	}
	public static int binTOdecimal(String s) {
		int decimal = 0;
		int counter = 0;
		for (int i = s.length() - 1; i >= 0; i--) {
			if (s.charAt(i) == '1') {
				decimal = (int) (decimal + Math.pow(2, counter));
			}
			counter++;
		}
		return decimal;

	}

	public static int[] HexToDecimal(String s) {
		int[] binary = new int[32];
		String bin = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '0') {
				bin = bin + "0000";
			} else if (s.charAt(i) == '1') {
				bin = bin + "0001";
			} else if (s.charAt(i) == '2') {
				bin = bin + "0010";
			} else if (s.charAt(i) == '3') {
				bin = bin + "0011";
			} else if (s.charAt(i) == '4') {
				bin = bin + "0100";
			} else if (s.charAt(i) == '5') {
				bin = bin + "0101";
			} else if (s.charAt(i) == '6') {
				bin = bin + "0110";
			} else if (s.charAt(i) == '7') {
				bin = bin + "0111";
			} else if (s.charAt(i) == '8') {
				bin = bin + "1000";
			} else if (s.charAt(i) == '9') {
				bin = bin + "1001";
			} else if (s.charAt(i) == 'A') {
				bin = bin + "1010";
			} else if (s.charAt(i) == 'B') {
				bin = bin + "1011";
			} else if (s.charAt(i) == 'C') {
				bin = bin + "1100";
			} else if (s.charAt(i) == 'D') {
				bin = bin + "1101";
			} else if (s.charAt(i) == 'E') {
				bin = bin + "1110";
			} else if (s.charAt(i) == 'F') {
				bin = bin + "1111";
			}
		}
		for (int i = 0; i < bin.length(); i++) {
			binary[i] = bin.charAt(i);
		}
		return binary;

	}
	
	public void exit() {
		System.exit(0);
	}
	private static void readfile() throws FileNotFoundException {
		BufferedReader in = null;
		try {
		    in = new BufferedReader(new FileReader("//Users//snehasi//eclipse-workspace//COArmsimulator//test.mem//"));
		    String read = null;
		    while ((read = in.readLine()) != null) {
		        String[] splited = read.split("\\s+");
		        addr = splited[0];
		        ins = splited[1];
		        System.out.println("Fetch instruction " + ins + " from address " + addr );
		    }
		} catch (IOException e) {
		    System.out.println("error encountered: " + e);
		    e.printStackTrace();
		} finally {
		    try {
		        in.close();
		    } catch (Exception e) {
		    }
		}
		
	}
}
