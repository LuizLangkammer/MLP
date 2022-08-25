package fileReader;

import main.Sample;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MyInputReader {

	public static ArrayList<Sample> getBase(String path) throws FileNotFoundException, IOException {

		FileReader arq = new FileReader(path);

		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(arq);
		String read = "";
		int qntIn = 0;
		int qntOut = 0;
		int qntSample = 0;

		read = reader.readLine();
		String[] header = read.split(" ");
		qntIn = Integer.parseInt(header[0]);
		qntOut = Integer.parseInt(header[1]);
		qntSample = Integer.parseInt(header[2]);

		ArrayList<Sample> base = new ArrayList<Sample>();
		double[] in = new double[qntIn];
		double[] out = new double[qntOut];
		for (int i = 0; i < qntSample; i++) {

			read = reader.readLine();
			String[] line = read.split(" ");

			for (int j = 0; j < qntIn; j++) {
				in[j] = Double.parseDouble(line[j]);
			}
			for (int j = 0; j < qntOut; j++) {
				out[j] = Double.parseDouble(line[j + qntIn]);
			}
			base.add(new Sample(in.clone(), out.clone()));

		}

		
		reader.close();
		

		return base;
	}
}
