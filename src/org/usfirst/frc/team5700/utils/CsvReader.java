package org.usfirst.frc.team5700.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CsvReader {

	List<double[]> values = new ArrayList<double[]>();

	public CsvReader(String replayName) throws FileNotFoundException {
		String fileName = "/home/lvuser/data_capture/" + replayName + ".rpl";
		System.out.println(fileName);
		Scanner scanner = new Scanner(new File(fileName));
		scanner.useDelimiter("\n");
		scanner.nextLine(); //skip header
		
//		String[] data_fields ={
//				"time",
//				"moveValue",
//				"rotateValue",
//				"leftMotorSpeed",
//				"rightMotorSpeed",
//				"speed",
//				"leftSpeed",
//				"rightSpeed",
//				"leftDistance",
//				"rightDistance"
//		};

		while (scanner.hasNext()) {
			String string = scanner.next();
			//System.out.println("File line: " + string);
			String[] strings = string.split(",");
			int length = strings.length - 1; //last element is empty, line terminated by comma
			double[] numbers = new double[length];
			for (int i = 0; i < length; i ++) {
				//System.out.println(strings[i] + ", ");
			    numbers[i] = Double.parseDouble(strings[i].trim());
			}
			values.add(numbers);
		}
		
		scanner.close();
	}

	public List<double[]> getValues() {
		return values;
	}
}
