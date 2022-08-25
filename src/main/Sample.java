package main;

public class Sample {
	
	
	private double[] input;
	private double[] output;
	
	
	public Sample(double[] input, double[] output) {
		this.input = input;
		this.output = output;
	}

	
	public int getInDimension() {
		return input.length;
	}
	
	public int getOutDimension() {
		return output.length;
	}

	public double[] getInput() {
		return input;
	}


	public double[] getOutput() {
		return output;
	}
	
	
	
	
	
	

}
