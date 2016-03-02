package jmetal.problems;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.util.JMException;

/**
 * Need to ensure that the order of variables is the same as control primitives, and that
 * the main variables always ahead of the dependent variables.
 * 
 * @author tao
 *
 */
public abstract class SASSolution extends Solution {
	
	
	

	//public abstract double getVariableValueFromIndexValue(int indexValue);

	public SASSolution(Problem problem) throws ClassNotFoundException {
		super(problem);
		// TODO Auto-generated constructor stub
	}

	public SASSolution(Solution solution) {
		super(solution);
		// TODO Auto-generated constructor stub
	}

	public abstract double[] getObjectiveValuesFromIndexValue(int[] var);
	
	/**
	 * Need to ensure variable dependency.
	 * @param index
	 * @return index value
	 */
	public abstract int getUpperBoundforVariable(int index) throws JMException;
	
	/**
	 * Need to ensure variable dependency.
	 * @param index
	 * @return index value
	 */
	public abstract int getLowerBoundforVariable(int index) throws JMException;
	
	/**
	 * 
	 * @param index
	 * @return the array of index
	 */
	public abstract int[] getMainVariablesByDependentVariable(int index);
	
	//public abstract boolean isHavingMainVariable(int index);
	
	//public abstract boolean isHavingDependentVariables(int index);
}
