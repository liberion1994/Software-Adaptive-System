package jmetal.problems;

import jmetal.core.*;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.util.JMException;
import jmetal.util.wrapper.XInt;

public class SAS extends Problem {

	/**
	 * Constructor
	 */
	public SAS(String solutionType) throws ClassNotFoundException {
		this(solutionType, 10);
	}

	/**
	 * Create a new instance of problem for SAS
	 */
	public SAS(String solutionType, Integer numberOfVariables) throws ClassNotFoundException {
		numberOfVariables_ = numberOfVariables.intValue();
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 0;
		problemName_ = "SAS";

		upperLimitSAS_ = new int[numberOfVariables_];
		lowerLimitSAS_ = new int[numberOfVariables_];

		// Establishes upper and lower limits for the variables
		for (int i = 0; i < numberOfVariables_; i++) {
			// TODO: the upper and lower bounds is given by Tao
		}

		if (solutionType.compareTo("IntSolutionType") == 0)
			solutionType_ = new IntSolutionType(this);
		else {
			System.out.println("Error: solution type " + solutionType + " invalid");
			System.exit(-1);
		}
	}
	
	/**
	 * Evaluate a solution
	 */
	public void evaluate(Solution solution) throws JMException {
		XInt x = new XInt(solution);
		
		int[] var  = new int[numberOfVariables_];
		double[] f = new double[numberOfObjectives_];
		
		for (int i = 0; i < numberOfVariables_; i++)
			var[i] = x.getValue(i);
		
		// TODO: put "var[]" into Tao's System, note that var[] is only the index, we need to get the variable value
		// from a variable list
		
		
		
		solution.setObjective(0, f[0]);
		solution.setObjective(1, f[1]);
	}
}