package App;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class Main {
	private static char[] instruction = new char[10];
	private static String address = "";
	private static String instruct = "";
	private static int[] binary = new int[32];
	private static long result = 0;
	private static boolean flagEqual = false;
	private static boolean flagGreater = false;
	private static boolean flagSmaller = false;
	private static long operand1 = 0;
	private static long operand2 = 0;
	private static long destination = 0;
	private static long[] R = new long[16];
	private static long iii = 0;
	private static long[] memory = new long[8000];
	private static long[] mainmemory=new long[8000];
	/*
	 * main memory->we are taking the indices from 0xi as i giving them contiguous
	 * allocations in our implementaion now we have to increase R[15] by 4
	 */

	public static void main(String[] args) throws IOException {
		int count = initialise();
		for (int i = 0; i < count; i += 4) {
			if (i == R[15]) {
				fetch(i);
				R[15] += 4;
				decode();
				execute();
				Memory();
				writeback();
				System.out.print("[");
				for (int j = 0; j < 15; j++) {
					System.out.print(R[j] + ", ");
				}
				System.out.print(R[15]);
				System.out.println("]");
				System.out.println();
			}
			if (R[15] <= i) {
				i = 0;
			}
		}
	}

	public static int initialise() throws IOException {// Storing instructions from mem file in our memory
		BufferedReader in = null;
		int location = 0;
		String s = null;
		int count = 0;
		try {
			in = new BufferedReader(new FileReader("./files/simple_add.mem"));
			while ((s = in.readLine()) != null) {
				count++;
				String[] s2 = s.split(" ");
				location = Integer.parseInt(s2[0].substring(2), 16);
				memory[location] = Integer.parseUnsignedInt(s2[1].substring(2), 16);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				in.close();
		}
		return 4 * count;
	}

	public static void fetch(int location) throws IOException {
		long y = memory[location];
		// System.out.println(y);
		address = Integer.toString(location);
		address = Integer.toHexString(Integer.parseInt(address));
		address = "0x" + address;
		instruct = Long.toHexString(y).substring(8);
		instruct = "0x" + instruct;
		// System.out.println(instruct);
		System.out.println("FETCH: Fetch instruction " + instruct + " from address " + address);
	}

	public static long getcond() {
		String command2 = "";
		command2 = Integer.toString(binary[binary.length - 1 - 31]) + Integer.toString(binary[binary.length - 1 - 30])
				+ Integer.toString(binary[binary.length - 1 - 29]) + Integer.toString(binary[binary.length - 1 - 28])
				+ "";
		long num2 = binTOdecimal(command2);
		return num2;
	}

	public static long getOpcode() {
		String command2 = "";
		command2 = Integer.toString(binary[binary.length - 1 - 24]) + Integer.toString(binary[binary.length - 1 - 23])
				+ Integer.toString(binary[binary.length - 1 - 22]) + Integer.toString(binary[binary.length - 1 - 21]);
		long num2 = binTOdecimal(command2);
		return num2;
	}

	public static void decode() {
		System.out.print("Decode starts :");
		hexTobinary(instruct.substring(2));
		String command = "";
		String offset = "";
		command = Integer.toString(binary[binary.length - 1 - 27]) + Integer.toString(binary[binary.length - 1 - 26]);
		long num = binTOdecimal(command);
		if (num == 0) {
			dataProcess();
		} else if (num == 1) {
			dataStore();
		} else if (num == 2) {
			branchCondition();
		} else if (num == 3) {
			System.out.println("Exit the program.");
			resetval();
			System.exit(0);
		}
	}
	public static void dataProcess() {
		System.out.println(" dataProcess");
		int immediate = binary[6];
		long code = getOpcode();
		String cmd1 = Integer.toString(binary[binary.length - 1 - 19])
				+ Integer.toString(binary[binary.length - 1 - 18]) + Integer.toString(binary[binary.length - 1 - 17])
				+ Integer.toString(binary[binary.length - 1 - 16]);
		operand1 = binTOdecimal(cmd1);
		String cmd2 = Integer.toString(binary[binary.length - 1 - 15])
				+ Integer.toString(binary[binary.length - 1 - 14]) + Integer.toString(binary[binary.length - 1 - 13])
				+ Integer.toString(binary[binary.length - 1 - 12]);
		destination = binTOdecimal(cmd2);
		// System.out.println(immediate + " " + code);
		if (immediate == 0) {
			String cmd3 = Integer.toString(binary[binary.length - 4]) + Integer.toString(binary[binary.length - 3])
					+ Integer.toString(binary[binary.length - 2]) + Integer.toString(binary[binary.length - 1]);
			operand2 = binTOdecimal(cmd3);
			if (code == 0) {
				System.out.println("Operation decoded is AND, First Operand is R" + operand1 + ",Second Operand is R"
						+ operand2 + ",Destination Register is R" + destination);
			} else if (code == 1) {
				System.out.println("Operation decoded is XOR, First Operand is R" + operand1 + ",Second Operand is R"
						+ operand2 + ",Destination Register is R" + destination);
			} else if (code == 2) {
				System.out.println("Operation decoded is SUB, First Operand is R" + operand1 + ",Second Operand is R"
						+ operand2 + ",Destination Register is R" + destination);
			} else if (code == 3) {
				System.out.println("Operation decoded is RSB, First Operand is R" + operand1 + ",Second Operand is R"
						+ operand2 + ",Destination Register is R" + destination);
			} else if (code == 4) {
				System.out.println("Operation decoded is ADD, First Operand is R" + operand1 + ",Second Operand is R"
						+ operand2 + ",Destination Register is R" + destination);
			} else if (code == 5) {
				System.out.println("Operation decoded is ADC, First Operand is R" + operand1 + ",Second Operand is R"
						+ operand2 + ",Destination Register is R" + destination);
			} else if (code == 6) {
				System.out.println("Operation decoded is SBC, First Operand is R" + operand1 + ",Second Operand is R"
						+ operand2 + ",Destination Register is R" + destination);
			} else if (code == 10) {
				System.out.println("Operation decoded is CMP, First Operand is R" + operand1 + ",Second Operand is R"
						+ operand2 + ",Destination Register is R" + destination);
			} else if (code == 11) {
				System.out.println("Operation decoded is CMN, First Operand is R" + operand1 + ",Second Operand is R"
						+ operand2 + ",Destination Register is R" + destination);
			} else if (code == 12) {
				System.out.println("Operation decoded is ORR, First Operand is R" + operand1 + ",Second Operand is R"
						+ operand2 + ",Destination Register is R" + destination);
			} else if (code == 13) {
				System.out.println("Operation decoded is MOV, First Operand is R" + operand1 + ",Second Operand is R"
						+ operand2 + ",Destination Register is R" + destination);
			} else if (code == 14) {
				System.out.println("Operation decoded is BIC, First Operand is R" + operand1 + ",Second Operand is R"
						+ operand2 + ",Destination Register is R" + destination);
			} else if (code == 15) {
				System.out.println("Operation decoded is MVN, First Operand is R" + operand1 + ",Second Operand is R"
						+ operand2 + ",Destination Register is R" + destination);
			}
		} else {
			String cmd3 = Integer.toString(binary[binary.length - 8]) + Integer.toString(binary[binary.length - 7])
					+ Integer.toString(binary[binary.length - 6]) + Integer.toString(binary[binary.length - 5])
					+ Integer.toString(binary[binary.length - 4]) + Integer.toString(binary[binary.length - 3])
					+ Integer.toString(binary[binary.length - 2]) + Integer.toString(binary[binary.length - 1]);
			operand2 = binTOdecimal(cmd3);
			if (code == 0) {
				System.out.println("Operation decoded is AND, First Operand is R" + operand1
						+ ",Immediate Second Operand is " + operand2 + ",Destination Register is R" + destination);
			} else if (code == 1) {
				System.out.println("Operation decoded is XOR, First Operand is R" + operand1
						+ ",Immediate Second Operand is " + operand2 + ",Destination Register is R" + destination);
			} else if (code == 2) {
				System.out.println("Operation decoded is SUB, First Operand is R" + operand1
						+ ",Immediate Second Operand is " + operand2 + ",Destination Register is R" + destination);
			} else if (code == 3) {
				System.out.println("Operation decoded is RSB, First Operand is R" + operand1
						+ ",Immediate Second Operand is " + operand2 + ",Destination Register is R" + destination);
			} else if (code == 4) {
				System.out.println("Operation decoded is ADD, First Operand is R" + operand1
						+ ",Immediate Second Operand is " + operand2 + ",Destination Register is R" + destination);
			} else if (code == 5) {
				System.out.println("Operation decoded is ADC, First Operand is R" + operand1
						+ ",Immediate Second Operand is " + operand2 + ",Destination Register is R" + destination);
			} else if (code == 6) {
				System.out.println("Operation decoded is SBC, First Operand is R" + operand1
						+ ",Immediate Second Operand is " + operand2 + ",Destination Register is R" + destination);
			} else if (code == 10) {
				System.out.println("Operation decoded is CMP, First Operand is R" + operand1
						+ ",Immediate Second Operand is " + operand2 + ",Destination Register is R" + destination);
			} else if (code == 11) {
				System.out.println("Operation decoded is CMN, First Operand is R" + operand1
						+ ",Immediate Second Operand is " + operand2 + ",Destination Register is R" + destination);
			} else if (code == 12) {
				System.out.println("Operation decoded is ORR, First Operand is R" + operand1
						+ ",Immediate Second Operand is " + operand2 + ",Destination Register is R" + destination);
			} else if (code == 13) {
				System.out.println("Operation decoded is MOV, First Operand is R" + operand1
						+ ",Immediate Second Operand is " + operand2 + ",Destination Register is R" + destination);
			} else if (code == 14) {
				System.out.println("Operation decoded is BIC, First Operand is R" + operand1
						+ ",Immediate Second Operand is " + operand2 + ",Destination Register is R" + destination);
			} else if (code == 15) {
				System.out.println("Operation decoded is MVN, First Operand is R" + operand1
						+ ",Immediate Second Operand is " + operand2 + ",Destination Register is R" + destination);
			}

		}
	}

	public static void dataStore() {
		System.out.println("dataStore");
		long offset = getlsoffset();
		int load = binary[11];
		long lsoffset = getlsoffset();
		String oset = "";
		if (lsoffset <= 15) {
			oset = "R" + Long.toString(lsoffset);
		} else
			oset = Long.toString(lsoffset);
		String rn = "Source Register is R";
		String rd = "Destination Register is R";
		String x = "", y = "";
		for (int i = 12; i < 16; i++) {
			x += Integer.toString(binary[i]);
			y += Integer.toString(binary[i + 4]);
		}
		long x1 = binTOdecimal(x);
		long y1 = binTOdecimal(y);
		rn = rn + Long.toString(x1);
		rd = rd + Long.toString(y1);
		if (load == 1) {
			System.out.println("Operation is Load" + " " + rn + " " + rd + " offset is " + oset);
		} else {
			System.out.println("Operation is Store" + " " + rn + " " + rd + " offset is " + oset);
		}
		// setMemory(lsoffset,x1,y1,load);
	}

	public static long getMemory(int x) {
		String hex = Integer.toHexString(x);
		int location = Integer.parseUnsignedInt(hex, 16);
		return mainmemory[location];
	}
    public static long getlsoffset() {// getting load store offset
        String offset = "";
        for (int i = 20; i < 32; i++) {
            offset += Integer.toString(binary[i]);
            //System.out.print(binary[i]);
        }
        //System.out.println();
        long decimaloffset = Integer.parseUnsignedInt(offset,2);
        //System.out.println(decimaloffset);
        return decimaloffset;
    }
    public static void Memory() {
//	    String s="11100101100000100001001111001000";
//	    for(int i=0;i<s.length();i++){
//	        binary[i]=Integer.parseInt(s.substring(i,i+1));
//        }
//        R[1]=44;
//        R[2]=565;
       // mainmemory[44+(968/4)]=565;
		String command = "";
		command = Integer.toString(binary[binary.length - 1 - 27]) + Integer.toString(binary[binary.length - 1 - 26]);
		long num = binTOdecimal(command);
		if (num == 1) {
            System.out.print("Memory :");
			long off = getlsoffset();
			String x = "", y = "";
			for (int i = 12; i < 16; i++) {
				x += Integer.toString(binary[i]);//source
				y += Integer.toString(binary[i + 4]);//destination
			}
			long source = binTOdecimal(x);
			long destination = binTOdecimal(y);
			int load = binary[11];
			if(load==1)
                System.out.println(" Command is Load");
			else
                System.out.println(" Command is Store");
			if (off == 0) {
				if (load == 1) {
					long set = getMemory((int) R[(int) source]);
					R[(int) destination] = (int) set;
					System.out.println("Source Memory :"+(int)set+" Destination Register R"+(int)destination+" : "+R[(int)destination]);
				} else {
                    mainmemory[(int) R[(int) destination]] = R[(int) source];
                    System.out.println("Source Register R"+source+" : "+R[(int)source]+" Destination Memory "+(int) R[(int) destination]+" : "+mainmemory[(int) R[(int) destination]]);
                }
			} else {
				if (load == 1) {
					int temp = (int) R[(int) source];
					temp += off / 4;
					R[(int) destination] = (int) mainmemory[temp];
                    System.out.println("Source Memory :"+(int)mainmemory[temp]+" Destination Register R"+(int)destination+" : "+R[(int)destination]);
				} else {
					int temp = (int) R[(int) destination];
					temp += off / 4;
					mainmemory[temp] = R[(int) source];
                    System.out.println("Source Register R"+source+" : "+R[(int)source]+" Destination Memory "+temp+" : "+mainmemory[temp]);
				}
			}
		} else {
			System.out.println("No memory operation");
		}
	}

	public static void branchCondition() {
		System.out.println("branch condition");
		long num2 = getcond();
		if (num2 == 0) {
			System.out.println("Operation decoded is BEQ");
		} else if (num2 == 1) {
			System.out.println("Operation decoded is BNE");
		} else if (num2 == 10) {
			System.out.println("Operation decoded is BGE");
		} else if (num2 == 11) {
			System.out.println("Operation decoded is BLT");
		} else if (num2 == 12) {
			System.out.println("Operation decoded is BGT");
		} else if (num2 == 13) {
			System.out.println("Operation decoded is BLE");
		} else if (num2 == 14) {
			System.out.println("Operation decoded is BAL");
		} else if (num2 == 15) {
			System.out.println("Operation decoded is B");
		}
	}

	public static void execute() {
		hexTobinary(instruct.substring(2));
		String command = "";
		command = Integer.toString(binary[binary.length - 1 - 27]) + Integer.toString(binary[binary.length - 1 - 26]);
		long num = binTOdecimal(command);
		long code = getOpcode();
		if (num == 0) {
			int immidiate = binary[6];
			if (immidiate == 0) {
				if (code == 0) {
					result = (R[(int) operand1]) & (R[(int) operand2]);
					System.out.println("Execute: And " + R[(int) operand1] + " " + R[(int) operand2]);
				} else if (code == 1) {
					result = (R[(int) operand1]) ^ (R[(int) operand2]);
					System.out.println("Execute: XOR " + R[(int) operand1] + " " + R[(int) operand2]);
				} else if (code == 2) {
					result = R[(int) operand1] - R[(int) operand2];
					System.out.println("Execute: SUB " + R[(int) operand1] + " " + R[(int) operand2]);
				} else if (code == 3) {
					result = R[(int) operand2] - R[(int) operand1];
					System.out.println("Execute: RSB " + R[(int) operand1] + " " + R[(int) operand2]);
				} else if (code == 4) {
					result = R[(int) operand1] + R[(int) operand2];
					System.out.println("Execute: ADD " + R[(int) operand1] + " " + R[(int) operand2]);
				} else if (code == 5) {
					result = R[(int) operand1] + R[(int) operand2];
					System.out.println("Execute: ADC " + R[(int) operand1] + " " + R[(int) operand2]);
				} else if (code == 6) {
					result = R[(int) operand1] - R[(int) operand2] - 1;
					System.out.println("Execute: SB " + R[(int) operand1] + " " + R[(int) operand2]);
				} else if (code == 10) {
					if (R[(int) operand1] == R[(int) operand2]) {
						result = 0;
						flagEqual = true;
					} else if (R[(int) operand1] > R[(int) operand2]) {
						result = 1;
						flagGreater = true;
					} else {
						result = -1;
						flagSmaller = true;
					}
					System.out.println("Execute: CMP " + R[(int) operand1] + " " + R[(int) operand2]);
				} else if (code == 11) {
					if (R[(int) operand1] == R[(int) operand2]) {
						result = 0;
						flagEqual = true;
					} else if (R[(int) operand1] > R[(int) operand2]) {
						result = -1;
						flagSmaller = true;
					} else {
						result = 1;
						flagGreater = true;
					}
					System.out.println("Execute: CMN " + R[(int) operand1] + " " + R[(int) operand2]);
				} else if (code == 12) {
					result = (R[(int) operand1]) | (R[(int) operand2]);
					System.out.println("Execute: ORR " + R[(int) operand1] + " " + R[(int) operand2]);
				} else if (code == 13) {
					result = R[(int) operand2];
					System.out.println("Execute: MOV " + R[(int) operand2]);
				} else if (code == 14) {
					result = (R[(int) operand1]) & (~(R[(int) operand2]));
					System.out.println("Execute: BIC " + R[(int) operand1] + " " + R[(int) operand2]);
				} else if (code == 15) {
					result = ~(R[(int) operand2]);
					System.out.println("Execute: MVN " + R[(int) operand2]);
				}
			} else {
				if (code == 0) {
					result = (int) ((R[(int) operand1]) & (operand2));
					System.out.println("Execute: AND Immediate " + R[(int) operand1] + " " + operand2);
				} else if (code == 1) {
					result = (int) ((R[(int) operand1]) ^ (operand2));
					System.out.println("Execute: XOR Immediate " + R[(int) operand1] + " " + operand2);
				} else if (code == 2) {
					result = (int) (R[(int) operand1] - operand2);
					System.out.println("Execute: SUB Immediate " + R[(int) operand1] + " " + operand2);
				} else if (code == 3) {
					result = (int) (R[(int) operand2] - operand1);
					System.out.println("Execute: RSB Immediate " + operand1 + " " + R[(int) operand2]);
				} else if (code == 4) {
					result = (int) (R[(int) operand1] + operand2);
					System.out.println("Execute: ADD Immediate" + R[(int) operand1] + " " + operand2);
				} else if (code == 5) {
					result = (int) (R[(int) operand1] + operand2);
					System.out.println("Execute: ADC  Immediate" + R[(int) operand1] + " " + operand2);
				} else if (code == 6) {
					result = (int) (R[(int) operand1] - operand2 - 1);
					System.out.println("Execute: SB Immediate" + R[(int) operand1] + " " + operand2);
				} else if (code == 10) {
					if (R[(int) operand1] == operand2) {
						result = 0;
						flagEqual = true;
					} else if (R[(int) operand1] > operand2) {
						result = 1;
						flagGreater = true;
					} else {
						result = -1;
						flagSmaller = true;
					}
					System.out.println("Execute: CMP Immediate" + R[(int) operand1] + " " + operand2);
				} else if (code == 11) {
					if (operand1 == R[(int) operand2]) {
						result = 0;
						flagEqual = true;
					} else if (operand1 > R[(int) operand2]) {
						result = -1;
						flagSmaller = true;
					} else {
						result = 1;
						flagGreater = true;
					}
					System.out.println("Execute: CMN Immediate" + operand1 + " " + R[(int) operand2]);
				} else if (code == 12) {
					result = (int) ((R[(int) operand1]) | (operand2));
					System.out.println("Execute: ORR Immediate" + R[(int) operand1] + " " + operand2);
				} else if (code == 13) {
					result = (int) operand2;
					System.out.println("Execute: MOV Immediate" + operand2);
				} else if (code == 14) {
					result = (int) ((R[(int) operand1]) & (~(operand2)));
					System.out.println("Execute: BIC Immediate" + R[(int) operand1] + " " + operand2);
				} else if (code == 15) {
					result = (int) ~(operand2);
					System.out.println("Execute: MVN Immediate" + operand2);
				}

			}
		} else if (num == 1) {
			System.out.println("No Execution");
		} else if (num == 2) {
			code = getcond();
			String signed = "";
			for (int i = 8; i < binary.length; i++) {
				// sined[i] = binary[i];
				signed = signed + binary[i];
			}
			String ones = "111111";
			String zeroes = "000000";
			signed = signed + "00";
			if (signed.charAt(0) - '0' == 0) {
				signed = zeroes + signed;
			} else {
				signed = ones + signed;
			}
			long counter = 0;
			// counter = binTOdecimal(signed);
			counter = Long.parseLong(signed, 2);
			if (code == 0) {
				if (flagEqual == true) {
					R[15] = (int) (R[15] + counter + 4);
					System.out.println("EXECUTE: BEQ ,New PC : ");
				}
			} else if (code == 1) {
				if (flagEqual == false) {
					R[15] = (int) (R[15] + counter + 4);
					String as = Integer.toHexString((int) R[15]);
					System.out.println("EXECUTE: BNE New PC : " + as);
				}
				 else {
						System.out.println("EXECUTE: No execution");
					}
			} else if (code == 10) {
				if ((flagSmaller == false) || (flagEqual == true)) {
					R[15] = (int) (R[15] + counter + 4);
					String as = Integer.toHexString((int) R[15]);
					System.out.println("EXECUTE: BGE New PC : " + as);
				} else {
					System.out.println("EXECUTE: No execution");
				}
			} else if (code == 11) {
				if ((flagSmaller == true) && (flagEqual == false)) {
					R[15] = (int) (R[15] + counter + 4);
					String as = Integer.toHexString((int) R[15]);
					System.out.println("EXECUTE: BLT New PC :" + as);
				}
				 else {
						System.out.println("EXECUTE: No execution");
					}
			} else if (code == 12) {
				if ((flagSmaller == false) && (flagEqual == false)) {
					R[15] = (int) (R[15] + counter + 4);
					String as = Integer.toHexString((int) R[15]);
					System.out.println("EXECUTE: BGT New PC : " + as);
				}
				 else {
						System.out.println("EXECUTE: No execution");
					}
			} else if (code == 13) {
				if ((flagGreater == true) || (flagEqual == true)) {
					R[15] = (int) (R[15] + counter + 4);
					String as = Integer.toHexString((int) R[15]);
					System.out.println("EXECUTE: BLE New PC : " + as);
				}
				 else {
						System.out.println("EXECUTE: No execution");
					}
			} else if (code == 14) {
				R[15] = (int) (R[15] + counter + 4);
				String as = Integer.toHexString((int) R[15]);
				System.out.println("EXECUTE: BAL New PC : " + as);
			}
		}
	}

	public static void writeback() {
		System.out.println("Writeback starts");
		hexTobinary(instruct.substring(2));
		String command3 = "";
		String offset3 = "";
		command3 = Integer.toString(binary[binary.length - 1 - 27]) + Integer.toString(binary[binary.length - 1 - 26]);
		long num3 = binTOdecimal(command3);
		long code3 = getOpcode();
		if (((num3 == 0) && (code3 != 10)) || (num3 == 1) && (code3 == 25)) {
			R[(int) destination] = result;
			System.out.println("Write " + result + " to R" + destination);
		} else if ((num3 == 1) && (code3 == 24)) {
			iii = R[(int) destination];
			System.out.println("Write " + iii + " to memory.");

		} else if (num3 == 2 || ((num3 == 0) && (code3 == 10))) {
			System.out.println("No writeback.");
		}
	}

	public static void resetval() {
		for (int i = 0; i < 10; i++) {
			instruction[i] = '0';
		}
		for (int j = 0; j < 32; j++) {
			binary[j] = 0;
		}
		for (int i = 0; i < 16; i++) {
			R[i] = 0;
		}
		operand1 = operand2 = destination = iii = result = 0;
		address = instruct = "";
		flagEqual = flagGreater = flagSmaller = false;
		for (int i = 0; i < 8000; i++) {
			memory[i] = 0;
		}

	}

	public static long binTOdecimal(String s) {
		long decimal = 0;
		long counter = 0;
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
		// System.out.println(s);
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
			} else if (s.charAt(i) == 'A' || s.charAt(i) == 'a') {
				bin = bin + "1010";
			} else if (s.charAt(i) == 'B' || s.charAt(i) == 'b') {
				bin = bin + "1011";
			} else if (s.charAt(i) == 'C' || s.charAt(i) == 'c') {
				bin = bin + "1100";
			} else if (s.charAt(i) == 'D' || s.charAt(i) == 'd') {
				bin = bin + "1101";
			} else if (s.charAt(i) == 'E' || s.charAt(i) == 'e') {
				bin = bin + "1110";
			} else if (s.charAt(i) == 'F' || s.charAt(i) == 'f') {
				bin = bin + "1111";
			}
		}
		for (int i = 0; i < bin.length(); i++) {
			binary[i] = bin.charAt(i) - '0';
		}
	}
}
