package jmetal.problems;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;

/**
 * A facade model in order to lower code coupling
 * 
 * TODO: make sure that all the instances of Solution in 
 * MOEAD_STM_SAS are instantiated from here.
 * 
 * @author tao
 *
 */
public interface SASSolutionInstantiator {

	public Solution getSolution (Problem problem);
	
	public Solution getSolution (Solution solution);
	
	public Solution getSolution (Problem problem, Variable[] variables);
	
	public Solution getSolution();
}
