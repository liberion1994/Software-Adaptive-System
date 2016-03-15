package jmetal.problems;

import java.io.IOException;

import jmetal.core.SolutionSet;
import jmetal.util.JMException;

public interface SASAlgorithmAdaptor {
	
	public SolutionSet execute(SASSolutionInstantiator factory, int[][] vars,  int numberOfObjectives_, int numberOfConstraints_) throws JMException,
	SecurityException, IOException, ClassNotFoundException;

}
