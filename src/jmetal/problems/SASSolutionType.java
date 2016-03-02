package jmetal.problems;

import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import jmetal.encodings.variable.Int;

public class SASSolutionType extends SolutionType {
	/**
	 * Constructor
	 * @param problem
	 * @throws ClassNotFoundException 
	 */
	public SASSolutionType(Problem problem) throws ClassNotFoundException {
		super(problem) ;
	} // Constructor

	/**
	 * Creates the variables of the solution
	 * @param decisionVariables
	 */
	public Variable[] createVariables() {
		Variable[] variables = new Variable[problem_.getNumberOfVariables()];

		for (int var = 0; var < problem_.getNumberOfVariables(); var++)
			variables[var] = new Int((int)problem_.getLowerLimitSAS(var), (int)problem_.getUpperLimitSAS(var));

		return variables ;
	} // createVariables

}
