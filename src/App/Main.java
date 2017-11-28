package App;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	private static char[] instruction = new char[10];
	private static String address = "";
	private static String instruct = "";
	private static int[] binary=new int[32];
	public static void main(String[] args) throws IOException {
		fetch();
		decode();
	}

	public static void fetch() throws IOException {
		BufferedReader in = null;
		String s = null;
		try {
//			in = new BufferedReader(new FileReader("//Users//snehasi//eclipse-workspace//COArmsimulator//test.mem//"));
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
	public static int getcond() {
		String command2="";
		command2=binary[binary.length-1-31]+binary[binary.length-1-30]+binary[binary.length-1-29]+binary[binary.length-1-28]+"";
		int num2=binTOdecimal(command2);
		return num2;
	}
	public static int getOpcode() {
		String command2="";
		command2=binary[binary.length-1-24]+binary[binary.length-1-23]+binary[binary.length-1-22]+binary[binary.length-1-21]+"";
		int num2=binTOdecimal(command2);
		return num2;
	}
	public int getlsoffset(){//getting load store offset
		String offset="";
		for(int i=31;i>19;i--){
			offset=Integer.toString(binary[i]);
		}
		int decimaloffset=binTOdecimal(offset);
		return decimaloffset;
	}
	public void decode() {
		hexTobinary(instruct.substring(2));
		String command="";
		command=Integer.toString(binary[binary.length-1-27])+Integer.toString(binary[binary.length-1-26]);
		int num=binTOdecimal(command);
		if(num==0) {
			dataProcess();
		}
		else if(num==1) {
			dataStore();
		}
		else if(num==2){
			branchCondition();
		}
	}
	public void dataProcess() {
		System.out.println("dataProcess");
		int immediate = binary[6];
		int code = getOpcode();
		String cmd1 = Integer.toString(binary[binary.length - 1 - 19])
				+ Integer.toString(binary[binary.length - 1 - 18]) + Integer.toString(binary[binary.length - 1 - 17])
				+ Integer.toString(binary[binary.length - 1 - 16]);
		int operand1=binTOdecimal(cmd1);
		String cmd2 = Integer.toString(binary[binary.length - 1 - 15])
				+ Integer.toString(binary[binary.length - 1 - 14]) + Integer.toString(binary[binary.length - 1 - 13])
				+ Integer.toString(binary[binary.length - 1 - 12]);
		int destination=binTOdecimal(cmd2);
		System.out.println(immediate+" "+code);
		if (immediate == 0) {
			String cmd3 = Integer.toString(binary[binary.length - 4])
					+ Integer.toString(binary[binary.length - 3]) + Integer.toString(binary[binary.length - 2])
					+ Integer.toString(binary[binary.length - 1]);
			int operand2=binTOdecimal(cmd3);
			if (code == 0) {
				System.out.println("Operation decoded is AND, First Operand is R"+operand1+",Second Operand is R"+operand2+",Destination Register is R"+destination);
			}
			else if(code==1) {
				System.out.println("Operation decoded is XOR, First Operand is R"+operand1+",Second Operand is R"+operand2+",Destination Register is R"+destination);
			}
			else if(code==2) {
				System.out.println("Operation decoded is SUB, First Operand is R"+operand1+",Second Operand is R"+operand2+",Destination Register is R"+destination);
			}
			else if(code==3) {
				System.out.println("Operation decoded is RSB, First Operand is R"+operand1+",Second Operand is R"+operand2+",Destination Register is R"+destination);
			}
			else if(code==4) {
				System.out.println("Operation decoded is ADD, First Operand is R"+operand1+",Second Operand is R"+operand2+",Destination Register is R"+destination);
			}
			else if(code==5) {
				System.out.println("Operation decoded is ADC, First Operand is R"+operand1+",Second Operand is R"+operand2+",Destination Register is R"+destination);
			}
			else if(code==6) {
				System.out.println("Operation decoded is SBC, First Operand is R"+operand1+",Second Operand is R"+operand2+",Destination Register is R"+destination);
			}
			else if(code==10) {
				System.out.println("Operation decoded is CMP, First Operand is R"+operand1+",Second Operand is R"+operand2+",Destination Register is R"+destination);
			}
			else if(code==11) {
				System.out.println("Operation decoded is CMN, First Operand is R"+operand1+",Second Operand is R"+operand2+",Destination Register is R"+destination);
			}
			else if(code==12) {
				System.out.println("Operation decoded is ORR, First Operand is R"+operand1+",Second Operand is R"+operand2+",Destination Register is R"+destination);
			}
			else if(code==13) {
				System.out.println("Operation decoded is MOV, First Operand is R"+operand1+",Second Operand is R"+operand2+",Destination Register is R"+destination);
			}
			else if(code==14) {
				System.out.println("Operation decoded is BIC, First Operand is R"+operand1+",Second Operand is R"+operand2+",Destination Register is R"+destination);
			}
			else if(code==15) {
				System.out.println("Operation decoded is MVN, First Operand is R"+operand1+",Second Operand is R"+operand2+",Destination Register is R"+destination);
			}
		} else {
			String cmd3 = Integer.toString(binary[binary.length - 8])
					+ Integer.toString(binary[binary.length - 7]) + Integer.toString(binary[binary.length - 6])
					+ Integer.toString(binary[binary.length - 5])+Integer.toString(binary[binary.length - 4])
					+ Integer.toString(binary[binary.length - 3]) + Integer.toString(binary[binary.length - 2])
					+ Integer.toString(binary[binary.length - 1]);
			int operand2=binTOdecimal(cmd3);
			if (code == 0) {
				System.out.println("Operation decoded is AND, First Operand is R"+operand1+",Immediate Second Operand is "+operand2+",Destination Register is R"+destination);
			}
			else if(code==1) {
				System.out.println("Operation decoded is XOR, First Operand is R"+operand1+",Immediate Second Operand is "+operand2+",Destination Register is R"+destination);
			}
			else if(code==2) {
				System.out.println("Operation decoded is SUB, First Operand is R"+operand1+",Immediate Second Operand is "+operand2+",Destination Register is R"+destination);
			}
			else if(code==3) {
				System.out.println("Operation decoded is RSB, First Operand is R"+operand1+",Immediate Second Operand is "+operand2+",Destination Register is R"+destination);
			}
			else if(code==4) {
				System.out.println("Operation decoded is ADD, First Operand is R"+operand1+",Immediate Second Operand is "+operand2+",Destination Register is R"+destination);
			}
			else if(code==5) {
				System.out.println("Operation decoded is ADC, First Operand is R"+operand1+",Immediate Second Operand is "+operand2+",Destination Register is R"+destination);
			}
			else if(code==6) {
				System.out.println("Operation decoded is SBC, First Operand is R"+operand1+",Immediate Second Operand is "+operand2+",Destination Register is R"+destination);
			}
			else if(code==10) {
				System.out.println("Operation decoded is CMP, First Operand is R"+operand1+",Immediate Second Operand is "+operand2+",Destination Register is R"+destination);
			}
			else if(code==11) {
				System.out.println("Operation decoded is CMN, First Operand is R"+operand1+",Immediate Second Operand is "+operand2+",Destination Register is R"+destination);
			}
			else if(code==12) {
				System.out.println("Operation decoded is ORR, First Operand is R"+operand1+",Immediate Second Operand is "+operand2+",Destination Register is R"+destination);
			}
			else if(code==13) {
				System.out.println("Operation decoded is MOV, First Operand is R"+operand1+",Immediate Second Operand is "+operand2+",Destination Register is R"+destination);
			}
			else if(code==14) {
				System.out.println("Operation decoded is BIC, First Operand is R"+operand1+",Immediate Second Operand is "+operand2+",Destination Register is R"+destination);
			}
			else if(code==15) {
				System.out.println("Operation decoded is MVN, First Operand is R"+operand1+",Immediate Second Operand is "+operand2+",Destination Register is R"+destination);
			}
			
		}
	}
	public void dataStore() {
		int offset=getlsoffset();
		int load=binary[11];
		//TODO FIND RL AND RD
		if(load==1){
			System.out.println("Operation is Load");
		}
		else{
			System.out.println("Operation is Store");
		}
		System.out.println("dataStore");
	}
	public void branchCondition() {
	
		System.out.println("branch condition");
		int num2=getcond();
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
	public void memory(int x,int y){

	}
	public void writeback(int x,int y){

	}
	public void execute(){

	}
	public static void hexTobinary(String s) {
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
	}
}
