package org.usfirst.frc.team5700.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CsvReader {

	List<float[]> values = new ArrayList<float[]>();

	public CsvReader() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("/home/lvuser/data_capture/replay.csv"));
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
