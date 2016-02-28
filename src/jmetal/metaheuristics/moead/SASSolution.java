package jmetal.metaheuristics.moead;

import jmetal.core.Solution;

public abstract class SASSolution extends Solution {

	public abstract double getVariableValueFromIndex(int index);

	public abstract double[] getObjectiveValuesFromIndex(int[] var);
	
	//TODO override getDecisionVariables
}
