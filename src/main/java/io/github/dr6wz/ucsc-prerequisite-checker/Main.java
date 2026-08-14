package main;
import java.io.File;
import java.util.Scanner;

import graphs.TokenReader;

public class Main {
	public static void main(String[] args) {
		try {
			File f = new File("courses.txt");
			System.out.println("File found.");
			System.out.println(f.getAbsolutePath());
			Scanner in = new Scanner(f);
			while (in.hasNextLine()) {
				
			}
			in.close();
		} catch (java.io.FileNotFoundException fi) {
			System.out.println("File Not Found");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Some other exception occurred.");
		}
	}
}