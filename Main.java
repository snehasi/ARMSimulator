import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
public class Main {
	private static char[] instruction = new char[10];
	private static String address = "";
	private static String instruct = "";
	private static int[] binary = new int[32];
	private static int result=0;
	private static boolean flagEqual=false;
	private static boolean flagGreater=false;
	private static boolean flagSmaller=false;
	private static int operand1=0;
	private static int operand2=0;
	private static int destination=0;
	private static int[] R=new int[15];
	private static int iii=0;
	/*main memory->we are taking the indices from 0xi as i giving them contiguous allocations in our implementaion now we have to increase R[15] by 4*/
	private static long[] memory=new long[8000];
    public static void main(String[] args) throws IOException {
    	int count=initialise();
    	for(int i=0;i<count;i+=4) {
			fetch(i);
			decode();
			execute();
			writeback();
		}
//		execute();
	}
	public static int initialise()throws IOException{//Storing instructions from mem file in our memory
		BufferedReader in = null;
		int location=0;
		String s = null;
		int count=0;
		try {
			in = new BufferedReader(new FileReader("./files/simple_add.mem"));
			while((s=in.readLine())!=null){
				count++;
				String[] s2=s.split(" ");
				location=Integer.parseInt(s2[0].substring(2),16);
				//System.out.println("loc"+location);
				memory[location]=Integer.parseUnsignedInt(s2[1].substring(2),16);
				//System.out.println("xxx"+memory[location]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			if (in != null)
				in.close();
		}
		return 4*count;
	}
	public static void fetch(int location) throws IOException {
		//int location=Integer.parseInt(x,16);
		long y=memory[location];
		address =Integer.toString(location);
		//System.out.println("testing"+y);
		address=Integer.toHexString(Integer.parseInt(address));
		address="0x"+address;
		
		//System.out.println(y);
		instruct =Long.toHexString(y).substring(8);
		instruct="0x"+instruct;
		System.out.println("FETCH: Fetch instruction " + instruct + " from address " + address);
	}
	public static int getcond() {
		String command2 = "";
		command2 = binary[binary.length - 1 - 31] + binary[binary.length - 1 - 30] + binary[binary.length - 1 - 29]
				+ binary[binary.length - 1 - 28] + "";
		int num2 = binTOdecimal(command2);
		return num2;
	}
	public static int getOpcode() {
		String command2 = "";
		command2 = Integer.toString(binary[binary.length - 1 - 24]) + Integer.toString(binary[binary.length - 1 - 23]) + Integer.toString(binary[binary.length - 1 - 22])
				+ Integer.toString(binary[binary.length - 1 - 21]);
		int num2 = binTOdecimal(command2);
		return num2;
	}

	public static void decode() {
		hexTobinary(instruct.substring(2));
		String command = "";
		String offset = "";
		command = Integer.toString(binary[binary.length - 1 - 27]) + Integer.toString(binary[binary.length - 1 - 26]);
		int num = binTOdecimal(command);
		System.out.println("DECODE starts");
		if (num == 0) {
			dataProcess();
		} else if (num == 1) {
			dataStore();
		} else if (num == 2) {
			branchCondition();
		}
		else if (num == 3) {
			System.out.println("Exit the program.");
			resetval();
			System.exit(0);
		}
	}
	public static int getlsoffset(){//getting load store offset
		String offset="";
		for(int i=31;i>19;i--){
			offset=Integer.toString(binary[i]);
		}
		int decimaloffset=binTOdecimal(offset);
		return decimaloffset;
	}

	public static void dataProcess() {
		System.out.println("dataProcess");
		int immediate = binary[6];
		int code = getOpcode();
		String cmd1 = Integer.toString(binary[binary.length - 1 - 19])
				+ Integer.toString(binary[binary.length - 1 - 18]) + Integer.toString(binary[binary.length - 1 - 17])
				+ Integer.toString(binary[binary.length - 1 - 16]);
		operand1=binTOdecimal(cmd1);
		String cmd2 = Integer.toString(binary[binary.length - 1 - 15])
				+ Integer.toString(binary[binary.length - 1 - 14]) + Integer.toString(binary[binary.length - 1 - 13])
				+ Integer.toString(binary[binary.length - 1 - 12]);
		destination=binTOdecimal(cmd2);
		//System.out.println(immediate+" "+code);
		if (immediate == 0) {
			String cmd3 = Integer.toString(binary[binary.length - 4])
					+ Integer.toString(binary[binary.length - 3]) + Integer.toString(binary[binary.length - 2])
					+ Integer.toString(binary[binary.length - 1]);
			operand2=binTOdecimal(cmd3);
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
			operand2=binTOdecimal(cmd3);
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

	public static void dataStore() {
		System.out.println("dataStore");
		int offset=getlsoffset();
		int load=binary[11];
		int lsoffset=getlsoffset();
		String oset="";
		if(lsoffset<=15){
			oset="R"+Integer.toString(lsoffset);
		}
		else
			oset=Integer.toString(lsoffset);
		String rn="Source Register is R";
		String rd="Destination Register is R";
		String x="",y="";
		for(int i=12;i<16;i++){
			x+=Integer.toString(i);
			y+=Integer.toString(i+4);
		}
		int x1=binTOdecimal(x);
		int y1=binTOdecimal(y);
		rn=rn+Integer.toString(x1);
		rd=rd+Integer.toString(y1);
		if(load==1){
			System.out.println("Operation is Load"+" "+rn+" "+rd+" offset is "+oset);
		}
		else{
			System.out.println("Operation is Store"+" "+rn+" "+rd+" offset is "+oset);
		}
		setMemory(lsoffset,x1,y1);
	}
	public static void setMemory(int off,int source,int destination){
		if(off==0){

		}else{

		}
	}
	public static void branchCondition() {
		System.out.println("branch condition");
		int num2 = getcond();
		if (num2 == 0) {
			System.out.println("Operation decoded is BEQ\n");
		} else if (num2 == 1) {
			System.out.println("Operation decoded is BNE\n");
		} else if (num2 == 10) {
			System.out.println("Operation decoded is BGE\n");
		} else if (num2 == 11) {
			System.out.println("Operation decoded is BLT\n");
		} else if (num2 == 12) {
			System.out.println("Operation decoded is BGT\n");
		} else if (num2 == 13) {
			System.out.println("Operation decoded is BLE\n");
		} else if (num2 == 14) {
			System.out.println("Operation decoded is BAL\n");
		} else if (num2 == 15) {
			System.out.println("Operation decoded is B\n");
		} 
	}
	public static void execute() {
		hexTobinary(instruct.substring(2));
		String command = "";
		String offset = "";
		command = Integer.toString(binary[binary.length - 1 - 27]) + Integer.toString(binary[binary.length - 1 - 26]);
		int num = binTOdecimal(command);
		int code=getOpcode();
		if (num == 0) {
			int immidiate=binary[6];
			if(immidiate==0) {
				if (code == 0) {
					result=(R[operand1])&(R[operand2]);
					System.out.println("Execute: And "+ R[operand1]+" "+R[operand2]);
				}
				else if(code==1) {
					result=(R[operand1])^(R[operand2]);
					System.out.println("Execute: XOR "+ R[operand1]+" "+R[operand2]);
				}
				else if(code==2) {
					result=R[operand1]-R[operand2];
					System.out.println("Execute: SUB "+ R[operand1]+" "+R[operand2]);
				}
				else if(code==3) {
					result=R[operand2]-R[operand1];
					System.out.println("Execute: RSB "+ R[operand1]+" "+R[operand2]);
				}
				else if(code==4) {
					result=R[operand1]+R[operand2];
					System.out.println("Execute: ADD "+ R[operand1]+" "+R[operand2]);
				}
				else if(code==5) {
					result=R[operand1]+R[operand2];
					System.out.println("Execute: ADC "+ R[operand1]+" "+R[operand2]);
				}
				else if(code==6) {
					result=R[operand1]-R[operand2]-1;
					System.out.println("Execute: SB "+ R[operand1]+" "+R[operand2]);
				}
				else if(code==10) {
					if(R[operand1]==R[operand2]) {
						result=0;
						flagEqual=true;
					}
					else if(R[operand1]>R[operand2]) {
						result=1;
						flagGreater=true;
					}
					else {
						result=-1;
						flagSmaller=true;
					}
					System.out.println("Execute: CMP "+ R[operand1]+" "+R[operand2]);
				}
				else if(code==11) {
					if(R[operand1]==R[operand2]) {
						result=0;
						flagEqual=true;
					}
					else if(R[operand1]>R[operand2]) {
						result=-1;
						flagSmaller=true;
					}
					else {
						result=1;
						flagGreater=true;
					}
					System.out.println("Execute: CMN "+ R[operand1]+" "+R[operand2]);
				}
				else if(code==12) {
					result=(R[operand1])|(R[operand2]);
					System.out.println("Execute: ORR "+ R[operand1]+" "+R[operand2]);
				}
				else if(code==13) {
					result=R[operand2];
					System.out.println("Execute: MOV "+R[operand2]);
				}
				else if(code==14) {
					result=(R[operand1])&(~(R[operand2]));
					System.out.println("Execute: BIC "+ R[operand1]+" "+R[operand2]);
				}
				else if(code==15) {
					result=~(R[operand2]);
					System.out.println("Execute: MVN "+R[operand2]);
				}
			}
			else {
				if (code == 0) {
					result=(R[operand1])&(operand2);
					System.out.println("Execute: AND Immediate "+ R[operand1]+" "+operand2);
				}
				else if(code==1) {
					result=(R[operand1])^(operand2);
					System.out.println("Execute: XOR Immediate "+ R[operand1]+" "+operand2);
				}
				else if(code==2) {
					result=R[operand1]-operand2;
					System.out.println("Execute: SUB Immediate "+ R[operand1]+" "+operand2);
				}
				else if(code==3) {
					result=R[operand2]-operand1;
					System.out.println("Execute: RSB Immediate "+ operand1+" "+R[operand2]);
				}
				else if(code==4) {
					result=R[operand1]+operand2;
					System.out.println("Execute: ADD Immediate"+ R[operand1]+" "+operand2);
				}
				else if(code==5) {
					result=R[operand1]+operand2;
					System.out.println("Execute: ADC  Immediate"+ R[operand1]+" "+operand2);
				}
				else if(code==6) {
					result=R[operand1]-operand2-1;
					System.out.println("Execute: SB Immediate"+ R[operand1]+" "+operand2);
				}
				else if(code==10) {
					if(R[operand1]==operand2) {
						result=0;
						flagEqual=true;
					}
					else if(R[operand1]>operand2) {
						result=1;
						flagGreater=true;
					}
					else {
						result=-1;
						flagSmaller=true;
					}
					System.out.println("Execute: CMP Immediate"+ R[operand1]+" "+operand2);
				}
				else if(code==11) {
					if(operand1==R[operand2]) {
						result=0;
						flagEqual=true;
					}
					else if(operand1>R[operand2]) {
						result=-1;
						flagSmaller=true;
					}
					else {
						result=1;
						flagGreater=true;
					}
					System.out.println("Execute: CMN Immediate"+ operand1+" "+R[operand2]);
				}
				else if(code==12) {
					result=(R[operand1])|(operand2);
					System.out.println("Execute: ORR Immediate"+ R[operand1]+" "+operand2);
				}
				else if(code==13) {
					result=operand2;
					System.out.println("Execute: MOV Immediate"+operand2);
				}
				else if(code==14) {
					result=(R[operand1])&(~(operand2));
					System.out.println("Execute: BIC Immediate"+ R[operand1]+" "+operand2);
				}
				else if(code==15) {
					result=~(operand2);
					System.out.println("Execute: MVN Immediate"+operand2);
				}
				
			}
		} else if (num == 1) {
			System.out.println("No Execution");
		} else if (num == 2) {
			int[] sined=new int[32];
			for(int i=8;i<sined.length;i++) {
				sined[i]=binary[i];
			}
			if(sined[8]==1) {
				for(int i=0;i<8;i++) {
					sined[i]=1;
				}
			}
			else {
				for(int i=0;i<8;i++) {
					sined[i]=0;
				}
			}
			String sinedPadded="";
			for(int i=0;i<sined.length;i++) {
				sinedPadded+=sined[i];
			}
			int counter = 0;
			counter=binTOdecimal(sinedPadded);
			System.out.println(counter+"offset");
			if (code == 0) {
				if (flagEqual == true) {
					R[15] =counter ;
				}
				System.out.println("EXECUTE: BEQ offset :");
			} else if (code == 1) {
				if (flagEqual == false) {
					R[15] = counter;
				}
				System.out.println("EXECUTE: BNE offset : " + counter);
			} else if (code == 10) {
				if ((flagSmaller == false) || (flagEqual == true))
					R[15] = counter;
				System.out.println("EXECUTE: BGE offset : " + counter);
			} else if (code == 11) {
				if ((flagSmaller == true) && (flagEqual == false))
					R[15] = counter;
				System.out.println("EXECUTE: BLT offset :" + counter);
			} else if (code == 12) {
				if ((flagSmaller == false) && (flagEqual == false))
					R[15] = counter;
				System.out.println("EXECUTE: BGT offset : " + counter);
			} else if (code == 13) {
				if ((flagGreater == true) || (flagEqual == true)) {
					R[15] = counter;
				}
				System.out.println("EXECUTE: BLE offset : " + counter);
			} else if (code == 14) {
				R[15] =counter;
				System.out.println("EXECUTE: BAL offset : " + counter);
			}
		}	
	}
	public static void writeback() {
		System.out.println("WRITEBACK starts");
		hexTobinary(instruct.substring(2));
		String command3 = "";
		String offset3 = "";
		command3 = Integer.toString(binary[binary.length - 1 - 27]) + Integer.toString(binary[binary.length - 1 - 26]);
		int num3 = binTOdecimal(command3);
		int code3=getOpcode();
		if(((num3==0)&&(code3!=10))||(num3==1)&&(code3==25)) {			
				R[destination]=result;
				System.out.println("Write "+result+" to "+destination);	
		}
		else if((num3==1)&&(code3==24)) {			
			iii=R[destination];
			System.out.println("Write "+iii+" to memory.");
			
		}
		else if(num3==2||((num3==0)&&(code3==10))) {
			System.out.println("No writeback.");
		}
		else if(num3==3) {
			System.out.println("Exit the program.");
			resetval();
			System.exit(0);
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

	public static void hexTobinary(String s) {
		String bin = "";
		//System.out.println(s);
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
			} else if (s.charAt(i) == 'A'|| s.charAt(i)=='a') {
				bin = bin + "1010";
			} else if (s.charAt(i) == 'B'|| s.charAt(i)=='b') {
				bin = bin + "1011";
			} else if (s.charAt(i) == 'C'|| s.charAt(i)=='c') {
				bin = bin + "1100";
			} else if (s.charAt(i) == 'D'|| s.charAt(i)=='d') {
				bin = bin + "1101";
			} else if (s.charAt(i) == 'E'|| s.charAt(i)=='e') {
				bin = bin + "1110";
			} else if (s.charAt(i) == 'F'|| s.charAt(i)=='f') {
				bin = bin + "1111";
			}
		}
		//System.out.println(bin);
		for (int i = 0; i < bin.length(); i++) {
			binary[i] = bin.charAt(i) - '0';
		}
	}
	
	public static void resetval() {
		for(int i=0;i<10;i++) {
			instruction[i]='0';
		}
		for(int j=0;j<32;j++) {
			binary[j]=0;
		}
		for(int i=0;i<15;i++) {
			R[i]=0;
		}
		operand1=operand2=destination=iii=result=0;
		address=instruct="";
		flagEqual=flagGreater=flagSmaller=false;
		for(int i=0;i<8000;i++) {
			memory[i]=0;
		}
		
		
	}
}