package org.femosaa.core;

public class EAConfigure {
	
	static {
		con = new EAConfigure(100, 10, 0.9, 0.1);
		//con = new EAConfigure(20, 50, 0.5, 0.05);
	}

	public int pop_size;
	public int generation;
	public double crossover_rate;
	public double mutation_rate;
	
	
	
	
	public EAConfigure(int pop_size, int generation, double crossover_rate,
			double mutation_rate) {
		super();
		this.pop_size = pop_size;
		this.generation = generation;
		this.crossover_rate = crossover_rate;
		this.mutation_rate = mutation_rate;
	}


	private static EAConfigure con;
	
	
	public static EAConfigure getInstance(){
		return con;
	}
	
	public void setupWSConfiguration(){
		con = new EAConfigure(100, 50, 0.9, 0.1);
	}
}
