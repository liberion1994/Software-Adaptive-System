package jmetal.metaheuristics.moead;

import jmetal.core.Solution;

/**
 * Need to ensure that the order of variables is the same as control primitives, and that
 * the main variables always ahead of the dependent variables.
 * 
 * @author tao
 *
 */
public abstract class SASSolution extends Solution {

	public abstract double getVariableValueFromIndexValue(int indexValue);

	public abstract double[] getObjectiveValuesFromIndexValue(int[] var);
	
	/**
	 * Need to ensure variable dependency.
	 * @param index
	 * @return index value
	 */
	public abstract int getUpperBoundforVariable(int index);
	
	/**
	 * Need to ensure variable dependency.
	 * @param index
	 * @return index value
	 */
	public abstract int getLowerBoundforVariable(int index);
	
	/**
	 * 
	 * @param index
	 * @return the array of index
	 */
	public abstract int[] getMainVariablesByDependentVariable(int index);
	
	//public abstract boolean isHavingMainVariable(int index);
	
	//public abstract boolean isHavingDependentVariables(int index);
}
