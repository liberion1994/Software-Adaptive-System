package jmetal.problems;

import jmetal.core.*;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.metaheuristics.moead.SASSolution;
import jmetal.util.JMException;
import jmetal.util.wrapper.XInt;

public class SAS extends Problem {

	/**
	 * Constructor
	 */
//	public SAS(String solutionType) throws ClassNotFoundException {
//		this(solutionType, 10);
//	}

	/**
	 * vars [number of variables][upper and lower bounds]
	 * 
	 * Create a new instance of problem for SAS
	 */
	public SAS(String solutionType, int[][] vars) throws ClassNotFoundException {
		numberOfVariables_ = vars.length;
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 0;
		problemName_ = "SAS";

		upperLimitSAS_ = new int[numberOfVariables_];
		lowerLimitSAS_ = new int[numberOfVariables_];
//
		// Establishes upper and lower limits for the variables
		for (int i = 0; i < numberOfVariables_; i++) {
			upperLimitSAS_[i] = vars[i][0];
			lowerLimitSAS_[i] = vars[i][1];
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
//		double[] varValues  = new double[numberOfVariables_];
//		double[] f = new double[numberOfObjectives_];
		SASSolution sol = null;
		if (solution instanceof SASSolution) {
			sol = (SASSolution)solution;
		}
		
		// put "var[]" into Tao's System, note that var[] is only the index, we need to get the variable value
		// from a variable list
//		for (int i = 0; i < numberOfVariables_; i++) {
//			var[i] = x.getValue(i);
//			varValues[i] = sol.getVariableValueFromIndex(var[i]);
//		}
		
		for (int i = 0; i < numberOfVariables_; i++) 
			var[i] = x.getValue(i);
		
		// It is possible to directly retrieve objective' values from indices, and this
		// can be done from my end.
		double[] f = sol.getObjectiveValuesFromIndex(var);
		
		solution.setObjective(0, f[0]);
		solution.setObjective(1, f[1]);
	}
}