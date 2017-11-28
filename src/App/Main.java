import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	private static char[] instruction = new char[10];
	private static String address = "";
	private static String instruct = "";

	public static void main(String[] args) throws IOException {
		fetch();
		decode();
	}

	public static void fetch() throws IOException {
		BufferedReader in = null;
		String s = null;
		try {
			in = new BufferedReader(new FileReader("//Users//snehasi//eclipse-workspace//COArmsimulator//test.mem//"));
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
		//hexTobinary(instruct.substring(2,instruct.length())+"checkkk");
	}

	public static void decode() {
		int[] binary=hexTobinary(instruct.substring(2));
		String command="";
		String command2="";
		command=binary[binary.length-1-27]+""+binary[binary.length-1-26]+"";
		//System.out.println(command+" cc ");
		command2=binary[binary.length-1-31]+binary[binary.length-1-30]+binary[binary.length-1-29]+binary[binary.length-1-28]+"";
		int num=binTOdecimal(command);
//		for(int i=0;i<binary.length;i++) {
//			System.out.println(binary[i] +"checking array");
//		}
		//System.out.println(binary[binary.length-1-27]+" "+binary[binary.length-1-26]+" test ");
		int num2=binTOdecimal(command2);
		//System.out.println(num+" num");
		if(num==0) {
			dataProcess();
		}
		else if(num==1) {
			dataStore();
		}
		else if(num==2){
			//System.out.println("ooo");
			branchCondition(num2);
		}
		
		
	}
	public static void dataProcess() {
		System.out.println("dataProcess");
	}
	public static void dataStore() {
		System.out.println("dataStore");
	}
	public static void branchCondition(int num2) {
		System.out.println("branch condition");
		if(num2==0) {
			System.out.println("Operation is BEQ\n");
		}
		else if(num2==1) {
			System.out.println("Operation is BNE\n");
		}
		else if(num2==11) {
			System.out.println("Operation is BLT\n");
		}
		else if(num2==13) {
			System.out.println("Operation is BLE\n");
		}
		else if(num2==12) {
			System.out.println("Operation is BGT\n");
		}
		else if(num2==10) {
			System.out.println("Operation is BGE\n");
		}
		else if(num2==14) {
			System.out.println("Operation is BAL\n");
		}
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

	public static int[] hexTobinary(String s) {
		int[] binary = new int[32];
		String bin = "";
		System.out.println(s);
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
		System.out.println(bin);
		for (int i = 0; i < bin.length(); i++) {
			binary[i] = bin.charAt(i)-'0';
		}
		return binary;

	}
}