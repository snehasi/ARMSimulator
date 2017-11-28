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
	public static void main(String[] args) throws FileNotFoundException {
		fetch();
		decode();
		execute();
		mem();
		writeback();
	}
	private static void fetch() throws FileNotFoundException {
		readfile();//reading all instructions make a while loop for reading nstruction one by one
		
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
