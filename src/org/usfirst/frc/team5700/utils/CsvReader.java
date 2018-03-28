package org.usfirst.frc.team5700.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CsvReader {

	List<float[]> values = new ArrayList<float[]>();

	public CsvReader(String replayName) throws FileNotFoundException {
		String fileName = "/home/lvuser/data_capture/" + replayName + ".rpl";
		System.out.println(fileName);
		Scanner scanner = new Scanner(new File(fileName));
		scanner.useDelimiter("\n");
		scanner.nextLine(); //skip header

		while (scanner.hasNext()) {
			String[] strings = scanner.next().split(",");
			float[] numbers = new float[3];
			numbers[0] = Float.parseFloat(strings[0].trim()); //time
			numbers[1] = Float.parseFloat(strings[1].trim()); //right joystick
			numbers[2] = Float.parseFloat(strings[2].trim());	 //left joystick
			values.add(numbers);
		}

//		for (float[] element : values) {
//			System.out.println("move_value: " + element[1] + ", rotate_value: " + element[2]);
//		}
		scanner.close();
	}

	public List<float[]> getValues() {
		return values;
	}
}
